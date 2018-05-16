package nativelevel.ComandosNovos.commands.terreno.terrenosubs;

import nativelevel.ComandosNovos.SubCmd;
import nativelevel.ComandosNovos.commands.terreno.terrenosubs.amigosubs.AmigoAdd;
import nativelevel.ComandosNovos.commands.terreno.terrenosubs.amigosubs.AmigoLimpar;
import nativelevel.ComandosNovos.commands.terreno.terrenosubs.amigosubs.AmigoRemover;
import nativelevel.Lang.L;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.ExecutorType;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TerrenoAmigo extends SubCmd {

    public TerrenoAmigo() {
        super("amigo", ExecutorType.TODOS);
        subs.add(new AmigoAdd());
        subs.add(new AmigoRemover());
        subs.add(new AmigoLimpar());
    }

    @Override
    public void execute(CommandSender cs, String[] args) {

        Player p = (Player) cs;
        ClanPlayer cp = ClanLand.manager.getClanPlayer(p);

        if (args.length < 2) {
            showSubCommands(cs, "terreno amigo");
            return;
        }

        if (ClanLand.getClanAt(p.getLocation()) == null || !ClanLand.getClanAt(p.getLocation()).equals(cp.getClan())) {
            ClanLand.msg(p, L.m("Esse terreno não é do seu clan."));
            return;
        }

        if (ClanLand.getOwnerAt(p.getLocation()).equals("none")) {
            ClanLand.msg(p, L.m("Esse terreno eh publico do clan."));
            return;
        }

        if (!ClanLand.getOwnerAt(p.getLocation()).equals(p.getUniqueId().toString()) && !cp.isLeader()) {
            ClanLand.msg(p, L.m("Voce nao eh dono desse terreno."));
            return;
        }

        if (args[1].contains("limpar")) {

            executeBase(cs, args, "terreno amigo");
            return;

        }

        if (args.length < 3) {
            ClanLand.msg(p, L.m("Complemente o comando com um jogador."));
            ClanLand.msg(p, L.m("Ex: /terreno amigo add ZidenVentania"));
            return;
        }

        if (args[2].equalsIgnoreCase(p.getName())) {
            ClanLand.msg(p, L.m("I know you're somewhere, somewhere..."));
            return;
        }

        if (args[1].contains("remover")) {

            executeBase(cs, args, "terreno amigo");
            return;

        }

        String player = args[2];
        Player p2 = Bukkit.getPlayer(player);

        if (p2 != null) {

            executeBase(cs, args, "terreno amigo");

        } else {

            ClanLand.msg(p, L.m("Preciso do jogador " + ChatColor.GREEN + "%§e online para executar esse comando.", player));

        }

    }

}
