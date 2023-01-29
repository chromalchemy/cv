(ns cv.home
  (:require [lambdaisland.hiccup :as hiccup]))

(do
  (def home-hiccup
    [:html
     [:head]
     [:body
      [:a {:href "Ryan Martin Resume.pdf"} "Resume (PDF)"]
      [:div "hello github!"]]])

  (->> home-hiccup
    (hiccup/render)
    (spit "docs/index.html")))