package model.grid

import controller.Settings.GOAL_REWARD
import controller.Settings.HAZARD_REWARD

enum class SpaceType(val label: Char, val reward: Double = 0.0) {
    MOVE_LEFT('˂'),
    MOVE_RIGHT('˃'),
    MOVE_UP('˄'),
    MOVE_DOWN('˅'),
    HAZARD('☠', HAZARD_REWARD),
    START('S'),
    GOAL('G', GOAL_REWARD),
    NONE(' ');
}