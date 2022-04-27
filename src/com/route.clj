(ns com.route (:require
               [com.htmlUtil :refer [content-type-json page]]
               [compojure.core :refer [GET POST defroutes]]
               [compojure.route :as route]
               [rum.core :as rum]
               [ring.util.response :as res]
               [taoensso.sente :as sente]
               [taoensso.timbre :as timbre]
               [taoensso.sente.server-adapters.http-kit      :refer (get-sch-adapter)]
               [clojure.core.async :as async  :refer (<! <!! >! >!! put! chan go go-loop)]))


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

;; can add-watch to check connecteduids change (it is a atom) ^^
(add-watch connected-uids :connected-uids
           (fn [_ _ old new]
             (when (not= old new)
               (timbre/infof "Connected uids change: %s", new))))

;; sente end

;; XXX: test broadcast
(defonce broadcast-enabled?_ (atom true))
(defn start-example-broadcaster!
  "As an example of server>user async pushes, setup a loop to broadcast an
  event to all connected users every 10 seconds"
  []
  (let [broadcast!
        (fn [i]
          (let [uids (:any @connected-uids)]
            (timbre/debugf "Broadcasting server>user: %s uids" (count uids))
            (doseq [uid uids]
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

(defn index
  [_]
  (-> (res/response {:status "hello, world"})
      content-type-json))

(defn test-rum
  [_]
  (rum/render-static-markup (page)))

(defroutes app-routes
  (GET "/" [] index)
  (GET  "/chsk" req (ring-ajax-get-or-ws-handshake req))
  (POST "/chsk" req (ring-ajax-post                req))
  (GET "/testRum" [] test-rum)
  (route/not-found "Not Found"))