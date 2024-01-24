(def version
  (or (System/getenv "VERSION")
      "0.0.0"))

(defproject uk.co.jordanrobinson/clj-wiremock-extras version
  :description "Extras, helpers and convenience methods for clj-wiremock."
  :url "https://github.com/jordanrobinson/clj-wiremock-extras"

  :license {:name "The MIT License"
            :url "https://opensource.org/licenses/MIT"}

  :dependencies [[org.clojure/clojure "1.11.1"]
                 [kelveden/clj-wiremock "1.8.0"]
                 [potemkin "0.4.6"]
                 [camel-snake-kebab "0.4.2"]]

  :plugins [[lein-cloverage "1.2.3"]
            [lein-shell "0.5.0"]
            [lein-ancient "0.7.0"]
            [lein-changelog "0.3.2"]
            [lein-eftest "0.5.9"]
            [lein-codox "0.10.8"]
            [lein-kibit "0.1.8"]
            [lein-bikeshed "0.5.2"]]

  :profiles
  {:shared {:dependencies
            [[org.clojure/clojure "1.11.1"]
             [freeport "1.0.0"]
             [eftest "0.6.0"]
             [clj-http "3.12.3"]]}
   :dev [:shared {:source-paths ["dev"]
                  :eftest {:multithread? false}}]
   :test [:shared {:eftest {:multithread? false}}]

   :prerelease
   {:release-tasks
    [["shell" "git" "diff" "--exit-code"]
     ["change" "version" "leiningen.release/bump-version" "rc"]
     ["change" "version" "leiningen.release/bump-version" "release"]
     ["vcs" "commit" "Pre-release version %s [skip ci]"]
     ["vcs" "tag"]
     ["deploy"]]}
   :release
   {:release-tasks
    [["deploy"]]}}

  :target-path "target/%s/"

  :bikeshed {:max-line-length 120}

  :deploy-repositories
  {"releases" {:url "https://repo.clojars.org"
               :username :env/clojars_deploy_username
               :password :env/clojars_deploy_token
               :sign-releases false}})
