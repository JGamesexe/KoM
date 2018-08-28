package nativelevel.ComandosNovos.commands.terreno.terrenosubs;

import nativelevel.ComandosNovos.SubCmd;
import nativelevel.guis.terreno.TerrenoOutherGUI;
import nativelevel.guis.terreno.TerrenoPrivadoGUI;
import nativelevel.guis.terreno.TerrenoPublicoGUI;
import nativelevel.guis.terreno.TerrenoWildGUI;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.ExecutorType;
import nativelevel.utils.GUI;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TerrenoInfo extends SubCmd {

    public TerrenoInfo() {
        super("info", ExecutorType.PLAYER);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {

        Player p = (Player) cs;

        String tipoTerreno = ClanLand.getTypeAt(p.getLocation());

        if (tipoTerreno.equalsIgnoreCase("CLAN")) {
            ClanPlayer cp = ClanLand.manager.getClanPlayer(p);
            Clan clan = ClanLand.getClanAt(p.getLocation());
            if (cp.getTag().equalsIgnoreCase(clan.getTag())) {
                if (ClanLand.getOwnerAt(p.getLocation())[0].equalsIgnoreCase("none")) GUI.open(p, new TerrenoPublicoGUI(p));
                else GUI.open(p, new TerrenoPrivadoGUI(p));
            } else {
                GUI.open(p, new TerrenoOutherGUI(cp, clan, p.getLocation()));
            }
        } else if (tipoTerreno.equalsIgnoreCase("WILD")) {
            GUI.open(p, new TerrenoWildGUI(p));
        } else if (tipoTerreno.equalsIgnoreCase("RUIN")) {
            p.sendMessage("§eEste terreno está em ruína, em breve ira se tornar terras sem dono...");
        }

    }

}
