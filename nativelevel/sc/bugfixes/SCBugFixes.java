package nativelevel.sc.bugfixes;

import net.sacredlabyrinth.phaed.simpleclans.events.PlayerDemoteEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author NeT32
 */
public class SCBugFixes implements Listener {

    public static final int PROTOCOL_VERSION = 47;

    @EventHandler
    public void PlayerDemote(PlayerDemoteEvent event) {
        // Clan cai em ruinas quando não tem Leaders
        if (event.getClan().getLeaders().isEmpty()) {
            event.getClanPlayer().toPlayer().sendMessage(ChatColor.AQUA + "Você rebaixou você mesmo, sua Guilda caiu em ruinas.");
            event.getClan().disband();
        }
    }
}
