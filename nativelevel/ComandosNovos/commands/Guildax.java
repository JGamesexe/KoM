package nativelevel.ComandosNovos.commands;

import nativelevel.ComandosNovos.Comando;
import nativelevel.guis.guilda.GuildaGUI;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.ExecutorType;
import nativelevel.utils.GUI;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Guildax extends Comando {

    public Guildax() {
        super("guilda", ExecutorType.PLAYER);
    }

    @Override
    public void usouComando(CommandSender cs, String[] args) {
        Player player = (Player) cs;
        ClanPlayer clanPlayer = ClanLand.manager.getClanPlayer(player);

        if (args.length == 0 && clanPlayer == null) {
            player.sendMessage("§cPara executar esse comando é necessario uma guilda, caso queira zoiar outras guildas complemente o comando com uma TAG, mas se deseja fundar uma nova Guilda procure pelo Mestre Prince");
            return;
        }

        if (args.length == 0) {
            GUI.open(player, new GuildaGUI(player, clanPlayer.getClan()));
        } else {
            Clan clan = ClanLand.getClan(args[0]);
            if (clan != null) GUI.open(player, new GuildaGUI(player, clan));
            else player.sendMessage("§cNão existe nenhuma guilda com essa TAG");
        }

    }
}
