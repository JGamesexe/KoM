package nativelevel.ComandosNovos.commands.komx.komxsubs;

import nativelevel.ComandosNovos.SubCmd;
import nativelevel.KoM;
import nativelevel.MetaShit;
import nativelevel.utils.ExecutorType;
import nativelevel.utils.TitleAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdAutoClaim extends SubCmd {

    public CmdAutoClaim() {
        super("autoclaim", ExecutorType.OP);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {

        Player player = (Player) cs;

        if (MetaShit.getMetaObject("autoClaim", player) != null) {
            Bukkit.getScheduler().cancelTask((Integer) MetaShit.getMetaObject("autoClaim", player));
            MetaShit.setMetaObject("autoClaim", player, null);
        } else {
            Runnable runnable = () -> TitleAPI.sendActionBar(player, "§5§lAutoCLAIM Ativado!!!");
            MetaShit.setMetaObject("autoClaim", player, Bukkit.getScheduler().scheduleSyncRepeatingTask(KoM._instance, runnable, 0, 50));
        }
    }

}
