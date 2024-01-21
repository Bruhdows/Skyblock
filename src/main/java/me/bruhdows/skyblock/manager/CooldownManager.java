package me.bruhdows.skyblock.manager;

import com.google.common.cache.CacheBuilder;
import me.bruhdows.skyblock.module.ability.Ability;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final Map<UUID, Instant> cooldowns = new HashMap<>();

    public void setCooldown(UUID key, Duration duration) {
        cooldowns.put(key, Instant.now().plus(duration));
    }

    public boolean hasCooldown(UUID key) {
        Instant cooldown = cooldowns.get(key);
        return cooldown != null && Instant.now().isBefore(cooldown);
    }

    public Instant removeCooldown(UUID key) {
        return cooldowns.remove(key);
    }

    public Duration getRemainingCooldown(UUID key) {
        Instant cooldown = cooldowns.get(key);
        Instant now = Instant.now();
        if (cooldown != null && now.isBefore(cooldown)) {
            return Duration.between(now, cooldown);
        } else {
            return Duration.ZERO;
        }
    }
}
