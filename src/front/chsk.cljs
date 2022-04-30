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
  (.log js/console "Debug:" (str msg))
  (-> (.querySelector js/document "#message-area")
      (.insertAdjacentHTML  "beforeend" (str "<li>" (str msg) "</li>"))))
;; define event-msg-handler.

(defmulti -event-msg-handler
  "Multimethod to handle Sente `event-msg`s"
  :id ; Dispatch on event-id
  )
(defmethod -event-msg-handler :default
  [{:as ev-msg :keys [ id ?data]}]
  (->output ?data))

(defn event-msg-handler
  "Wraps `-event-msg-handler` with logging, error catching, etc."
  [{:as ev-msg :keys [id ?data event]}]
  (.log js/console "received event, id: " (str id) ", data: " (str ?data) ", event: " (str event))
  (-event-msg-handler ev-msg))


(defonce router_ (atom nil))
(defn  stop-router! [] (when-let [stop-f @router_] (stop-f)))
(defn start-router! []
  (stop-router!)
  (reset! router_
          (sente/start-client-chsk-router!
           ch-chsk event-msg-handler)))

(defn start! [] (start-router!))


