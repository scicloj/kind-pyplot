(ns scicloj.kind-pyplot.v1.api
  (:require [libpython-clj2.require :refer [require-python]]
            [libpython-clj2.python :refer [py. py.. py.-] :as py]))

(require-python 'matplotlib.pyplot
                'matplotlib.backends.backend_agg)

(defmacro with-pyplot
  "Takes forms with mathplotlib.pyplot and returns a showable (SVG) plot.
  E.g.:

  (with-pyplot
    (matplotlib.pyplot/plot
     [1 2 3]
     [1 4 9]))
  "
  [& forms]
  `(let [_# (matplotlib.pyplot/clf)
         fig# (matplotlib.pyplot/figure)
         agg-canvas# (matplotlib.backends.backend_agg/FigureCanvasAgg fig#)
         path# (.getAbsolutePath
                (java.io.File/createTempFile "plot-" ".svg"))]
     ~(cons 'do forms)
     (libpython-clj2.python/py. agg-canvas# "draw")
     (matplotlib.pyplot/savefig path#)
     ;; Take the SVG file path and turn it into
     ;; a Clojure value that can be displayed in Kindly-compatible tools.
     (-> path#
         slurp
         vector
         (with-meta {:kindly/kind :kind/html}))))

(defn pyplot
  "Takes a function plotting using mathplotlib.pyplot, and returns a showable (SVG) plot.
  E.g.:

  (pyplot
    #(matplotlib.pyplot/plot
      [1 2 3]
      [1 4 9]))
  "
  [plotting-function]
  (with-pyplot
    (plotting-function)))
