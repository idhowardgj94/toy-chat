(ns dev.integrant.config (:require
                          [integrant.core :as ig]
                          [com.chatapp :refer [start-server]]
                          [taoensso.timbre :as timbre]))

(defonce config {:httpkit/server {:port 8080 :reload true}})

(defmethod ig/init-key :httpkit/server [_ {:keys [reload]}]
  (timbre/info "start portal...")
  (start-server reload))
(defmethod ig/halt-key! :httpkit/server [_ server]
  (server))

