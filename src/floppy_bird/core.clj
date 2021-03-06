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
   :time 0
   :bird-height 250
   :bird-speed 0
   :pipes (map conj (repeatedly pipe-generator)
               (iterate (partial + 150) 180))})

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :rgb)
  initial-state)

(defn collision? [state]
  (let [[pt pb px] (first (:pipes state))
        bh (:bird-height state)]
    (and (<= 15 px 65)
         (or (<= (- bh 15) pt)
             (<= pb (+ bh 15))))))

(defn update-state [state]
  (cond
    (:lost state) (if (q/key-pressed?)
                    initial-state
                    state)
    (neg? (:bird-height state)) (assoc-in state [:lost] true)
    (< 500 (:bird-height state)) (assoc-in state [:lost] true)
    (collision? state) (assoc-in state [:lost] true)
    :else (-> state
              (update-in [:time] inc)
              (update-in [:bird-height] - (:bird-speed state))
              (update-in [:bird-speed] (if (q/key-pressed?)
                                         (constantly 5)
                                         #(- % 0.5)))
              (update-in [:pipes] #(if (< (last (first %)) -30) (rest %) %))
              (update-in [:pipes] (fn [xs] (map #(update-in % [2] dec) xs))))))

(defn draw-state [state]
  (q/background 112 197 206)

  (if (:lost state)
    (do
      (q/text-align :center)
      (q/fill 0)
      (q/text (format "Final Score: %d" (-> state :time (/ 150) int))
              250 250))

    (do
      (q/ellipse-mode :center)
      (q/fill 212 191 39)
      (q/ellipse 50 (:bird-height state) 30 30)

      (q/rect-mode :corner)
      (q/fill 115 191 46)
      (doseq [[top bottom x] (take 4 (:pipes state))]
        (q/rect x -1 30 top)
        (q/rect x bottom 30 (- 500 bottom)))

      (q/text-align :left :top)
      (q/fill 0)
      (q/text (format "Score: %d" (-> state :time (/ 150) int)) 10 10))))

(q/defsketch floppy-bird
  :title "Floppy bird"
  :size [500 500]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])

(defn -main [])
