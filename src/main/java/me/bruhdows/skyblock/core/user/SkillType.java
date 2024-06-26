package me.bruhdows.skyblock.core.user;

import java.io.Serializable;

public enum SkillType implements Serializable {

    COMBAT(100, 1.2),
    MINING(100, 1.2),
    FARMING(100, 1.2),
    FORAGING(100, 1.2);

    public final int defaultRequirement;
    public final double multipiler;

    SkillType(int defaultRequirement, double multipiler) {
        this.defaultRequirement = defaultRequirement;
        this.multipiler = multipiler;
    }
}
