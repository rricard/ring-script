(defproject ring-node-adapter "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2644"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]]

  :plugins [[lein-cljsbuild "1.0.4"]
            [com.cemerick/clojurescript.test "0.3.3"]]

  :cljsbuild {
    :builds [{:source-paths ["src" "test"]
              :compiler {
                :output-to "target/ring_node_adapter/testable.js"
                :optimizations :simple
                :pretty-print true}}]
    :test-commands {"unit-tests" ["node" :node-runner
                                  "target/ring_node_adapter/testable.js"]}})
