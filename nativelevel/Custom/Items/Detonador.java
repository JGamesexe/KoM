/*

 ╭╮╭━╮╱╱╭━╮╭━╮
 ┃┃┃╭╯╱╱┃┃╰╯┃┃
 ┃╰╯╯╭━━┫╭╮╭╮┃
 ┃╭╮┃┃╭╮┃┃┃┃┃┃
 ┃┃┃╰┫╰╯┃┃┃┃┃┃
 ╰╯╰━┻━━┻╯╰╯╰╯

 Desenvolvedor: ZidenVentania
 Colaboradores: NeT32, Gabripj, Feldmann
 Patrocionio: InstaMC

 */
package nativelevel.Custom.Items;

import me.fromgate.playeffect.PlayEffect;
import me.fromgate.playeffect.VisualEffect;
import nativelevel.Attributes.Mana;
import nativelevel.Custom.CustomItem;
import nativelevel.Jobs;
import nativelevel.Lang.L;
import nativelevel.Listeners.DamageListener;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class Detonador extends CustomItem {

    public Detonador() {
        super(Material.WATCH, L.m("Detonador"), L.m("Detona suas armadilhas e bombas"), CustomItem.INCOMUM);
    }

    public static void explodeTrap(Location l, LivingEntity p, Player causador) {
        if (p != null && p instanceof Player) p.sendMessage(ChatColor.RED + L.m("Voce disparou uma armadilha !"));
        l.setY(l.getY() + 1);

        l.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, l, 3);

        for (Entity e : l.getWorld().getNearbyEntities(l, 3, 3, 3)) {
            if (e instanceof LivingEntity) {
                DamageListener.darDano(causador, 12D, (LivingEntity) e, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION);
                if (e instanceof Player) e.sendMessage(ChatColor.RED + "Uma armadilha explodiu !");
            }
        }
    }

    @Override
    public boolean onItemInteract(final Player p) {
        int lvl = Jobs.getJobLevel("Engenheiro", p);
        if (lvl != 1) {
            p.sendMessage(ChatColor.RED + L.m("Apenas engenheiros experientes sabem usar isto."));
            return true;
        }
        if (p.hasPotionEffect(PotionEffectType.SLOW)) {
            return true;
        }
        if (!Mana.spendMana(p, 5)) {
            return true;
        }
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 2, 0));
        Block explodiu = null;
        for (Block arma : Armadilha.armadilhas.keySet()) {
            UUID u = Armadilha.armadilhas.get(arma);
            if (u == p.getUniqueId()) {

                explodiu = arma;
                break;
            }
        }
        if (explodiu != null) {
            explodeTrap(explodiu.getLocation(), null, p);
            Armadilha.armadilhas.remove(explodiu);
            p.sendMessage(ChatColor.RED + L.m("Voce explodiu uma armadilha !"));
        }

        return true;
    }
}
