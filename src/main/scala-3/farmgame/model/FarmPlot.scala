package farmgame.model

class FarmPlot():
  private var crop: Option[Crop] = None

  //check if plot is empty
  def isEmpty: Boolean = crop.isEmpty

  //plant crop at given location
  def plant(newCrop: Crop): Boolean =
    if isEmpty then
      crop = Some(newCrop)
      true
    else false

  //grow crop each day
  def grow(): Unit =
    crop.foreach(_.grow())

  //harvest crop
  def harvest(): Option[Crop] =
    crop match
      case Some(c) if c.isReady =>
        val harvested = c.harvest()
        crop = None //reset plot after harvest
        harvested
      case _ => None

  //get name of crop
  def getCropName: String =
    crop.map(c =>
      if c.isReady then s"${c.name}"
      else s"${c.name} ${c.progress}/${c.growthTime}"
    ).getOrElse("Empty")

  //check if crop is ready to harvest
  def isReady: Boolean = crop.exists(_.isReady)

  //get progress of crop
  def progress(): Int = crop.map(_.progress).getOrElse(0)
  
  //get growth time of crop
  def growthTime(): Int = crop.map(_.growthTime).getOrElse(0)
