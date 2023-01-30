(ns cv.data
  (:require
    [lambdaisland.hiccup :as hiccup]
    [lambdaisland.ornament :as o :refer [defstyled]]
    [clojure.string :as string]
    [flatland.useful.seq :refer [partition-between]])
  (:use [com.rpl.specter]))



(def text-copied-from-resume-doc
  {"Personal Info"
   "Address\n1821 Amherst Ave.\nOrlando, FL\n\nPhone\n917-743-9113\n\nE-mail\nryanmarti@gmail.com\n\nLinkedIn\nlinkedin.com/in/ryan-martin-1bb63511/\n\nGitHub\u2028github.com/chromalchemy\n"
   "Additional Skills"
   "Functional Programming \u2028Clojure/Javascript\n\nREPL / Test Driven Development\u2028\u2028Data Modeling, Specification, Validation, Transformation\n\nData Visualization, Dashboards\n\nShell Scripting, CLI Tools, Git, Regex\n\nGraphQL, REST / JSON API\n\nAdvanced reactive UI state management\n\nGraphics coding, Adobe Creative Suite"})

;; parse personal info str
(comment
  (->
    "Address\n1821 Amherst Ave.\nOrlando, FL\n\nPhone\n917-743-9113\n\nE-mail\nryanmarti@gmail.com\n\nLinkedIn\nlinkedin.com/in/ryan-martin-1bb63511/\n\nGitHub\u2028github.com/chromalchemy\n"
    (string/split #"\n")
    (->>
      (filter #(not= % ""))
      (transform LAST #(string/split % #" "))
      flatten
      (setval (nthpath 2) NONE)
      (apply hash-map))))

;; parse skill keywords
(comment
  (->
    "Functional Programming Clojure/Javascript REPL / Test Driven Development Data Modeling, Specification, Validation, Transformation\n\nData Visualization, Dashboards\n\nShell Scripting, CLI Tools, Git, Regex\n\nGraphQL, REST / JSON API\n\nAdvanced reactive UI state management\n\nGraphics coding, Adobe Creative Suite"
    (string/split #"\n")
    (->>
      (filterv #(not= % "")))))


(comment
  (->> text-copied-from-resume-doc))

(def text-exported-from-resume-doc
  (slurp "rsc/ryan hybrid resume 2.txt"))

;; parse text from resume
(comment
  (->>
    text-exported-from-resume-doc
    (#(string/split % #"\n"))
    (filterv #(not= % ""))
    (partition-between
      (fn [[a b]]
        (or
          (= b "Experience")
          (= b "Education"))))
    ((fn [[skills-summary experience education]]
       {(first skills-summary)
        (->> skills-summary
          rest
          (partition-between
            (fn [[a b]]
              (= b "Full Stack Web Development")))
          (map
            (fn [[section-title & items]]
              {section-title
               (into [] items)}))
          (apply merge))
        (first experience)
        (->> experience
          rest
          (partition 2)
          (map #(apply hash-map %))
          (apply merge))
        (first education)
        (last education)}))))
;(#(get % "Experience")))


(def personal-info
  {"Address" "1821 Amherst Ave.",
   "LinkedIn" "linkedin.com/in/ryan-martin-1bb63511/",
   "E-mail" "ryanmarti@gmail.com",
   "GitHub" "github.com/chromalchemy",
   "Phone" "917-743-9113"})

(def skills-summary
  ;:skills-summary #_"Skills Summary"
  {"Clojure Programming"
   ["Wrote 30,000 lines of Clojure to support Get Bit Outdoors ERP system, aggregating data from 18 different silos and various formats into custom master database, verifying data integrity across 50K objects, generating reports, and coordinating complex CRUD operations to update system over different application API's."
    "Developed custom \"Real Time\" Inventory integration, reducing customer service calls 70%."
    "Scripted browser with WebDriver to automate record updates on an internal web app that lacked an API. Scraped catalog data from vendor websites."
    "Built reactive ClojureScript Todo app with custom in-memory graph datastore and Firebase integration. "],

   "Full Stack Web Development"
   ["Building responsive HTML/CSS designs using various templating frameworks. Created custom templating language that compiles to Handlebars."
    "Updated and managed BigCommerce e-commerce  website with 18K products and 60K visitors/mo, using REST API extensively."
    "Wrote Javascript to surface necessary product data, and upgrade issues in legacy theme UI code."
    "Scripted Photoshop and ImageMagick to produce image sets for complex holiday campaign product layouts."
    "Coded 300 email-ready HTML templates for newsletter campaigns that generated $100k/year revenue. "
    "Migrated production servers for 50% cost reduction"]})


(def experience
  #_"Experience"
  {"Software Developer, Get Bit Outdoors, Oviedo, FL 2019-Present" "Using Clojure to implement solutions at all levels of the software stack.",
   "Graphic Designer/Web Developer, TinyBoxer, TopRank Web, Orlando, FL  2010-2019" "Developed on 30+ websites on a contract basis, programming front-end web views and display logic. Used leading web CMS platforms, designed data schemas, configured web servers, optimized view code for performance and SEO. Consulted clients on information design, created working wireframe prototypes, crafted and batch-processed media assets.",
   "Painter, Orlando, FL 2007-Present" "Created, exhibited, and sold classical modern paintings in watercolor, acrylic, and oil color. Created a Linseed oil refinement system and produced custom oil paint mediums that support \"Old Master\" texture and technique.",
   "Graphic Designer, VGS Graphic Systems 2006-2007" "Prepared graphic files for a large format Durst printer and a Zune plotter., scripting a process to generate variations on a design. Cut and manually prepared vinyl signs for installation.",
   "IT Technician, BJM & Associates, Orlando, FL 2001-2005" "Purchased, installed, and maintained PC applications, hardware, and network solutions for medium sized Engineering firm."})

(def education
  #_"Education"
  "New College, Sarasota, FL, 1997-2000")

(def skill-keywords
  ["REPL / Test Driven Development"
   "Data Modeling, Specification, Validation, Transformation"
   "Data Visualization, Dashboards"
   "Shell Scripting, Git, Regex"
   "GraphQL, REST / JSON API"
   "Reactive Web UI"
   "Graphics, Adobe Creative Suite"])

(def technologies-interested-in
  {:membrane
   {:name "Membrane "
    :url "https://github.com/phronmophobic/membrane"
    :subtitle "Simple Functional UI Library That Runs Anywhere"}
   :humbleui
   {:name "HumbleUI "
    :url "https://github.com/HumbleUI/HumbleUI"
    :subtitle "Skia-Based Desktop Functional UI framework"}
   :hyperfiddle
   {:name "Hyperfiddle"
    :url "https://www.hyperfiddle.net/"
    :subtitle "UI modeling language in a notebook"}
   :data-rabbit
   {:name "Data Rabbit"
    :url "https://www.datarabbit.com/"
    :subtitle "Flow-based Clojure canvas for data exploration, visualization, & learning. \"A system built for seeing\""}
   :clerk
   {:name "Clerk"
    :url "https://github.com/nextjournal/clerk"
    :subtitle "Moldable Live Programming for Clojure"}
   :matrix
   {:name "Matrix"
    :url "https://github.com/kennytilton/matrix"
    :subtitle "Fine-grained, transparent data flow between generative objects."}
   :flutter/mx
   {:name "Flutter/MX"
    :url "https://github.com/kennytilton/flutter-mx"
    :subtitle "Programming Flutter with ClojureDart and Matrix, a generic, fine-grained, transparent, reactive state manager"}
   :malli
   {:name "Malli"
    :url "https://github.com/metosin/malli"
    :subtitle "Data-driven Schemas for Clojure/Script"}
   :babashka
   {:name "Babashka"
    :url "https://babashka.org/"
    :subtitle "Fast native Clojure scripting runtime"}
   :clojure-dart
   {:subtitle "Build Dart/Flutter mobile and desktop apps with Clojure"
    :name "ClojureDart"
    :url "https://github.com/Tensegritics/ClojureDart"}
   :meander
   {:name "Meander"
    :subtitle "Tools for transparent data transformation"
    :url "https://github.com/noprompt/meander"}
   :xtdb
   {:name "XTDB"
    :subtitle "Immutable Bitemporal Database for SQL, Datalog & graph queries"
    :url "https://xtdb.com/"}})

   ;:pathom
   ;:datalog})
