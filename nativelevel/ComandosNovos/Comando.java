/*

 */
package nativelevel.ComandosNovos;

import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.ExecutorType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Carlos André Feldmann Júnior
 */
public abstract class Comando extends Command {

    public void showSubCommands(CommandSender cs) {
        cs.sendMessage(ChatColor.YELLOW + ".________________oO " + getName() + " Oo_____________");
        cs.sendMessage(ChatColor.YELLOW + "|");
        for (SubCmd cmd : subs) {
            if (cmd.type != ExecutorType.OP) {
                if (cmd.type.equals(ExecutorType.LEADER)) {
                    cs.sendMessage(ChatColor.YELLOW + "|" + ChatColor.GREEN + " - /" + getName() + " " + cmd.cmd + "§6 (Lider)");
                } else {
                    cs.sendMessage(ChatColor.YELLOW + "|" + ChatColor.GREEN + " - /" + getName() + " " + cmd.cmd);
                }
            } else if (cs.isOp()) {
                cs.sendMessage(ChatColor.YELLOW + "|" + ChatColor.BLUE + " - /" + getName() + " " + cmd.cmd + " (Op)");
            }
        }
        cs.sendMessage(ChatColor.YELLOW + "|_______________________________________");
    }

    protected List<SubCmd> subs = new ArrayList<SubCmd>();

    ExecutorType tipo;
    public String permission = null;
    public String cmd;

    public abstract void usouComando(CommandSender cs, String[] args);

    protected void usouComandoBase(CommandSender cs, String[] args) {

        if (args.length >= 1) {
            for (SubCmd args0 : subs) {
                if (args0.cmd.equalsIgnoreCase(args[0])) {
                    args0.executeSubCmd(cs, args);
                }
            }
        } else {
            showSubCommands(cs);
        }

    }

    private CommandExecutor exe = null;

    public Comando(String name, ExecutorType c) {
        super(name);
        this.cmd = name;
        tipo = c;
    }

    @Override
    public boolean execute(CommandSender cs, String commandLabel, String[] strings) {
        if (exe != null) {

            if (commandLabel.equalsIgnoreCase(getName()) || getAliases().contains(commandLabel)) {

                if (tipo == ExecutorType.CONSOLE && cs instanceof Player) {
                    cs.sendMessage("§aComando só pode ser executado no console!");
                    return true;
                }

                if ((tipo == ExecutorType.OP || tipo == ExecutorType.PLAYER) && !(cs instanceof Player)) {
                    cs.sendMessage("Comando só pode ser executado em jogo");
                    return false;
                }

                if (tipo == ExecutorType.PERMISSION) {
                    if (permission != null && !cs.hasPermission(permission)) {
                        cs.sendMessage("§cVocê não tem permissão para esse comando!");
                        return false;
                    }
                }

                if (tipo == ExecutorType.LEADER) {
                    if (ClanLand.manager.getClanPlayer((Player) cs) == null) {
                        cs.sendMessage("§cVocê não tem uma guilda para executar esse comando");
                        return false;
                    } else if (!ClanLand.manager.getClanPlayer((Player) cs).isLeader()) {
                        cs.sendMessage("§cVocê não é lider da guilda para executar esse comando");
                        return false;
                    }
                }

                if (tipo == ExecutorType.OPCONSOLE) {
                    if (cs instanceof Player) {
                        if (!cs.isOp()) {
                            cs.sendMessage("§cVocê não tem permissão para esse comando!");
                            return true;
                        }
                    }

                }

                if (tipo == ExecutorType.OP && !((Player) cs).isOp()) {
                    cs.sendMessage("§cVocê não tem permissão para esse comando!");
                    return false;
                }

                usouComando(cs, strings);

            }
            exe.onCommand(cs, this, commandLabel, strings);
        }
        return false;
    }

    public void setExecutor(CommandExecutor exe) {
        this.exe = exe;
    }

}
