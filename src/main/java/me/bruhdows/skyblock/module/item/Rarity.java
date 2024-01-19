package me.bruhdows.skyblock.module.item;

import lombok.Getter;

import java.awt.*;

public enum Rarity {

    COMMON(Color.WHITE),
    UNCOMMON(Color.GREEN),
    RARE(Color.CYAN),
    EPIC(Color.MAGENTA),
    LEGENDARY(Color.ORANGE),
    MYTHIC(Color.PINK);

    public final Color color;

    Rarity(Color color) {
        this.color = color;
    }

}
