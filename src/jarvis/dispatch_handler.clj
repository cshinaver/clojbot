(ns jarvis.dispatch-handler
  (:require [jarvis.irc :as irc]))


(def command-words #{"buify"})
(def handle-message-link? (comp some? irc/pull-link-from-message))

(defn handle-message-link
  [recipient msg]
  (irc/send-buified-image-link-from-msg recipient msg))

(def handle-whose-better-than-bobbit?
  (partial irc/message-contains? #"who's better than bobbit?"))

(defn handle-whose-better-than-bobbit
  [recipient]
  (irc/send-message recipient "I am."))

(defn usage
  []
  '("Usage:"
    "        (buify url)    Returns buified image"
    "        (troll-bobbit) Trolls bobbit"))

(defn troll-bobbit
  []
  (irc/send-message "#lug" "bobbit: Do you have a purpose?"))

(def handle-lisp? (comp some? (partial re-find #"\([a-zA-Z-]+ ?(.*)\)")))

(defn handle-lisp
  [recipient msg]
  (let [m (re-find #"\(([a-zA-Z-]+) ?(.*)\)" msg)
        call (second m)
        arg (nth m 2 nil)]
    (cond
      (= call "buify") (irc/send-buified-image-link-from-msg recipient arg)
      (= call "usage") (doall
                        (map
                         (partial irc/send-message recipient)
                         (usage)))
      (= call "troll-bobbit") (troll-bobbit)
      :else (doall (map (partial irc/send-message recipient) (usage))))))

(defn dispatch-handler
  [type s]
  (let [msg (:text s)
        sender (:nick s)
        link (irc/pull-link-from-message msg)
        recipient (irc/determine-recipient s)]
    (cond
      (handle-lisp? msg) (handle-lisp recipient msg)
      (and (not= irc/username sender) (or (irc/message-contains? #"hotbot:" msg) (irc/message-contains? #"@hotbot" msg)))
      (cond
        (handle-message-link? msg) (handle-message-link recipient msg) 
        (handle-whose-better-than-bobbit? msg) (handle-whose-better-than-bobbit recipient)
        :else (irc/send-message recipient "wut")))))

