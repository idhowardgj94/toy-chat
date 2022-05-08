(ns front.components.room
  (:require [rum.core :as rum :refer [defc]]))

(defc room < rum/reactive
  []
  [:div.flex.flex-row.room-container
   [:div.header.bg-yellow-100.flex.flex-1.items-center 
    [:div.mx-4 
     [:span
      "家齊, 鬆恩, 立昕（3）" [:i.fa.fa-volume-up.fa-xl.ml-1 {:style {:color "grey"}}]]]]])