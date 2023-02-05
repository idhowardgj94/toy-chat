(ns toy-chat.server.publisher.interface
  (:require [toy-chat.server.publisher.core :as core]))

(defn greeting-message
  []
  (core/greeting-message))
