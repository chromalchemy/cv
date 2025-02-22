(ns cv.data
  (:require
   [clj-reload.core :as reload]
   [lambdaisland.hiccup :as hiccup]
   [lambdaisland.ornament :as o :refer [defstyled]]
   [clojure.string :as string]
   [flatland.useful.seq :refer [partition-between]])
  (:use [com.rpl.specter]))


(def text-copied-from-resume-doc
  {"Personal Info"
   #_"Address\n1230 Richmon Ave.\nOrlando, FL\n\n"
   "Phone\n917-743-9113\n\nE-mail\nryanmarti@gmail.com\n\nLinkedIn\nlinkedin.com/in/ryan-martin-1bb63511/\n\nGitHub\u2028github.com/chromalchemy\n"
   
   "Additional Skills"
   "Functional Programming \u2028Clojure/Javascript\n\nREPL / Test Driven Development\u2028\u2028Data Modeling, Specification, Validation, Transformation\n\nData Visualization, Dashboards\n\nShell Scripting, CLI Tools, Git, Regex\n\nGraphQL, REST / JSON API\n\nAdvanced reactive UI state management\n\nGraphics coding, Adobe Creative Suite"})

;; parse personal info str
(comment
  (->
    "Address\n1821 Amherst Ave.\nOrlando, FL\n\nPhone\n917-743-9113\n\nE-mail\nryanmarti@gmail.com\n\nLinkedIn\nlinkedin.com/in/ryan-martin-1bb63511/\n\nGitHub\u2028github.com/chromalchemy\n"
    (string/split #"\n")
    (->>
      (filter #(not= % ""))
      (transform LAST #(string/split % #""))
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
              (= b "Fullx Stack Web Development")))
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


(def personal-info-md
  "#### E-mail
[ryanmarti@gmail.com](mailto:ryanmarti@gmail.com)

#### Social
GitHub
[github.com/chromalchemy](https://github.com/chromalchemy)

LinkedIn   
[linkedin.com/in/ryan-martin-1bb63511](https://www.linkedin.com/in/ryan-martin-1bb63511)

#### Phone
[917-743-9113](tel:917-743-9113)
")

(def personal-info
  {"Address" "1821 Amherst Ave.",
   "LinkedIn" "linkedin.com/in/ryan-martin-1bb63511/",
   "E-mail" "ryanmarti@gmail.com",
   "GitHub" "github.com/chromalchemy",
   "Phone" "917-743-9113"})

(def skills-summary
  ;:skills-summary #_"Skills Summary"
  {"Clojure Programming"
   [
    "Wrote 30,000 lines of Clojure to upgrade and manage a BigCommerce e-commerce website with 18K products and 60K visitors/mo, using its REST API and local data ETL extensively."

    "Managed complex ERP inventory management system: cleaning, normalizing, and joining data from 18 vendors into custom master database of 50K product records. Verified data integrity, generated custom reports, and executed complex CRUD operations across multiple application API's."
    
    "Scripted browser to automate data updates in a web app that lacked a REST API. Scraped catalog data from websites when vendors could not provide it directly."
    #_"Built reactive ClojureScript Todo app with custom in-memory graph datastore and Firebase integration. "
    
    
],

   "Full Stack Web Development"
   [

    "Developed custom \"Real Time\" Inventory integration with front end, reducing customer service calls 70%."

    "Upgraded legacy ecommerce theme UI code, and implemented custom Javascript to surface required product data and session state."
    
    "Built responsive HTML/CSS designs using various templating frameworks. Created custom templating library that compiles Clojure data to Handlebars HTML."

    "Migrated production servers for 50% cost reduction"
    

"Coded 200 email HTML templates for newsletter campaigns that generated $100k/year revenue. "

    
    

    "Scripted Photoshop and ImageMagick to produce image sets for complex multi-target media campaigns and promotional product layouts."]})


(def experience
  #_"Experience"
  {"Lead Developer, Get Bit Outdoors, Oviedo, FL 2019-Present" "Used Clojure to implement solutions at all levels of the software stack for Ecommerce company integrating 8 cloud apps.",
   
   "Web Designer / Developer, TinyBoxer, TopRank Web, Orlando, FL  2010-2019" "Developed over 30 website front ends. Used leading web CMS platforms, designed data schemas, configured web servers, batch-processed media assets, and optimized view code for performance and SEO. Consulted clients on information design, interactively creating working prototypes.",
   
  #_#_ "Painter, Orlando, FL 2007-Present" "Created, exhibited, and sold classical modern paintings in watercolor, acrylic, and oil color. Created a Linseed oil refinement system and produced custom oil paint mediums that support \"Old Master\" texture and technique.",
   
   "Graphic Designer, VGS Graphic Systems, NYC 2006-2007" "Optimized image files for a large format Durst printer and Zune plotter. Scripted a process to generate variations on a design.",
   
   "IT Support Specialist, BJM & Associates, Orlando, FL 2001-2005" "Purchased, installed, and maintained PC applications, hardware, and network solutions for 20 person Civil Engineering firm."})

(def education
  #_"Education"
  "New College, Sarasota, FL, 1997-2000")

(def skill-keywords
  ["REPL Driven Development"
   "Data Modeling, Specification, Transformation, Validation, & Testing"
   "Data Visualization, Real-TimeDashboards"
   "Shell Scripting, Git, Regex"
   "Datalog, GraphQL, REST / JSON API"
   "Reactive Web UI"
   "Graphics Production, Adobe Creative Suite"])

(def technologies-interested-in
  [#_{:name "Membrane "
    :url "https://github.com/phronmophobic/membrane"
    :subtitle "Simple Functional UI Library that runs anywhere"}
   #_{:name "HumbleUI "
    :url "https://github.com/HumbleUI/HumbleUI"
    :subtitle "Functional UI framework for Desktop apps"}
   :electric
   {:name "Electric"
    :url "https://github.com/hyperfiddle/electric"
    :subtitle "Full-Stack Differential Dataflow for UI"}
   
   
   {:name "Malli"
    :url "https://github.com/metosin/malli"
    :subtitle "Data-driven Schemas for Clojure/Script"}
   {:name "Babashka"
    :url "https://github.com/babashka/babashka" #_"https://babashka.org/"
    :subtitle "Fast native Clojure scripting runtime"}
   #_{:subtitle "Dart/Flutter mobile and desktop apps with Clojure"
        :name "ClojureDart"
        :url "https://github.com/Tensegritics/ClojureDart"}
   {:name "Scryer Prolog"
    :subtitle "Modern Prolog in Clojure"
    :url "https://github.com/jjtolton/libscryer-clj"}
   {:name "Meander"
    :subtitle "Tools for transparent data transformation"
    :url "https://github.com/noprompt/meander"}
   {:name "XTDB"
    :subtitle "Immutable Bitemporal Database for SQL, Datalog & graph queries"
    :url "https://xtdb.com/"}
   {:name "FlowStorm"
    :subtitle "Omniscient Time Travel Debugger for Clojure"
    :url "https://github.com/flow-storm/flow-storm-debugger"}
   {:name "Clerk"
    :url "https://github.com/nextjournal/clerk"
    :subtitle "Moldable Live Programming for Clojure"}
   {:name "Data Rabbit"
    :url "https://www.datarabbit.com/"
    :subtitle (str "Flow-based Clojure canvas for data exploration.")}
   #_{:name "Matrix"
    :url "https://github.com/kennytilton/matrix"
    :subtitle "Fine-grained, transparent data flow between generative objects"}
   #_{:name "Flutter/MX"
    :url "https://github.com/kennytilton/flutter-mx"
    :subtitle "Programming Flutter with ClojureDart and fine-grained reactive state manager"}
   ])

   ;:pathom
   ;:datalog})

#_(reload/reload)