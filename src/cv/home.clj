(ns cv.home
  (:require
    [lambdaisland.hiccup :as hiccup]
    [lambdaisland.ornament :as o :refer [defstyled]]
    [clojure.string :as string]
    [garden.core :refer [css]]
    [garden.compiler :as gc :refer [compile-css]]
    [cv.tw-preflight :refer [tw-preflight-3-css-str]]
    [flatland.useful.seq :refer [partition-between]])
  (:require
    [cv.data :as data]
    :reload
    [garden.selectors :as s])
  (:use [com.rpl.specter]))

(defstyled title :div
  :mb-2
  [:h1 :text-4xl]
  ([]
   [:<>
    [:h1 "Ryan Martin"]
    [:h2 "Full Stack Developer"]]))

(defstyled resume-link :a
  :block :text-red :text-blue-500 :underline :mb-4
  ([]
   [:<>
    {:href "Resume.pdf"}
    "PDF Document"]))

(defstyled section-title :h2
  :text-2xl
  :font-bold)

(defstyled subsection-title :h3
  :text-lg
  :font-bold
  :text-blue-600)

(defstyled sidebar-title subsection-title
  :mb-2 #_:text-white
  #_:bg-gray-700)

(defstyled skill-keywords :div
  :mb-4
  [:li :mb-6]
  ([]
   [:<>
    [sidebar-title "Additional Skills"]
    [:ul
     (for [x data/skill-keywords]
       [:li x])]]))



(defstyled experience :div
  :mb-4
  [:li :mb-2]
  [:p :mb-4 :pl-3]
  ([]
   [:<>
    [section-title "Experience"]
    (for [[t s] data/experience]
      [:div
       [subsection-title t]
       [:p s]])]))

(defstyled education :div
  :mb-4
  ([]
   [:<>
    [section-title "Education"]
    [:span data/education]]))


(defstyled column :div
  :h-full :p-4)

(defstyled left-col column
  :max-w-300px :bg-black :text-white :pl-8)

(defstyled right-col column
  :bg-white)

(defstyled cols :div
  :flex :gap-3)
  ;[(s/> (s/&) (s/div (s/first-child))) :w-33% :bg-gray-400]
  ;[(s/> (s/&) (s/div (s/last-child)))  :w-77% :bg-red-500]
  ;([left right]
  ; [:<>
  ;  left
  ;  right]))

(defstyled body :body
  :p-0 :m-0)

(defstyled cool-technologies :div
  [:a :font-bold :min-w-24 :underline :text-blue-700]
  [:.item :flex :gap-8 :mb-2]
  ([]
   [:<>
    [section-title "Technologies I'm interested in"]
    (for [{lib-name :name :keys [url subtitle]} (vals data/technologies-interested-in)]
      [:div.item
       [:a {:href url} lib-name]
       [:span subtitle]])]))

(defstyled skills-summary :div
  :mb-8
  [:.block :mb-4]
  [:.subtitle :mb-2]
  [:ul :list-outside]
  [:li :mb-4]
  ([]
   [:<>
    [section-title "Skills Summary"]
    (for [[subtitle experience-lines] data/skills-summary]
      [:div.block
       [subsection-title {:class "subtitle"} subtitle]
       [:ul
        (for [x experience-lines]
          [:li x])]])]))

;todo: make links live
(defstyled personal-info :div
  :mb-8
  [:.title :font-bold]
  [:.item :mb-4]
  ([]
   [:<>
    [sidebar-title "Personal Info"]
    (for [[k v] data/personal-info]
      [:div.item
       [:div.title k]
       [:div v]])]))

(def home-hiccup
  [:html
   [:head
    [:style tw-preflight-3-css-str]
    [:style (o/defined-styles)]
    [:script {:src "https://livejs.com/live.js" :type "text/javascript"}]]
   [body]
   [cols
    [left-col
     [title]
     [resume-link]
     [personal-info]
     [skill-keywords]]
    [right-col
     [skills-summary]
     [experience]
     [education]
     [cool-technologies]]]])

;#_
(->> home-hiccup
  (hiccup/render)
  (spit "docs/index.html"))
