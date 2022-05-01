(ns dev.system (:require [com.chatapp :refer :all :exclude [-main]]
                         [taoensso.timbre.appenders.core :as appenders]
                         [integrant.core :as ig]
                         [taoensso.timbre :as timbre]
                         [dev.integrant.config :refer [config]]))

;; (defn stast-system
;;   "start server, and add reload for dev"
;;   [& _]
;;   (reset! server-state
;;           (run-server (-> (wrap-defaults #'app-routes  (assoc-in site-defaults [:static :resources] ["js" "css"]))
;;                           wrap-json-body
;;                           wrap-json-response
;;                           wrap-reload) {:port 8080})))

;; (defn stop-server-dev
;;   "stop dev server"
;;   []
;;   (stop-server))
(def system (atom nil))
(defn start-system
  []
  ;; (reset! p (p/open))
  ;; (add-tap #'p/submit)
  (timbre/merge-config!
   {:appenders {:spit (appenders/spit-appender {:fname "logs/debug-logs.txt"})}})
  (reset! system (ig/init config)))

#_(ig/halt! (config))
#_(require '[clojure.tools.deps.alpha.repl :refer [add-libs]])

#_(comment
  (require '[clojure.java.io :as io])
  (require '[ring.middleware.defaults :refer :all])
  (assoc site-defaults :a "h")
  (assoc site-defaults :static {:resources "oeoeo"})
  (assoc-in site-defaults [:static :resources] ["js" "css"])
  (io/resource "js/main.js")
  (print "hello"))