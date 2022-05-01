(ns dev.integrant.config (:require
                          [integrant.core :as ig]
                          [ring.middleware.defaults :refer [site-defaults
                                                            wrap-defaults]]
                          [ring.middleware.reload :refer [wrap-reload]]
                          [taoensso.timbre :as timbre]
                          [taoensso.timbre.appenders.core :as appenders]
                          [com.route :refer [app-routes]]
                          [ring.middleware.json :refer [wrap-json-body
                                                        wrap-json-response]]
                          [org.httpkit.server :refer [run-server]]))

(defonce config {:httpkit/server {:port 8080 :reload true}})

(defmethod ig/init-key :httpkit/server [_ {:keys [port reload]}]
  (timbre/info "start portal...")
  (run-server (-> (wrap-defaults #'app-routes  (assoc-in site-defaults [:static :resources] ["js" "css"]))
                  wrap-json-body
                  wrap-json-response
                  wrap-reload
                  (#(if (true? reload) (wrap-reload %) (%)))) {:port port}))
(defmethod ig/halt-key! :httpkit/server [_ server]
  (server))

