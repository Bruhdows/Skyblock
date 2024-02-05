package me.bruhdows.skyblock.core.mob;

import lombok.Getter;
import me.bruhdows.skyblock.util.TextUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MobManager {

    @Getter
    public final Map<String, Mob> mobs = new HashMap<>();
    private final Map<UUID, String> alive = new HashMap<>();

    public void registerMob(Mob mob) {
        if (mobs.containsKey(mob.getId())) {
            TextUtil.warn("Duplicate mob found! Skipping. (" + mob.getId() + ")");
            return;
        }
        mobs.put(mob.getId(), mob);
    }

    public Mob getAliveMob(UUID uuid) {
        return mobs.get(alive.get(uuid));
    }

    public void newMob(UUID uuid, String id) {
        if (alive.containsKey(uuid)) {
            TextUtil.warn("Duplicate mob found! Skipping. (" + uuid + ")");
            return;
        }
        alive.put(uuid, id);
    }

    public void removeMob(UUID uuid) {
        alive.remove(uuid);
    }

    public Mob getMob(String id) {
        return mobs.get(id);
    }
}
