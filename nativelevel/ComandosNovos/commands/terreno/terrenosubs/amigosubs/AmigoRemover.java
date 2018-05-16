package nativelevel.ComandosNovos.commands.terreno.terrenosubs.amigosubs;

import nativelevel.ComandosNovos.SubCmd;
import nativelevel.Lang.L;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.ExecutorType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AmigoRemover extends SubCmd {

    public AmigoRemover() {
        super("remover", ExecutorType.PLAYER);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {

        Player p = (Player) cs;
        String player = args[2];
        Player p2 = Bukkit.getPlayer(player);
        UUID uuid;

        if (p2 != null) {
            uuid = p2.getUniqueId();
        } else if (ClanLand.nickMemberToUUID(p.getLocation(), args[2]) != null) {
            uuid = ClanLand.nickMemberToUUID(p.getLocation(), args[2]);
        } else {
            ClanLand.msg(p, L.m("Esse jogador não existe..."));
            return;
        }

        if (!ClanLand.isMemberAt(p.getLocation(), uuid)) {
            ClanLand.msg(p, L.m("Este jogador nao eh membro deste terreno."));
            return;
        }

        ClanLand.removeMemberAt(p.getLocation(), uuid);
        ClanLand.msg(p, L.m("O jogador§c % §eagora nao eh mais membro deste terreno.", player));

    }
}
