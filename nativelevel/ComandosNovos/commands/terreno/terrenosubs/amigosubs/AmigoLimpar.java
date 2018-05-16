package nativelevel.ComandosNovos.commands.terreno.terrenosubs.amigosubs;

import nativelevel.ComandosNovos.SubCmd;
import nativelevel.Lang.L;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.ExecutorType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AmigoLimpar extends SubCmd {

    public AmigoLimpar() {
        super("limpar", ExecutorType.PLAYER);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {

        Player p = (Player) cs;

        ClanLand.clearMembersAt(p.getLocation());
        ClanLand.msg(p, L.m("Seu terreno nao tem mais membros."));

    }
}
