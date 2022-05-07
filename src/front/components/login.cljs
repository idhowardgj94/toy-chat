(ns front.components.login 
  (:require [rum.core :as rum]
            [reitit.frontend.easy :as rfe]))

(defn submit-handler
  [e]
  (.preventDefault e)
  (.log js/console "hello, world")
  (rfe/push-state :main-page )
  )

(rum/defc ^{:key "logins"} login 
  "let's play some logic thing"
  []
  [:div.login [:div.container.mx-auto.py-5
                [:h1.text-4xl.py-4.text-center "Toy Chat"]
                [:div.max-w-lg.rounded.overflow-hidden.shadow-lg.mx-auto.bg-white
                 [:h1.text-3xl.text-center.my-2 "Login"]
                 [:form.flex.flex-col
                  [:div.mb-4.mx-4
                   [:label.block.text-gray-700.text-sm.font-bold.mb-2 {:for "account"} "Account"]
                   [:input.shadow.appearance-none.border-rounded-wfull.py-2.px-3.text-gray-700.leading-tight.focus:outline-note.focus:shadow-outline.w-full
                    {:name "account" :type "text" :placeholder "account"}]]
                   [:div.mb-4.mx-4
                    [:label.block.text-gray-700.text-sm.font-bold.mb-2 {:for "password"} "password"]
                    [:input.shadow.appearance-none.border-rounded-wfull.py-2.px-3.text-gray-700.leading-tight.focus:outline-note.focus:shadow-outline.w-full
                     {:name "password" :type "password" :placeholder "password"}]]
                  [:button.bg-sky-600.hover:bg-sky-700.text-white.rounded.mb-4.w-fit.p-2.mx-auto {:type "submit" :on-click submit-handler} "Login"]]]]])