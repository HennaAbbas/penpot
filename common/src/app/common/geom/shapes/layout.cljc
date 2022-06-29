;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) UXBOX Labs SL

(ns app.common.geom.shapes.layout
  (:require
   [app.common.geom.matrix :as gmt]
   [app.common.geom.point :as gpt]
   [app.common.geom.shapes.rect :as gre]))

(defn calc-layout-data
  "Digest the layout data to pass it to the constrains"
  [_parent children transformed-rect]

  (let [[children-width children-height]
        (->> children (reduce (fn [[acc-width acc-height] shape]
                                [(+ acc-width (-> shape :points gre/points->rect :width))
                                 (+ acc-height (-> shape :points gre/points->rect :height))]) [0 0]))]
    {:start-x (:x transformed-rect)
     :start-y (:y transformed-rect)
     :children-width children-width
     :children-height children-height})
  )

(defn calc-layout-modifiers
  [_parent child modifiers _transformed-rect {:keys [start-x start-y] :as layout-data}]

  (let [bounds (-> child :points gre/points->selrect)
        delta (gpt/subtract (gpt/point start-x start-y) (gpt/point bounds))
        displacement (gmt/translate-matrix delta)
        modifiers (assoc modifiers :displacement displacement)
        next-x (+ start-x (:width bounds))]

    [modifiers (assoc layout-data :start-x next-x )]))
