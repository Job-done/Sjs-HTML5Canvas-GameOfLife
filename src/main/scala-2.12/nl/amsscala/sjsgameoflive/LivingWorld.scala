package nl.amsscala
package sjsgameoflive

object LivingWorld {
  lazy val r = scala.util.Random
  var generation = 0
  var livingWorld: LivingWorld = Set.empty

  def add(clickPos: Position[Int], origin: Position[Int]): Unit = {
    livingWorld += convertPx2CellCoord(clickPos, origin)
  }

  def convertPx2CellCoord(clickPos: Position[Int], origin: Position[Int]): Position[Int] = {
    val centered = clickPos - (origin + 5)
    Position((centered.x / 10.0).round.toInt, (centered.y / 10.0).round.toInt)
  }

  def containedInRect(gs: LivingWorld, rect: Position[Int]): LivingWorld =
    gs.filter(aa => aa.areTouching(Position(0, 0), rect))

  def randomize(): LivingWorld = {
    livingWorld = (for (n <- 0 to 4000) yield Position(r.nextInt(160) - 80, r.nextInt(80) - 40)).toSet
    livingWorld
  }

  def tick(rulestringB: Set[Int] = Set(3), // Default to Conway's GoL B3S23
           rulestringS: Set[Int] = Set(2, 3)): LivingWorld = {
    assume(generation != Long.MaxValue, "Generations outnumbered")
    generation += 1

    /** A Map containing only ''coordinates'' that are neighbors of XYpos which
      * are alive, together with the ''number'' of XYpos it is neighbor of.
      */
    val neighbors: Map[Position[Int], Int] =
      livingWorld.toSeq.flatMap(_.mooreNeighborHood(1)).groupBy(identity)
        .map { case (cell, list) => (cell, list.size) }

    // Filter all neighbors for desired characteristics

    // Criterion of rulestring Birth
    val reproduces: Set[Position[Int]] = neighbors.filter { case (_, n) => rulestringB contains n }.keySet

    // Criterion of Survivors rulestring
    @inline
    def survivors: Set[Position[Int]] = {
      val survivorPredicate: (Int) => Boolean = (n) => rulestringS -- rulestringB contains n

      (neighbors -- reproduces).filter { case (_, n) => survivorPredicate(n) }.keySet intersect livingWorld
    }

    livingWorld = survivors ++ reproduces
    livingWorld
  } // def tick(…

}
