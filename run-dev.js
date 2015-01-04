try {
  require("source-map-support").install();
} catch(err) {}
require('./target/dev/goog/bootstrap/nodejs');
require('./target/dev.js');
goog.require("ring_node_adapter.example");
ring_node_adapter.example._main();
