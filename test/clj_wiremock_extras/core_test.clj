(ns clj-wiremock-extras.core-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [clj-wiremock-extras.core :refer [first-call second-call]]))

(deftest has-expected-core-functions
  (testing "has imported the various utilities from the other namespaces"
    (let [empty-stub {}
          first-call-stub (first-call empty-stub)
          second-call-stub (second-call empty-stub)]
      (is (not= empty-stub first-call-stub second-call-stub)))))
