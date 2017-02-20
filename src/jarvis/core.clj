(ns jarvis.core
  (:require [jarvis.irc :as irc]
            [jarvis.dispatch-handler :refer [dispatch-handler]])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (irc/connect-to-slack dispatch-handler)
  (while true))
