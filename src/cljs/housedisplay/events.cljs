(ns housedisplay.events
  (:require [ajax.core :as ajax]
            [re-frame.core :as re-frame]
            [housedisplay.db :as db]
            [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
            [day8.re-frame.http-fx]
            [district0x.re-frame.interval-fx]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::load-config
 (fn [db [_ config]]
   (merge db {:api-key (-> "api-key" config)
              :stops (-> "stops" config)})))

(re-frame/reg-event-db
  :set-active-panel
 (fn [db [_ new-active-panel params]]
   (.log js/console (str "Setting active panel to " new-active-panel " with params [" params "]"))
   (merge db {:active-panel new-active-panel
              :route-params params})))


(re-frame/reg-event-fx
  :get-bus
 (fn [{db :db} _]
   {:http-xhrio {:method :get
                 :uri ; todo clean this up into a utility function somewhere?
                 (str "https://developer.trimet.org/ws/v2/arrivals?json=true&appID="
                      (:api-key db)
                      "&locIDs="
                      (clojure.string/join "," (:stops db)))
                 :timeout 5000
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::bus-fetched]
                 :on-failure [::bus-fetch-failure]}}))

(re-frame/reg-event-db
 ::bus-fetched
 (fn [db [_ response]]
   (merge db {:bus-data response})))

(re-frame/reg-event-db
  ::bus-fetch-failure
 (.log js/console "Failed to fetch buses"))

(re-frame/reg-event-db
  :update-clock
 (fn [db [_ new-time]]
   (assoc db :current-time new-time)))

(defn dispatch-update-clock-event
  []
  (let [now (js/Date.)]
    (re-frame/dispatch [:update-clock now])))

(defonce do-clock-dispatch (js/setInterval dispatch-update-clock-event 1000))