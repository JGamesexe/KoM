/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.Classes.Mage.spelllist;

import nativelevel.Classes.Mage.Elements;
import nativelevel.Classes.Mage.MageSpell;
import nativelevel.Listeners.DamageListener;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.sisteminhas.Tralhas;
import nativelevel.utils.GeneralUtils;
import nativelevel.utils.LocUtils;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashSet;

/**
 * @author User
 */
public class Lightning extends MageSpell {

    public Lightning() {
        super("Relampago");
    }

    @Override
    public void cast(Player p) {

        HashSet<Material> m = null;
        Block target = p.getTargetBlock(m, 100);

        if (GeneralUtils.getHighestBlockAt(target.getLocation()).getY() != (target.getY() + 1)) target = null;

        double range = 1.3 + p.getLevel() / 30;
        ClanPlayer playerCP = ClanLand.manager.getClanPlayer(p);

        if (target != null) {
            target.getWorld().strikeLightningEffect(target.getLocation());
            for (Entity entity : target.getWorld().getNearbyEntities(target.getLocation(), range, range, range)) {
                if (!(entity instanceof LivingEntity)) continue;
                ClanPlayer nearbyCP = null;
                if (playerCP != null) nearbyCP = ClanLand.manager.getAnyClanPlayer(entity.getUniqueId());
                if (nearbyCP == null || !nearbyCP.getTag().equalsIgnoreCase(playerCP.getTag())) {
                    double damage = (5 + (p.getLevel() / 25));
                    if (entity.getLocation().getBlock().getType().equals(Material.WATER) || entity.getLocation().getBlock().getType().equals(Material.STATIONARY_WATER)) damage *= 1.75;
                    DamageListener.darDano(p, damage, (LivingEntity) entity, EntityDamageEvent.DamageCause.LIGHTNING);
                    Tralhas.doRandomKnock((LivingEntity) entity, 0.6F);
                }
            }
        }

//      GeneralListener.wizard.soltaRaio(p);
    }

    @Override
    public double getManaCost() {
        return 70;
    }

    @Override
    public double getExpRatio() {
        return 1;
    }

    @Override
    public int getMinSkill() {
        return 65;
    }

    @Override
    public Elements[] getElements() {
        return new Elements[]{Elements.Raio, Elements.Raio, Elements.Raio};
    }

    @Override
    public int getCooldownInSeconds() {
        return 2;
    }

}
