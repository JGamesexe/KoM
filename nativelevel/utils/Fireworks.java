package nativelevel.utils;

import nativelevel.KoM;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.EntityEffect;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class Fireworks {

    public static void purpleFirework(Firework fw) {
        FireworkMeta fwm = fw.getFireworkMeta();
        fwm.addEffect(FireworkEffect.builder()
                .flicker(false)
                .with(FireworkEffect.Type.BURST)
                .withColor(Color.PURPLE)
                .withFade(Color.AQUA)
                .build());
        fwm.setPower(0);
        fw.setFireworkMeta(fwm);

//        Bukkit.getScheduler().runTaskLater(KoM._instance, fw::detonate, 1);
        Bukkit.getScheduler().runTaskLater(KoM._instance, () -> {
            fw.playEffect(EntityEffect.FIREWORK_EXPLODE);
            fw.remove();
        }, 2L);
    }

}
