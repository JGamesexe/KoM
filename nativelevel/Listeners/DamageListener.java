package nativelevel.Listeners;

import nativelevel.Attributes.AtributeListener;
import nativelevel.*;
import nativelevel.Classes.Alchemy.Alchemist;
import nativelevel.Classes.Blacksmithy.Blacksmith;
import nativelevel.Classes.*;
import nativelevel.Classes.Mage.Wizard;
import nativelevel.Custom.Items.Pistola;
import nativelevel.Custom.Mobs.IncursionIronTotem;
import nativelevel.Equipment.WeaponDamage;
import nativelevel.karma.Criminoso;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.sisteminhas.XP;
import nativelevel.utils.LocUtils;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityStatus;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

/**
 * @author Ziden
 */
public class DamageListener implements Listener {

    public static ArrayList<KoMBoss> bosses = new ArrayList<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public static void reciveDamage(EntityDamageEvent ev) {
        if (ev.getEntity().hasMetadata("NPC")) return;

        if (ev.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            ev.setCancelled(true);
            return;
        }

        KoM.debug("!=- reciveDamage " + ev.getEntity().getType() + " " + ev.getCause() + " " + LocUtils.loc2str(ev.getEntity().getLocation()));

        if (ev instanceof EntityDamageByEntityEvent) doDamage((EntityDamageByEntityEvent) ev);
        if (ev.isCancelled()) {
            KoM.debug("!=- Cancelado dps doDamage");
            return;
        }

        if (ev.getEntity() instanceof Player) {
            playerLevaHit(ev);
            if (ev.isCancelled()) KoM.debug("!=- Cancelado dps playerLevaHit");
        } else if (ev.getEntity() instanceof Creature) {
            mobLevaHit(ev);
            if (ev.isCancelled()) KoM.debug("!=- Cancelado dps de mobLevaHit");
        }

        if (ev.isCancelled()) return;

        if (ev instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) ev).getDamager() instanceof Player)
            Minerador.desarma((Player) ((EntityDamageByEntityEvent) ev).getDamager(), (LivingEntity) ev.getEntity());

        if (ev instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) ev).getDamager() instanceof Player)
            KoM.dano.mostraDano((Player) ((EntityDamageByEntityEvent) ev).getDamager(), ev.getDamage(), Dano.BATI);
        if (ev.getEntity() instanceof Player)
            KoM.dano.mostraDano((Player) ev.getEntity(), ev.getDamage(), Dano.TOMEI);

    }

    private static void doDamage(EntityDamageByEntityEvent ev) {
        if (ev.getEntity() instanceof Player) {

            if (ClanLand.isSafeZone(ev.getEntity().getLocation())) {
                if (!Criminoso.isCriminoso((Player) ev.getEntity())) {
                    ev.setCancelled(true);
                    ((Player) ev.getEntity()).playSound(ev.getDamager().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.05F, 2);
                    KoM.debug("!=- Cancelei em SAFEZONE");
                    LivingEntity hitter = getShooter(ev.getDamager());
                    if (hitter == null) return;
                    if (ev.getDamager() instanceof Player) ((Player) hitter).playSound(ev.getDamager().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.05F, 2);
                    else hitter.remove();
                    return;
                }
            }

            Clan clanAt = ClanLand.getClanAt(ev.getEntity().getLocation());
            if (clanAt != null) {
                ClanPlayer cp = ClanLand.manager.getClanPlayer((Player) ev.getEntity());
                if (cp != null && clanAt.getTag().equalsIgnoreCase(cp.getTag())) {
                    LivingEntity hitter = getShooter(ev.getDamager());
                    if (hitter != null && !(hitter instanceof Player)) {
                        hitter.remove();
                        ev.getEntity().sendMessage("§6Jabu retirou o monstro de sua guilda!");
                        return;
                    }
                }
            }

        }

        if (ev.getEntity() instanceof LivingEntity)
            if (ev.getCause() == EntityDamageEvent.DamageCause.POISON || ev.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK)
                ((LivingEntity) ev.getEntity()).setNoDamageTicks(0);

        if (ev.getDamager().getType().equals(EntityType.FIREWORK)) {
            ev.setCancelled(true);
            KoM.debug("!=- Cancelei firework " + ev.getCause().name());
        } else if (ev.getDamager() instanceof Projectile) {
            projectileHit(ev);
            if (ev.isCancelled()) KoM.debug("!=- Cancelado dps projectileHit");
        } else if (ev.getDamager() instanceof Player) {
            playerDoDamage(ev);
            if (ev.isCancelled()) KoM.debug("!=- Cancelado dps playerDoDamage");
        } else if (ev.getDamager() instanceof Creature) {
            mobDoDamage(ev);
            if (ev.isCancelled()) KoM.debug("!=- Cancelado dps mobDoDamage");
        }

        if (!ev.isCancelled()) registraDanos(ev.getEntity(), ev.getDamager(), ev.getDamage());

    }

    private static void playerDoDamage(EntityDamageByEntityEvent ev) {
        if (ev.isCancelled()) KoM.debug("Sei lá como, mas tem algo cancelando antes de playerDoDamage...");
        if (ev.isCancelled()) return;

        Player attacker = (Player) ev.getDamager();

        if (InteractEvents.decorationsEntitys.contains(ev.getEntity().getType()) && !InteractEvents.touchIn(attacker, ev.getEntity().getLocation())) {
            ev.setCancelled(true);
            return;
        }

        if (ev.getEntity() instanceof Player && ev.getEntity().getWorld().getName().equalsIgnoreCase(CFG.mundoDungeon)) {
            ev.setCancelled(true);
            KoM.debug("!=- Cancelei em mundo de DG");
            return;
        }

        if (Minerador.bracoAdormecido(attacker)) {
            attacker.sendMessage("§cSeu braço não responde seus comandos");
            ev.setCancelled(true);
            KoM.debug("!=- Cancelei braço cansado");
            return;
        }

        if (!(ev.getEntity() instanceof Player)) MetaShit.setMetaObject("hitOnMob", attacker, System.currentTimeMillis() + 100);

        KoM.debug("!=- playerDoDamage chegou com " + ev.getDamage());

        ItemStack hand = attacker.getInventory().getItemInMainHand();

        WeaponDamage.getItemDamage(hand);

        if (hand != null && hand.getType() != Material.AIR && ev.getEntity() instanceof LivingEntity) {

            if (Jobs.getPrimarias(attacker).contains(Paladin.name)) Paladin.onHit(ev);
            else if (Jobs.getSecundarias(attacker).contains(Paladin.name)) Paladin.onHitSec(ev);
            else Paladin.noHit(ev);

            if (Jobs.getJobLevel(Jobs.Classe.Ladino, attacker) == Jobs.TipoClasse.PRIMARIA) Thief.onHit(ev);
            else if (Jobs.getJobLevel(Jobs.Classe.Ladino, attacker) == Jobs.TipoClasse.NADA) Thief.noHit(ev);

            if (Jobs.getJobLevel(Jobs.Classe.Mago, attacker) == Jobs.TipoClasse.PRIMARIA) Wizard.onHit(ev);
            else Wizard.noHit(ev);

            if (Jobs.getJobLevel(Jobs.Classe.Lenhador, attacker) == Jobs.TipoClasse.PRIMARIA) Lumberjack.onHit(ev);

            if (Jobs.getJobLevel(Minerador.classe, attacker) == Jobs.TipoClasse.PRIMARIA) Minerador.onHit(ev);

            if (Jobs.getJobLevel(Jobs.Classe.Fazendeiro, attacker) == Jobs.TipoClasse.PRIMARIA) Farmer.onHit(ev);

            if (Jobs.getPrimarias(attacker).contains("Alquimista")) Alchemist.onHit(ev);

            if (Jobs.getPrimarias(attacker).contains("Ferreiro")) Blacksmith.onHit(ev);

        }

        AtributeListener.entityDamage(ev);

        //Leveling de ZONAs
        int zona = ClanLand.getMobLevel(attacker.getLocation());
        if (zona <= 20) zona = (zona * 5);
        else zona = 225;

        if (attacker.getLevel() <= zona)
            ev.setDamage(ev.getDamage() * (1 + ((attacker.getLevel() - zona) / 200)));
        else
            ev.setDamage(ev.getDamage() * (1 - ((attacker.getLevel() - zona) / 200)));

        if (Thief.taInvisivel(attacker)) Thief.revela(attacker);

        if (ev.isCancelled()) KoM.debug("!=- playerDoDamage foi cancelado");
        else KoM.debug("!=- playerDoDamage saiu com " + ev.getDamage());
    }

    private static void playerLevaHit(EntityDamageEvent ev) {
        Player receptor = (Player) ev.getEntity();

        if (ev.getCause().equals(EntityDamageEvent.DamageCause.FALL) || ev.getCause().equals(EntityDamageEvent.DamageCause.DROWNING))
            if (ClanLand.isSafeZone(receptor.getLocation())) {
                ev.setCancelled(true);
                KoM.debug("!=- Cancelei em SAFEZONE");
                return;
            }

        if (quebraPernoca(ev)) return;

        KoM.debug("!=- playerLevaHit chegou com " + ev.getDamage() + " " + ev.getCause());

        if (receptor.isBlocking()) Paladin.onBlocking(ev);
        if (ev.isCancelled()) KoM.debug("!=- Cancelado em onBlocking");
        if (ev.isCancelled()) return;

        if (Jobs.getPrimarias(receptor).contains(Paladin.name)) Paladin.onDamaged(ev);
        else if (Jobs.getSecundarias(receptor).contains(Paladin.name)) Paladin.onDamagedSec(ev);

        if (Jobs.getPrimarias(receptor).contains(Thief.name)) Thief.onDamaged(ev);
        else if (Jobs.getSecundarias(receptor).contains(Thief.name)) Thief.onDamagedSec(ev);

        if (Jobs.getPrimarias(receptor).contains(Farmer.name)) Farmer.onDamaged(ev);

        if (Jobs.getPrimarias(receptor).contains(Alchemist.name)) Alchemist.onDamaged(ev);

        if (Jobs.getPrimarias(receptor).contains(Blacksmith.name)) Blacksmith.onDamaged(ev);

        if (Jobs.getPrimarias(receptor).contains(Engineer.name)) Engineer.onDamaged(ev);

        if (!ev.isCancelled()) AtributeListener.takeDamage(ev);

        //Leveling de ZONAs
        if (!ev.isCancelled()) {
            int zona = ClanLand.getMobLevel(receptor.getLocation());
            if (zona <= 20) zona = (zona * 5);
            else zona = 250;

            ev.setDamage(ev.getDamage() * (1 - ((receptor.getLevel() - zona) / 400)));
        }

        if (!ev.isCancelled() && Thief.taInvisivel(receptor)) Thief.revela(receptor);

        if (ev.isCancelled()) KoM.debug("!=- playerLevaHit foi cancelado");
        else KoM.debug("!=- playerLevaHit saiu com " + ev.getDamage());

    }

    private static boolean quebraPernoca(EntityDamageEvent ev) {
        if (!ev.getCause().equals(EntityDamageEvent.DamageCause.FALL)) return false;

        float distance = ev.getEntity().getFallDistance();
        if (distance <= 5 && Jobs.dado(3) == 1) {
            int amplifier;

            if (distance <= 10) amplifier = 0;
            else if (distance <= 20) amplifier = 1;
            else amplifier = 2;

            ev.getEntity().sendMessage("§cVocê machucou sua perna !");
            ((Player) ev.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) (distance * 10), amplifier), true);
        }
        return true;
    }

    private static void projectileHit(EntityDamageByEntityEvent ev) {

        if (!(ev.getDamager() instanceof Projectile)) return;

        Projectile projectile = (Projectile) ev.getDamager();

        if (projectile.getShooter() instanceof Player && ev.getEntity() instanceof Player && ev.getEntity().getWorld().getName().equalsIgnoreCase(CFG.mundoDungeon)) {
            ev.setCancelled(true);
            KoM.debug("!=- Cancelei Projetil em mundo de DG");
            return;
        }

        KoM.debug("!=- projectileHit " + projectile.getType() + " com " + ev.getDamage() + " em " + LocUtils.loc2str(projectile.getLocation()));

        if (projectile.hasMetadata("modDano")) ev.setDamage((Double) MetaShit.getMetaObject("modDano", projectile));

        if (projectile.getType().equals(EntityType.ARROW)) {

            if (projectile.hasMetadata("exitLocation")) ev.setDamage(((Location) MetaShit.getMetaObject("exitLocation", projectile)).distance(ev.getEntity().getLocation()) / 3);

            if (getPlayerDamager(projectile) != null) Thief.onFlechada(ev);
        } else if (projectile.getType().equals(EntityType.SNOWBALL)) {
            if (projectile.hasMetadata("bonka")) Pistola.onHit(ev);
        } else if (projectile.getType().equals(EntityType.SMALL_FIREBALL)) {
            //ToDo Magias são necassarias??
        }

        AtributeListener.dealDamage(ev);

        KoM.debug("!=- projectileHit " + projectile.getType() + " saiu com " + ev.getDamage() + " em " + LocUtils.loc2str(projectile.getLocation()));

    }

    private static void mobDoDamage(EntityDamageByEntityEvent ev) {

        if (ev.getDamager() instanceof IronGolem) IncursionIronTotem.doDamage(ev);
        if (ev.isCancelled()) KoM.debug("!=- Cancelado em IncIT.doDamaged");

    }

    private static void mobLevaHit(EntityDamageEvent ev) {
        Creature mob = (Creature) ev.getEntity();

        if (ev instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) ev).getDamager() instanceof LivingEntity) {
            LivingEntity leDamager = (LivingEntity) ((EntityDamageByEntityEvent) ev).getDamager();
            if (((EntityDamageByEntityEvent) ev).getDamager() instanceof Wolf) mob.setTarget(leDamager);
        }

        if (mob instanceof IronGolem) IncursionIronTotem.onDamaged(ev);
        if (ev.isCancelled()) KoM.debug("Cancelado em IncIT.onDamaged");

    }

    private static void registraDanos(Entity entity, Entity damager, double damage) {
        Player player = getPlayerDamager(damager);
        if (player != null)
            if (entity instanceof Monster)
                if (entity.hasMetadata("boss")) {
                    if (damage > ((Monster) entity).getHealth()) getBoss(entity.getUniqueId()).addDamage(player.getUniqueId(), ((Monster) entity).getHealth());
                    else getBoss(entity.getUniqueId()).addDamage(player.getUniqueId(), damage);
                }
    }

    public static Player getPlayerDamager(Entity damager) {
        if (damager == null) return null;

        if (damager instanceof Player) {
            return (Player) damager;
        } else if (damager instanceof Projectile) {
            if (((Projectile) damager).getShooter() instanceof Player)
                return (Player) ((Projectile) damager).getShooter();
        } else if (damager instanceof TNTPrimed) {
            if (((TNTPrimed) damager).getSource() != null && ((TNTPrimed) damager).getSource() instanceof Player)
                return (Player) ((TNTPrimed) damager).getSource();
        } else if (damager instanceof Wolf) {
// TODO     if (Lobo.fazendeirosList.containsKey(damager.getTamer())) return player...
        }

        return null;
    }

    private static LivingEntity getShooter(Entity damager) {
        if (damager == null) return null;

        if (damager instanceof Projectile) {
            if (((Projectile) damager).getShooter() instanceof LivingEntity)
                return (LivingEntity) ((Projectile) damager).getShooter();
        } else if (damager instanceof LivingEntity) {
            return (LivingEntity) damager;
        }

        return null;
    }

    @EventHandler
    public static void vehicleDamage(VehicleDamageEvent ev) {
        if (InteractEvents.minecartsEntitys.contains(ev.getVehicle().getType())) {
            if (ev.getAttacker() instanceof Player) {
                if (!ev.getAttacker().isOp()) {
                    ev.setCancelled(true);
                    if (ev.getVehicle() instanceof InventoryHolder) ((InventoryHolder) ev.getVehicle()).getInventory().clear();
                    if (InteractEvents.touchIn((Player) ev.getAttacker(), ev.getVehicle().getLocation())) ev.getVehicle().remove();
                }
            } else {
                ev.setCancelled(true);
            }
        }
    }

    @EventHandler
    public static void vehicleDestroy(VehicleDestroyEvent ev) {
        if (InteractEvents.minecartsEntitys.contains(ev.getVehicle().getType())) {
            if (ev.getVehicle() instanceof InventoryHolder) ((InventoryHolder) ev.getVehicle()).getInventory().clear();
        }
    }

