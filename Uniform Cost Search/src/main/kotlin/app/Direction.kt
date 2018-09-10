package app

enum class Direction(val offset: Position) {
  N (Position(x =   0, y =  10)),
  NE(Position(x =  10, y =  10)),
  E (Position(x =  10, y =   0)),
  SE(Position(x =  10, y = -10)),
  S (Position(x =   0, y = -10)),
  SW(Position(x = -10, y = -10)),
  W (Position(x = -10, y =   0)),
  NW(Position(x = -10, y =  10));
}

val allDirections = listOf(Direction.N, Direction.NE, Direction.E, Direction.SE, Direction.S, Direction.SW, Direction.W, Direction.NW)