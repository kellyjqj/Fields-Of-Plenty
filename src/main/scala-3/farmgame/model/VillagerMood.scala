package farmgame.model

import javafx.scene.image.Image

//villager moods with different sprites
trait VillagerMood {
  def sprite(v: VillagerSprites): Image
  def frames(v: VillagerSprites): Seq[Image] = Nil
  def isAnimated: Boolean = false
}

case object NormalMood extends VillagerMood {
  def sprite(v: VillagerSprites): Image = v.normal
}

case object HappyMood extends VillagerMood {
  def sprite(v: VillagerSprites): Image = v.happy
}

case object SadMood extends VillagerMood {
  def sprite(v: VillagerSprites): Image = v.sad
}

case object EatingMood extends VillagerMood {
  def sprite(v: VillagerSprites): Image = v.eatingFrames.head
  override def frames(v: VillagerSprites): Seq[Image] = v.eatingFrames
  override def isAnimated: Boolean = true
}

case object DeadMood extends VillagerMood {
  def sprite(v: VillagerSprites): Image = v.dead
}
