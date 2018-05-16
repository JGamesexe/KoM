package nativelevel.ComandosNovos.commands.terreno.terrenosubs;

import nativelevel.ComandosNovos.SubCmd;
import nativelevel.Lang.L;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.ExecutorType;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TerrenoDono extends SubCmd {

    public TerrenoDono() {
        super("dono", ExecutorType.LEADER);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {

        Player p = (Player) cs;

        if (args.length < 2) {
            ClanLand.msg(p, L.m("Complemente o comando com um jogador."));
            ClanLand.msg(p, L.m("Ex: /terreno dono ZidenVentania"));
            return;
        }

        Clan c = ClanLand.getClanAt(p.getLocation());
        ClanPlayer cp = ClanLand.manager.getClanPlayer(p);

        if (c == null || !c.getTag().equals(cp.getClan().getTag())) {
            ClanLand.msg(p, L.m("Este terreno nem eh do seu clan."));
            return;
        }
        Player alvo = Bukkit.getPlayer((args[1]));
        if (c.getAllMembers() != null && !c.getAllMembers().contains(ClanLand.manager.getClanPlayer(alvo))) {
            ClanLand.msg(p, L.m("Este jogador nao esta no seu clan."));
            return;
        }
        String[] owner = ClanLand.getOwnerAt(p.getLocation());
        if (owner.length != 0 && owner[0].equals(alvo.getUniqueId().toString())) {
            ClanLand.msg(p, L.m("Esse terreno ja eh do jogador §a%", args[1]));
            return;
        }

        ClanLand.setOwnerAt(p.getLocation(), p);
        ClanLand.msg(p, L.m("Agora esse terreno é privado, o dono é o, §a%", args[1]));
        ClanLand.update(p, p.getLocation());

    }

}
