package me.bruhdows.skyblock.core.ability;

import java.awt.*;

public enum AbilityTriggerType {

    LEFT_CLICK("LEFT CLICK", Color.YELLOW),
    LEFT_CLICK_SHIFT("SNEAK LEFT CLICK", Color.YELLOW),
    RIGHT_CLICK("RIGHT CLICK", Color.YELLOW),
    RIGHT_CLICK_SHIFT("SNEAK RIGHT CLICK", Color.YELLOW),
    SHIFT("SNEAK", Color.YELLOW);

    public final String name;
    public final Color color;

    AbilityTriggerType(String name, Color color) {
        this.name = name;
        this.color = color;
    }
}
