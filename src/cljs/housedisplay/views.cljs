(ns housedisplay.views
  (:require
   [re-frame.core :as re-frame]
   [housedisplay.subs :as subs]
   [housedisplay.trimet.core :as trimet]
   [cljs-time.core]
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
    [:h1.clock time-string]))

(defn single-arrival [arrival]
  ^{:key (:id arrival)}
  [:div {:style {:display "inline-block"
                 :border "1px dashed blue"}}
   [:div {:style {:margin "10px" }} (:id arrival)]
   [:div {:style {:margin "10px" }} (:shortSign arrival)]
   [:div
    (str "soonest bus is "
         (fmt/unparse arrival-format
                      (c/from-long
                       (trimet/get-soonest-bus arrival))))]
   ; this fails if the now > soonest, should check that
   [:div (str (cljs-time.core/in-minutes (cljs-time.core/interval (cljs-time.core/now) (c/from-long (trimet/get-soonest-bus arrival)))) "minutes ")]
;   [:div (:estimated arrival)]
;   [:div (:scheduled arrival)]
   ]
  )

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        arrivals (re-frame/subscribe [::subs/arrivals])]
    [:div
     [clock]
     [:div#bus-info
     [:div {:on-click #(re-frame/dispatch [:get-bus])
            :style {:width "100px"
                    :height "100px"
                    :display "inline-block"
                    :background-color "black"
                    :cursor "pointer"}}
      [:div#arrivals {:style {:border "1px dotted red"
                              :margin-left "100px"}}
       (map #(single-arrival %) @arrivals)]]]]))
