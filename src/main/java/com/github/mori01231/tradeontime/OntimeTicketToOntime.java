package com.github.mori01231.tradeontime;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Bukkit.getServer;

public class OntimeTicketToOntime implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //Executed by Player
        if (sender instanceof Player){
            Player player = (Player) sender;


            //Convert all ontime tickets in inventory to ontime points.
            if(args.length == 0) {
                //Player has at least 1 ontime ticket in their inventory
                if(OntimeTickets(player) > 0){
                    ConvertOntimeTickets(player);
                }
            }
            //Convert ontime tickets in inventory to ontime points up until the given number.
            else{

            }

        }
        //Executed by Console
        else{
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lこのコマンドはコンソールからは実行できません"));
        }

        return true;
    }

    //get amount of available slots in player inventory excluding armor and offhand
    public int OntimeTickets(Player player){

        //get display name of ontime ticket
        String MMDisplayName = TradeOntime.getInstance().getConfig().getString("MythicMobsDisplayName");

        //get player inventory.
        Inventory inv = player.getInventory();

        //initializing counter for slots.
        int tickets=0;

        //counting the number of available slots.
        for (ItemStack item: inv.getContents()) {
            try{
                if(item.getItemMeta().getDisplayName().equals(MMDisplayName)){
                    tickets += item.getAmount();
                }

            }catch (Exception e){
            }
        }


        //return the number of available slots.
        return tickets;
    }

    public String ConvertOntimeTickets(Player player){

        //get display name of ontime ticket
        String MMDisplayName = TradeOntime.getInstance().getConfig().getString("MythicMobsDisplayName");

        //get player inventory.
        Inventory inv = player.getInventory();

        //initializing counter for tickets and points.
        int tickets = 0;
        int points = 0;

        //counting the number of available slots.
        for (ItemStack item: inv.getContents()) {
            try{
                if(item.getItemMeta().getDisplayName().equals(MMDisplayName)){
                    tickets += item.getAmount();
                    item.setType(Material.AIR);
                }

            }catch (Exception e){
            }
        }

        points = tickets * 10;

        //Give player ontime points.
        getServer().dispatchCommand(getServer().getConsoleSender(), "points give " + player.getName() + " " + points);

        //Return the message to be sent to the player
        return "オンタイムチケットを" + tickets + "枚オンタイムポイントに変換しました。";
    }
}
