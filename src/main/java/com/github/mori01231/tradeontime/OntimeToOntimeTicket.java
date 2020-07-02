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

                int points = Integer.parseInt(args[0]);

                String MMItemName = TradeOntime.getInstance().getConfig().getString("MythicMobsItemName");

                final MessageInterceptingCommandRunner cmdRunner = new MessageInterceptingCommandRunner(Bukkit.getConsoleSender());
                Bukkit.dispatchCommand(cmdRunner, "points take " + PlayerName + " " + points );

                // TODO: do something useful with the captured messages

                System.out.println(cmdRunner.getMessageLogStripColor());

                // You can then reset the message buffer with the following and re-use the the cmdRunner to run more commands - or just let all the outputs concatenate together
                cmdRunner.clearMessageLog();

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

            }
        }
        else{
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&lこのコマンドはコンソールからは実行できません"));
        }


        return true;
    }
}
