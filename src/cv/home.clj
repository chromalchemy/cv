(ns cv.home
  (:require [lambdaisland.hiccup :as hiccup]))

(def home-hiccup
  [:html
   [:head]
   [:body
    [:div "hello github!"]]])

(->> home-hiccup
  (hiccup/render)
  (spit "docs/out/index.html"))