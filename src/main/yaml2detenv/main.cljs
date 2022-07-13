(ns yaml2detenv.main
  (:require [reagent.dom :as reagent-dom]
            [yaml2detenv.app-state :refer [app-state]]
            [yaml2detenv.transform :refer [yaml2dotenv]]))

(defn on-convert []
  (let [current-state @app-state
        yaml          (:yaml current-state)
        dotenv        (yaml2dotenv yaml)]
    (swap! app-state assoc :dotenv dotenv)))


(defn app []
  (let [current-state @app-state]
    [:div.container
     [:div.row
      [:h1 "yaml -> .env"]]
     [:div.row
      [:label {:for "yaml"} "yaml"]
      [:textarea {:name      :yaml
                  :id        :yaml
                  :rows      20
                  :cols      80
                  :value     (:yaml current-state)
                  :on-change #(swap! app-state assoc :yaml (.. % -target -value))}]
      [:label {:for "yaml"} ".env"]
      [:textarea {:name      :dotenv
                  :id        :dotenv
                  :rows      20
                  :cols      80
                  :value     (:dotenv current-state)
                  :read-only true}]
      [:div.row
       [:button
        {:on-click on-convert}
        "convert"]]]
     #_[:pre (pr-str current-state)]]))

(defn mount-root []
  (reagent-dom/render [app] (.getElementById js/document "root")))

(defn ^:dev/after-load init []
  (mount-root))
