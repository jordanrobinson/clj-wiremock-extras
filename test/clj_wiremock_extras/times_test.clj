(ns clj-wiremock-extras.times-test
  (:require
   [clj-wiremock-extras.times :refer [first-call second-call]]
   [clj-wiremock.core :as wmk]
   [freeport.core :refer [get-free-port!]]
   [clojure.test :refer [deftest is testing use-fixtures]]
   [clj-http.client :as http]))

(def wiremock-port (get-free-port!))
(def wiremock-url (str "http://localhost:" wiremock-port))

(use-fixtures :once
  (partial wmk/wiremock-fixture [{:port wiremock-port}]))

(defn- http-get
  []
  (:status (http/get (str wiremock-url "/foo") {:throw-exceptions false})))

(deftest first-call-only-replies-with-expected-on-first-call
  (testing "first-call responds with expected response on first time"
    (let [success-stub {:req [:GET "/foo"]
                        :res [200 {:body "OK"}]}
          failure-stub {:req [:GET "/foo"]
                        :res [500 {:body "Not OK"}]}]

      (wmk/with-stubs [failure-stub
                       (first-call success-stub)]
        (is (= 200 (http-get)))
        (is (= 500 (http-get)))))))

(deftest second-call-only-replies-with-expected-on-second-call
  (testing "second-call responds with expected response on second time"
    (let [success-stub {:req [:GET "/foo"]
                        :res [200 {:body "OK"}]}
          failure-stub {:req [:GET "/foo"]
                        :res [500 {:body "Not OK"}]}]

      (wmk/with-stubs [(first-call failure-stub)
                       (second-call success-stub)]
        (is (= 500 (http-get)))
        (is (= 200 (http-get)))
        (is (= 404 (http-get)))))))
