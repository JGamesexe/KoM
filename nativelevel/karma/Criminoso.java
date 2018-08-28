package nativelevel.karma;

import nativelevel.KoM;
import nativelevel.MetaShit;
import nativelevel.scores.SBCore;
import nativelevel.scores.SBCoreListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

/**
 * @author Ziden
 */
public class Criminoso {

    private static HashSet<UUID> criminosos = new HashSet<UUID>();

    public static boolean isCriminoso(Player p) {
        return criminosos.contains(p.getUniqueId());
    }

    public static void setCriminoso(Player p) {

        KoM.debug("Setando crim");

        if (p.getWorld().getName().equalsIgnoreCase("arena") || p.getWorld().getName().equalsIgnoreCase("woe"))
            return;

        if (criminosos.contains(p.getUniqueId())) {

            int idTask = (Integer) MetaShit.getMetaObject("crim", p);
            Bukkit.getScheduler().cancelTask(idTask);

        } else {
            p.sendMessage(KoM.tag + ChatColor.RED + "Você agora é um criminoso por alguns minutos");
            if (p.getLevel() < 10 && !p.hasMetadata("1crim")) {
                MetaShit.setMetaObject("1crim", p, true);
                p.sendMessage("§e§l[Dica] §aEnquanto você for um criminoso, poderão te atacar livremente.");
                p.sendMessage("§e§l[Dica] §aE Jogadores poderão te matar sem perder Karma.");
            }
        }

        final UUID u = p.getUniqueId();
        criminosos.add(p.getUniqueId());

        KoM.sb.updatePlayer(p);

        Runnable r = () -> {
            KoM.debug("Rodando task acabando criminal");
            if (criminosos.contains(u)) criminosos.remove(u);

            Player p1 = Bukkit.getPlayer(u);
            if (p1 != null) {
                KoM.sb.updatePlayer(p1);
                p1.sendMessage("§2Voce não é mais um criminoso");
            }
        };
        int idTask = Bukkit.getScheduler().scheduleSyncDelayedTask(KoM._instance, r, 20 * 60 * 3);
        MetaShit.setMetaObject("crim", p, idTask);
    }

}
