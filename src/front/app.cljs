(ns front.app (:require [rum.core :as rum]
                        [front.chsk :refer [start!]]
                        [taoensso.timbre :as timbre :refer-macros (tracef debugf infof warnf errorf)])); <--- Add this))


;; (def output-el (.getElementById js/document "output"))

(def counter (atom 1))

;; basic event handler
;; and how to reactive in rum.
(defn button-onclick-handler
  [_]
  (.log js/console "hello, handler")
  (swap! counter inc))

(defn did-mount-app
  []
  (.log js/console "app mount! will start chsk router....")
  (start!)
  (.log js/console "start chsk..."))

(rum/defc app < rum/reactive
  {:did-mount did-mount-app}
  []
  [:div [:h1.text-xl "This is a Sente test."]
   [:p.text-red "we will recieved message from backend and post here"]
   [:ul#message-area]
   [:p.text-blue "this is the atom count: " (rum/react counter)]
   [:button#add-count.bg-blue-500.hover:bg-blue-700.text-white.font-bold.py-2.px-4.rounded 
    {:on-click button-onclick-handler} "click me to add count"]]
  )

