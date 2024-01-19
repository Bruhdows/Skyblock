package me.bruhdows.skyblock.storage.config;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import lombok.Setter;
import me.bruhdows.skyblock.storage.config.section.DatabaseSection;

@Getter
@Setter
public class Configuration extends OkaeriConfig {

    private boolean debug = false;
    private DatabaseSection database = new DatabaseSection();

}
