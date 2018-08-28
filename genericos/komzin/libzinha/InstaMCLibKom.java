package genericos.komzin.libzinha;

import genericos.komzin.libzinha.comandos.*;
import genericos.komzin.libzinha.listeners.GeralListener;
import genericos.komzin.libzinha.utils.ConfigProperties;
import nativelevel.KoM;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Logger;

public class InstaMCLibKom {
    public static InstaMCLibKom instancia = null;
    public static final Logger log = Logger.getLogger("Minecraft");
    public static Permission permission = null;
    public static Economy economy = null;
    public static Chat chat = null;
    public static SimpleClans sc = null;
    public static ConfigProperties conf;

    public static void addlog(String loga) {
        log.info("[LibKom] " + loga);
    }

    public void onEnable() {
        instancia = this;
        try {
            conf = new ConfigProperties(KoM._instance.getDataFolder() + "/chat.properties");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (!conf.getConfig().containsKey("ValorGlobal")) {
            conf.getConfig().setProperty("ValorGlobal", "50");
            conf.getConfig().setProperty("Moeda", "Coins");
            conf.saveConfig();
        }
        setupPermissions();
        setupEconomy();
        setupChat();
        Bukkit.getPluginCommand("gm").setExecutor(new ComandoGm());
        Bukkit.getPluginCommand("ops").setExecutor(new ComandoOps());
        Bukkit.getPluginCommand("komlib").setExecutor(new ComandoKomLib());
        Bukkit.getPluginCommand("foguete").setExecutor(new ComandoFoguete());
        Bukkit.getPluginCommand("hat").setExecutor(new ComandoHat());
        Bukkit.getServer().getPluginManager().registerEvents(new GeralListener(), KoM._instance);

        Plugin plug = KoM._instance.getServer().getPluginManager().getPlugin("SimpleClans");
        if (plug != null) {
            sc = (SimpleClans) plug;
        }
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = KoM._instance.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return economy != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = KoM._instance.getServer().getServicesManager().getRegistration(Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return permission != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = KoM._instance.getServer().getServicesManager().getRegistration(Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }
        return chat != null;
    }

    public static PlayerInfo getinfo(Player p) {
        if (p.hasMetadata("PlayerInfoKomLib")) {
            return (PlayerInfo) p.getMetadata("PlayerInfoKomLib").get(0).value();
        }
        PlayerInfo meta = new PlayerInfo();
        p.setMetadata("PlayerInfoKomLib", new FixedMetadataValue(KoM._instance, meta));
        return meta;
    }
}

/* Location:              C:\Users\User\Desktop\REPO\InstaMCLibKom.jar!\instamc\coders\libkom\InstaMCLibKom.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
