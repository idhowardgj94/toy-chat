(ns dev.system (:require [com.chatapp :refer :all :exclude [-main]]
                         [taoensso.timbre.appenders.core :as appenders]
                         [integrant.core :as ig]
                         [taoensso.timbre :as timbre]
                         [dev.integrant.config :refer [config]]))

(def system (atom nil))
(defn start-system
  []
  ;; (reset! p (p/open))
  ;; (add-tap #'p/submit)
  (timbre/merge-config!
   {:appenders {:spit (appenders/spit-appender {:fname "logs/debug-logs.txt"})}})
  (reset! system (ig/init config)))

(defn stop-system
  []
  (ig/halt! @system)
  (reset! system nil))