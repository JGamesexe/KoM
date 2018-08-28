package nativelevel.ComandosNovos.commands.komx.komxsubs;

import nativelevel.ComandosNovos.SubCmd;
import nativelevel.Lang.L;
import nativelevel.Menu.netMenu;
import nativelevel.utils.ExecutorType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdVerClasses extends SubCmd {

    public CmdVerClasses() {
        super("verclasses", ExecutorType.PLAYER);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        Player p = (Player) cs;
        p.sendMessage(ChatColor.GREEN + L.m("Vendo suas classes !"));
        netMenu.mostraClasses(p);
    }

}
