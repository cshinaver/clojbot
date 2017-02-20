(ns jarvis.irc
  (:require [irclj.core :as irclj]))

(def username "hotbot")
;(def host "irc.freenode.net")
(def host "")
(def pass "")
(def port 6667)

(defn connect
  "Connects to irc using definitions.
   Callbacks shoud be hashmap that take 2 args."
  [callbacks]
  (def connection
    (irclj/connect
     host
     port
     username
     :pass
     pass
     :callbacks
     callbacks)))

(defn quit
  "Kills connection to irc"
  []
  (irclj/quit connection))

(defn send-message
  "Sends message to user"
  [username msg]
  (irclj/message connection username msg))

(defn connect-with-basic-privmsg-callback
  "Connects to irc with privmsg callback
   that prints out all private messages
   to console"
  []
  (connect
   {:privmsg (fn [type s] (prn (str (:nick s) ": " (:text s))))}))



