(ns dev.integrant.configs.db (:require
                              [integrant.core :as ig]
                              [com.chatapp :refer [start-server]]
                              [taoensso.timbre :as timbre]))

(defonce db-config {:db/setting {:auto-commit        true
                                 :read-only          false
                                 :connection-timeout 30000
                                 :validation-timeout 5000
                                 :idle-timeout       600000
                                 :max-lifetime       1800000
                                 :minimum-idle       10
                                 :maximum-pool-size  10
                                 :pool-name          "db-pool"
                                 :adapter            "postgresql"
                                 :username           "username"
                                 :password           "password"
                                 :database-name      "database"
                                 :server-name        "localhost"
                                 :port-number        5432
                                 :register-mbeans    false}})

(defmethod ig/init-key :db/setting [_ config]
  (timbre/info "restart datasource...")
  )

(defmethod ig/halt-key! :db/setting [_]
  )