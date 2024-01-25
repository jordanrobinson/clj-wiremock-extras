(ns clj-wiremock-extras.filters
  (:require [clj-wiremock.core :as wmk]
            [clojure.string :as str]))

(defn filter-request-journal
  [predicates server]
  (let [server (or server (wmk/root-server))]
    (->> (wmk/request-journal server)
      (filter (apply every-pred predicates))
      (mapv :request))))

(defn requests-by-method
  [method & server]
  (filter-request-journal
    [(fn
      [{:keys [request]}]
      (= (str/upper-case (name method)) (:method request)))]
    server))

(defn requests-by-url
  [url & server]
  (filter-request-journal
    [(fn
      [{:keys [request]}]
      (= url (:url request)))]
    server))
