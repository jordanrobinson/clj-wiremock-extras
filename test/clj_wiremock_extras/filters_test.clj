(ns clj-wiremock-extras.filters-test
  (:require
   [clj-wiremock-extras.filters :as filters]
   [clj-wiremock.core :as wmk]
   [freeport.core :refer [get-free-port!]]
   [clojure.test :refer [deftest is testing use-fixtures]]
   [clj-http.client :as http]))

(def wiremock-port (get-free-port!))
(def wiremock-url (str "http://localhost:" wiremock-port))

(use-fixtures :once
  (partial wmk/wiremock-fixture [{:port wiremock-port}]))

(deftest filter-by-method
  (testing "filters requests made by the method or url given"
    (let [get-stub {:req [:GET "/foo"]
                    :res [200 {:body "OK"}]}
          post-stub {:req [:POST "/bar"]
                     :res [200 {:body "OK"}]}
          delete-stub {:req [:DELETE "/foo"]
                       :res [200 {:body "OK"}]}]
      (wmk/with-stubs [get-stub post-stub delete-stub]
        (http/get (str wiremock-url "/foo"))
        (http/post (str wiremock-url "/bar"))
        (http/delete (str wiremock-url "/foo"))

        (is (= (count (filters/requests-by-method :get)) 1))
        (is (= (count (filters/requests-by-method :post)) 1))
        (is (= (count (filters/requests-by-method :delete)) 1))
        (is (= (count (filters/requests-by-url "/foo")) 2))
        (is (= (count (filters/requests-by-url "/bar")) 1))))))
