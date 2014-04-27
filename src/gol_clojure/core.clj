(ns gol-clojure.core
  (:gen-class))

(use '[clojure.string :only (join split)])

(require '[clojure.java.io :as io])

(def board-height 50)
(def board-width 50)
(def game-speed 100)
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
	(case (or (< y 0) (> y (- board-height 1)))
		true dead
		false 
			(case (or (< x 0) (> x (- board-width 1)))
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

(defn clear-screen
  "Clears the screen, using ANSI control characters."
  []
  (let [esc (char 27)]
    (print (str esc "[2J"))     ; ANSI: clear screen
    (print (str esc "[;H"))))   ; ANSI: move cursor to top left corner of screen

(defn game-of-life
	[board]
	(print-board board board-height)
	(Thread/sleep game-speed)
	(clear-screen)
	(game-of-life (next-generation board)))

(defn read-seed
	[key]
	(def file (case key
		:oscillators "seeds/oscillators.csv"
		:gliders "seeds/gliders.csv"
		:gliders2 "seeds/gliders2.csv"
		:glidergun "seeds/glidergun.csv"
		:bomber "seeds/bomber.csv"))
	(def seed (slurp (io/resource file)))
	(def rows (split seed #"\n"))
	(vec (map
		(fn [row]
			(vec (map
				(fn [cell] (if (= cell "0") 0 1))
			(split row #","))))
	rows)))

(defn -main
  [& args]
  (def seed (first args))
  (case seed
  	"oscillators" (game-of-life (read-seed :oscillators))
  	"gliders" (game-of-life (read-seed :gliders))
  	"gliders2" (game-of-life (read-seed :gliders2))
  	"glidergun" (game-of-life (read-seed :glidergun))
  	"bomber" (game-of-life (read-seed :bomber))))