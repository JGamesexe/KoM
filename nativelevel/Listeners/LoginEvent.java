package nativelevel.Listeners;

import nativelevel.KoM;
import nativelevel.utils.Whitelist;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import pixelmc.ServerReboot;

public class LoginEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void loginEvent(PlayerLoginEvent event) {
        if (event.getPlayer().isOnline()) return;
        if (!KoM.serverIniciado) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§fAguarde servidor ainda iniciando...");
            return;
        }

        if (KoM.reiniciando || ServerReboot.deepRestart) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§6O Inverno está Chegando.");
            return;
        }

        if (Whitelist.whitelistMode && Whitelist.loginEvent(event)) return;

        if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            if (event.getPlayer().isOp())
                event.allow();
            else if (event.getPlayer().hasPermission("kom.vip"))
                event.allow();
            else
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§6Servidor Lotado! Adquira seu Cavaleiro, Templario ou Lord e entre a qualquer momento!");
        }

    }


}
