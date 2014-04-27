# gol-clojure

Conway's Game of Life implemented in Clojure.

## Installation

Install Leiningen (http://leiningen.org/). The easiest way is with Homebrew:
    
    brew install leiningen
    
Checkout the code:

    git clone https://github.com/Paul-Bod/game-of-life-clojure.git

## Usage

Use leiningen to run the project and start game of life. The program accepts a command line argument corresponding to one of a series of seed patterns it has available. The arguments available are:

- oscillators
- gliders
- gliders2
- glidergun
- bomber

For example:

    lein run glidergun
