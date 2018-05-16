package nativelevel.ComandosNovos.commands.terreno.terrenosubs;

import nativelevel.Comandos.Terreno;
import nativelevel.ComandosNovos.SubCmd;
import nativelevel.Lang.L;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.ExecutorType;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class TerrenoConquistar extends SubCmd {

    public TerrenoConquistar() {
        super("conquistar", ExecutorType.LEADER);
    }


    @Override
    public void execute(CommandSender cs, String[] args) {

        Player p = (Player) cs;
        ClanPlayer cp = ClanLand.manager.getClanPlayer(p);

        ArrayList<String> emvolta = Terreno.getGuildasPerto(p.getLocation());

        if (emvolta.get(0).equalsIgnoreCase(cp.getTag())) {
            ClanLand.msg(p, L.m("Este terreno já é de sua guilda."));
            return;
        }

        if (emvolta.get(0).equalsIgnoreCase("WILD")) {

            if (Terreno.temGuildaPerto(p, p.getLocation(), false)) {
                ClanLand.msg(p, L.m("Um terreno que não pertence a sua guilda está muito proximo para conquistar este terreno."));
                return;
            }

            if (emvolta.get(1).equalsIgnoreCase(cp.getTag()) || emvolta.get(2).equalsIgnoreCase(cp.getTag()) ||
                    emvolta.get(3).equalsIgnoreCase(cp.getTag()) || emvolta.get(4).equalsIgnoreCase(cp.getTag())) {

                ClanLand.setClanAt(p.getLocation(), "#" + cp.getClan().getTag());
                ClanLand.update(p.getPlayer(), p.getLocation());

            } else {
                ClanLand.msg(p, L.m("Você só pode conquistar terrenos ao lado de sua gld."));
                return;
            }
        }

        if (cp.getClan().getRivals().contains(emvolta.get(0))) {

        }

    }
}
