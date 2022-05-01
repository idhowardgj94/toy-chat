(ns com.html-util)

(defn content-type-json
  "response as json type"
  [res]
  (assoc res :headers {"Content-Type" "application/json"}))

(defn head
  []
  [:head
   [:meta {:http-equiv "content-type" :content "text/html;charset=UTF-8"}]
   [:title "rum test"]
   [:link {:href "main.css" :rel "stylesheet"}]])

(defn page
  []
  [:html (head)
   [:body [:div#root ""] [:script {:src "main.js"}]]])