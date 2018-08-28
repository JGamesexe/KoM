/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.Comandos;

import nativelevel.KoM;
import nativelevel.Listeners.GeneralListener;
import nativelevel.sisteminhas.QuestsIntegracao;
import nativelevel.sisteminhas.XP;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author vntgasl
 */

public class DarExp implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {

        if (args.length >= 2) {

            String player = args[0];

            int level;
            int exp;

            if (args.length >= 3) {

                level = Integer.valueOf(args[2]);

                args[1] = args[1].replaceAll("%", "");

                exp = ((XP.getExpProximoNivel(level) * Integer.valueOf(args[1])) / 100);
                KoM.debug("Tentando dar " + Integer.valueOf(args[2]) + "% de XP do level " + level + " pro " + player);

            } else {

                exp = Integer.valueOf(args[1]);
                KoM.debug("Tentando dar " + exp + "XP pro " + player);

            }

            Player p = Bukkit.getPlayer(player);

            if (p != null) {

                p.sendMessage(ChatColor.GREEN + "§f- §b" + exp + " EXP");
                XP.bruteChangeExp(p, exp);
                KoM.debug("Dei XP pro " + player);

            }


        }

        return true;
    }


}
