package com.expensivekoala.refined_avaritia.util;

public enum RecipeType {
    AVARITIA(9, 9),
    EC_BASIC(3, 3),
    EC_ADVANCED(5, 5),
    EC_ELITE(7, 7),
    EC_ULTIMATE(9, 9);

    public int width, height;

    RecipeType(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
