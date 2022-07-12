(ns yaml2detenv.transform
  (:require [clojure.string :as str]
            [cljs-yaml.core :as yaml]
            [camel-snake-kebab.core :as csk]))


;; https://andersmurphy.com/2019/11/30/clojure-flattening-key-paths.html
(defn flatten-paths
  ([m separator]
   (flatten-paths m separator []))
  ([m separator path]
   (->> (map (fn [[k v]]
               (if (and (map? v) (not-empty v))
                 (flatten-paths v separator (conj path k))
                 [(->> (conj path k)
                       (map name)
                       (str/join separator)
                       keyword) v]))
             m)
        (into {}))))


(defn yaml2dotenv [yaml-content & {:keys [separator
                                          to-camel-case?]
                                   :or   {separator      "_"
                                          to-camel-case? true}}]
  (let [[status deserialized]
        (-> yaml-content
            yaml/deserialize)]
    (if (= :ok status)
      (let [flattened (update-keys (flatten-paths deserialized separator) name)]
        (->> (if to-camel-case?
               (update-keys flattened csk/->camelCaseString)
               flattened)
             (map #(str/join "=" %))
             (sort)
             (str/join "\n")))
      deserialized)))
