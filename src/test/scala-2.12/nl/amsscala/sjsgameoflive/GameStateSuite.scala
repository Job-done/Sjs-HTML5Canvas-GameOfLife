package nl.amsscala
package sjsgameoflive

class GameStateSuite extends SuiteSpec {

  describe("Living world") {
    describe("should perform world to next world function for blinkers") {
      it("should transform 3 alive cell vertically") {
        assert(LivingWorld.tick(LivingWorld((0, 0), (0, 1), (0, -1))) === LivingWorld((0, 0), (-1, 0), (1, 0)))
      }
      it("should transform 3 alive cell horizontally") {
        assert(LivingWorld.tick(LivingWorld((0, 0), (-1, 0), (1, 0))) === LivingWorld((0, 0), (0, 1), (0, -1)))
      }
      val original6v = LivingWorld((0, 0), (0, 1), (0, -1), (1, 0), (1, -1), (1, -2))
      val original6h = original6v.map { case Position(x, y) => Position(y, x) }
      it("should transform 6 alive cell vertically") {
        assert(LivingWorld.tick(original6v) === LivingWorld((2, -1), (1, -2), (1, 1), (0, -2), (-1, 0), (0, 1)))
      }
      it("should transform blinker6 back") {
        assert(LivingWorld.tick(LivingWorld((2, -1), (1, -2), (1, 1), (0, -2), (-1, 0), (0, 1))) === original6v)
      }
      it("should transform blinker6 horizontally twice") {
        assert(LivingWorld.tick(LivingWorld.tick(original6h)) === original6h)
      }
    }
  }
}
