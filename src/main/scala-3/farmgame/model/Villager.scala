package farmgame.model

import scala.collection.mutable

class Villager(val name: String):
  var nutritionLevel: Int = 50 //initial nutrition level
  private val dietHistory = mutable.Queue.empty[NutritionCategory]

  def eat(crop: Crop): Unit =
    nutritionLevel += crop.nutritionValue
    if nutritionLevel > 100 then nutritionLevel = 100

    //track diet history
    dietHistory.enqueue(crop.category)
    if dietHistory.size > 5 then dietHistory.dequeue()//keep last 5 meals

  def applyVarietyPenalty(): Unit =
    if dietHistory.nonEmpty && dietHistory.forall(_ == dietHistory.head) then
      nutritionLevel -= 10
      if nutritionLevel < 0 then nutritionLevel = 0

  def isAlive: Boolean = nutritionLevel > 0