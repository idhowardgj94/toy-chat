(ns dev.main (:require [com.chatapp :refer :all :exclude [-main]]
                       [com.route :refer [app-routes]]
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
          (run-server (-> (wrap-defaults #'app-routes  (assoc-in site-defaults [:static :resources] ["js" "css"]) )
                         wrap-json-body
                         wrap-json-response
                         wrap-reload) {:port 8080})))

;; TODO: stop server-dev not woorking
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

(comment
  (require '[clojure.java.io :as io])
  (require '[ring.middleware.defaults :refer :all])
  (assoc site-defaults :a "h")
  (assoc site-defaults :static {:resources "oeoeo"})
  (assoc-in site-defaults [:static :resources] ["js" "css"])
  (io/resource "js/main.js")
  ,)