(ns clj-wiremock-extras.times)

(defn first-call
  [mock]
  (assoc mock :state {:new "1"}))

(defn second-call
  [mock]
  (assoc mock :state {:new "2" :required "1"}))
