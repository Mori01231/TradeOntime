package com.github.mori01231.tradeontime.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GetOpenInventorySlots {

    public int AvailableSlots(Player player){
        //get player inventory.
        Inventory inv = player.getInventory();

        //initializing counter for slots.
        int slots=0;

        //counting the number of available slots.
        for (ItemStack item: inv.getContents()) {
            if(item == null) {
                slots++;
            }
        }

        //return the number of available slots.
        return slots;
    }
}
