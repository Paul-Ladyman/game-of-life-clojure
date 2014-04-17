(ns gol-clojure.core
  (:gen-class))

(def alive 1)
(def dead 0)

(defn cell-score
	[neighbours]
	(reduce + neighbours))

(defn is-alive
	[cell-status]
	(= 1 cell-status))

(defn is-lonely
	[neighbours-status]
	(<= (cell-score neighbours-status) 1))

(defn is-over-populated
	[neighbours-status]
	(>= (cell-score neighbours-status) 4))

(defn next-cell-status
	[current-cell-status neighbours-status]
	(case (is-alive current-cell-status)
		true
			(if (is-lonely neighbours-status) dead 
				(if (is-over-populated neighbours-status) dead alive))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

