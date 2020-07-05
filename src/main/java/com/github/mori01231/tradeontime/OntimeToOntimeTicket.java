package com.github.mori01231.tradeontime;

import com.github.mori01231.tradeontime.utils.MessageInterceptingCommandRunner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;

public class OntimeToOntimeTicket implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (sender instanceof Player){
            if(args.length == 1) {

                Player player = (Player) sender;
                String PlayerName = player.getName();
                String MMItemName = TradeOntime.getInstance().getConfig().getString("MythicMobsItemName");

                int points = 0;
                int haspoints;
                try{
                    points = Integer.parseInt(args[0]);
                }catch(Exception e){
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l変換するポイント数は正の整数で指定してください。"));
                    return false;
                }

                //Points was not negative
                if (points < 0){
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l変換するポイント数は正の整数で指定してください。"));
                    return false;
                }

                haspoints = PlayerPoints(player);

                //points was less than 10
                if (haspoints < 10){
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lあなたの所持オンタイムポイントが10未満のためオンタイムチケットへの変換が出来ません。"));
                    return false;
                }

                //Convert to ontime tickets
                if (points <= haspoints){
                    int takepoints = points - (points % 10);
                    System.out.println(takepoints);

                    //points was between 0 and 10
                    if(takepoints == 0){
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lオンタイムポイントは10以上の整数で指定してください。"));
                        return false;
                    }

                    int giveitems = takepoints/10;
                    int RequiredSlots = 100;

                    if (giveitems % 64 == 0){
                        RequiredSlots = giveitems / 64;
                    }else{
                        RequiredSlots = (giveitems - (giveitems % 64) + 64) / 64;
                    }

                    //If the players doesn't have enough slots, no transaction
                    if (AvailableSlots(player) < RequiredSlots) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l現在インベントリには" + AvailableSlots(player) + "スロットの空きがあります。"));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lオンタイムチケットがインベントリに入りきりません。インベントリに空きを増やしたうえで再度コマンドを実行してください。"));
                        return false;
                    }

                    //Actual transaction
                    getServer().dispatchCommand(getServer().getConsoleSender(), "mm i give " + PlayerName + " " + MMItemName + " " + giveitems);
                    getLogger().info(PlayerName + "にMMアイテム " + MMItemName + " を " + giveitems + " 個与えました。");
                    getServer().dispatchCommand(getServer().getConsoleSender(), "points take " + PlayerName + " " + takepoints);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b" + takepoints + " オンタイムポイントをオンタイムチケット " + giveitems + " 枚に変換しました。"));
                }
                //Has less points than the points in argument
                else{
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lオンタイムポイントは" + haspoints + "以下かつ10以上の整数で指定してください。"));
                    return false;
                }
            }
            //No points argument
            else if(args.length == 0){
                Player player = (Player) sender;
                String PlayerName = player.getName();
                String MMItemName = TradeOntime.getInstance().getConfig().getString("MythicMobsItemName");

                int haspoints = PlayerPoints(player);

                int takepoints = haspoints - (haspoints % 10);
                System.out.println(takepoints);

                //points was between 0 and 10
                if(takepoints == 0){
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lあなたの所持オンタイムポイントが10未満のためオンタイムチケットへの変換が出来ません。"));
                    return false;
                }

                int giveitems = takepoints/10;
                int RequiredSlots = 100;

                if (giveitems % 64 == 0){
                    RequiredSlots = giveitems / 64;
                }else{
                    RequiredSlots = (giveitems - (giveitems % 64) + 64) / 64;
                }

                //If the players doesn't have enough slots, no transaction
                if (AvailableSlots(player) < RequiredSlots) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l現在インベントリには" + AvailableSlots(player) + "スロットの空きがあり、" + AvailableSlots(player)*640 + "ポイントまでしか変換できません。"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lオンタイムチケットがインベントリに入りきりません。インベントリに空きを増やしたうえで再度コマンドを実行してください。"));
                    return false;
                }

                //Actual transaction
                getServer().dispatchCommand(getServer().getConsoleSender(), "mm i give " + PlayerName + " " + MMItemName + " " + giveitems);
                getLogger().info(PlayerName + "にMMアイテム " + MMItemName + " を " + giveitems + " 個与えました。");
                getServer().dispatchCommand(getServer().getConsoleSender(), "points take " + PlayerName + " " + takepoints);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b" + takepoints + " オンタイムポイントをオンタイムチケット " + giveitems + " 枚に変換しました。"));
            }
        }
        //Can't convert tickets from console
        else{
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lこのコマンドはコンソールからは実行できません"));
        }
        return true;
    }


    //get amount of available slots in player inventory excluding armor and offhand
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

        //Compensating for empty offhand slot
        if(player.getInventory().getItemInOffHand().getType() == Material.AIR){
            slots--;
        }

        //Compensating for empty armor slots
        for (ItemStack item: player.getInventory().getArmorContents()){
            if(item == null) {
                slots--;
            }
        }

        //return the number of available slots.
        return slots;
    }

    public int PlayerPoints(Player player){

        String PlayerName = player.getName();
        String MMItemName = TradeOntime.getInstance().getConfig().getString("MythicMobsItemName");
        String originalOutput;
        String pointsholder = "";

        int points = 0;
        int haspoints;

        //Get the points of the player.
        final MessageInterceptingCommandRunner cmdRunner = new MessageInterceptingCommandRunner(Bukkit.getConsoleSender());
        Bukkit.dispatchCommand(cmdRunner, "points look " + PlayerName);

        //parse the returned string and make it a single integer of points
        originalOutput = cmdRunner.getMessageLogStripColor();
        originalOutput = originalOutput.replace("\n", "").replace("\r", "");
        pointsholder = originalOutput.substring(27 + PlayerName.length());
        haspoints = Integer.parseInt(pointsholder.substring(0, pointsholder.length() - 7));

        // You can then reset the message buffer with the following and re-use the the cmdRunner to run more commands - or just let all the outputs concatenate together
        cmdRunner.clearMessageLog();

        return haspoints;
    }
}
