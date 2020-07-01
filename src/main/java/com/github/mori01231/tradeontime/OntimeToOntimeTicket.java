package com.github.mori01231.tradeontime;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class OntimeToOntimeTicket implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        int points = Integer.parseInt(args[0]);

        if(TradeOntime.getInstance().getPlayerPoints().getAPI().take("Player", points)) {
            //Success
        } else {
            //Failed, probably not enough points
        }


        return false;
    }
}
