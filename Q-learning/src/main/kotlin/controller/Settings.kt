package controller

import model.table.p

object Settings {
    const val INITIAL_CONVERGENCE_ITERATION_LIMIT = 10000000
    const val INITIAL_LEARNING_RATE = 0.1
    const val INITIAL_EXPLORATION_RATE = 0.05
    const val INITIAL_MISTAKE_RATE = 0.5
    const val INITIAL_FUTURE_FOCUS_RATE = 0.97
    const val INITIAL_CONVERGENCE_ALTERATION_RATE_LIMIT = 0.05
    const val HAZARD_REWARD = -0.1
    const val GOAL_REWARD = 5.0
    const val NUM_COLS = 20
    const val NUM_ROWS = 10
    const val NUM_HAZARDS = 15
    val GOAL_LOCATION = NUM_COLS - 1 p NUM_ROWS - 1
    val START_LOCATION = 0 p 0
}