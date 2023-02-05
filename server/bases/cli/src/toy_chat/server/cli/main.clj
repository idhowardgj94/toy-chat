(ns toy-chat.server.cli.main
  (:require [toy-chat.server.publisher.interface :as publisher])
  (:gen-class))

(defn -main
  "Say Hello!"
  [& args]
  (println (publisher/greeting-message )))
