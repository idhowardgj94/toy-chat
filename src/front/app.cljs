(ns front.app (:require [rum.core :as rum]
                        [cljs.core.async :as async :refer (<! >! put! chan)]
                        [taoensso.sente  :as sente :refer (cb-success?)] 
                        [taoensso.encore :as encore :refer-macros (have have?)]
                        [taoensso.timbre :as timbre :refer-macros (tracef debugf infof warnf errorf)])); <--- Add this))


(def output-el (.getElementById js/document "output"))

(defn ->output! [fmt & args]
  (let [msg (apply encore/format fmt args)]
    (timbre/debug msg)
    (aset output-el "value" (str "• " (.-value output-el) "\n" msg))
    (aset output-el "scrollTop" (.-scrollHeight output-el))))

(def ?csrf-token
  (when-let [el (.getElementById js/document "sente-csrf-token")]
    (.getAttribute el "data-csrf-token")))

(let [{:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket-client!
       "/chsk" ; Note the same path as before
       ?csrf-token
       {:type :auto ; e/o #{:auto :ajax :ws}
        })]

  (def chsk       chsk)
  (def ch-chsk    ch-recv) ; ChannelSocket's receive channel
  (def chsk-send! send-fn) ; ChannelSocket's send API fn
  (def chsk-state state)   ; Watchable, read-only atom
  )

(when-let [target-el (.getElementById js/document "btn2")]
  (.addEventListener target-el "click"
                     (fn [ev]
                       (->output! "Button 2 was clicked (will receive reply from server)")
                       (chsk-send! [:example/button2 {:had-a-callback? "indeed"}] 5000
                                   (fn [cb-reply] (->output! "Callback reply: %s" cb-reply))))))

(rum/defc app
  []
  [:div.text-lg "hello, world"])