package nativelevel.utils;

import nativelevel.KoM;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class Fireworks {

    public static void doFirework(Location location, FireworkEffect.Type type, Color color, Color fade, long ticksExplode) {
        Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        fwm.addEffect(FireworkEffect.builder()
                .flicker(false)
                .with(type)
                .withColor(color)
                .withFade(fade)
                .build());
        fwm.setPower(0);
        fw.setFireworkMeta(fwm);

//        Bukkit.getScheduler().runTaskLater(KoM._instance, fw::detonate, 1);
        Bukkit.getScheduler().runTaskLater(KoM._instance, () -> {
            fw.playEffect(EntityEffect.FIREWORK_EXPLODE);
            fw.remove();
        }, ticksExplode);
    }

}
