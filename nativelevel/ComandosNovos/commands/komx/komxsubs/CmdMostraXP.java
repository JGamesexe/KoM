package nativelevel.ComandosNovos.commands.komx.komxsubs;

import nativelevel.ComandosNovos.SubCmd;
import nativelevel.KoM;
import nativelevel.Lang.L;
import nativelevel.MetaShit;
import nativelevel.utils.ExecutorType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdMostraXP extends SubCmd {

    public CmdMostraXP() {
        super("mostraxp", ExecutorType.PLAYER);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            Player p = (Player) cs;
            if (!p.hasMetadata("mostraXP")) {
                MetaShit.setMetaObject("mostraXP", p, true);
                p.sendMessage("§aMostrando xp...");
            } else {
                p.removeMetadata("mostraXP", KoM._instance);
                p.sendMessage("§aNao mostrando xp...");
            }
        } else {
            cs.sendMessage("Comando só pode ser executado IN-GAME");
        }
    }
}
