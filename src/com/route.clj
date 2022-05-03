(ns com.route (:require
               [com.html-util :refer [content-type-json page]]
               [compojure.core :refer [GET POST defroutes]]
               [compojure.route :as route]
               [rum.core :as rum]
               [ring.util.response :as res]
               [taoensso.sente :as sente]
               [taoensso.timbre :as timbre]
               [taoensso.sente.server-adapters.http-kit      :refer (get-sch-adapter)]
               [clojure.core.async :as async  :refer (<! <!! >! >!! put! chan go go-loop, put!)]))


;; try sente
(let [{:keys [ch-recv send-fn connected-uids
              ajax-post-fn ajax-get-or-ws-handshake-fn]}
      (sente/make-channel-socket! (get-sch-adapter) {:csrf-token-fn nil})]

  (def ring-ajax-post                ajax-post-fn)
  (def ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn)
  (def ch-chsk                       ch-recv) ; ChannelSocket's receive channel
  (def chsk-send!                    send-fn) ; ChannelSocket's send API fn
  (def connected-uids                connected-uids) ; Watchable, read-only atom
  )

(comment
  (print @connected-uids))
;; can add-watch to check connecteduids change (it is a atom) ^^
(add-watch connected-uids :connected-uids
           (fn [_ _ old new]
             (when (not= old new)
               (timbre/infof "Connected uids change: %s", new))))

;;;; Sente event handlers

;;;; Some server>user async push examples

(defn test-fast-server>user-pushes
  "Quickly pushes 100 events to all connected users. Note that this'll be
  fast+reliable even over Ajax!"
  []
  (doseq [uid (:any @connected-uids)]
    (doseq [i (range 100)]
      (chsk-send! uid [:fast-push/is-fast (str "hello " i "!!")]))))

;; XXX: test broadcast
(defonce broadcast-enabled?_ (atom true))

(defmulti -event-msg-handler
  "Multimethod to handle Sente `event-msg`s"
  :id ; Dispatch on event-id
  )

(defn event-msg-handler
  "Wraps `-event-msg-handler` with logging, error catching, etc."
  [{:as ev-msg :keys [id ?data event]}]
  (-event-msg-handler ev-msg) ; Handle event-msgs on a single thread
  ;; (future (-event-msg-handler ev-msg)) ; Handle event-msgs on a thread pool
  )

(defmethod -event-msg-handler
  :default ; Default/fallback case (no other matching handler)
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [session (:session ring-req)
        uid     (:uid     session)]
    (timbre/debugf "Unhandled event: %s" event)
    (when ?reply-fn
      (?reply-fn {:umatched-event-as-echoed-from-server event}))))

(defmethod -event-msg-handler :example/test-rapid-push
  [ev-msg] (test-fast-server>user-pushes))

(defmethod -event-msg-handler :example/toggle-broadcast
  [{:as ev-msg :keys [?reply-fn]}]
  (let [loop-enabled? (swap! broadcast-enabled?_ not)]
    (?reply-fn loop-enabled?)))
;; sente end


(defn start-example-broadcaster!
  "As an example of server>user async pushes, setup a loop to broadcast an
  event to all connected users every 10 seconds"
  []
  (let [broadcast!
        (fn [i]
          (let [uids (:any @connected-uids)]
            (timbre/debugf "Broadcasting server>user: %s uids" (count uids))
            (doseq [uid uids]
              (timbre/debugf "start broad cast to uid: " uid)
              (chsk-send! uid
                          [:some/broadcast
                           {:what-is-this "An async broadcast pushed from server"
                            :how-often "Every 10 seconds"
                            :to-whom uid
                            :i i}]))))]

    (go-loop [i 0]
      (<! (async/timeout 10000))
      (when @broadcast-enabled?_ (broadcast! i))
      (recur (inc i)))))

(def broad (atom nil))

(defn index
  [_]
  (-> (res/response {:status "hello, world!!!"})
      content-type-json))

(defn test-rum
  [_]
  (rum/render-static-markup (page)))

(defroutes app-routes
  (GET "/" [] index)
  (GET  "/chsk" req (ring-ajax-get-or-ws-handshake req))
  (POST "/chsk" req (ring-ajax-post                req))
  (GET "/testRum" [] test-rum))

(defn auth-helloworld
  [req]
  (timbre/info "hello, world")
  "hello, world")

(defroutes auth-routes
  (GET "/auth/helloworld" [req] (auth-helloworld req)))

#_(comment
  (reset! broad (start-example-broadcaster!))
  (print @broad)
  (put! @broad true)
  (async/close! @broad))