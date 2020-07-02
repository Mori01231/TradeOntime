package com.github.mori01231.tradeontime;

import com.github.mori01231.tradeontime.utils.GetOpenInventorySlots;
import com.github.mori01231.tradeontime.utils.MessageInterceptingCommandRunner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;

public class OntimeToOntimeTicket implements CommandExecutor {

    public GetOpenInventorySlots GetSlots;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){
            if(args.length == 1) {

                Player player = (Player) sender;

                String PlayerName = player.getName();

                String originalOutput;
                String pointsholder = "";

                int points = 0;
                int haspoints;
                try{
                    points = Integer.parseInt(args[0]);
                }catch(NullPointerException e){
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l変換するポイント数は正の整数で指定してください。"));
                    return false;
                }



                if (points < 0){
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l変換するポイント数は正の整数で指定してください。"));
                    return false;
                }

                String MMItemName = TradeOntime.getInstance().getConfig().getString("MythicMobsItemName");

                final MessageInterceptingCommandRunner cmdRunner = new MessageInterceptingCommandRunner(Bukkit.getConsoleSender());
                Bukkit.dispatchCommand(cmdRunner, "points look " + PlayerName);


                originalOutput = cmdRunner.getMessageLogStripColor();


                originalOutput = originalOutput.replace("\n", "").replace("\r", "");

                pointsholder = originalOutput.substring(27 + PlayerName.length());


                haspoints = Integer.parseInt(pointsholder.substring(0, pointsholder.length() - 7));

                // You can then reset the message buffer with the following and re-use the the cmdRunner to run more commands - or just let all the outputs concatenate together
                cmdRunner.clearMessageLog();

                if (points <= haspoints){
                    int takepoints = points - (points % 10);
                    System.out.println(takepoints);
                    if(takepoints == 0){
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lオンタイムポイントは10以上の整数で指定してください。"));
                        return false;
                    }
                    int giveitems = takepoints/10;
                    getServer().dispatchCommand(getServer().getConsoleSender(), "mm i give " + PlayerName + " " + MMItemName + " " + giveitems);
                    getLogger().info(PlayerName + "にMMアイテム " + MMItemName + " を " + giveitems + " 個与えました。");
                    getServer().dispatchCommand(getServer().getConsoleSender(), "points take " + PlayerName + " " + takepoints);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&l" + takepoints + " オンタイムポイントをオンタイムチケット " + giveitems + " 枚に変換しました。"));
                }
                else{
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lオンタイムポイントは" + haspoints + "以下の10以上の整数で指定してください。"));
                    return false;
                }
                /*
                if (TradeOntime.getInstance().getPlayerPoints().getAPI().take(PlayerName, points)) {
                    //Success
                    getServer().dispatchCommand(getServer().getConsoleSender(), "mm i give " + player.getName() + " " + MMItemName + " " + points);
                    getLogger().info(PlayerName + "にMMアイテム " + MMItemName + " を " + points + " 個与えました。");

                } else {
                    //Failed, probably not enough points
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lポイントが足りません。"));
                }
                */

            }
            else if(args.length == 0){
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l変換するポイント数は正の整数で指定してください。"));
                return false;
            }
        }
        else{
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lこのコマンドはコンソールからは実行できません"));
        }


        return true;
    }
}
