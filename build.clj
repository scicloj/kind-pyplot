(ns build
  "Kind-pyplot's build script.

  clojure -T:build ci
  clojure -T:build deploy

  Run tests via:
  clojure -X:test

  For more information, run:

  clojure -A:deps -T:build help/doc"
  (:refer-clojure :exclude [test])
  (:require [clojure.tools.build.api :as b]
            [deps-deploy.deps-deploy :as dd]))

(def lib 'org.scicloj/kind-pyplot)
(def version "1-beta2.1")

(def snapshot (str version "-SNAPSHOT"))
(def class-dir "target/classes")

(defn test "Run all the tests." [opts]
  (println "\nRunning tests for Clojure")
  (let [basis    (b/create-basis {:aliases [:test]})
        cmds     (b/java-command
                  {:basis     basis
                   :main      'clojure.main
                   :main-args ["-m" "cognitect.test-runner"]})
        {:keys [exit]} (b/process cmds)]
    (when-not (zero? exit) (throw (ex-info "Tests failed" {}))))
  opts)

(defn- pom-template [version]
  [[:description "Kind-pyplot is a small Clojure for visualizing Python plots using the Kindl convention."]
   [:url "https://scicloj.github.io/kind-pyplot/"]
   [:licenses
    [:license
     [:name "Eclipse Public License - v 2.0"]
     [:url "https://www.eclipse.org/legal/epl-2.0/"]]]
   [:scm
    [:url "https://github.com/scicloj/kind-pyplot"]
    [:connection "scm:git:https://github.com/scicloj/kind-pyplot.git"]
    [:developerConnection "scm:git:ssh:git@github.com:scicloj/kind-pyplot.git"]
    [:tag (str "v" version)]]])

(defn- jar-opts [opts]
(let [version (if (:snapshot opts) snapshot version)]
  (assoc opts
         :lib lib   :version version
         :jar-file  (format "target/%s-%s.jar" lib version)
         :basis     (b/create-basis {})
         :class-dir class-dir
         :target    "target"
         :src-dirs  ["src"]
         :pom-data  (pom-template version))))

(defn ci "Run the CI pipeline of tests (and build the JAR)." [opts]
(test opts)
(b/delete {:path "target"})
(let [opts (jar-opts opts)]
  (println "\nWriting pom.xml...")
  (b/write-pom opts)
  (println "\nCopying source...")
  (b/copy-dir {:src-dirs ["resources" "src"] :target-dir class-dir})
  (println "\nBuilding" (:jar-file opts) "...")
  (b/jar opts))
opts)

(defn deploy "Deploy the JAR to Clojars." [opts]
(let [{:keys [jar-file] :as opts} (jar-opts opts)]
  (dd/deploy {:installer :remote :artifact (b/resolve-path jar-file)
              :pom-file (b/pom-path (select-keys opts [:lib :class-dir]))}))
opts)
