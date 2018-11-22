(def +version+ "1.0.1")

(set-env!
 :resource-paths #{"src"}
 :dependencies '[[adzerk/bootlaces "0.1.13" :scope "test"]])

(require '[adzerk.bootlaces :refer :all])

(bootlaces! +version+)

(task-options!

 pom {:project     'witek/page-reload
      :version     +version+
      :description "Reloads a HTML page when it changes on the server."
      :developers  {"Witoslaw Koczewski" "wi@koczewski.de"}
      :url         "https://github.com/witek/page-reload"
      :scm         {:url "https://github.com/witek/page-reload.git"}
      :license     {"Eclipse Public License - v 2.0" "https://raw.githubusercontent.com/witek/page-reload/master/LICENSE"}})
