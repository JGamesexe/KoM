package nativelevel.ComandosNovos.commands;

import nativelevel.ComandosNovos.Comando;
import nativelevel.utils.ExecutorType;
import nativelevel.utils.Whitelist;
import org.bukkit.command.CommandSender;

import java.util.List;

public class WhitelistCMD extends Comando {

    public WhitelistCMD() {
        super("whitelist", ExecutorType.OPCONSOLE);
    }

    private static final String help = "/whitelist (on, off, list, put, remove) [nick]";

    @Override
    public void usouComando(CommandSender cs, String[] args) {
        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("put")) {
                cs.sendMessage(args[1] + " putted? " + Whitelist.put(args[1]));
            } else if (args[0].equalsIgnoreCase("remove")) {
                cs.sendMessage(args[1] + " removed? " + Whitelist.remove(args[1]));
            } else {
                cs.sendMessage(help);
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                StringBuilder lista = new StringBuilder("");
                int index = 0;
                List<String> whitelisteds = Whitelist.getWhitelisteds();
                for (String player : whitelisteds) {
                    if (index != (whitelisteds.size() - 1)) lista.append(player).append(", ");
                    else lista.append(player);
                    index++;
                }
                cs.sendMessage("[ " + whitelisteds.size() + " ] " + lista.toString());
            } else if (args[0].equalsIgnoreCase("on")) {
                if (!Whitelist.opsMode) {
                    cs.sendMessage("§6WhiteList agora está §aativada!");
                    Whitelist.setMode(true);
                } else {
                    cs.sendMessage("§6WhiteList em modo §4§lOP");
                }
            } else if (args[0].equalsIgnoreCase("off")) {
                if (!Whitelist.opsMode) {
                    cs.sendMessage("§6WhiteList agora está §cdesativada!");
                    Whitelist.setMode(false);
                } else {
                    cs.sendMessage("§6WhiteList em modo §4§lOP");
                }
            } else if (args[0].equalsIgnoreCase("opmode")) {
                if (!Whitelist.opsMode) {
                    Whitelist.setOpsMode(true);
                    cs.sendMessage("§6WhiteList em modo §4§lOP");
                } else {
                    Whitelist.setOpsMode(false);
                    Whitelist.cantLogin.clear();
                    cs.sendMessage("§6WhiteList agora está §aativada");
                }
            } else {
                cs.sendMessage(help);
            }
        } else {
            cs.sendMessage(help);
        }
    }

}
