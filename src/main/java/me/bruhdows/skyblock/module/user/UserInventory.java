package me.bruhdows.skyblock.module.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public final class UserInventory implements Serializable {

    private ItemStack[] contents;
    private ItemStack[] armorContents;
    private ItemStack[] enderContents;

}
