(ns front.components.room
  (:require [rum.core :as rum :refer [defc]]))

(defc room < rum/reactive
  []
  [:div.flex.flex-col.room-container
   [:div.flex.flex-row
    [:div.header.flex.flex-1.items-center
     [:div.mx-4.flex.flex-1
      [:div
       "家齊, 鬆恩, 立昕（3）" [:i.fa.fa-volume-up.fa-xl.ml-1 {:style {:color "grey"}}]]
      [:ul.to-right.flex.flex-row
       [:li.ml-2 [:i.fa.fa-xl.fa-eye]]
       [:li.ml-2 [:i.fa.fa-xl.fa-file]]
       [:li.ml-2 [:i.fa.fa-bars.fa-xl]]]]]]
   [:div.block.flex-1.message-container
    [:div.flex.mb-2 [:span.px-4.rounded-xl.border-gray-700.border-opacity-100.border.ml-2 "hello, it's me"]]
    [:div.flex.mb-2 [:span.px-4.rounded-xl.border-gray-700.border-opacity-100.border.ml-2 "hello, it's me"]]
    [:div.flex.mb-2 [:span.px-4.rounded-xl.border-gray-700.border-opacity-100.border.ml-2 "hello, it's me"]]
    [:div.flex.mb-2 [:span.px-4.rounded-xl.border-gray-700.border-opacity-100.border.mr-2.ml-auto "hello, it's me"]]
    [:div.flex.mb-2 [:span.px-4.rounded-xl.border-gray-700.border-opacity-100.border.mr-2.ml-auto "hello, it's me"]]]
   [:div.message-toolbox.flex.justify-center.items-center
    [:div [:input {:type "text" :placeholder "input"}]
     [:button.round.bg-blue-600.hover:bg-blue-400.p-2.rounded-xl.text-white.ml-4 "send"]]
    ]]
  )