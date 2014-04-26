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


(defn get-cell
	[y x board]	
	(get (get board y) x))

(defn get-neighbour
	[y x board]
	(case (or (< y 0) (> y 19))
		true dead
		false 
			(case (or (< x 0) (> x 19))
				true dead
				false (get-cell y x board))))

(defn get-neighbours
	[y x board]
	(vector
		(get-neighbour (- y 1) (- x 1) board)
		(get-neighbour (- y 1) x board)
		(get-neighbour (- y 1) (+ x 1) board)

		(get-neighbour y (- x 1) board)
		(get-neighbour y (+ x 1) board)

		(get-neighbour (+ y 1) (- x 1) board)
		(get-neighbour (+ y 1) x board)
		(get-neighbour (+ y 1) (+ x 1) board)
		))

(defn next-generation
	[board]
	(vec (map-indexed
		(fn [y row]
			(vec (map-indexed
				(fn [x cell]
					(next-cell-status cell (get-neighbours y x board)))
				row)))
		board)))

(defn print-row
	[row]
	(def painted-row
		(vec (map
			(fn [x] (if (= x 0) "-" "*"))
		row)))
	(join " " painted-row))

(defn print-board
	[board height]
	(doseq [y (range height)] (println (print-row (board y)))))

(defn empty-board
	[width height]
	(vec (repeat width (vec (repeat height dead)))))

(defn seed-glider
	[board]
	(assoc
		(assoc
			(assoc board 1 (assoc (board 1) 2 1))
		2 (assoc (board 2) 3 1))
	 3 (assoc (board 3) 1 1 2 1 3 1))
	)

(defn seed-board
	[board seed]
	(case seed
		:glider (seed-glider board)))

(defn clear-screen
  "Clears the screen, using ANSI control characters."
  []
  (let [esc (char 27)]
    (print (str esc "[2J"))     ; ANSI: clear screen
    (print (str esc "[;H"))))   ; ANSI: move cursor to top left corner of screen

(defn game-of-life
	[board]
	(print-board board 20)
	(Thread/sleep 150)
	(clear-screen)
	(game-of-life (next-generation board)))

(defn -main
  [& args]
  (def board (seed-board (empty-board 20 20) :glider))
  (game-of-life board))

