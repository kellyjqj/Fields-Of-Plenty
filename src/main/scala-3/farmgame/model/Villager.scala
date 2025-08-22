package farmgame.model

import scala.collection.mutable

class Villager(val name: String):
  var nutritionLevel: Double = 50.0 //initial nutrition level
  private val dietHistory = mutable.Queue.empty[NutritionCategory]
  private var daysSinceLastMeal: Int = 0

  def eat(crop: Crop): Unit =
    nutritionLevel += crop.nutritionValue
    if nutritionLevel > 100 then nutritionLevel = 100
    
    //reset starvation counter
    daysSinceLastMeal = 0

    //track diet history
    dietHistory.enqueue(crop.category)
    if dietHistory.size > 5 then dietHistory.dequeue()//keep last 5 meals

    //variety penalty
    if dietHistory.size == 5 && dietHistory.forall(_ == dietHistory.head) then
      nutritionLevel -= 10
      if nutritionLevel < 0 then nutritionLevel = 0
  
  def nextDay(): Unit =
    daysSinceLastMeal += 1

    //use daysSinceLastMeal *1.5 as amount to decrease
    if daysSinceLastMeal >= 1 then
      nutritionLevel -= math.ceil(daysSinceLastMeal * 1.5)
      if nutritionLevel < 0 then nutritionLevel = 0

    // or more days without food, villager's nutrition level decreases
//    if daysSinceLastMeal >= 1 then
//      nutritionLevel -= 2
//      if nutritionLevel < 0 then nutritionLevel = 0

  def isAlive: Boolean = nutritionLevel > 0