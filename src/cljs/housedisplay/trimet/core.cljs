(ns housedisplay.trimet.core
  (:require [cljs-time.core :as t]
            [cljs-time.coerce :as c]
            [cljs-time.format :as fmt])
  )

(defn handle-estimated [est]
  (if (nil? est)
    ##Inf
    est))

(defn get-soonest-bus
  "Parse an arrival for the soonest of scheduled or estimated"
  [arrival]
  (min (:scheduled arrival) (handle-estimated (:estimated arrival))))

(defn time-until-bus
  "Print number of minutes until arrival time, or nil if passed"
  [arrival-time]
  (let [n (t/now)]
    (if (t/before? n arrival-time)
      (t/in-minutes (t/interval n arrival-time))
      nil)))