(ns jarvis.irc
  (:require [irclj.core :as irclj]
            [jarvis.util :as util]))

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

(defn pull-link-from-message
  [msg]
  (re-find #"https?://\S*" msg))

(defn send-buified-image-link-from-msg
  "Finds image link in message and sends buified link to user"
  [user msg]
  (let [link (pull-link-from-message msg)
        buified-link (util/buify-image link)]
    (send-message user buified-link)))

(defmacro debug-slack
  [s & args]
  `(do
     (prn ~s)
     ~@args))

(defn determine-recipient
  [s]
  (if (= (:target s) username)
    (:nick s)
    (:target s)))

(defn message-contains?
  [re msg]
  (if (not (nil? (re-find re msg)))
    true
    false))

(defn connect-to-slack
  "Main method that should be called at beginning of the app"
  []
  (connect
   {:privmsg
    (fn [type s]
      (let [msg (:text s)
            user (:nick s)
            link (pull-link-from-message msg)
            recipient (determine-recipient s)]
        (if (or (not (nil? (re-find #"hotbot:" msg))) (message-contains? #"@hotbot" msg))
          (cond
            (not (nil? link)) (send-buified-image-link-from-msg (determine-recipient s) (:text s))
            :else (send-message (determine-recipient s) msg)))))}))

