package farmgame.model

class Farm(rows: Int, cols: Int, villagers: List[Villager]):
  //2d grid of farm plots
  val plots: Array[Array[FarmPlot]] =
    Array.fill(rows, cols)(new FarmPlot)

  //villagers
  var people: List[Villager] = villagers
  
  def numRows: Int = rows
  def numCols: Int = cols

  def nextTurn(): Unit =
    //grow all crops
    for row <- plots; plot <- row do
      plot.grow()
    //apply variety penalty
    for v <- people do
      v.applyVarietyPenalty()

  def plantAt(row: Int, col: Int, crop: Crop): Boolean =
    plots(row)(col).plant(crop)

  //harvest crop and return the crop (no feed yet)
  def harvestAt(row: Int, col: Int): Option[Crop] =
    plots(row)(col).harvest()

  def feedVillager(villagerIndex: Int, crop: Crop): Unit =
    if villagerIndex >= 0 && villagerIndex < people.size then
      people(villagerIndex).eat(crop)

  //check if all villagers are alive
  def allAlive: Boolean =
    people.forall(_.isAlive)
