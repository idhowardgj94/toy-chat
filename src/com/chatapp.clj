(ns com.chatapp (:require
                 [compojure.core :refer :all]
                 [compojure.route :as route]
                 [ring.middleware.defaults :refer :all]
                 [ring.util.response :as res]
                 [taoensso.sente :as sente]
                 [org.httpkit.server :refer [run-server]]
                 [ring.middleware.json :refer [wrap-json-response wrap-json-body]])
    (:use [org.httpkit.server]))

#_(require '[clojure.repl :as repl])
(defonce server-state (atom nil))

(defn content-type-json
  "response as json type"
  [res]
  (assoc res :headers {"Content-Type" "application/json"}))

(defn index
  [_]
  (-> (res/response {:status "hello, world"})
      content-type-json))

(defroutes app-routes
  (GET "/" [] index)
  (route/not-found "Not Found"))

(defn start-server
  ([]
   reset! server-state
           (run-server (-> (wrap-defaults #'app-routes site-defaults)
                           wrap-json-body
                           wrap-json-response) {:port 8080})))

(defn -main
  "I don't do a whole lot ... yet."
  []
  (start-server))

(defn stop-server
  []
  (if (not (nil? @server-state))
    (when-let [server @server-state]
      (println "stop server")
      (.stop server)
      (reset! server-state nil))
    (println "server not start yet")))
(comment
  (start-server)
  (stop-server)
  (defn testremain [& r]
    (def result (vec r))
    (println result))
  (defn hello [& who]
    (println  who))
  (testremain "123" "456" "789")
  (hello 1 2 3))
