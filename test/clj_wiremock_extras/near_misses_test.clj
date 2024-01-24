(ns clj-wiremock-extras.near-misses-test
  (:require
   [clj-wiremock-extras.near-misses :refer [near-misses]]
   [clj-wiremock.core :as wmk]
   [freeport.core :refer [get-free-port!]]
   [clojure.test :refer [deftest is testing use-fixtures]]
   [clj-http.client :as http]))

(def wiremock-port (get-free-port!))
(def wiremock-url (str "http://localhost:" wiremock-port))

(use-fixtures :once
  (partial wmk/wiremock-fixture [{:port wiremock-port}]))

(def correct-path "/foo")
(def miss-path "/bar")

(defn- http-get
  [path]
  (:status (http/get (str wiremock-url path) {:throw-exceptions false})))

(deftest near-misses-test
  (testing "near misses returns empty list when no near misses"
    (let [stub {:req [:GET "/foo"]
                :res [200 {:body "OK"}]}]

      (wmk/with-stubs [stub]
        (http-get correct-path)
        (is (= [] (near-misses))))))

  (testing "near misses returns values of a near miss when one occurs"
    (let [stub {:req [:GET "/foo"]
                :res [200 {:body "OK"}]}]

      (wmk/with-stubs [stub]
        (http-get miss-path)
        (let [misses (near-misses)]
          (is (= 1 (count misses)))
          (is (= "/bar" (get-in misses [0 :request :url])))
          (is (= "/foo" (get-in misses [0 :stub-mapping :request :url-path])))))))

  (testing "near misses returns values of multiple near misses in descending order"
    (let [stub {:req [:GET "/foo"]
                :res [200 {:body "OK"}]}]

      (wmk/with-stubs [stub]
        (http-get "/1")
        (http-get correct-path)
        (http-get "/3")
        (let [misses (near-misses)]
          (is (= 2 (count misses)))
          (is (= "/foo" (get-in misses [0 :stub-mapping :request :url-path])))
          (is (= "/3" (get-in misses [0 :request :url])))
          (is (= "/foo" (get-in misses [1 :stub-mapping :request :url-path])))
          (is (= "/1" (get-in misses [1 :request :url]))))))))
