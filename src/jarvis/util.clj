(ns jarvis.util
  [:require [clojure.java.shell :refer [sh]]])

(defn uuid [] (str (java.util.UUID/randomUUID)))

(defmacro use-tmp-dir
  [[d] & args]
  (let [parent-dir (System/getProperty "java.io.tmpdir")
        dirname (uuid)
        full-path (str parent-dir "/" dirname)]
   `(let [~d ~full-path]
      (sh "mkdir" ~full-path)
      (try
        ~@args
        (finally (sh "rm" "-rf" ~full-path))))))

(defn download-file
  "Downloads file using wget"
  [url output-name]
  (sh "wget" "-O" output-name url))

(defn upload-img
  "Uploads image to Imagebin api"
  [filepath]
  (->>
   (sh "curl" "-F" (str "file=@" filepath) "https://imagebin.ca/upload.php")
   :out
   clojure.string/split-lines
   second
   (re-find #"https:.*")))

(defn buify
  "uber unportable. 
   Uses go binary in my home folder as well as
   other terrible practices."
  [filepath output-name]
  (sh "sh" "-c"
      (clojure.string/join
       " "
       (list
        "/home/cshinave/bin/_chrisify"
        "-haar"
        "/home/cshinave/lib/chrisify/haarcascade_frontalface_alt.xml"
        "-faces"
        "/home/cshinave/faces"
        filepath
        "> "
        output-name))))

(defn buify-image
  "Buify's image at url and returns link to new image"
  [url]
  (use-tmp-dir
   [tmp]
   (let [imgname (str tmp "/" "img.jpg")
         output-imgname (str tmp "/" "buified.jpg")]
     (download-file url imgname)
     (buify imgname output-imgname)
     (upload-img output-imgname))))





