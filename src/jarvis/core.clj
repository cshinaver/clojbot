(ns jarvis.core
  (:require [jarvis.irc :as irc])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (irc/connect-to-slack)
  (while true))
