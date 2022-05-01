
(ns front.main (:require [rum.core :as rum]
                         [front.app :refer [app]]
                         [front.components.login :refer [login]]
                         ["react-dom/client" :as react]))

;; TODO: help rum to update to react18
(defn ^:export start
  []
  (let [root (react/createRoot (.querySelector js/document "#root"))]
    (.render root (login)))
)

(defn ^:dev/before-load stop-dev []
  (js/console.log "stop"))

(defn ^:dev/after-load start-dev []
  (js/console.log "reload rum app...")
  (start))