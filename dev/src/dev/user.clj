(ns dev.user (:require
              [dev.system :refer [start-system stop-system]]))

;; where you can put some running function here
(start-system)

(stop-system)

#_(require '[clojure.tools.deps.alpha.repl :refer [add-libs]])
#_(comment 
    (add-libs '{integrant/integrant {:mvn/version "0.8.0"}})
    ,)