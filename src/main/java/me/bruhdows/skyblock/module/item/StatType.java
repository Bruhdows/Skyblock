package me.bruhdows.skyblock.module.item;

import lombok.Getter;

import java.awt.*;

@Getter
public enum StatType {

    DAMAGE("Damage", Color.RED),
    STRENGTH("Strength", Color.RED),
    CRIT_DAMAGE("Crit Damage", Color.CYAN),
    CRIT_CHANCE("Crit Chance", Color.CYAN),
    HEALTH("Health", Color.RED),
    DEFENSE("Defense", Color.GREEN),
    SPEED("Speed", Color.WHITE);

    public final String name;
    public final Color color;

    StatType(String name, Color color) {
        this.name = name;
        this.color = color;
    }

}
