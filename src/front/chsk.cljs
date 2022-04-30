(ns front.chsk (:require [taoensso.sente  :as sente :refer (cb-success?)]))


(let [{:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket-client!
       "/chsk" ; Note the same path as before
       nil
       {:type :auto ; e/o #{:auto :ajax :ws}
        })]

  (def chsk       chsk)
  (def ch-chsk    ch-recv) ; ChannelSocket's receive channel
  (def chsk-send! send-fn) ; ChannelSocket's send API fn
  (def chsk-state state)   ; Watchable, read-only atom
  )

(defn ->output
  [msg]
  (let [t (.-name msg)]
    (.log js/console "try to see the msg.name: " t ", " (type t))
    (-> (.querySelector js/document "#message-area")
        (.insertAdjacentHTML  "beforeend" (str "<li>" t "</li>")))))
;; define event-msg-handler.

(defmulti -event-msg-handler
  "Multimethod to handle Sente `event-msg`s"
  :id ; Dispatch on event-id
  )
(defmethod -event-msg-handler :default
  [{:as ev-msg :keys [ id]}]
  (->output id))

(defn event-msg-handler
  "Wraps `-event-msg-handler` with logging, error catching, etc."
  [{:as ev-msg :keys [id ?data event]}]
  (.log js/console "received event, id: " id ", data: " ?data ", event: " event)
  (-event-msg-handler ev-msg))


(defonce router_ (atom nil))
(defn  stop-router! [] (when-let [stop-f @router_] (stop-f)))
(defn start-router! []
  (stop-router!)
  (reset! router_
          (sente/start-client-chsk-router!
           ch-chsk event-msg-handler)))

(defn start! [] (start-router!))


