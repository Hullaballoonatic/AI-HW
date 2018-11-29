package controller

object Settings {
    const val INITIAL_CONVERGENCE_ITERATION_LIMIT = 10000000
    const val INITIAL_LEARNING_RATE = 0.1
    const val INITIAL_EXPLORATION_RATE = 0.05
    const val INITIAL_MISTAKE_RATE = 0.3
    const val MISTAKE_RATE_FLOOR = 0.03
    const val DECAY_RATE = 0.98
    const val INITIAL_FUTURE_FOCUS_RATE = 0.97
    const val INITIAL_CONVERGENCE_ALTERATION_RATE_LIMIT = 0.1
    const val HAZARD_REWARD = -0.1
    const val GOAL_REWARD = 1.0
    const val NUM_COLS = 20
    const val NUM_ROWS = 10
    const val NUM_HAZARDS = 15

    const val OUT_FP = "src\\main\\resources\\output.txt"
}