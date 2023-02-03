(ns cv.home
  (:require
    [lambdaisland.hiccup :as hiccup]
    [lambdaisland.ornament :as o :refer [defstyled]]
    [clojure.string :as string]
    [garden.core :refer [css]]
    [garden.compiler :as gc :refer [compile-css]]
    [cv.tw-preflight :refer [tw-preflight-3-css-str]]
    [cybermonday.core :as md]
    [flatland.useful.seq :refer [partition-between]])
  (:require
    [cv.data :as data]
    :reload
    [garden.selectors :as s])
  (:use [com.rpl.specter]))



(def font
  {:pt-mono "PT Mono"
   :pt-sans "PT Sans"
   :pt-serif "PT Serif"
   :pt-sans-narrow "PT Sans Narrow"})

(defstyled title :div
  :mb-2
  [:h1 :text-4xl
   {:font-family (font :pt-mono)} :ml-0 :pl-0]
  [:h2 {:font-family (font :pt-serif)} :tracking-wide :text-sm]
  ([]
   [:<>
    [:h1 "Ryan" [:br] "Martin"]
    [:h2 "Full Stack Developer"]]))

(defstyled resume-link :a
  :block :text-red :text-blue-500 :underline :mb-4
  ([]
   [:<>
    {:href "Ryan Martin Resume.pdf"}
    "PDF Document"]))

(defstyled section-title :h2
  :text-md
  :mb-5
  :font-bold
  :border-b-1
  :border-t-1
  :border-black
  :py-2
  {:font-family (font :pt-mono)})


(defstyled subsection-title :h3
  :text-md
  :text-blue-600
  ;:tracking-wider
  :mb-0
  :font-normal
  {:font-family (font :pt-sans)})

(defstyled sidebar-title subsection-title
  :mb-4 :text-md :font-normal #_:text-white
  {:font-family (font :pt-sans)}
  :text-blue-500
  #_:bg-gray-700)

(defstyled skill-keywords :div
  :mb-4
  [:li :mb-6 :text-sm]
  ([]
   [:<>
    [sidebar-title "More Skills"]
    [:ul
     (for [x data/skill-keywords]
       [:li x])]]))



(defstyled experience :div
  [:li :mb-2]
  [:p :mb-4 :pl-3 :leading-tight]
  ([]
   [:<>
    [section-title "Experience"]
    (for [[t s] data/experience]
      [:div
       [subsection-title t]
       [:p s]])]))

(defstyled education :div
  ([]
   [:<>
    [section-title "Education"]
    [:span data/education]]))


(defstyled column :div
  :p-5)

(defstyled left-col column
  :min-w-300px :max-w-400px
  :text-white :pl-8)

(defstyled right-col column
  :bg-white :pr-10 :pt-6 :pb-8 :mb-15 :pl-2.5rem
  [" > div" :mb-8])

(defstyled cols :div
  :flex :items-stretch #_:gap-8 :h-full :flex-row)
  ;[(s/> (s/&) (s/div (s/first-child))) :w-33% :bg-gray-400]
  ;[(s/> (s/&) (s/div (s/last-child)))  :w-77% :bg-red-500]
  ;([left right]
  ; [:<>
  ;  left
  ;  right]))

(defstyled body :body
  :p-0 :m-0 :bg-black
  :tracking-wide)
  ;{:font-family (font :pt-sans)} )

(defstyled cool-technologies :div
  [:a :font-bold :min-w-24 :underline :text-blue-700 :text-sm]
  [:.item :flex :gap-8 :mb-2
   [:a {:font-family (font :pt-sans)}
    :tracking-wider :text-md :font-normal]
   [:span :mt-0 :text-sm]]
  ([]
   [:<>
    [section-title "Technologies I'm Exploring"]
    (for [{lib-name :name :keys [url subtitle]} (vals data/technologies-interested-in)]
      [:div.item
       [:a {:href url} lib-name]
       [:span subtitle]])]))

(defstyled skills-summary :div
  [:.block :mb-5]
  [:.subtitle :mb-5 :mt-0]
  [:ul :list-outside :list-disc :pl-3]
  [:li :mb-4 :text-sm :leading-tight]
  ([]
   [:<>
    [section-title "Skills Summary"]
    (for [[subtitle experience-lines] data/skills-summary]
      [:div.block
       [subsection-title {:class "subtitle"} subtitle]
       [:ul
        (for [x experience-lines]
          [:li x])]])]))

(defn hiccup->html [html]
  (-> html
    (hiccup/render {:doctype? false})))

(defn md->hiccup [markdown-str]
  (-> markdown-str
    (md/parse-md)
    :body))

(defn md->html [markdown-str]
  (-> markdown-str
    md->hiccup
    hiccup->html))

;todo: make links live
(do
  (defstyled personal-info :div
    :mb-8 :text-sm
    [:h4 :font-bold {:font-family (font :pt-sans)} :text-gray-300]
    [:p :flex :flex-col :leading-loose
     :mb-4
     [:a :block :underline]]
    ([]
     [:<>
      [sidebar-title "Personal Info"]
      (md->hiccup data/personal-info-md)]))



  (-> [personal-info]
    (hiccup/render)))


(def fonts-links
  [:<>
   [:link {:rel "preconnect" :href "https://fonts.googleapis.com"}]
   [:link {:rel "preconnect" :href "https://fonts.gstatic.com" :crossorigin "true"}]
   [:link {:href "https://fonts.googleapis.com/css2?family=PT+Mono&family=PT+Sans+Narrow:wght@400;700&family=PT+Sans:wght@400;700&family=PT+Serif:wght@400;700&display=swap" :rel "stylesheet"}]])


(def home-hiccup
  [:html
   [:head
    [:style tw-preflight-3-css-str]
    [:style (o/defined-styles)]
    [:script {:src "https://livejs.com/live.js" :type "text/javascript"}]
    fonts-links]
   [body
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
      [cool-technologies]]]]])

;#_
(->> home-hiccup
  (hiccup/render)
  (spit "docs/index.html"))
