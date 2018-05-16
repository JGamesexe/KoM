package nativelevel.ComandosNovos.commands.list.KomSubs;

import nativelevel.ComandosNovos.SubCmd;
import nativelevel.utils.ExecutorType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * @author vntgasl
 */
public class CmdTeste extends SubCmd {

    public CmdTeste() {
        super("teste", ExecutorType.PLAYER);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            Player p = (Player) cs;
            Entity pig = p.getWorld().spawnEntity(p.getLocation(), EntityType.fromName(args[1]));
            ((LivingEntity) pig).setLeashHolder(p);
            p.addPassenger(pig);
            p.sendMessage("Teste Feito");
        }
    }

}
