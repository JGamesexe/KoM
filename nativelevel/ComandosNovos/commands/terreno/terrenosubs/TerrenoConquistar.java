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

        Player player = (Player) cs;
        ClanPlayer clanPlayer = ClanLand.manager.getClanPlayer(player);

        ArrayList<String> emvolta = Terreno.getGuildasPerto(player.getLocation());

        if (emvolta.get(0).equalsIgnoreCase(clanPlayer.getTag())) {
            ClanLand.msg(player, L.m("Este terreno já é de sua guilda."));
            return;
        }

        if (emvolta.get(0).equalsIgnoreCase("WILD")) {

            if (Terreno.temGuildaPerto(player, player.getLocation(), false)) {
                ClanLand.msg(player, L.m("Um terreno que não pertence a sua guilda está muito proximo para conquistar este terreno."));
                return;
            }

            if (ClanLand.getQtdTerrenos(clanPlayer.getTag()) == 0 ||
                    emvolta.get(1).equalsIgnoreCase(clanPlayer.getTag()) || emvolta.get(2).equalsIgnoreCase(clanPlayer.getTag()) ||
                    emvolta.get(3).equalsIgnoreCase(clanPlayer.getTag()) || emvolta.get(4).equalsIgnoreCase(clanPlayer.getTag())) {


                int poder = ClanLand.getPoder(clanPlayer.getTag());

                if (poder > 0) {
                    int preco = ClanLand.priceOfTerreno(clanPlayer.getTag());
                    if (ClanLand.econ.has(player, preco)) {

                        if (preco == 0) {
                            ClanLand.setClanAt(player.getLocation(), clanPlayer.getTag());
                            ClanLand.msg(player, L.m("Terreno conquistado, como este é o primeiro terreno de sua guilda, ele foi colocado como Terreno Primário, os proximos terrenos vão precisar de uma melhoria para torna-los Terrenos Primários"));
                        } else {
                            ClanLand.setClanAt(player.getLocation(), "#" + clanPlayer.getTag());
                            ClanLand.msg(player, L.m("Terreno conquistado, este terreno é um Terreno de Poder e pode ser dominado a qualquer momento, para melhorar este terreno torne-o um Terreno Primário"));
                            ClanLand.econ.withdrawPlayer(player, preco);
                        }

                        ClanLand.msg(player, L.m("Para modificicar este terreno utilize, /terreno"));
                        ClanLand.setPoder(clanPlayer.getTag(), poder - 1);
                        if (player.getLocation().getChunk().equals(player.getLocation().getChunk()))
                            ClanLand.update(player, player.getLocation());

                    } else {
                        ClanLand.msg(player, L.m("Você não possui " + preco + " esmeraldas"));
                    }
                } else {
                    ClanLand.msg(player, L.m("Sua guilda não tem poder para conquistar este terreno"));
                }

            } else {
                ClanLand.msg(player, L.m("Você só pode conquistar terrenos ao lado de sua gld."));
                return;
            }
        }

        if (clanPlayer.getClan().getRivals().contains(emvolta.get(0))) {

        }

    }
}
