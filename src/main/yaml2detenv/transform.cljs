(ns yaml2detenv.transform
  (:require [clojure.string :as str]
            [cljs-yaml.core :as yaml]))


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


(defn yaml2dotenv [yaml-content]
  (let [[status deserialized]
        (-> yaml-content
            yaml/deserialize)]
    (if (= :ok status)
      (let [flattened
            (-> deserialized
                (flatten-paths "_")
                (update-keys name)
                (update-keys #(str/replace % #"-" ""))
                (update-keys str/upper-case))
            dotenv
            (->> flattened
                 (map #(str/join "=" %))
                 (sort)
                 (str/join "\n"))]
        dotenv)
      deserialized)))
