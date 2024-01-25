(ns clj-wiremock-extras.core-test
  (:require
   [clj-wiremock-extras.filters :as filters]
   [clj-wiremock-extras.near-misses :as near-misses]
   [clojure.test :refer [deftest is testing]]
   [clj-wiremock-extras.core :refer [first-call second-call near-misses
                                     requests-by-method requests-by-url filter-request-journal]]
   [clj-wiremock-extras.times :as times]
   [clojure.repl :refer [doc]]))

(defmacro out= [& args]
  `(= ~@(map (fn [x] `(with-out-str ~x)) args)))

(deftest has-expected-core-functions
  (testing "has imported the various utilities from the other namespaces"
    (is (out= (doc times/first-call) (doc first-call)))
    (is (out= (doc times/second-call) (doc second-call)))
    (is (out= (doc near-misses/near-misses) (doc near-misses)))
    (is (out= (doc filters/requests-by-method) (doc requests-by-method)))
    (is (out= (doc filters/requests-by-url) (doc requests-by-url)))
    (is (out= (doc filters/filter-request-journal) (doc filter-request-journal)))))
