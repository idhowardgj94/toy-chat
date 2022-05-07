(ns front.route (:require
           [front.components.login :refer [login]]
           [front.components.main :refer [main-page]]
           [reitit.frontend :as rf]
           [reitit.frontend.easy :as rfe]
           [reitit.coercion.spec :as rss]))

(defonce match (atom nil))

(def routes
  [["/"
    {:name :frontpage
     :view login}]
   ["/main-page"
    {:name :main-page
     :view main-page}]])

(defn init! []
  (rfe/start!
   (rf/router routes {:data {:coercion rss/coercion}})
   (fn [m] (reset! match m))
    ;; set to false to enable HistoryAPI
   {:use-fragment true}))