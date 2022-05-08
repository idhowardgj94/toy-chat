(ns front.components.friend-card
  (:require [rum.core :as rum :refer [defc]]))

(defc friend-card
  []
 [:li.hover:bg-gray-100.friend-item-card.flex-row.flex.items-center
  [:div.m-2.overflow-hidden.inline-block.h-12.w-12.rounded-full.ring-2.ring-white.relative 
   [:img.img-in {:src "https://www.w3schools.com/html/img_girl.jpg"}]]
  [:div.flex-1 "王瑀寶貝"]])