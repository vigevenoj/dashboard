(ns housedisplay.routes
  (:require [re-frame.core :as re-frame]
            [bidi.bidi :as bidi]
            ))

; we dont' need this yet so this namespace isn't included anywhere
(def routes ["/" {"" :home}])