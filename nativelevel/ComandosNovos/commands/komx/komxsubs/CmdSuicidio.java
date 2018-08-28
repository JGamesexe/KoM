package nativelevel.ComandosNovos.commands.komx.komxsubs;

import nativelevel.ComandosNovos.SubCmd;
import nativelevel.utils.ExecutorType;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdSuicidio extends SubCmd {

    public CmdSuicidio() {
        super("suicidio", ExecutorType.PLAYER);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            ((Player) cs).damage(((Player) cs).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            cs.sendMessage("§cVocê bate com a cabeça no chão");
        }
    }
}
