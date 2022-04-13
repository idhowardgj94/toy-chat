
(ns front.main (:require [rum.core :as rum]
                         ["react-dom/client" :as react]))

(rum/defc app
  []
  [:div.text-lg "hello, world"])
;; TODO: help rum to update to react18
(defn ^:export start
  []
  (let [root (react/createRoot (.querySelector js/document "#root"))]
    (.render root (app)))
)

