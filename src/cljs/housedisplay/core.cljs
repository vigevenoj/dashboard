(ns housedisplay.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [housedisplay.events :as events]
   [housedisplay.views :as views]
   [housedisplay.config :as config]
   ))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init [config-json]
  (let [config (js->clj config-json)
        api-key (-> "api-key" config)
        stops (-> "stops" config)]
    (.log js/console config)
    (re-frame/dispatch-sync [::events/initialize-db])
    (re-frame/dispatch [::events/load-config config])
    (dev-setup)
    (mount-root)))
