(ns yaml2dotenv.main-test
  (:require [cljs.test :refer (deftest is)]
            [clojure.string :as str]
            [yaml2detenv.transform :refer [yaml2dotenv]]))


(def example-yaml "spring:
    config:
        activate:
            on-profile: test
application:
  internal:
    api:
      username: 'john'
      password: 'pasw0rd'
    app-index-url: http://app:8080
    api-index-url: http://api:8080
")

(def target-dotenv
  (str/join "\n"
            ["APPLICATION_INTERNAL_APIINDEXURL=http://api:8080"
             "APPLICATION_INTERNAL_API_PASSWORD=pasw0rd"
             "APPLICATION_INTERNAL_API_USERNAME=john"
             "APPLICATION_INTERNAL_APPINDEXURL=http://app:8080"
             "SPRING_CONFIG_ACTIVATE_ONPROFILE=test"]))

(deftest a-failing-test
  (is (= (yaml2dotenv example-yaml) target-dotenv)))
