package me.bruhdows.skyblock.module.ability;

import java.awt.*;

public enum AbilityCostType {

    MANA("Mana", Color.CYAN),
    COINS("Coins", Color.ORANGE),
    HEALTH("Health", Color.RED);

    public final String name;
    public final Color color;

    AbilityCostType(String name, Color color) {
        this.name = name;
        this.color = color;
    }
}
