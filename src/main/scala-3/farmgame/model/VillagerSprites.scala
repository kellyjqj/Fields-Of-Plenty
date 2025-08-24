package farmgame.model

import javafx.scene.image.Image

case class VillagerSprites(normal: Image,
                           happy: Image,
                           sad: Image,
                           eatingFrames: Seq[Image],
                           dead: Image,
                          )

//store all villager assets in one object
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
