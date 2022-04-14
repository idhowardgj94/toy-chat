(ns com.chatapp (:require
                 [clojure.core.async :as async  :refer (<! <!! >! >!! put! chan go go-loop)]
                 [compojure.core :refer :all]
                 [compojure.route :as route]
                 [org.httpkit.server :refer [run-server]]
                 [ring.middleware.defaults :refer :all]
                 [ring.middleware.json :refer [wrap-json-body
                                               wrap-json-response]]
                 [ring.util.response :as res]
                 [taoensso.sente :as sente]
                 [taoensso.sente.server-adapters.http-kit      :refer (get-sch-adapter)]
                 [taoensso.timbre :as timbre])
    (:use [org.httpkit.server]))

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
;; test broadcast end

;; TODO: router to dispatch event
;; https://github.com/ptaoussanis/sente/blob/master/example-project/src/example/server.clj

(defonce server-state (atom nil))
(defn content-type-json
  "response as json type"
  [res]
  (assoc res :headers {"Content-Type" "application/json"}))

(defn index
  [_]
  (-> (res/response {:status "hello, world"})
      content-type-json))

(defroutes app-routes
  (GET "/" [] index)
  (GET  "/chsk" req (ring-ajax-get-or-ws-handshake req))
  (POST "/chsk" req (ring-ajax-post                req))
  (route/not-found "Not Found"))

(defn start-server
  ([]
   reset! server-state
   (run-server (-> (wrap-defaults #'app-routes site-defaults)
                   wrap-json-body
                   wrap-json-response) {:port 8080})))



(defn stop-server
  []
  (if (not (nil? @server-state))
    (when-let [server @server-state]
      (timbre/info "stop server")
      (.stop server)
      (reset! server-state nil))
    (timbre/info "server not start yet")))

(defn -main
  "I don't do a whole lot ... yet."
  []
  (start-server))


#_(require '[clojure.repl :as repl])
#_(comment
  (start-server)
  (stop-server)
  (defn testremain [& r]
    (def result (vec r))
    (println result))
  (defn hello [& who]
    (println  who))
  (testremain "123" "456" "789")
  (hello 1 2 3))
