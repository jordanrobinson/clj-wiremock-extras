(ns clj-wiremock-extras.near-misses
  (:require [clj-http.client :as http]
            [clj-wiremock.core :as wmk]
            [clj-wiremock.server :as wmk-server]
            [cheshire.core :as cheshire]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as csk-extras]))

(defn near-misses
  ([]
   (near-misses (wmk/root-server)))
  ([server]
   (let [url (wmk-server/admin-url server "/requests/unmatched/near-misses")]
     (->> url
       (http/get)
       (:body)
       (cheshire/decode)
       (csk-extras/transform-keys csk/->kebab-case-keyword)
       (:near-misses)))))