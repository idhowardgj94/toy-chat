(ns com.chatapp (:require
                 [org.httpkit.server :refer [run-server]]
                 [ring.middleware.defaults :refer :all]
                 [ring.middleware.json :refer [wrap-json-body
                                               wrap-json-response]]
                 [com.route :refer [app-routes ring-ajax-post ring-ajax-get-or-ws-handshake]]
                 [taoensso.timbre :as timbre])
    (:use [org.httpkit.server]))



;; TODO: router to dispatch event
;; https://github.com/ptaoussanis/sente/blob/master/example-project/src/example/server.clj

(defonce server-state (atom nil))

(defn start-server
  ([]
   reset! server-state
   (let [site-config (assoc-in site-defaults [:static :resources] ["js" "css"])]
     (run-server (-> (wrap-defaults #'app-routes site-config)
                     wrap-json-body
                     wrap-json-response) {:port 8080}))))



(defn stop-server
  []
  (if (not (nil? @server-state))
    (when-let [server @server-state]
      (timbre/info "stop server")(
      (server)
      (reset! server-state nil))
    (timbre/info "server not start yet"))))

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
