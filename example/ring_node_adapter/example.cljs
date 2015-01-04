(ns ring-node-adapter.example
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [<! chan >! close!]]
            [ring-node-adapter.core :refer [run]]))

(defn handler [{:keys [body] :as req}]
  (let [res-body (chan)]
    (go (loop [chnk (<! body)]
          (if chnk
            (do (>! res-body chnk) (recur (<! body)))
            (do (>! res-body (str req)) (close! res-body)))))
    {:status 200
     :headers {"content-type" "text/plain"}
     :body res-body}))

(defn -main [] (run handler {:port 8000}))
