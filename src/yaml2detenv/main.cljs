(ns yaml2detenv.main
  (:require [reagent.dom :as reagent-dom]
            [yaml2detenv.app-state :refer [app-state]]
            [yaml2detenv.transform :refer [yaml2dotenv]]))

(defn on-convert [& _]
  (let [current-state @app-state
        yaml          (:yaml current-state)
        dotenv        (yaml2dotenv yaml
                            :separator (:separator current-state)
                            :to-camel-case? (:to-camel-case? current-state))]
    (swap! app-state assoc :dotenv dotenv)))

(defn app []
  [:div.container
   [:div.row
    [:h1 "yaml -> .env"]]
   [:div.row
    [:label {:for "yaml"} "yaml"]
    [:textarea {:name      :yaml
                :id        :yaml
                :rows      20
                :cols      80
                :value     (:yaml @app-state)
                :on-change #(swap! app-state assoc :yaml (.. % -target -value))}]
    [:label {:for "yaml"} ".env"]
    [:textarea {:name  :dotenv
                :id    :dotenv
                :rows  20
                :cols  80
                :value (:dotenv @app-state)}]
    [:div.row
     [:label {:for :to-camel-case} "separator"]
     [:input {:type      :text
              :id        :separator
              :name      :separator
              :value     (:separator @app-state)
              :on-change #(swap! app-state assoc :separator (.. % -target -value))}]
     [:label {:for :to-camel-case} "to camelCase?"]
     [:input {:type      :checkbox
              :id        :to-camel-case
              :name      :to-camel-case
              :checked   (:to-camel-case? @app-state)
              :on-change #(swap! app-state update :to-camel-case? not)}]]
    [:div.row
     [:button
      {:on-click on-convert}
      "convert"]]]
   #_[:pre (pr-str @app-state)]])

(defn mount-root []
  (reagent-dom/render [app] (.getElementById js/document "root")))

(defn ^:dev/after-load init []
  (mount-root))
