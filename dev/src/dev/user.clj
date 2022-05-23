(ns dev.user (:require
              [dev.system :refer [start-system stop-system]]))

;; where you can put some running function here
(start-system)

(stop-system)

(comment
  (require '[hikari-cp.core :refer :all])
  (require '[clojure.java.jdbc :as jdbc])
  ,)
#_(require '[clojure.tools.deps.alpha.repl :refer [add-libs]])
#_(comment 
    (add-libs '{hikari-cp {:mvn/version "2.14.0"}})
    (add-libs '{org.clojure/java.jdbc {:mvn/version "0.7.12"}})
    ,)
