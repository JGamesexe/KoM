package nativelevel.ComandosNovos.commands.komx.komxsubs;

import nativelevel.ComandosNovos.SubCmd;
import nativelevel.skills.SkillMaster;
import nativelevel.utils.ExecutorType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdSkills extends SubCmd {

    public CmdSkills() {
        super("skills", ExecutorType.PLAYER);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        SkillMaster.abreSkills((Player) cs);
    }

}
