(ns dev.main (:require [com.chatapp :refer :all]
                       [portal.api :as p]
                       [ring.adapter.jetty :refer [run-jetty]]
                       [ring.middleware.defaults :refer [site-defaults
                                                         wrap-defaults]]
                       [ring.middleware.json :refer [wrap-json-body
                                                     wrap-json-response]]
                       [ring.middleware.reload :refer [wrap-reload]]))

(defonce p (atom nil))
(defn start
  []
  (reset! p (p/open))
  (add-tap #'p/submit)
  (print "[DEBUG] start portal..."))

(defn start-server-dev
  "start server, and add reload for dev"
  []
  [wrap-reload]
   (when (not (nil? wrap-reload))
     (reset! server-state
             (run-jetty (-> (wrap-defaults #'app-routes site-defaults)
                            wrap-json-body
                            wrap-json-response
                            wrap-reload)
                        {:port 8080 :join? false}))))


#_(start-server-dev)
#_(stop-server)


#_(require '[clojure.tools.deps.alpha.repl :refer [add-libs]])
