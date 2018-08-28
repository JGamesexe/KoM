package nativelevel.ComandosNovos.commands.komx.komxsubs;

import nativelevel.ComandosNovos.SubCmd;
import nativelevel.KoM;
import nativelevel.Lang.L;
import nativelevel.sisteminhas.XP;
import nativelevel.utils.ExecutorType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdSetLevel extends SubCmd {

    public CmdSetLevel() {
        super("setlevel", ExecutorType.OP);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {

        Player playerAlvo = Bukkit.getPlayer(args[1]);
        if (playerAlvo != null) {
            int lvl;
            try {
                lvl = Integer.valueOf(args[2]);
            } catch (Exception ignored) {
                cs.sendMessage("Insira um LeveL valido apos o nick do manolo");
                return;
            }
            XP.setaLevel(playerAlvo, lvl);
            KoM.database.resetPlayer(playerAlvo);
            KoM.database.changeMaxLevel(playerAlvo, playerAlvo.getLevel());
            playerAlvo.removeMetadata("expAtual", KoM._instance);
            playerAlvo.sendMessage(ChatColor.GREEN + L.m("Seu nivel foi alterado !"));
            cs.sendMessage(ChatColor.GREEN + L.m("Nivel alterado !"));
        } else {
            int lvl;
            try {
                lvl = Integer.valueOf(args[1]);
            } catch (Exception ignored) {
                cs.sendMessage("Jogador não encontrado");
                return;
            }
            playerAlvo = (Player) cs;
            playerAlvo.sendMessage(L.m("Seu level agora eh " + lvl));
            XP.setaLevel(playerAlvo, lvl);
            playerAlvo.removeMetadata("expAtual", KoM._instance);
        }

    }

}
