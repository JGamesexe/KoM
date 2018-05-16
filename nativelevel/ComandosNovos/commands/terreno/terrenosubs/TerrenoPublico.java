package nativelevel.ComandosNovos.commands.terreno.terrenosubs;

import nativelevel.ComandosNovos.SubCmd;
import nativelevel.Lang.L;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.ExecutorType;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TerrenoPublico extends SubCmd {

    public TerrenoPublico() {
        super("publico", ExecutorType.LEADER);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {

        Player p = (Player) cs;
        Clan c = ClanLand.getClanAt(p.getLocation());
        ClanPlayer cp = ClanLand.manager.getClanPlayer(p);

        if (c == null || !c.getTag().equals(cp.getClan().getTag())) {
            ClanLand.msg(p, L.m("Este terreno nem eh do seu clan."));
            return;
        }

        if (ClanLand.getOwnerAt(p.getLocation()).equals("none")) {
            ClanLand.changePermLevel(p.getLocation());
            ClanLand.msg(p, "Terreno publico alterado pro modo " + ClanLand.getPermLevel(p.getLocation()));
            return;
        }

        ClanLand.msg(p, L.m("Este terreno agora eh publico."));
        ClanLand.setOwnerAt(p.getLocation(), null);
        ClanLand.update(p, p.getLocation());

    }
}
