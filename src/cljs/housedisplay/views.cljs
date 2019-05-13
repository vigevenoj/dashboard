(ns housedisplay.views
  (:require
   [re-frame.core :as re-frame]
   [housedisplay.subs :as subs]
   [housedisplay.trimet.core :as trimet]
   [cljs-time.core :as time]
   [cljs-time.coerce :as c]
   [cljs-time.format :as fmt]
   ))

; pi foundation screen is 800x480
(def arrival-format (fmt/formatters :hour-minute-second))

(defn clock []
  (let [t @(re-frame/subscribe [::subs/current-time])
         time-string (-> @(re-frame/subscribe [::subs/current-time])
                    .toTimeString (clojure.string/split " ") first)
        ]
    [:h1.clock {:style {:font "monospace"}} time-string]))

(defn single-arrival [arrival]
  ^{:key (:id arrival)}
  [:div.w-30
   [:div.card
   [:div.card-body
    [:h5.card-title (:shortSign arrival)]
    ; this fails if the now > soonest, should check that
    [:p.card-text (str (time/in-minutes
                        (time/interval
                         (time/now)
                         (c/from-long
                          (trimet/get-soonest-bus arrival)))) " minutes ")]
    [:p.card-text (str "soonest bus is "
                       (fmt/unparse arrival-format
                                    (time/to-default-time-zone (c/from-long
                                     (trimet/get-soonest-bus arrival)))))]]]])

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        arrivals (re-frame/subscribe [::subs/arrivals])]
    [:div#top-wrapper.wrapper
     [clock]
     [:div.wrapper.d-flex
      [:div#sidebar-wrapper.bg-light.border-right
       [:div.list-group.list-group-flush
        [:a.list-group-item.list-group-item-action.bg-light {:href "#"
                                                             :on-click #(re-frame/dispatch [:get-bus])}
         "BUSES"]]]
      [:div#page-content-wrapper
       [:div.card-deck
        (map #(single-arrival %) @arrivals)]]]]))
