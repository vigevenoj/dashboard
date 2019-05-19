(ns housedisplay.db)

(def default-db
  {:name "re-frame"
   :current-time (js/Date.)
   :api-key nil
   :stops nil
   :valid-config false
   :active-panel :home
   :route-params nil
   :bus-data nil})
