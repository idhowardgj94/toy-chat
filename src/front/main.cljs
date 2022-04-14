
(ns front.main (:require [rum.core :as rum]
                         [front.app :refer [app]]
                         ["react-dom/client" :as react]))

;; TODO: help rum to update to react18
(defn ^:export start
  []
  (let [root (react/createRoot (.querySelector js/document "#root"))]
    (.render root (app)))
)

