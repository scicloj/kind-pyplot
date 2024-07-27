
;; Kind-pyplot is a small Clojure library for visualizing Python plots. It uses the [Kindly](https://scicloj.github.io/kindly/) convention, thus works in Kindly-compatible tools such as [Clay](https://scicloj.github.io/clay/).

;; The implementation is inspired by the [Parens for Pyplot](https://gigasquidsoftware.com/blog/2020/01/18/parens-for-pyplot/) tutorial by Carin Meier from Jan 2020.

;; **Source:** [![(GitHub repo)](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)](https://github.com/scicloj/kind-pyplot)

;; **Artifact:** [![Clojars Project](https://img.shields.io/clojars/v/org.scicloj/kind-pyplot.svg)](https://clojars.org/org.scicloj/kind-pyplot)
                                        ;
;; ## Setup

(ns index
  (:require [scicloj.kind-pyplot.v1.api :as pyplot]))


;; ## API

;; The `with-pyplot` macro takes Clojure forms that create a plot.
;; It evaluates them and returns an SVG plot. For example:

(pyplot/with-pyplot
  (matplotlib.pyplot/plot
   [1 2 3 4 5 6]
   [1 4 9 16 25 36]))

;; The `pyplot` function takes a Clojure function that creates a plot.
;; It calls the function and returns an SVG plot. For example:

(pyplot/pyplot
 #(matplotlib.pyplot/plot
   [1 2 3 4 5 6]
   [1 4 9 16 25 36]))

;; ## More examples

;; ## From the [Parens for Pyplot tutorial](https://gigasquidsoftware.com/blog/2020/01/18/parens-for-pyplot/)

(require '[libpython-clj2.require :refer [require-python]]
         '[clojure.math :as math])
(require-python '[numpy :as np])

(def sine-data
  (let [x (range 0 (* 3 np/pi) 0.1)]
    (-> {:x (vec x)
         :y (mapv math/sin x)})))

(pyplot/with-pyplot
  (matplotlib.pyplot/plot
   (:x sine-data)
   (:y sine-data)))

(pyplot/pyplot
 #(matplotlib.pyplot/plot
   (:x sine-data)
   (:y sine-data)))

;; ## From the [Seaborn intro](https://seaborn.pydata.org/tutorial/introduction):

(require-python '[seaborn :as sns])

(let [tips (sns/load_dataset "tips")]
  (sns/set_theme)
  (pyplot/pyplot
   #(sns/relplot :data tips
                 :x "total_bill"
                 :y "tip"
                 :col "time"
                 :hue "smoker"
                 :style "smoker"
                 :size "size")))
