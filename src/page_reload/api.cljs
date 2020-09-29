(ns page-reload.api
  (:require
   [ajax.core :as ajax]))

(defonce !data (atom nil))


(def url (-> js/window .-location .-href))


(defn log [& args]
  (apply js/console.log (into ["[page-reload]"] args)))

(declare schedule-load-data)


(defn on-data-received [data]
  (if-let [prev-data @!data]
    (when (not= data prev-data)
      (log "Page on the server changed. Reloading...")
      (.reload js/location))
    (reset! !data data))
  (schedule-load-data))


(defn output-div [text]
  (let [div (-> js/document (.createElement "div"))
        style (-> div .-style)
        text (-> js/document (.createTextNode (str text)))]
    (set! (.-fontFamily style) "monospace")
    (set! (.-whiteSpace style) "pre-wrap")
    (set! (.-padding style) "1rem")
    (.appendChild div text)
    div))


(defn html-div [html]
  (let [div (-> js/document (.createElement "div"))
        style (-> div .-style)]
    (set! (.-padding style) "1rem")
    (set! (.-innerHTML div) html)
    div))


(defn on-error [{:keys [failure status status-text response] :as data}]
  (log "ERROR" data)
  (let [body (-> js/document .-body)]
    (set! (.-innerHTML body) "")
    (.appendChild body (output-div (str failure)))
    (.appendChild body (output-div (str status " " status-text)))
    (.appendChild body (html-div response)))
  (reset! !data data)
  (schedule-load-data))


(defn load-data []
  (ajax/GET url {:handler on-data-received
                 :error-handler on-error}))


(defn schedule-load-data []
  (js/setTimeout #(load-data) 1000))


(schedule-load-data)
;; (on-error "boom dada")
(log "Started. Polling" url)
