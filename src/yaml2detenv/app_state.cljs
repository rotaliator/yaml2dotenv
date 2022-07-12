(ns yaml2detenv.app-state
  (:require [reagent.core :as r]))

(def initial-state
  {:yaml           ""
   :dotenv         ""
   :separator      "_"
   :to-camel-case? false})

(defonce app-state (r/atom initial-state))
