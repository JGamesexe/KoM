/*

 ╭╮╭━╮╱╱╭━╮╭━╮
 ┃┃┃╭╯╱╱┃┃╰╯┃┃
 ┃╰╯╯╭━━┫╭╮╭╮┃
 ┃╭╮┃┃╭╮┃┃┃┃┃┃
 ┃┃┃╰┫╰╯┃┃┃┃┃┃
 ╰╯╰━┻━━┻╯╰╯╰╯

 Desenvolvedor: ZidenVentania
 Colaboradores: NeT32, Gabripj, Feldmann
 Patrocionio: InstaMC

 */
package nativelevel.Comandos;

import nativelevel.CFG;
import nativelevel.KoM;
import nativelevel.Lang.L;
import nativelevel.Listeners.DeathEvents;
import nativelevel.sisteminhas.ClanLand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if (cs instanceof Player) {
            Player p = (Player) cs;
            if (!KoM.database.hasRegisteredClass(p.getUniqueId().toString())) {
                p.sendMessage(ChatColor.RED + L.m("Complete o tutorial, mero mortal ! Não tenha preguiça !"));
                return true;
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("set") && p.isOp()) {
                Location l = p.getLocation();
                p.getWorld().setSpawnLocation(l.getBlockX(), l.getBlockY(), l.getBlockZ());
            } else {
                if (p.isOp()) p.teleport(DeathEvents.nearbyVila(p));
                if (p.isOp()) return true;
                if (p.getWorld().getName().equalsIgnoreCase(CFG.mundoGuilda)) {
                    if (ClanLand.isSafeZone(p.getLocation())) {
                        p.teleport(DeathEvents.nearbyVila(p));
                        p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation().add(0, 1.7, 0), 3);
                    } else {
                        p.sendMessage("§cVocê só pode fazer isso em cidades, se ficou preso o unico jeito de sair é morrendo, você pode utilizar §o/kom suicidio§c para morrer...");
                    }
                } else if (p.getWorld().getName().equalsIgnoreCase(CFG.mundoDungeon)) {
                    p.sendMessage("§cO único modo de você sair de uma dungeon é encontrando o final ou voltando para o inicio...");
                    p.sendMessage("§cAo morrer em uma dungeon você volta para o inicio dela, você pode utilizar §o/kom suicidio§c para morrer...");
                }
            }
        }
        return false;
    }
}
