package me.bruhdows.skyblock.storage.config;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Data extends OkaeriConfig {

    private String serverId = UUID.randomUUID().toString();

}
