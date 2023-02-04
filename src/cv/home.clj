(ns cv.home
  (:require
    [lambdaisland.hiccup :as hiccup]
    [lambdaisland.ornament :as o :refer [defstyled]]
    [clojure.string :as string]
    [garden.core :refer [css]]
    [garden.compiler :as gc :refer [compile-css]]
    [cv.tw-preflight :refer [tw-preflight-3-css-str]]
    [cybermonday.core :as md]
    [cybermonday.utils]
    [com.rpl.specter]
    [cybermonday.ir :refer [md-to-ir]]
    [cybermonday.parser]
    [flatland.useful.seq :refer [partition-between]])
  (:require
    [cv.data :as data]
    :reload
    [garden.selectors :as s])
  (:use [com.rpl.specter]))

(defn hiccup->html [html]
  (-> html
    (hiccup/render {:doctype? false})))

(defn md->hiccup [markdown-str]
  (-> markdown-str
    (md/parse-body)))

(defn md->html [markdown-str]
  (-> markdown-str
    md->hiccup
    hiccup->html))

;-----------------


(defstyled left-block :div
  :mb-4 :sm:mb-6 :pl-6 :lg:pl-10 :pr-6)
(def font
  {:pt-mono "PT Mono"
   :pt-sans "PT Sans"
   :pt-serif "PT Serif"
   :pt-sans-narrow "PT Sans Narrow"})

(defstyled resume-link :a
  :text-blue-500 #_:underline :mb-4 :ml-4 :sm:text-sm #_:font-bold
  :border-b-1 :border-solid
  :hover:border-white
  {:font-family (font :pt-serif)}
  :text-lg
  ([]
   [:<> {:href "Ryan Martin Resume.pdf"}
    "PDF"]))

(defstyled resume-title left-block
  [:h1 :text-6xl :sm:text-4xl :tracking-tight
   {:font-family (font :pt-mono)} :ml-0 :pl-0]
  [:h2 {:font-family (font :pt-sans)} :tracking-wider :text-xl :sm:text-sm :text-center :sm:text-left]
  [:br :hidden :sm:inline]
  ([]
   [:<>
    [:h1 "Ryan " [:br] "Martin"]
    [:h2 "Full Stack Developer"
     [resume-link]]]))

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
  :text-lg #_:font-bold #_:sm:font-normal :sm:text-md
  :text-blue-600
  ;:tracking-wider
  :mb-0
  {:font-family (font :pt-sans)})



(defstyled sidebar-title subsection-title
  left-block :mb-6 :text-sm
  :font-normal :tracking-wide
  :hidden :sm:block
  {:font-family (font :pt-sans)}
  :text-blue-500
  :py-2
  #_:text-white
  :bg-black)


(defstyled skill-keywords :div
  :hidden :sm:block
  [:h3 :mb-10]
  [:li.keywords :mb-8 :leading-snug :tracking-tight :text-sm]
  ([]
   [:<>
    [sidebar-title "More Skills"]
    [left-block
     [:ul
      (for [x data/skill-keywords]
        [:li.keywords x])]]]))



(defstyled experience :div
  [:p :mb-4 #_:pl-2 :leading-snug :text-md :mt-1]
  ([]
   [:<>
    [section-title "Experience"]
    (for [[t s] data/experience]
      [:div
       [subsection-title t]
       [:p s]])]))

(defstyled education :div
  [:span :text-sm]
  ([]
   [:<>
    [section-title "Education"]
    [:span data/education]]))

(defstyled column :div
  :p-5)

(defstyled left-col column
  :sm:min-w-300px :sm:max-w-400px
  :px-0
  :text-white
  :mx-auto
  :sm:block
  :pb-4)
  ;:border-4 :border-red-500 :boder-dotted)

(defstyled right-col column
  :bg-white :lg:pr-15  :pb-8 :md:mb-15 :md:pt-6 :pl-2.5rem
  :md:rounded-bl-lg
  [" > div" :mb-8]

  (defstyled cols :div
    :flex :items-stretch #_:gap-8 :h-full :flex-col :sm:flex-row))
    ;[(s/> (s/&) (s/div (s/first-child))) :w-33% :bg-gray-400]
    ;[(s/> (s/&) (s/div (s/last-child)))  :w-77% :bg-red-500]
    ;([left right]
    ; [:<>
    ;  left
    ;  right]))


(defstyled body :body
  :p-0 :m-0 :bg-#011d45
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
  [:li :mb-2 :text-md :sm:text-sm :leading-tight]
  ([]
   [:<>
    [section-title "Skills Summary"]
    (for [[subtitle experience-lines] data/skills-summary]
      [:div.block
       [subsection-title {:class "subtitle"} subtitle]
       [:ul
        (for [x experience-lines]
          [:li x])]])]))

(defstyled personal-info :div
  :sm:mb-8 :text-lg :sm:text-sm
  [:h4 :font-bold {:font-family (font :pt-sans)} :text-gray-300]
  [:.info :flex :justify-center :sm:block #_#_#_:border-1 :border-solid :border-white
   [:h4 :mt-1]
   [:p :ml-2 :md :ml-0]]
  [:p :flex :flex-row :sm:flex-col :leading-loose
   :sm:mb-4
   [:a :block :text-blue-300 :hover:text-blue-500
    :mr-2 :sm:mr-0]]
  ([]
   [:<>
    [sidebar-title "Personal Info"]
    [left-block
     (for
       [[title content]
        (->> data/personal-info-md
             md->hiccup
            (filter vector?)
            (partition 2))]
       [:div.info
          title
          content])]]))


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
      [resume-title]
      ;[resume-link]
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
