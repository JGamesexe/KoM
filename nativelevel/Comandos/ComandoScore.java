/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.Comandos;

import nativelevel.KoM;
import nativelevel.scores.SBCore;
import nativelevel.utils.Cooldown;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.HashSet;
import java.util.UUID;

/**
 * @author Carlos
 */
public class ComandoScore implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        Player p = (Player) cs;
        if (Cooldown.isCooldown(p, "comandoscore")) {
            p.sendMessage("§4§l[!] §eEspere um pouco...");
            return false;
        }
        Cooldown.setMetaCooldown(p, "comandoscore", 10000);
        if (KoM.sb.disableSidebar.contains(p.getUniqueId())) {
            KoM.sb.enableSidebar(p);
            p.sendMessage("§c§l[!] Scoreboard adicionado!");
        } else {
            KoM.sb.disableSidebar(p);
            p.sendMessage("§c§l[!] Scoreboard removido!");
        }
        return false;
    }

}
