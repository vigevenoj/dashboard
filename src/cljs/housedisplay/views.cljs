(ns housedisplay.views
  (:require
   [re-frame.core :as re-frame]
   [housedisplay.subs :as subs]
   ))

; pi foundation screen is 800x480


(defn single-arrival [arrival]
  ^{:key (:id arrival)}
  [:div {:style {:display "inline-block"
                 :border "1px dashed blue"}}
   [:span (:id arrival)]
   [:span (:shortSign arrival)]
   ]
  )

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        arrivals (re-frame/subscribe [::subs/arrivals])]
    [:div
     [:h1 "Hello from " @name]
     [:div#bus-info
     [:div {:on-click #(re-frame/dispatch [:get-bus])
            :style {:width "100px"
                    :height "100px"
                    :display "inline-block"
                    :background-color "black"
                    :cursor "pointer"}}
      (.log js/console @arrivals)
      (.log js/console (seq? @arrivals))
      [:div#arrivals {:style {:border "1px dotted red"
                              :margin-left "100px"}}
       (map #(single-arrival %) @arrivals)]]]]))
