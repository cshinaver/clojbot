(ns jarvis.core
  (:require [irclj.core :as irc])
  (:gen-class))

(def connection
  (irc/connect
   "irc.freenode.net"
   6667
   "dopemopemope"
   :callbacks
   {:privmsg (fn [type s] (prn s))}))

(irc/message connection "austrinus" "hi")

(irc/quit connection)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
