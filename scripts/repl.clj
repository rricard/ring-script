(require
  '[cljs.repl :as repl]
  '[cljs.repl.node :as node])

(repl/repl* (node/repl-env)
  {:output-dir "target/dev"
   :optimizations :none
   :cache-analysis true
   :source-map true})
