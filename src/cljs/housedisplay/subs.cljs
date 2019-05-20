(ns housedisplay.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
  ::current-time
 (fn [db]
   (:current-time db)))

(re-frame/reg-sub
  ::valid-config
 (fn [db]
   (:valid-config db)))

(re-frame/reg-sub
  ::arrivals
 (fn [db]
   (:arrival (:resultSet (:bus-data db)))))

(re-frame/reg-sub
  ::bus-data
 (fn [db]
   (:bus-data db)))

(re-frame/reg-sub
  ::bus-check-enabled
 (fn [db]
   (:bus-check-enabled db)))

(re-frame/reg-sub
  ::last-bus-check
 (fn [db]
   (:queryTime (:resultSet (:bus-data db)))))