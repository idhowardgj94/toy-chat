(ns com.auth (:require
              [buddy.auth.backends :as backends]
              [taoensso.timbre :as timbre]
              [buddy.auth :refer [authenticated? throw-unauthorized]]))

(defonce mockAuthData {:username "howard" :password "eva1219"})

(defn auth?
  [username password]
  (if (and (= username (mockAuthData :username)) (= password (mockAuthData :password)))
    true
    false))

(defn my-authfn
  [_ authdata]
  (let [username (:username authdata)
        password (:password authdata)]
    (timbre/info "myauth, " username ", " password)
    (when (auth? username password)
      (keyword username))))

(def backend (backends/basic {:realm "MyApi"
                              :authfn my-authfn}))