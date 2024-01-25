# clj-wiremock-extras

Extras, helpers and convenience methods for [clj-wiremock](https://github.com/kelveden/clj-wiremock).

[![Clojars Project](https://img.shields.io/clojars/v/uk.co.jordanrobinson/clj-wiremock-extras.svg)](https://clojars.org/uk.co.jordanrobinson/clj-wiremock-extras)

## Add to your project

```clojure
  project.clj

  ...
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [kelveden/clj-wiremock "1.8.0"]
                 [uk.co.jordanrobinson/clj-wiremock-extras "0.0.6"]]
  ...
```

Deps, Maven, Gradle, etc methods shown on [clojars](https://clojars.org/uk.co.jordanrobinson/clj-wiremock-extras).

## Examples

### Near misses
```clojure
(:require
 [clj-wiremock.core :as wmk]
 [clj-wiremock-extras.core :refer [near-misses]])

;; grabs the near misses from the admin api (https://wiremock.org/docs/verifying/#near-misses)
;; some wiremock setup removed for brevity, see tests for more examples

(wmk/with-stubs [{:req [:GET "/foo"]
                :res [200 {:body "OK"}]}]
  
  (http/get (str wiremock-url "/bar"))
  (near-misses))
  ;; => [{:match-result 
  ;; {:distance 0.18292682926829268} 
  ;; :request {:absolute-url ... }
  ;; :stub-mapping {:request {:method "GET", :url-path "/foo"}
  ;;               ...
  ;;               :response {:body "OK", :status 200}
  ;;               ...}}]
```

### Filtering convenience functions

```clojure
(:require
 [clj-wiremock.core :as wmk]
 [clj-wiremock-extras.core :refer [requests-by-method requests-by-url]])

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

    (requests-by-method :get))
    ;; => [{:absoluteUrl ... :method "GET" ... :url "/foo"}]
  
    (count (requests-by-method :get)) ;; => 1

    (count (requests-by-url "/foo"))) ;; => 2
```
### Value change on repeated calls
```clojure
(:require
 [clj-wiremock.core :as wmk]
 [clj-wiremock-extras.core :refer [first-call second-call]])

(let [success-stub {:req [:GET "/foo"]
                    :res [200 {:body "OK"}]}
      failure-stub {:req [:GET "/foo"]
                    :res [500 {:body "Not OK"}]}]

  (wmk/with-stubs [(first-call failure-stub)
                   (second-call success-stub)]
    (http/get (str wiremock-url "/foo")) ;; => {:body "Not OK" :status 500 ...}
    (http/get (str wiremock-url "/foo")))) ;; => {:body "OK" :status 200 ...}
```

## License

Copyright © 2024 Jordan Robinson

Distributed under the MIT license.
