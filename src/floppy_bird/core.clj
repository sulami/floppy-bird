(ns floppy-bird.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :rgb)
  {})

(defn update-state [state]
  state)

(defn draw-state [state]
  (q/background 240)
  (q/fill (:color state) 255 255))


(q/defsketch floppy-bird
  :title "Floppy bird"
  :size [500 500]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
