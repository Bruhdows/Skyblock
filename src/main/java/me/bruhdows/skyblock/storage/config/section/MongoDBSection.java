package me.bruhdows.skyblock.storage.config.section;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MongoDBSection extends OkaeriConfig {

    private String url = "";
    private String address = "";
    private String username = "";
    private String password = "";
    private String database = "";

}
