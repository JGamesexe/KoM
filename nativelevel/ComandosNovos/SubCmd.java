/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.ComandosNovos;

import nativelevel.KoM;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.ExecutorType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author User
 */

public abstract class SubCmd {

    public String cmd;
    public ExecutorType type;
    public String permission = null;
    protected List<SubCmd> subs = new ArrayList<SubCmd>();

    public SubCmd(String cmd, ExecutorType type) {
        this.cmd = cmd;
        this.type = type;
    }

    protected void showSubCommands(CommandSender cs, String mainCmd) {
        cs.sendMessage(ChatColor.YELLOW + "|________________oO " + cmd + " Oo_____________");
        cs.sendMessage(ChatColor.YELLOW + "|");
        for (SubCmd cmd : subs) {
            if (cmd.type != ExecutorType.OP) {
                if (cmd.type.equals(ExecutorType.LEADER)) {
                    cs.sendMessage(ChatColor.YELLOW + "|" + ChatColor.GREEN + " - /" + mainCmd + " " + cmd.cmd + " §6(Lider)");
                } else {
                    cs.sendMessage(ChatColor.YELLOW + "|" + ChatColor.GREEN + " - /" + mainCmd + " " + cmd.cmd);
                }
            } else if (cs.isOp()) {
                cs.sendMessage(ChatColor.YELLOW + "|" + ChatColor.BLUE + " - /" + mainCmd + " " + cmd.cmd + " (Op)");
            }
        }
        cs.sendMessage(ChatColor.YELLOW + "|_______________________________________");
    }

    public abstract void execute(CommandSender cs, String[] args);

    public boolean executeSubCmd(CommandSender cs, String[] args) {

        if (type == ExecutorType.CONSOLE && cs instanceof Player) {
            cs.sendMessage("§aComando só pode ser executado no console!");
            return true;
        }

        if ((type == ExecutorType.OP || type == ExecutorType.PLAYER) && !(cs instanceof Player)) {
            cs.sendMessage("Comando só pode ser executado em jogo");
            return false;
        }

        if (type == ExecutorType.PERMISSION) {
            if (permission != null && !cs.hasPermission(permission)) {
                cs.sendMessage("§cVocê não tem permissão para esse comando!");
                return false;
            }
        }

        if (type == ExecutorType.LEADER) {
            if (ClanLand.manager.getClanPlayer((Player) cs) == null) {
                cs.sendMessage("§cVocê não tem uma guilda para executar esse comando.");
                return false;
            } else if (!ClanLand.manager.getClanPlayer((Player) cs).isLeader()) {
                cs.sendMessage("§cVocê não é lider da guilda para executar esse comando");
                return false;
            }
        }

        if (type == ExecutorType.OPCONSOLE) {
            if (cs instanceof Player) {
                if (!cs.isOp()) {
                    cs.sendMessage("§cVocê não tem permissão para esse comando...");
                    return true;
                }
            }

        }

        if (type == ExecutorType.OP && !((Player) cs).isOp()) {
            cs.sendMessage("§cVocê não tem permissão para esse comando!");
            return false;
        }

        execute(cs, args);
        return true;
    }


    public void executeBase(CommandSender cs, String[] args, String mainCmd) {

        if (args.length >= 2) {
            for (SubCmd args0 : subs) {
                if (args0.cmd.equalsIgnoreCase(args[1])) {
                    args0.executeSubCmd(cs, args);
                    type = args0.type;
                }
            }
        } else {
            showSubCommands(cs, mainCmd);
        }

    }

    public void runSync(Runnable r) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(KoM._instance, r);
    }

    public void runAsync(Runnable r) {
        new Thread(r).start();
    }
}