////    @EventHandler(priority = EventPriority.HIGHEST)
//    public void tomaDano2(EntityDamageEvent ev) {
//
//        KoM.debug("Recebendo do tomaDano2 " + ev.getDamage() + " em " + ev.getEntity().getName());
//
//        if(ev.getEntity().getType().equals(EntityType.ARMOR_STAND)) {
//            KoM.debug("Cancelando tomaDano2 em ArmorStand");
//            ev.setCancelled(true);PlayerInteractEventP
//            return;
//        }
//
//        if (ev.getCause() == DamageCause.SUFFOCATION) {
//            if (ev.getEntity() instanceof Monster) {
//                ev.getEntity().remove();
//            } else if (ev.getEntity().getType() == EntityType.PLAYER) {
//
//                if (KoM.database.hasRegisteredClass(ev.getEntity().getUniqueId().toString())) {
//
//                    /*
//                     if (WorldGuardKom.ehSafeZone(ev.getEntity().getLocation())) {
//                     BungeeCordKom.tp((Player) ev.getEntity(), CFG.spawnTree);
//                     } else if (ev.getEntity().getWorld().getName().equalsIgnoreCase("NewDungeon")) {
//
//                     }
//                     */
//                } else {
//                    KoM.log.info("Forcei " + ev.getEntity().getName() + " ir para localTutorial");
//                    BungeeCordKom.tp((Player) ev.getEntity(), CFG.localTutorial);
//                }
//
//            }
//        }
//
//        if (ev.getEntity().getType() == EntityType.BAT) {
//            if (ev.getEntity().getVehicle() != null) {
//                ev.setCancelled(true);
//                return;
//            }
//        }
//
//        if (ev.getEntity().getType() == EntityType.HORSE) {
//            Horse h = (Horse) ev.getEntity();
//            if (h.getPassenger() != null && h.getPassenger().getType() == EntityType.PLAYER) {
//                h.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 3, 4));
//            }
//            ev.setCancelled(true);
//            return;
//        }
//
//        if (ev.getEntity().getType() == EntityType.PLAYER) {
//
//            MetaShit.setMetaObject("tempoDano", ev.getEntity(), System.currentTimeMillis() / 1000);
//            Player p = (Player) ev.getEntity();
//            if (p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
//                if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() == Material.CARPET) {
//                    ev.setDamage(ev.getDamage() * 0.75);
//                }
//            }
//
//            Atadura.tomaDano(p, ev.getDamage());
//
//        }
//        if (ev.getEntity().getVehicle() != null && ev.getEntity().getVehicle().getType() == EntityType.PLAYER && ev.getEntity().getType() == EntityType.BAT) {
//            ev.setCancelled(true);
//            return;
//        }
//        if (ev.getEntity().hasMetadata("mount")) {
//            ev.setCancelled(true);
//            return;
//        }
//        if (!ev.isCancelled() && ev.getDamage() < 1) {
//            ev.setDamage(1D);
//        }
//
//        KoM.debug("final do tomaDano2 " + ev.getDamage() + " em " + ev.getEntity().getName());
//
//    }
//
//    ==================================================================================================================================================================================================
//
//    //    @EventHandler(priority = EventPriority.HIGH)
//    public void tomaDano(EntityDamageEvent ev) {
//
//        KoM.debug("recebendo do tomaDano " + ev.getDamage() + " (" + ev.getCause() + ") no [" + ev.getEntity().getType() + "] " + ev.getEntity().getName());
//
//        // evitar multiclick/multihit
//        if (ev.getEntity() instanceof LivingEntity) {
//            if (((LivingEntity) ev.getEntity()).getNoDamageTicks() > 5) {
//                ev.setCancelled(true);
//                return;
//            }
//        }
//
//        if (ev.getEntity().hasMetadata("NPC")) return;
//
//        if (ev.getEntity().getType().equals(EntityType.ARMOR_STAND)) {
//            ev.setCancelled(true);
//            KoM.debug("Cancelando tomaDano em ArmorSTAND");
//        }
//
//        if (ev.isCancelled()) return;
//
//        if (!SimpleClanKom.canDamage(ev)) {
//            ev.setCancelled(true);
//            return;
//        }
//
//        if (ev.getCause() == DamageCause.SUFFOCATION) {
//            return;
//        }
//
//        if (ev.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
//            ev.setDamage(ev.getDamage() * 2);
//            return;
//        } else if (ev.getCause() == EntityDamageEvent.DamageCause.FALL) {
//            if (ev.getEntity().getType() == EntityType.PIG) {
//                ev.setCancelled(true);
//                return;
//            }
//            if (ev.getEntity().getType() == EntityType.PLAYER) {
//                if (ClanLand.getTypeAt(ev.getEntity().getLocation()).equalsIgnoreCase("SAFE")) {
//                    ev.setCancelled(true);
//                    return;
//                }
//                if (ev.getDamage() >= 2) {
//                    ((Player) ev.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 10, 1));
//                    ((Player) ev.getEntity()).sendMessage(ChatColor.RED + L.get("HitLeg"));
//                }
//            }
//        } else if (ev.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) {
//            ev.setDamage(ev.getDamage() * 1.5);
//            if (ev.getEntity() instanceof LivingEntity) {
//                if (ev.getEntity().getType() == EntityType.PLAYER) {
//                    // engenheiro primário imune eletrecidade
//                    if (Jobs.getJobLevel("Engenheiro", (Player) ev.getEntity()) == 1) {
//                        ev.setCancelled(true);
//                        return;
//                    }
//                }
//                Mobs.doRandomKnock((LivingEntity) ev.getEntity(), 0.9f);
//            }
//        } else if (ev.getCause() == EntityDamageEvent.DamageCause.FIRE || ev.getCause() == EntityDamageEvent.DamageCause.LAVA) {
//            if (ev.getEntity() instanceof LivingEntity) {
//                if (ev.getEntity().getType() == EntityType.PLAYER) {
//                    // engenheiro primário imune eletrecidade
//                    if (ev.getDamage() > 0 && !ev.isCancelled() && Jobs.getJobLevel("Fazendeiro", (Player) ev.getEntity()) == 1) {
//                        ev.setDamage(ev.getDamage() * 2);
//                    }
//                }
//            }
//        } else if (ev.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || ev.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
//            if (ev.getEntity() instanceof Player) {
//                // explosões power
//                ev.setDamage(((Player) ev.getEntity()).getMaxHealth() / 3);
//            }
//        }
//
//        boolean mobHitou = false;
//
//        ///// PLAYER TOMA DE KKER COISA
//        if (ev.getEntity() instanceof Player) {
//
//            ExecutaSkill.playerToma(ev);
//            KoM.debug("Dano no final do playertoma " + ev.getDamage());
//            if (ev.isCancelled()) {
//                return;
//            }
//        }
//
//        if (ev instanceof EntityDamageByEntityEvent) {
//            EntityDamageByEntityEvent ev2 = (EntityDamageByEntityEvent) ev;
//
//            if (ev.getEntity().getType() == EntityType.PLAYER && ev2.getDamager().getType() == EntityType.WOLF && ev.getEntity().getWorld().getName().equalsIgnoreCase("NewDungeon")) {
//                ev.setCancelled(true);
//                return;
//            } else {
//                this.rolaAPorrada(ev2);
//            }
//            if (ev2.getDamager() instanceof Monster || (ev2.getDamager() instanceof Projectile && ((Projectile) ev2.getDamager()).getShooter() instanceof Monster)) {
//                mobHitou = true;
//            }
//        }
//
//        if (ev.getEntity() instanceof Player || ev.getEntity() instanceof Animals) {
//
//            if (!mobHitou && ev.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || ev.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
//                ev.setDamage(ev.getDamage() * 0.75);
//            }
//
//            Location l = ev.getEntity().getLocation();
//            l.setY(l.getBlockY() + 1.1D);
//            if (ev.getDamage() >= 1 && !ev.isCancelled()) {
//                ev.getEntity().getLocation().getWorld().playEffect(ev.getEntity().getLocation().add(0.0D, 1.0D, 0.0D), Effect.STEP_SOUND, 152);
//            }
//        }
//
//        KoM.debug("final do tomaDano " + ev.getDamage() + " em " + ev.getEntity().getName());
//    }
//
//    public void rolaAPorrada(final EntityDamageByEntityEvent event) {
//
//        KoM.debug("Dano no começo do rolaporrada " + event.getDamage());
//
//        if (event.getCause() == DamageCause.BLOCK_EXPLOSION || event.getCause() == DamageCause.ENTITY_EXPLOSION) {
//            if (WorldGuardKom.ehSafeZone(event.getEntity().getLocation())) {
//                event.setCancelled(true);
//                return;
//            }
//        }
//
//        if (!(event.getEntity().getType() == EntityType.PLAYER) && event.getDamager().getType() == EntityType.PLAYER) {
//            Farmer.coletaDropExtraDeAnimal((Player) event.getDamager(), event.getEntity());
//        }
//
//        if (event.getDamager().getType() == EntityType.WOLF) {
//            if (event.getEntity() instanceof Monster) {
//                event.setDamage(event.getDamage() / 4);
//            } else if (event.getEntity().getType() == EntityType.PLAYER) {
//                if (WorldGuardKom.ehSafeZone(event.getEntity().getLocation())) {
//                    event.setCancelled(true);
//                    return;
//                } else {
//                    event.setDamage(event.getDamage() / 2);
//                }
//            }
//        }
//
//        if (event.getDamager().hasMetadata("prendeu")) {
//            event.setCancelled(true);
//            return;
//        }
//
//        if (event.getEntity() instanceof ItemFrame) {
//            event.setCancelled(true);
//            return;
//        }
//        // nao matar animais na vila
//        if (event.getEntity().getWorld().getName().equalsIgnoreCase("vila")) {
//            if (event.getEntity() instanceof Creature) {
//                if (event.getDamager() instanceof Player) {
//                    if (!((Player) event.getDamager()).isOp()) {
//                        event.setCancelled(true);
//                        return;
//                    }
//                }
//            }
//        }
//
//        KoM.debug("Meio rola porrada dano = " + event.getDamage());
//
//        IronGolem.dano(event);
//        if (event.isCancelled()) {
//            return;
//        }
//
//        if (event.getEntity().getType() == EntityType.WOLF) {
//            event.setDamage(event.getDamage() / 5);
//        }
//
//        if (event.getEntity() instanceof Horse) {
//            event.setDamage(event.getDamage() / 3);
//            String tipo = ClanLand.getTypeAt(event.getEntity().getLocation());
//            if (tipo.equalsIgnoreCase("SAFE")) {
//                event.setCancelled(true);
//            } else if (tipo.equalsIgnoreCase("CLAN")) {
//                Clan c = ClanLand.getClanAt(event.getEntity().getLocation());
//                Horse h = (Horse) event.getEntity();
//                if (h.getOwner() != null) {
//                    AnimalTamer dono = h.getOwner();
//                    Clan clanDono = ClanLand.manager.getClanByPlayerName(dono.getName());
//                    // se o cavalo ta na terra do dono
//                    boolean cavaloImune = false;
//
//                    if (clanDono != null && clanDono.getTag().equalsIgnoreCase(c.getTag())) {
//                        cavaloImune = true;
//                    }
//                    if (event.getDamager().getType() == EntityType.PLAYER) {
//                        ClanPlayer cp = ClanLand.manager.getClanPlayer((Player) event.getDamager());
//                        if (cp != null) {
//                            if (cp.getTag().equalsIgnoreCase(clanDono.getTag())) {
//                                cavaloImune = false;
//                            }
//                        }
//                    }
//                    if (cavaloImune) {
//                        event.setCancelled(true);
//                    }
//                }
//            }
//        }
//
//        if (event.getDamager() instanceof Projectile) {
//            Projectile projetil = (Projectile) event.getDamager();
//            LivingEntity atirador = (LivingEntity) projetil.getShooter();
//
//            if (projetil.hasMetadata("visual")) {
//                event.setCancelled(true);
//                return;
//            }
//
//            if (projetil.getCustomName() != null && projetil.getCustomName().equalsIgnoreCase("Foguete VIP")) {
//                event.setCancelled(true);
//                return;
//            }
//
//            // NEW MAGE SPELLS
//            if (projetil.hasMetadata("spell")) {
//                event.setCancelled(true);
//                MageSpell spell = (MageSpell) MetaShit.getMetaObject("spell", projetil);
//                spell.spellHit((LivingEntity) event.getEntity(), projetil.getLocation(), projetil);
//                return;
//            }
//
//            String meta = MetaShit.getMetaString("tipoTiro", projetil);
//            if (meta != null && meta.equalsIgnoreCase("kaboom")) {
//                event.setDamage(2D);
//            }
//
//            if (atirador != null && atirador.getType() == EntityType.PLAYER && event.getEntity().getType() == EntityType.PLAYER && event.getEntity().getWorld().getName().equalsIgnoreCase("NewDungeon")) {
//                event.setCancelled(true);
//                return;
//            }
//
//            if (projetil instanceof SmallFireball) {
//                event.getEntity().setFireTicks(100);
//            }
//
//            if (event.getEntity() instanceof Monster) {
//                Object objEf = MetaShit.getMetaObject("efeitos", event.getEntity());
//                if (objEf != null) {
//                    HashSet<Mobs.EfeitoMobs> efeitos = (HashSet) objEf;
//                    if (efeitos.contains(Mobs.EfeitoMobs.antiProjetil)) {
//                        event.setCancelled(true);
//                    }
//                }
//            }
//
//            if (atirador == null) {
//                return;
//            }
//            if (atirador.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
//                atirador.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
//            }
//
//            if (atirador instanceof Player && event.getEntity() instanceof Player) {
//                if (!SimpleClanKom.canPvp((Player) atirador, (Player) event.getEntity())) {
//                    event.setCancelled(true);
//                    return;
//                }
//                if (event.getDamage() > 0 && !event.isCancelled()) {
//                    MetaShit.setMetaObject("logout", event.getEntity(), System.currentTimeMillis() / 1000);
//                }
//            }
//
//            // se um player atira num mob
//            if (event.getEntity() instanceof Monster && atirador instanceof Player) {
//                Mobs.playerBateEmMob((Player) atirador, (Monster) event.getEntity(), event);
//                // se um mob atira num player
//            } else if (event.getEntity() instanceof Player && atirador instanceof Monster) {
//                Mobs.enemyCauseDamage((Monster) atirador, (Player) event.getEntity(), event);
//            }
//            meta = MetaShit.getMetaString("tipoTiro", projetil);
//            if (meta != null && meta.equalsIgnoreCase("kaboom")) {
//                if (event.getEntity() instanceof LivingEntity) {
//                    event.setDamage(event.getDamage() + 2D);
//                    //if (projetil.hasMetadata("modDano")) {
//                    //    event.setDamage(event.getDamage() * (double) MetaShit.getMetaObject("modDano", projetil));
//                    //cp = Terrenos.manager.getClanPlayer(ev.getEntity().getUniqueId());
//                    //}
//
//                    double alturaflecha = projetil.getLocation().getY();
//                    double diferenca = alturaflecha - event.getEntity().getLocation().getY();
//
//                    if (PlayerSpec.temSpec((Player) atirador, PlayerSpec.Fuzileiro)) {
//                        event.setDamage(event.getDamage() * 1.3);
//                    } else if (PlayerSpec.temSpec((Player) atirador, PlayerSpec.Inventor)) {
//                        event.setDamage(event.getDamage() * 0.6);
//                    }
//
//                    double pode = 1.5;
//                    if (event.getEntity() instanceof Player) {
//                        if (((Player) event.getEntity()).isSneaking()) {
//                            pode = 1.19;
//                        }
//                    }
//                    //BOOM PERANHA
//                    if (diferenca > pode) {
//                        if (atirador instanceof Player) {
//                            ((Player) atirador).sendMessage(ChatColor.GREEN + "! Head Shot !");
//                            event.setDamage(event.getDamage() * 3.5);
//                            if (event.getEntity().getType() == EntityType.PLAYER) {
//                                Player tomou = (Player) event.getEntity();
//                                if (tomou.getInventory().getHelmet() != null && tomou.getInventory().getHelmet().getType() == Material.GOLD_HELMET) {
//                                    event.setDamage(event.getDamage() * 0.5);
//                                    tomou.getInventory().getHelmet().setDurability((short) (tomou.getInventory().getHelmet().getDurability() + 5));
//                                    if (tomou.getInventory().getHelmet().getDurability() >= tomou.getInventory().getHelmet().getType().getMaxDurability()) {
//                                        tomou.getInventory().setHelmet(null);
//                                    }
//                                }
//                            }
//
//                        } else {
//                            event.setDamage(event.getDamage() * 0.9);
//                        }
//                    }
//
//                    //((LivingEntity) event.getEntity()).damage(5D, atirador);
//                    if (event.getEntity().getType() == EntityType.PLAYER && atirador.getType() == EntityType.PLAYER) {
//                        ClanPlayer atirou = ClanLand.manager.getClanPlayer((Player) atirador);
//                        ClanPlayer tomou = ClanLand.manager.getClanPlayer((Player) event.getEntity());
//                        if (atirou != null && tomou != null && (atirou.isAlly(tomou.toPlayer()) || atirou.getTag().equalsIgnoreCase(tomou.getTag()))) {
//                            event.setDamage(0D);
//                            event.setCancelled(true);
//                            return;
//                        }
//                    }
//
//                    if (event.getEntity() instanceof Monster) {
//                        event.setDamage(event.getDamage() / 1.5);
//                    }
//
//                    PlayEffect.play(VisualEffect.LAVA, event.getEntity().getLocation(), "num:3");
//                    event.getEntity().getWorld().playEffect(event.getEntity().getLocation(), Effect.SMOKE, 10);
//                    if (atirador.getType() == EntityType.PLAYER) {
//                        ((Player) atirador).sendMessage(ChatColor.RED + "*boom*");
//                    }
//                    if (KoM.debugMode) {
//                        KoM.log.info("Dano Final Bonka: " + event.getDamage());
//                    }
//                }
//            }
//
//            if (meta != null && meta.equalsIgnoreCase("autoDispenser")) {
//                event.getEntity().getWorld().playEffect(event.getEntity().getLocation(), Effect.SMOKE, 10);
//                if (event.getEntity() instanceof LivingEntity) {
//                    ((LivingEntity) event.getEntity()).damage(4D, atirador);
//                }
//            }
//            // se tomou magia
//            if (event.getDamager().getType() == EntityType.ARROW || event.getDamager().getType() == EntityType.SNOWBALL || event.getDamager().getType() == EntityType.FIREBALL || event.getDamager().getType() == EntityType.SMALL_FIREBALL) {
//                if (event.getEntity().getType() == EntityType.PLAYER) {
//                    Player tomou = (Player) event.getEntity();
//
//                    /*
//                     AttributeInfo info = KnightsOfMania.database.getAtributos(tomou);
//                     if (KnightsOfMania.debugMode) {
//                     tomou.sendMessage("Tomei " + event.getDamage());
//                     }
//                     */
//                    //double ratio = Attributes.calcDamageAbsorbtion(info.attributes.get(Attr.tuffness));
//                    //double dano = event.getDamage() * (1 - ratio);
//                    //event.setDamage(dano);
//                    //if (KnightsOfMania.debugMode) {
//                    //    tomou.sendMessage("Com calculo de rigidez tomei " + event.getDamage() + " - " + dano + " ratio=" + ratio);
//                    //}
//                }
//            }
//            Thief.bonusDanoDeLonge(event);
//
//            //////// CALCULANDO BONUS /////////
//            if (projetil.hasMetadata("modDano")) {
//
//                double ratio = (double) MetaShit.getMetaObject("modDano", projetil);
//                if (KoM.debugMode) {
//                    KoM.log.info("Dano Inicial: " + event.getDamage());
//                }
//                if (KoM.debugMode) {
//                    KoM.log.info("Mod dano EntityDamage: " + ratio);
//                }
//                event.setDamage(event.getDamage() * ratio);
//                if (KoM.debugMode) {
//                    KoM.log.info("Dano Final: " + ratio);
//                }
//            }
//        } else if (event.getEntity() instanceof Monster && event.getDamager() instanceof Player) {
//            Mobs.playerBateEmMob((Player) event.getDamager(), (Monster) event.getEntity(), event);
//        }
//        if (event.getEntity() instanceof Monster) {
//            KoM.debug("inicio mob efeitos = " + event.getDamage());
//            HashSet<Mobs.EfeitoMobs> efeitos = (HashSet) MetaShit.getMetaObject("efeitos", event.getEntity());
//            if (efeitos != null) {
//                if (efeitos.contains(Mobs.EfeitoMobs.bateFogo)) {
//                    if (event.getCause() == EntityDamageEvent.DamageCause.STARVATION || event.getCause() == EntityDamageEvent.DamageCause.THORNS || event.getCause() == EntityDamageEvent.DamageCause.SUICIDE || event.getCause() == EntityDamageEvent.DamageCause.WITHER || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.LAVA || event.getCause() == EntityDamageEvent.DamageCause.MELTING) {
//                        event.setCancelled(true);
//                        event.setDamage(0D);
//                    }
//
//                }
//                if (efeitos.contains(Mobs.EfeitoMobs.fantasma)) {
//                    if (event.getCause() == EntityDamageEvent.DamageCause.CONTACT || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.getCause() == EntityDamageEvent.DamageCause.SUICIDE) {
//                        event.setDamage(0.5d);
//                    }
//                }
//            }
//        }
//        if (event.getEntity() instanceof Player && event.getDamager() instanceof Monster) {
//            Monster m = (Monster) event.getDamager();
//            Mobs.enemyCauseDamage(m, (Player) event.getEntity(), event);
//        }
//
//        // player hits something..
//        ExecutaSkill.playerDoDamage(event);
//        KoM.debug("Dano no final do rolaporrada " + event.getDamage());
//    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void danoHighest(EntityDamageByEntityEvent ev) {
        KoM.debug("Recebendo do danoHighest " + ev.getDamage() + " em " + ev.getEntity().getName() + " cancelado " + ev.isCancelled());
        KoM.debug("DAMAGER " + (ev.getDamager() instanceof Player) + "  canceled " + ev.isCancelled() + "  dano " + ev.getDamage());
    }

    public static class KoMBoss {

        private UUID entityUUID;
        private LivingEntity entity;
        private int mobLevel;
        private double exp;
        private HashMap<UUID, Double> danos = new HashMap<>();

        public KoMBoss(LivingEntity entity) {
            this.entityUUID = entity.getUniqueId();
            this.entity = entity;
            int mobLevel = (int) MetaShit.getMetaObject("mobLevel", entity);
            if (mobLevel > 20) mobLevel = 20;
            this.mobLevel = mobLevel;
            this.exp = (XP.getExpProximoNivel(mobLevel) * 0.10);
        }

        public LivingEntity getEntity() {
            return entity;
        }

        public UUID getEntityUUID() {
            return entityUUID;
        }

        public int getMobLevel() {
            return mobLevel;
        }

        public double getExp() {
            return exp;
        }

        public void addDamage(UUID uuid, double damage) {
            if (danos.containsKey(uuid)) damage = danos.get(uuid) + damage;
            danos.put(uuid, damage);
        }

        public Set<Map.Entry<UUID, Double>> getDanos() {
            return danos.entrySet();
        }

    }

    public static KoMBoss getBoss(UUID entityUUID) {
        for (KoMBoss boss : bosses) {
            if (boss.getEntityUUID() == entityUUID) return boss;
        }
        return null;
    }

    private static void darDano(Player damager, Double damage, Player damaged) {
        if (damaged.getHealth() <= damage) DeathEvents.ultimoDano.put(damaged.getUniqueId(), damager.getUniqueId());
    }

    private static void darDano(Entity damager, Double damage, LivingEntity damaged) {
        if (damaged instanceof Player && damager instanceof Player) darDano((Player) damager, damage, (Player) damaged);
        damaged.setHealth(damaged.getHealth() - damage);
        actionDamage(damaged);

    }

    public static void darDano(Entity damager, Double damage, Entity damaged) {
        if (damaged instanceof LivingEntity) darDano(damager, damage, (LivingEntity) damaged);
    }

    public static void darDano(LivingEntity damager, Double damage, LivingEntity damaged, EntityDamageEvent.DamageCause cause) {
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, damaged, cause, damage);
        DamageListener.reciveDamage(event);
        if (event.isCancelled()) return;
        darDano(damager, event.getDamage(), damaged);
    }

    public static void actionDamage(org.bukkit.entity.LivingEntity livingEntity) {
        EntityLiving entityLiving = ((CraftLivingEntity) livingEntity).getHandle();
        PacketPlayOutEntityStatus status = new PacketPlayOutEntityStatus(entityLiving, (byte) 2);
        EntityPlayer craftDamaged;
        if (livingEntity instanceof Player) {
            craftDamaged = ((CraftPlayer) livingEntity).getHandle();
            craftDamaged.playerConnection.sendPacket(status);
        }
        for (Entity nearby : livingEntity.getNearbyEntities(48, 20, 48)) {
            if (nearby instanceof Player) {
                craftDamaged = ((CraftPlayer) nearby).getHandle();
                craftDamaged.playerConnection.sendPacket(status);
            }
        }
    }

    public static void deiDano(Player damager, LivingEntity damaged, long ticks) {
        if (damaged instanceof Player) {
            DeathEvents.ultimoDano.put(damaged.getUniqueId(), damager.getUniqueId());
            Bukkit.getScheduler().runTaskLater(KoM._instance, () -> {
                if (DeathEvents.ultimoDano.containsKey(damaged.getUniqueId())) DeathEvents.ultimoDano.remove(damaged.getUniqueId());
            }, ticks);
        }
    }

}
