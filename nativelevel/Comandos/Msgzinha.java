/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.Comandos;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author vntgasl
 */
public class Msgzinha implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if (args.length > 2) {
            String jogador = args[0];
            Player p = Bukkit.getPlayer(jogador);
            if (p == null) {
                return false;
            }
            String msg = "";
            for (int x = 1; x < args.length; x++) {
                msg += args[x];
            }
            p.sendMessage(msg.replaceAll("\\&", "§"));
        }
        return true;
    }

}
