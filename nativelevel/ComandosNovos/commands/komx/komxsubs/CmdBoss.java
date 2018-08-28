package nativelevel.ComandosNovos.commands.komx.komxsubs;

import nativelevel.ComandosNovos.SubCmd;
import nativelevel.KoM;
import nativelevel.Lang.LangMinecraft;
import nativelevel.Listeners.DamageListener;
import nativelevel.MetaShit;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.ExecutorType;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

public class CmdBoss extends SubCmd {

    public CmdBoss() {
        super("boss", ExecutorType.OP);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {

        Player player = (Player) cs;

        Entity entity = player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE_VILLAGER);

        int zona = ClanLand.getMobLevel(entity.getLocation());

        if (zona > 20) zona = 666;
        else zona = zona * 5;
        int finalZona = zona;

        entity.setCustomName("§7§l" + LangMinecraft.get().getMobName(entity.getType(), true) + " §8[§c" + finalZona + "§8]");
        entity.setCustomNameVisible(true);
        ((Monster) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1000 + (finalZona * 10));
        ((Monster) entity).setHealth(((Monster) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        MetaShit.setMetaObject("boss", entity, true);
        MetaShit.setMetaObject("mobLevel", entity, finalZona);
        DamageListener.bosses.add(new DamageListener.KoMBoss((LivingEntity) entity));
        KoM.announce("§cBOSS Spawnado na Zona " + finalZona + "!!!");

    }

}
