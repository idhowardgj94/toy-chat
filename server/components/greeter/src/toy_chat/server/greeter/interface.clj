(ns toy-chat.server.greeter.interface
  (:require [toy-chat.server.greeter.core :as greeter]))

(defn greeting
  "Return a suitable greeting for the person."
  [{:keys [person] :as data}]
  (greeter/greeting data))
