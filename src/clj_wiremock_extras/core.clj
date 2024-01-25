(ns clj-wiremock-extras.core
  (:require
   [potemkin.namespaces :refer [import-fn]]
   [clj-wiremock-extras.times :as times]
   [clj-wiremock-extras.near-misses :as near-misses]
   [clj-wiremock-extras.filters :as filters]))

(import-fn times/first-call)
(import-fn times/second-call)
(import-fn near-misses/near-misses)
(import-fn filters/requests-by-method)
(import-fn filters/requests-by-url)
(import-fn filters/filter-request-journal)
