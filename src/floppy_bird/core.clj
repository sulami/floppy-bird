(ns floppy-bird.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn pipe-generator []
  (let [top (+ 50 (rand-int 300))
        bottom (-> (+ 300 (rand-int 200))
                   (max (+ top 100))
                   (min (+ top 200)))]
    [top bottom]))

(def initial-state
  {:lost false
   :bird-height 250
   :bird-speed 0
   :pipes (map conj (repeatedly pipe-generator)
               (iterate (partial + 150) 300))})

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
              (update-in [:bird-speed] (if (q/key-pressed?)
                                         (constantly 4)
                                         #(- % 0.3)))
              (update-in [:pipes] #(if (< (last (first %)) -30) (rest %) %))
              (update-in [:pipes] (fn [xs] (map #(update-in % [2] dec) xs))))))

(defn draw-state [state]
  (q/background 240)
  (q/fill 255)

  (q/ellipse-mode :center)
  (q/ellipse 50 (- 500 (:bird-height state)) 30 30)

  (q/rect-mode :corner)
  (doseq [[top bottom x] (take 4 (:pipes state))]
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
