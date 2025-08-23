package farmgame.model

import javafx.scene.image.Image
import scala.collection.mutable

class Villager(val name: String, val sprites: VillagerSprites):
  var nutritionLevel: Double = 50.0 //initial nutrition level
  private val dietHistory = mutable.Queue.empty[NutritionCategory]
  private var daysSinceLastMeal: Int = 0
  var isEating: Boolean = false

  def eat(crop: Crop): Unit =
    nutritionLevel += crop.nutritionValue
    if nutritionLevel > 100 then nutritionLevel = 100
    
    //reset starvation counter
    daysSinceLastMeal = 0

    //track diet history
    dietHistory.enqueue(crop.category)
    if dietHistory.size > 5 then dietHistory.dequeue()//keep last 5 meals

    //variety penalty
    if dietHistory.size >= 5 && dietHistory.forall(_ == dietHistory.head) then
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

  def currentMood: VillagerMood =
    if !isAlive then DeadMood
    else if nutritionLevel < 30 then SadMood
    else if nutritionLevel < 60 then NormalMood
    else HappyMood
    
    

case class VillagerSprites(normal: Image,
                            happy: Image,
                            sad: Image,
                            eatingFrames: Seq[Image],
                            dead: Image,
                          )

object VillagerAssets:
  val v1Sprites: VillagerSprites = VillagerSprites(
    new Image(getClass.getResourceAsStream("/farmgame/view/images/v1.png")),
    new Image(getClass.getResourceAsStream("/farmgame/view/images/v1happy.png")),
    new Image(getClass.getResourceAsStream("/farmgame/view/images/v1sad.png")),
    Seq(
      new Image(getClass.getResourceAsStream("/farmgame/view/images/v1.png")),
      new Image(getClass.getResourceAsStream("/farmgame/view/images/v1eat.png")),
      new Image(getClass.getResourceAsStream("/farmgame/view/images/v1.png")),
      new Image(getClass.getResourceAsStream("/farmgame/view/images/v1eat.png"))
    ),
    new Image(getClass.getResourceAsStream("/farmgame/view/images/v1dead.png"))
  )

  val v2Sprites: VillagerSprites = VillagerSprites(
    new Image(getClass.getResourceAsStream("/farmgame/view/images/v2.png")),
    new Image(getClass.getResourceAsStream("/farmgame/view/images/v2happy.png")),
    new Image(getClass.getResourceAsStream("/farmgame/view/images/v2sad.png")),
    Seq(
      new Image(getClass.getResourceAsStream("/farmgame/view/images/v2.png")),
      new Image(getClass.getResourceAsStream("/farmgame/view/images/v2eat.png")),
      new Image(getClass.getResourceAsStream("/farmgame/view/images/v2.png")),
      new Image(getClass.getResourceAsStream("/farmgame/view/images/v2eat.png"))
    ),
    new Image(getClass.getResourceAsStream("/farmgame/view/images/v2dead.png"))
  )
  val grave1: Image = new Image(getClass.getResourceAsStream("/farmgame/view/images/v1grave.png"))
  val grave2: Image = new Image(getClass.getResourceAsStream("/farmgame/view/images/v2grave.png"))
