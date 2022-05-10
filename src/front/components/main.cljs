(ns front.components.main 
  (:require 
   [rum.core :as rum :refer [defc]]
   [front.components.friend-card :refer [friend-card]]
   [front.components.room :refer [room]]))

(defn did-mount
  [prop]
  (.log js/console "hello, wold22333"))

(defc main-page < rum/reactive
  {:did-mount did-mount}
  [res]
  [:div.lg:container.mx-auto.flex-row.flex.full-height
   [:div.navigator 
    [:div 
     [:input.search-bar.rounded.mx-auto.w-full.focus:outline-none.pl-2.mt-2.bg-gray-100 {:type "text" :placeholder "Search"} ]]
    [:ul.bg-white-200.overflow-hidden
     (friend-card)
     (friend-card)
     (friend-card)
     (friend-card)
     (friend-card)
     (friend-card)]]
   [(room)]])