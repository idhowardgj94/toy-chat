(ns dev.system (:require [com.chatapp :refer :all :exclude [-main]]
                         [taoensso.timbre.appenders.core :as appenders]
                         [integrant.core :as ig]
                         [taoensso.timbre :as timbre]
                         [dev.integrant.configs.server :refer [server-config]]
                         [dev.integrant.configs.db :refer [db-config]]))

(defonce config (merge server-config db-config))
(print config)
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