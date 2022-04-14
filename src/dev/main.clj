(ns dev.main (:require [com.chatapp :refer :all :exclude [-main]]
                       [portal.api :as p]
                       [taoensso.timbre :as timbre]
                       [org.httpkit.server :refer [run-server]]
                       [ring.middleware.defaults :refer [site-defaults
                                                         wrap-defaults]]
                       [ring.middleware.json :refer [wrap-json-body
                                                     wrap-json-response]]
                       [ring.middleware.reload :refer [wrap-reload]]))

(defonce p (atom nil))

(defn start-server-dev
  "start server, and add reload for dev"
  [& _]
  (reset! server-state
          (run-server (-> (wrap-defaults #'app-routes  site-defaults )
                         wrap-json-body
                         wrap-json-response
                         wrap-reload) {:port 8080})))

(defn stop-server-dev
  "stop dev server"
  []
  (stop-server))

(defn start
  []
  (reset! p (p/open))
  (add-tap #'p/submit)
  (start-server-dev)
  (timbre/info "start portal..."))

#_(start-server-dev)
#_(stop-server-dev)

#_(require '[clojure.tools.deps.alpha.repl :refer [add-libs]])
