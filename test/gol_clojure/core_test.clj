(ns gol-clojure.core-test
  (:require [clojure.test :refer :all]
            [gol-clojure.core :refer :all]))

(deftest next-cell-status-test
  (testing "Lonely live cell dies"
    (is (= 0 (next-cell-status 1 [0 0 0 0 0 0 0 0]))))
  (testing "Over crowded live cell dies"
    (is (= 0 (next-cell-status 1 [1 1 1 1 0 0 0 0]))))
  (testing "Live cell with two neighbours survives"
    (is (= 1 (next-cell-status 1 [1 1 0 0 0 0 0 0]))))
  (testing "Live cell with three neighbours survives"
    (is (= 1 (next-cell-status 1 [1 1 1 0 0 0 0 0]))))
  (testing "Dead cell with three neighbours becomes alive"
    (is (= 1 (next-cell-status 0 [1 1 1 0 0 0 0 0]))))
  (testing "Dead cell with two neighbours remains dead"
    (is (= 0 (next-cell-status 0 [1 1 0 0 0 0 0 0]))))
  (testing "Lonely dead cell remains dead"
    (is (= 0 (next-cell-status 0 [0 0 0 0 0 0 0 0]))))
  (testing "Over crowded dead cell remains dead"
    (is (= 0 (next-cell-status 0 [1 1 1 1 0 0 0 0])))))

(deftest is-alive-test
	(testing "true if cell status is 1"
		(is (= true (is-alive 1))))
	(testing "false if cell status is 0"
		(is (= false (is-alive 0)))))

(deftest is-lonely-test
	(testing "Cell is lonely with no living neighbours"
		(is (= true (is-lonely [0 0 0 0 0 0 0 0]))))
	(testing "Cell is lonely with a single living neighbour"
		(is (= true (is-lonely [1 0 0 0 0 0 0 0]))))
	(testing "Cell is not lonely with multiple living neighbours"
		(is (= false (is-lonely [1 1 0 0 0 0 0 0]))))
	(testing "Cell is not lonely with all living neighbours"
		(is (= false (is-lonely [1 1 1 1 1 1 1 1])))))

(deftest is-over-populated-test
	(testing "Cell is over populated with four living neighbours"
		(is (= true (is-over-populated [1 1 1 1 0 0 0 0]))))
	(testing "Cell is over populated with more than four living neighbours"
		(is (= true (is-over-populated [1 1 1 1 1 0 0 0]))))
	(testing "Cell is over populated with all living neighbours"
		(is (= true (is-over-populated [1 1 1 1 1 1 1 1]))))
	(testing "Cell is not over populated with less than four living neighbours"
		(is (= false (is-over-populated [1 1 1 0 0 0 0 0]))))
	(testing "Cell is not over populated with no living neighbours"
		(is (= false (is-over-populated [0 0 0 0 0 0 0 0])))))

(deftest cell-score-test
	(testing "Adds values of neighbours"
		(is (= 5 (cell-score [1 1 1 1 1 0 0 0])))))

(deftest get-neighbours-test
	(println "hello")
	(let [board (vec (repeat 5 (vec (repeat 5 1))))]
		(testing "Returns each neighbour of a cell"
			(is (= [1 1 1 1 1 1 1 1] (get-neighbours 2 2 board))))
		(println "============")
		(testing "Returns 0 for out of bound neighbours"
			(is (= [0 0 0 1 1 1 1 1] (get-neighbours 0 2 board))))
		(testing "Returns neighbours for corner cells"
			(is (= [0 0 0 0 1 0 1 1] (get-neighbours 0 0 board))))))