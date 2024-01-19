package me.bruhdows.skyblock.storage.config.section;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabaseSection extends OkaeriConfig {

    private MongoDBSection mongoDB = new MongoDBSection();
    private RedisSection redis = new RedisSection();

}
