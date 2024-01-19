package me.bruhdows.skyblock.util;

import java.awt.*;

public class ColorUtil {

    public static String getColorHex(Color color) {
        return String.format("&#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
}
