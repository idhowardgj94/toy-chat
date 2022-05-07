(ns front.app (:require 
               [rum.core :as rum :refer [defc]]
               [front.route :refer [match]]))


;; (def output-el (.getElementById js/docuoment "output"))

(defn did-mount-app
  []
  (.log js/console (clj->js @match))
  (.log js/console "hello"))

(defc app < rum/reactive
  {:did-mount did-mount-app}
  []
  [:div 
   (if @match
     (let [view (:view (:data (rum/react match)))]
       (view (rum/react match)))
     "Not found")])

#_(require '[clojure.tools.deps.alpha.repl :refer [add-libs]])
#_(comment
    (print "hello")
    (require '[ajax.core :refer [GET POST]])
    (GET "https://catfact.ninja/fact")
    )