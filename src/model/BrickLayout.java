package model;

/**
 * Enum containing predefined brick layouts for each level and difficulty.
 * Each layout is represented by a 10x10 integer matrix, where:
 * 1 - brick is present,
 * 0 - empty space.
 */
public enum BrickLayout {
    LEVEL_1_EASY(new int[][]{
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1 ,1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1 ,1, 1, 1, 1, 1, 1, 1, 1},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0}
    }),
    LEVEL_1_MEDIUM(new int[][]{
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {0, 0, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 0 ,0, 1, 1, 1, 1, 0, 0, 0},
            {0, 0 ,1, 0, 0, 0, 0, 1, 0, 0},
            {0, 1 ,0, 0, 0, 0, 0, 0, 1, 0},
            {1, 0 ,0, 0, 0, 0, 0, 0, 0, 1},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0}
    }),
    LEVEL_1_HARD(new int[][]{
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 0, 0, 0, 0, 0, 0, 1, 1},
            {1, 0, 1, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 0, 1, 0, 0, 1, 0, 0, 1},
            {1, 0, 0, 0, 1, 1, 0, 0, 0, 1},
            {1, 0, 0, 1, 0, 0, 1, 0, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 1, 0, 1},
            {1, 1, 0, 0, 0, 0, 0, 0, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0}
    }),
    LEVEL_2_EASY(new int[][]{
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1 ,1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1 ,1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1 ,0, 0, 0, 0, 0, 0, 1, 1},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0}
    }),
    LEVEL_2_MEDIUM(new int[][]{
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {0, 0, 1, 1, 1, 1, 1, 1, 0, 0},
            {1, 0 ,0, 1, 1, 1, 1, 0, 0, 1},
            {0, 0 ,1, 0, 1, 1, 0, 1, 0, 0},
            {0, 1 ,0, 0, 0, 0, 0, 0, 1, 0},
            {1, 1 ,1, 0, 0, 0, 0, 1, 1, 1},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0}
    }),
    LEVEL_2_HARD(new int[][]{
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 1, 1, 0, 0, 1, 1, 0, 1},
            {1, 0, 1, 0, 1, 1, 0, 1, 0, 1},
            {1, 0, 1, 1, 0, 0, 1, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0}
    }),
    LEVEL_3_EASY(new int[][]{
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1 ,1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1 ,1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1 ,1, 1, 1, 1, 1, 1, 1, 1},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0}
    }),
    LEVEL_3_MEDIUM(new int[][]{
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0 ,0, 1, 1, 1, 1, 0, 0, 1},
            {0, 0 ,1, 0, 1, 1, 0, 1, 0, 0},
            {0, 1 ,0, 0, 1, 1, 0, 0, 1, 0},
            {1, 1 ,1, 0, 1, 1, 0, 1, 1, 1},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0 ,0, 0, 0, 0, 0, 0, 0, 0}
    }),
    LEVEL_3_HARD(new int[][]{
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 0, 0, 0, 0, 0, 0, 1, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 1, 1, 0, 0, 1, 1, 0, 1},
            {1, 0, 1, 0, 1, 1, 0, 1, 0, 1},
            {1, 0, 1, 1, 0, 0, 1, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 1, 0, 0, 0, 0, 0, 0, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1 ,0, 0, 0, 0, 0, 0, 1, 1}
    });

    public final int[][] layout;

    BrickLayout(int[][] layout) { this.layout = layout; }

    /**
     * Returns a 2D array representing the brick layout for this level/difficulty.
     */
    public int[][] getLayout() { return layout; }
}