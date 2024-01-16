(ns clj-wiremock-extras.core
  (:require
   [potemkin.namespaces :refer [import-vars]]))

(import-vars
  [clj-wiremock-extras.times first-call second-call])