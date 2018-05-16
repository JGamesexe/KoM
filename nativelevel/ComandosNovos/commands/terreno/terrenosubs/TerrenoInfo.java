package nativelevel.ComandosNovos.commands.terreno.terrenosubs;

import nativelevel.ComandosNovos.SubCmd;
import nativelevel.guis.terreno.TerrenoPrivadoGUI;
import nativelevel.guis.terreno.TerrenoPublicoGUI;
import nativelevel.guis.terreno.TerrenoWildGUI;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.ExecutorType;
import nativelevel.utils.GUI;
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
            if (ClanLand.getOwnerAt(p.getLocation())[0].equalsIgnoreCase("none")) GUI.open(p, new TerrenoPublicoGUI(p));
            else GUI.open(p, new TerrenoPrivadoGUI(p));
        } else if (tipoTerreno.equalsIgnoreCase("WILD")) {
            GUI.open(p, new TerrenoWildGUI(p));
        }

//        ClanPlayer cp = ClanLand.manager.getClanPlayer(p);
//        Clan c = ClanLand.getClanAt(p.getLocation());
//
//        if (c == null) {
//
//            ClanLand.msg(p, L.m("Esse terreno nao tem dono."));
//            return;
//
//        }
//
//        if (cp != null && c.getTag().equals(cp.getClan().getTag())) {
//
//            String owner = ClanLand.getOwnerAt(p.getLocation());
//
//            if (owner.equals("none")) {
//                ClanLand.msg(p, L.m("Esse terreno eh publico, do seu clan."));
//            } else {
//                List<String> membros = ClanLand.getMembersAt(p.getLocation());
//                ClanLand.msg(p, "Esse terreno eh do jogador " + owner + ChatColor.GREEN + ", do seu clan.");
//                if (membros.size() == 1 && membros.get(0).isEmpty()) {
//                    ClanLand.msg(p, L.m("Este terreno nao tem membros."));
//                } else {
//                    ClanLand.msg(p, L.m("Membros do terreno:"));
//                    for (String s : membros) {
//                        UUID id = UUID.fromString(s);
//                        OfflinePlayer mem = Bukkit.getPlayer(id);
//                        if (mem == null) {
//                            mem = Bukkit.getOfflinePlayer(id);
//                        }
//                        if (mem != null) {
//                            ClanLand.msg(p, "- " + mem.getName());
//                        }
//                    }
//                }
//            }
//        }
    }

}
