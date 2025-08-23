package farmgame.model

class FarmPlot():
  private var crop: Option[Crop] = None

  def isEmpty: Boolean = crop.isEmpty

  def plant(newCrop: Crop): Boolean =
    if isEmpty then
      crop = Some(newCrop)
      true
    else false

  def grow(): Unit =
    crop.foreach(_.grow())

  def harvest(): Option[Crop] =
    crop match
      case Some(c) if c.isReady =>
        val harvested = c.harvest()
        crop = None //reset plot after harvest
        harvested
      case _ => None

  def getCropName: String =
    crop.map(c =>
      if c.isReady then s"${c.name}"
      else s"${c.name} ${c.progress}/${c.growthTime}"
    ).getOrElse("Empty")

  def isReady: Boolean = crop.exists(_.isReady)

  def progress: Int = crop.map(_.progress).getOrElse(0)

  def growthTime: Int = crop.map(_.growthTime).getOrElse(0)
