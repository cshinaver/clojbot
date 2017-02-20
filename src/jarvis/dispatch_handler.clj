(ns jarvis.dispatch-handler
  (:require [jarvis.irc :as irc]))

(declare handle-message-link handle-whose-better-than-bobbit)

(defn dispatch-handler
  [type s]
  (let [msg (:text s)
        user (:nick s)
        link (irc/pull-link-from-message msg)
        recipient (irc/determine-recipient s)]
    (if (or (irc/message-contains? #"hotbot:" msg) (irc/message-contains? #"@hotbot" msg))
      (cond
        (not (nil? link)) (handle-message-link recipient msg) 
        (irc/message-contains? #"who's better than bobbit?" msg) (handle-whose-better-than-bobbit "bobbit")
        :else (irc/send-message recipient msg)))))

(defn handle-message-link
  [recipient msg]
  (irc/send-buified-image-link-from-msg recipient msg))

(defn handle-whose-better-than-bobbit
  [recipient]
  (irc/send-message recipient "I am."))
