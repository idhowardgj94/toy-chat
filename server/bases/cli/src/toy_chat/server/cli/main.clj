(ns toy-chat.server.cli.main
  (:require [toy-chat.server.greeter.interface :as greeter])
  (:gen-class))

(defn -main
  "Say Hello!"
  [& args]
  (println (greeter/greeting {:person (first args)})))
