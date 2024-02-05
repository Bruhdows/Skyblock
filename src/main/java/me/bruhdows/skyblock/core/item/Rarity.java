package me.bruhdows.skyblock.core.item;

import java.awt.*;

public enum Rarity {

    COMMON(Color.WHITE),
    UNCOMMON(Color.GREEN),
    RARE(Color.CYAN),
    EPIC(Color.MAGENTA),
    LEGENDARY(Color.ORANGE),
    MYTHIC(Color.PINK),
    SPECIAL(Color.RED);

    public final Color color;

    Rarity(Color color) {
        this.color = color;
    }

}
