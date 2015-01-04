# ring-script

A ring-like API for ClojureScript, Node.js & core.async.

## Overview

ring-script works in a very similar way than Clojure's ring making it easier to port existing ring middleware and application.

Usual ring libraries such as ring-core are likely to not work with ring-script as they often use Java interop. An ongoing effort is made in order to port tightly coupled JVM middlewares to Node.js. One other thing not supported by ring-script are InputStreams replaced by core.async channels.

One particular goal of ring-script is to run compojure with minimal porting.

## Setup

    lein cljsbuild auto

To start a Node REPL (requires rlwrap):

    ./scripts/repl

Clean project specific out:

    lein clean

Run:

    node run-dev.js

## License

Copyright © 2014 Robin Ricard

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
