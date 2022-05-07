(ns front.main (:require
                [front.route :refer [init!]]
                [front.app :refer [app]]
                ["react-dom/client" :as react]))

;; TODO: help rum to update to react18
(defn ^:export start
  []
  (let [root (react/createRoot (.querySelector js/document "#root"))]
    (init!)
    (.render root (app)))
)

(defn ^:dev/before-load stop-dev []
  (js/console.log "stop"))

;; TODO: find a way to supress the use root.render() instead warning
;; currently, use root.render won't hot-reload
(defn ^:dev/after-load start-dev []
  (start))