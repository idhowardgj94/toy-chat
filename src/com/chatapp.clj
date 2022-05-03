(ns com.chatapp (:require
                 [org.httpkit.server :refer [run-server]]
                 [ring.middleware.defaults :refer :all]
                 [ring.middleware.json :refer [wrap-json-body
                                               wrap-json-response]]
                 [ring.middleware.reload :refer [wrap-reload]]
                 [compojure.core :refer [routes wrap-routes]]
                 [compojure.route :as route]
                 [com.route :refer [app-routes auth-routes]]
                 
                 [taoensso.timbre :as timbre])
    (:use [org.httpkit.server]))



;; TODO: router to dispatch event
;; https://github.com/ptaoussanis/sente/blob/master/example-project/src/example/server.clj
(defonce server-state (atom nil))

(defn setup-routes
  "setting up route"
  []
  (routes
   #'app-routes
   #'auth-routes
   (route/not-found "Not Found")))
(defn start-server
  ([]
   (start-server false))
   ([reload]
    (let [site-config (assoc-in site-defaults [:static :resources] ["js" "css"])]
     (reset! server-state
             (run-server (-> (wrap-defaults (setup-routes) site-config)
                             wrap-json-body
                             wrap-json-response
                             (#(if (true? reload) (wrap-reload %) (%)))) {:port 8080})))))   


(defn stop-server
  []
  (if (not (nil? @server-state))
    (when-let [server @server-state]
      (timbre/info "stop server") 
      ((server)
       (reset! server-state nil))
      (timbre/info "server not start yet")) nil))

(defn -main
  "I don't do a whole lot ... yet."
  []
  (start-server))
