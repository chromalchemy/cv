(ns cv.home
  (:require
    [lambdaisland.hiccup :as hiccup]
    [lambdaisland.ornament :as o :refer [defstyled]]
    [clojure.string :as string]
    [flatland.useful.seq :refer [partition-between]])
  (:use [com.rpl.specter]
        [cv.data]))

(defstyled title :div
  ([]
   [:<>
    [:h1 "Ryan Martin"]
    [:h2 "Full Stack Developer"]]))

(defstyled resume-link :a
  ([]
   [:<>
    {:href "Resume.pdf"}
    "Resume (PDF)"]))

(do
  (def home-hiccup
    [:html
     [:head]
     [:body
      [title]
      [resume-link]]])

  (->> home-hiccup
    (hiccup/render)
    (spit "docs/index.html")))

