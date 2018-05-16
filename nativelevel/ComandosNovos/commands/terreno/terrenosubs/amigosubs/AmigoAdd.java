package nativelevel.ComandosNovos.commands.terreno.terrenosubs.amigosubs;

import nativelevel.ComandosNovos.SubCmd;
import nativelevel.Lang.L;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.ExecutorType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AmigoAdd extends SubCmd {

    public AmigoAdd() {
        super("add", ExecutorType.PLAYER);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {

        Player p = (Player) cs;
        String player = args[2];
        Player p2 = Bukkit.getPlayer(player);
        UUID uuid = p2.getUniqueId();

        if (ClanLand.isMemberAt(p.getLocation(), uuid)) {
            ClanLand.msg(p, L.m("Este jogador ja eh membro deste terreno."));
            return;
        }

        ClanLand.addMemberAt(p.getLocation(), p2);
        ClanLand.msg(p, L.m("O jogador " + ChatColor.GREEN + "%§e agora eh membro deste terreno.", player));

    }

}
