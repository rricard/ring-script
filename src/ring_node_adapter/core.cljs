(ns ring-node-adapter.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [chan put! <! close!]]
            [clojure.string :as str]))

(def http (js/require "http"))
(def url (js/require "url"))

(defn node-req-to-ring
  "Transform an incoming Node.js request to an async ring request"
  [incoming]
  (let [body-chan (chan)
        socket (.-socket incoming)
        parsed-url (.parse url (.-url incoming))
        ring-req {:server-port (.-localPort socket)
                  :server-name (.-localAddress socket)
                  :remote-addr (.-remoteAddress socket)
                  :uri (.-pathname parsed-url)
                  :query-string (.-query parsed-url)
                  :scheme :http ; todo: https features
                  :request-method (keyword
                                    (str/lower-case
                                      (.-method incoming)))
                  :ssl-cient-cert nil ; todo: https features
                  :headers (js->clj (.-headers incoming))
                  :body body-chan}]
    (.on incoming "data" (partial put! body-chan))
    (.on incoming "end" (partial close! body-chan))
    ring-req))

(defn ring-res-to-node
  "Transform an outgoing async ring response to a Node.js response
  by taking an existing node res object"
  [{:keys [status headers body]} node-res]
  (set! (.-statusCode node-res) status)
  (dorun (map #(.setHeader node-res % (get headers %))
              (keys headers)))
  (if (string? body)
    (.end node-res body)
    (go (loop [chnk (<! body)]
          (if chnk
            (do (.write node-res chnk) (recur (<! body)))
            (.end node-res)))))
  nil)

(defn build-node-handler
  "Return Node.js-like HTTP Handler"
  [ring-handler]
  (fn [node-req node-res]
    (ring-res-to-node
      (ring-handler (node-req-to-ring node-req))
      node-res)))

(defn run
  "Run a Node.js HTTP Server supporting async ring handlers"
  [handler {:keys [port]
            :or {port 8000}}]
  (.listen
    (.createServer http
                   (build-node-handler handler))
    port))
