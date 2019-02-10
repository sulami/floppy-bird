(ns floppy-bird.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def initial-state
  {:lost false
   :bird-height 250
   :bird-speed 0
   :pipes '([300 150 250])})

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :rgb)
  initial-state)

(defn update-state [state]
  (cond
    (:lost state) initial-state
    (neg? (:bird-height state)) (assoc-in state [:lost] true)
    :else (-> state
              (update-in [:bird-height] + (:bird-speed state))
              (update-in [:bird-speed] #(- (* % 0.9) 0.5)))))

(defn draw-state [state]
  (q/background 240)
  (q/fill 255)

  (q/ellipse-mode :center)
  (q/ellipse 50 (- 500 (:bird-height state)) 30 30)

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
