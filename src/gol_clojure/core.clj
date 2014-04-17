(ns gol-clojure.core
  (:gen-class))

(use '[clojure.string :only (join split)])

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
				(if (is-over-populated neighbours-status) dead current-cell-status))
		false
			(if (= (cell-score neighbours-status) 3) alive current-cell-status)))

(defn print-row
	[column]
	(join " " column))

(defn print-board
	[board height]
	(doseq [y (range height)] (println (print-row (board y)))))

(defn empty-board
	[width height]
	(vec (repeat width (vec (repeat height 0)))))

(defn seed-glider
	[board]
	(assoc
		(assoc
			(assoc board 1 (assoc (board 1) 2 1))
		2 (assoc (board 2) 3 1))
	1 1 2 1 3 1))

(defn seed-board
	[board seed]
	(case seed
		:glider (seed-glider board)))

(defn -main
  [& args]
  (print-board (seed-board (empty-board 50 50) :glider) 50))

