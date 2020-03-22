(ns webdev.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.handler.dump :refer [handle-dump]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]))

(defn greet [req]
  {:status 200 :body "Hello, wepa !" :headers {}})

(defn yo [req]
  (let [name (get-in req [:route-params :name])]
    {:status 200 :body (str "Yo! " name "!") :headers {}}))

(defn goodbye [req]
  {:status 200 :body "Goodbye, Cruel World!" :headers {}})

(defn about [req]
  {:status 200 :body "About" :headers {}})

(def ops {"+" +
         ":" /
          "-" -
         "*" *})

(defn calc [req]
  (let [a (Integer. (get-in req [:route-params :a]))
        b (Integer. (get-in req [:route-params :b]))
        op (get-in req [:route-params :op])
        f (get ops op)]
    (if f
      {:status 200 :body (str (f a b)) :headers {}}
      {:status 404 :body (str "No op: " op) :headers {}})))

(defroutes app
   (GET "/" [] greet)
   (GET "/yo/:name" [] yo)
   (GET "/goodbye" [] goodbye)
   (GET "/calc/:a/:op/:b" [] calc)
   (GET "/about" [] about)
   (GET "/request" [] handle-dump)
   (not-found "Page not found"))

(defn -main [port]
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))
