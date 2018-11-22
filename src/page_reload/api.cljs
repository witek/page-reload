(ns page-reload.api
  (:require
   [ajax.core :refer [GET]]))

(defonce !data (atom nil))


(defn on-data-received [data]
  (if-let [prev-data @!data]
    (when (not= data prev-data)
      (.log js/console "page-reload: Page on the server changed. Reloading...")
      (.reload js/location))
    (reset! !data data))
  (js/setTimeout #(GET (-> js/window .-location .-href) {:handler on-data-received}) 1000))


(defn- load-initial-data [url]
  (GET url {:handler on-data-received}))


(defn ^:export watch []
  (let [url (-> js/window .-location .-href)]
    (.log js/console "page-reload: Starting polling:" url)
    (load-initial-data url)))
