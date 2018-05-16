package nativelevel.scores;

import org.bukkit.entity.Player;

/**
 * @author gabripj
 */
public class ThreadScoreJoinEvent implements Runnable {

    Player p;

    public ThreadScoreJoinEvent(Player p) {
        this.p = p;
    }

    @Override
    public void run() {
        /*
        PermissionUser u = PermissionsEx.getPermissionManager().getUser(p);
        String prefixo = u.getPrefix();
        String sufixo = u.getSuffix();
        if ((sufixo != null && !sufixo.equals("")))
        {
            sufixo += " ";
        }
        prefixo = ChatUtils.translateColorCodes(prefixo);
        sufixo = ChatUtils.translateColorCodes(sufixo);
        final String prefixob = prefixo;
        final String sufixob = sufixo;
        ScoreboardManager.addToTeam(p.getName(), ChatColor.stripColor(prefixo), prefixob + "§f", sufixob, false);
                */


    }
}
