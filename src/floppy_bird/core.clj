(ns floppy-bird.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :rgb)
  {:bird-height 250
   :pipes '([300 150 250])})

(defn update-state [state]
  state)

(defn draw-state [state]
  (q/background 240)
  (q/fill 255)

  (q/rect-mode :center)
  (q/rect 30 (:bird-height state) 10 10)

  (q/rect-mode :corner)
  (doseq [[x top bottom] (:pipes state)]
    (q/rect x 0 30 top)
    (q/rect x bottom 30 (- 500 bottom))))

(q/defsketch floppy-bird
  :title "Floppy bird"
  :size [500 500]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])

(defn -main [])
