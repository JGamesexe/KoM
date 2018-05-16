package nativelevel.ComandosNovos.commands.terreno.terrenosubs;

import nativelevel.ComandosNovos.SubCmd;
import nativelevel.Lang.L;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.ExecutorType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TerrenoSafe extends SubCmd {

    public TerrenoSafe() {
        super("safe", ExecutorType.OP);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {

        Player p = (Player) cs;

        if (ClanLand.isSafeZone(p.getLocation())) {
            ClanLand.setClanAt(p.getLocation(), "WILD");
            ClanLand.msg(p, L.m("Nao eh mais safezone"));
        } else {
            ClanLand.setClanAt(p.getLocation(), "SAFE");
            if (args.length == 2) ClanLand.setCoisasSafe(p.getLocation(), args[1], "");
            else if (args.length > 2) ClanLand.setCoisasSafe(p.getLocation(), args[1], args[2]);
            else ClanLand.msg(p, L.m("Se pode complentar com nome da vila, e um subtitulo §o[Troque espaços por '_']"));
            ClanLand.msg(p, L.m("Agora eh safezone"));
        }

        ClanLand.update(p, p.getLocation());

    }
}
