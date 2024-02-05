package me.bruhdows.skyblock.storage.config;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Chat extends OkaeriConfig {

    public String chatFormat = "{NAME}&8: &7{MESSAGE}";
    public Map<String, String> groupsFormat = Map.of(
            "admin", "&c&lADMIN &c{NAME}&8: &f{MESSAGE}"
    );

}
