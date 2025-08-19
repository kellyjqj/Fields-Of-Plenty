package farmgame.model

import scalafx.beans.property.{IntegerProperty, StringProperty}

abstract class Crop(val name: String, val growthTime: Int, val category: NutritionCategory, val nutritionValue: Int):
  var progress: Int = 0

  def grow(): Unit =
    if progress < growthTime then
      progress += 1

  def isReady: Boolean = progress >= growthTime

  def harvest(): Option[Crop] =
    if isReady then {
      progress = 0
      Some(this)
    }
    else None


class Rice extends Crop("Rice", 1, NutritionCategory.Carb, 2)

class Soy extends Crop ("Soy", 4, NutritionCategory.Protein, 6)

class Potato extends Crop("Potato", 3, NutritionCategory.Carb, 4)

class Tomato extends Crop("Tomato", 4, NutritionCategory.Mineral, 5)

class Orange extends Crop("Orange", 5, NutritionCategory.Vitamin, 7)

