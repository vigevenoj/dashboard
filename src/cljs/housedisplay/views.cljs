(ns housedisplay.views
  (:require
    [re-frame.core :as re-frame]
    [housedisplay.subs :as subs]
    [housedisplay.trimet.core :as trimet]
    [cljs-time.core :as time]
    [cljs-time.coerce :as c]
    [cljs-time.format :as fmt]))

; pi foundation screen is 800x480
(def arrival-format (fmt/formatters :hour-minute-second))

(defn clock []
  (let [t           @(re-frame/subscribe [::subs/current-time])
        time-string (-> t .toTimeString (clojure.string/split " ") first)]
    [:h1.clock.display-1.text-center {:style {:font "monospace" }} time-string]))

(defn single-arrival [arrival]
  ^{:key (:id arrival)}
  [:div.w-27
   [:div.card
    [:div.card-body
     [:h5.card-title (:shortSign arrival)]
     [:p.card-text
      (let [n (time/now)
            soonest (trimet/get-soonest-bus arrival)]
        (if (< n soonest) ; todo: check if less than 5 minutes
          (str
           ; todo: should make it red if departing soon
           ; or make it some other color if delayed?
           (time/in-minutes ; todo: or do 5 minute check here?
            (time/interval
             (time/now)
             (c/from-long
              (trimet/get-soonest-bus arrival))))
           " minutes ")
          "Bus passed"))]
     [:p.card-text
      (str "soonest bus is "
           (fmt/unparse arrival-format
                        (time/to-default-time-zone
                          (c/from-long
                           (trimet/get-soonest-bus arrival)))))]]]])

(defn main-panel []
  (let [name     (re-frame/subscribe [::subs/name])
        arrivals (re-frame/subscribe [::subs/arrivals])]
    [:div#top-wrapper.wrapper
     [clock]
     [:div.wrapper.d-flex
      [:div#sidebar-wrapper.bg-light.border-right
       [:div.list-group.list-group-flush
        [:a.list-group-item.list-group-item-action.bg-light
         {:href     "#"
          :on-click #(re-frame/dispatch [:get-bus])}
         "BUSES"]]]
      [:div#page-content-wrapper
       [:div.card-deck
        (map #(single-arrival %) @arrivals)]]]]))
