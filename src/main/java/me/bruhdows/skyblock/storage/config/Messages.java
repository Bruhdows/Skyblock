package me.bruhdows.skyblock.storage.config;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Messages extends OkaeriConfig {

    private String usage = "&cUsage: {USAGE}";
    private String usageList = "&c- {USAGE}";
    private String noPermission = "&cNo permission!";

}
