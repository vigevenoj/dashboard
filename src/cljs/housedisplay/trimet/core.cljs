(ns housedisplay.trimet.core
  )

(defn handle-estimated [est]
  (if (nil? est)
    ##Inf
    est))

(defn get-soonest-bus
  "Parse an arrival for the soonest of scheduled or estimated"
  [arrival]
  (min (:scheduled arrival) (handle-estimated (:estimated arrival))))