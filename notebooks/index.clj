(ns index
  (:require [libpython-clj2.require :refer [require-python]]
            [libpython-clj2.python :refer [py. py.. py.-] :as py]
            [scicloj.kind-pyplot.v1.api :as pyplot]))

;; From the Parens for Pyplot blogpost:

(require-python '[numpy :as np])

(require '[clojure.math :as math])

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

;; From the [Seaborn intro](https://seaborn.pydata.org/tutorial/introduction):

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
