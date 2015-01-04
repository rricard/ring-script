(ns ring-node-adapter.example
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [<! chan >!]]
            [ring-node-adapter.core :refer [run]]))

(defn handler [{:keys [body] :as req}]
  (let [res-body (chan)]
    (go (loop []
          (let [chnk (<! body)]
            (if chnk
              (do (>! res-body chnk) (recur))
              (do (>! res-body (str req))
                  {:status 200
                   :headers {"content-type" "text/plain"}
                   :body (str req)})))))))

(defn -main [] (run handler {:port 8000}))
