package com.github.mori01231.tradeontime;

//import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class TradeOntime extends JavaPlugin {

    private static TradeOntime instance;
    public TradeOntime (){
        instance = this;
    }
    public static TradeOntime getInstance() {
        return instance;
    }
/*
    private PlayerPoints playerPoints;

    // Validate that we have access to PlayerPoints
    // @return True if we have PlayerPoints, else false.
    private boolean hookPlayerPoints() {
        final Plugin plugin = this.getServer().getPluginManager().getPlugin("PlayerPoints");
        playerPoints = PlayerPoints.class.cast(plugin);
        return playerPoints != null;
    }
    // Accessor for other parts of your plugin to retrieve PlayerPoints.
    // @return PlayerPoints plugin instance
    public PlayerPoints getPlayerPoints() {
        return playerPoints;
    }
*/

    @Override
    public void onEnable() {
        getLogger().info("BetaTest has been enabled.");
        this.getCommand("tradeontime").setExecutor(new OntimeToOntimeTicket());

        //hookPlayerPoints();

        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
