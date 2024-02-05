package me.bruhdows.skyblock.core.ability;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AbilityCost {

    private AbilityCostType type;
    private AbilityCostValueType valueType;
    private double value;

}
