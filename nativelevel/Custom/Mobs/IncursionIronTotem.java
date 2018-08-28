package nativelevel.Custom.Mobs;

import com.google.common.base.Predicate;
import nativelevel.CFG;
import nativelevel.Custom.CustomItem;
import nativelevel.Custom.Items.PedraDoPoder;
import nativelevel.Jobs;
import nativelevel.KoM;
import nativelevel.Listeners.DamageListener;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.Fireworks;
import nativelevel.utils.GeneralUtils;
import net.minecraft.server.v1_12_R1.*;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.*;
import org.bukkit.Chunk;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import pixelmc.utils.ConfigManager;

import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.util.*;

public class IncursionIronTotem extends EntityIronGolem {

    public static HashMap<UUID, IncursionIronTotem> totens = new HashMap<>();
    private static HashSet<Ruin> ruins = new HashSet<>();

    private class TotemStatus {

        private int life;
        private int damage;
        private double movementSpeed;
        private int iaLevel;

        private double rageLevel = 0;

        private TotemStatus(int life, int damage, double movementSpeed, int iaLevel) {
            this.life = life;
            this.damage = damage;
            this.movementSpeed = movementSpeed;
            this.iaLevel = iaLevel;
        }

        public int getDamage() {
            return damage;
        }
    }

    //TODO CRAFT, BANCO FOR TOTEMSTATUS, GETTING STATUS WHEN SPAWN...
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public String clan;
    private Location chunkCenter;
    private TotemStatus totemStatus;
    public int defeatRunnableId;

    public IncursionIronTotem(Location loc, String clanTag, String attackerTag) {
        super(((CraftWorld) loc.getWorld()).getHandle());
        persistent = true;

        chunkCenter = loc.getChunk().getBlock(8, GeneralUtils.getHighestBlockAt(loc.getChunk().getBlock(8, 0, 8).getLocation()).getY(), 8).getLocation();
        clan = clanTag;
        totemStatus = getTotemStatus(clan);

        setCustomName("§7Totem de §nIncursão§8 *§a0§8*");
        setCustomNameVisible(true);
        setLocation(chunkCenter.getX(), chunkCenter.getY(), chunkCenter.getZ(), 0, 0);

        getAttributeInstance(GenericAttributes.maxHealth).setValue(totemStatus.life);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(totemStatus.movementSpeed);

        setHealth(totemStatus.life);

        goalSelector.a(2, new PathfinderGoalMoveTowardsTargetInXChunk(this, new Vec3D(chunkCenter.getX(), chunkCenter.getY(), chunkCenter.getZ()), chunkCenter.getChunk(), totemStatus.iaLevel, 0.9D));
        goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 4.0F));
        goalSelector.a(4, new PathfinderGoalRandomLookaround(this));

        targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, 3, false, true, new TargetPredicate(this)));

        totens.put(this.getUniqueID(), this);

        defeatRunnableId = Bukkit.getScheduler().runTaskLater(KoM._instance, () -> {
            ClanLand.getClan(clan).addBb(clan.toUpperCase(), "§eA invasão iniciada pela guilda " + attackerTag + " não teve resultado algum.");
            getBukkitEntity().remove();
        }, 1800000).getTaskId();

    }

    @Override
    protected void r() {
        //CLEAR Let Constructor Deal With It
    }

//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    private class TargetPredicate implements Predicate {

        private IncursionIronTotem incursionIronTotem;

        private TargetPredicate(IncursionIronTotem incursionIronTotem) {
            this.incursionIronTotem = incursionIronTotem;
        }

        public boolean target(EntityHuman entityHuman) {
            if (entityHuman.getChunkX() != incursionIronTotem.chunkCenter.getChunk().getX() || entityHuman.getChunkZ() != incursionIronTotem.chunkCenter.getChunk().getZ()) {
                incursionIronTotem.getNavigation().a(incursionIronTotem.chunkCenter.getX(), incursionIronTotem.chunkCenter.getY(), incursionIronTotem.chunkCenter.getZ(), 1.1D);
                return false;
            }
            ClanPlayer clanPlayer = ClanLand.manager.getClanPlayer(entityHuman.getUniqueID());
            return clanPlayer == null || !clanPlayer.getTag().equalsIgnoreCase(clan);
        }

        @Override
        public boolean apply(@Nullable Object object) {
            return target((EntityHuman) object);
        }

    }

    private class PathfinderGoalMoveTowardsTargetInXChunk extends PathfinderGoal {
        private final EntityCreature entity;
        private EntityLiving entityTarget;
        private Vec3D recall;
        private int chanceRun;
        private org.bukkit.Chunk chunk;
        private final double velocity;

        private PathfinderGoalMoveTowardsTargetInXChunk(EntityCreature entity, Vec3D recall, org.bukkit.Chunk chunk, int chanceRun, double velocity) {
            this.entity = entity;
            this.recall = recall;
            this.chunk = chunk;
            this.chanceRun = chanceRun;
            this.velocity = velocity;
            this.a(1);
        }

        @Override
        public boolean a() {
            if (chanceRun <= 0 || entity.getRandom().nextInt(chanceRun) != 0) return false;
            entityTarget = entity.getGoalTarget();
            if (entityTarget == null || !entityTarget.isAlive() || entityTarget.getChunkX() != chunk.getX() || entityTarget.getChunkZ() != chunk.getZ()) {
                entity.setGoalTarget(null);
                if (entity.getX() != recall.x || entity.getZ() != recall.z) entity.getNavigation().a(recall.x, recall.y, recall.z, 1.1D);
                return false;
            }
            if (entity.h(entityTarget) < 1.7D) entity.B(entityTarget);
            if (entity.getNavigation().o()) {
                addRage(entity.getUniqueID(), (1 * 0.1));
            }
            entity.getNavigation().a(entityTarget, velocity);
            return false;
        }


//            if (chanceRun <= 0 || entity.getRandom().nextInt(chanceRun) != 0) return false;
//            entityTarget = entity.getGoalTarget();
//            if (entityTarget == null) return false;
//            if (!entityTarget.isAlive() || entityTarget.getChunkX() != chunk.getX() || entityTarget.getChunkZ() != chunk.getZ()) {
//                entity.setGoalTarget(null);
//                return false;
//            }
//            return true;


    }

    private static class RageRunnable implements Runnable {

        private int taskId;
        private org.bukkit.entity.Entity entity;
        private int times = 0;

        private RageRunnable(org.bukkit.entity.Entity entity) {
            this.entity = entity;
        }

        @Override
        public void run() {
            times++;
            if (entity.isDead() || times > 15) {
                KoM.announce("" + taskId);
                Bukkit.getScheduler().cancelTask(taskId);
                return;
            }
            for (int a = 0; a <= 5; a++) {
                entity.getWorld().spawnParticle(Particle.FLAME, entity.getLocation(), 2, Jobs.rnd.nextDouble(), 0.05, Jobs.rnd.nextDouble(), 0.2);
                entity.getWorld().spawnParticle(Particle.SMOKE_LARGE, entity.getLocation(), 1, (Jobs.rnd.nextDouble() * 0.15), 1, (Jobs.rnd.nextDouble() * 0.15), 0.3);
            }
        }

    }

    public static void onDamaged(EntityDamageEvent ev) {
        IronGolem entity = (IronGolem) ev.getEntity();
        if (!totens.containsKey(entity.getUniqueId())) return;
        ev.setDamage(ev.getDamage() / 5);
        if (addRage(entity.getUniqueId(), ev.getDamage())) {
            ev.setDamage(0D);
        } else if (totens.get(entity.getUniqueId()).totemStatus.rageLevel > 15) {
            LivingEntity target = entity.getTarget();
            if (target != null) {
                Vector toDamager = target.getLocation().toVector().subtract(entity.getLocation().toVector());
                entity.setVelocity(toDamager.normalize().multiply(0.35));
            }
        }

        if (ev instanceof EntityDamageByEntityEvent) {
            if (entity.getTarget() == null && ((EntityDamageByEntityEvent) ev).getDamager() instanceof LivingEntity) {
                LivingEntity leDamager = (LivingEntity) ((EntityDamageByEntityEvent) ev).getDamager();
                ClanPlayer cpDamager = null;
                if (leDamager instanceof Player) cpDamager = ClanLand.manager.getClanPlayer(leDamager.getUniqueId());
                if (cpDamager == null || !cpDamager.getTag().equalsIgnoreCase(totens.get(entity.getUniqueId()).clan)) entity.setTarget(leDamager);
            }
            double rage = totens.get(entity.getUniqueId()).totemStatus.rageLevel;
            if (rage >= 40) entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.3f, 1f);
            else if (rage >= 25) entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.2f, 1.2f);
            else if (rage >= 15) entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.1f, 1.4f);
        }

        if ((entity.getHealth() - ev.getDamage()) < 0) {
            Bukkit.getScheduler().cancelTask(totens.get(entity.getUniqueId()).defeatRunnableId);

            for (Entity ne : entity.getWorld().getNearbyEntities(entity.getLocation(), 36, 108, 36)) {
                if (ne instanceof Player) {
                    ne.sendMessage("§eO Totem que estava protegendo a guilda " + totens.get(entity.getUniqueId()).clan.toUpperCase() + " foi derrotado, o terreno que ele defendia está em ruínas por 30 minutos!");
                    ((Player) ne).playSound(entity.getLocation(), Sound.ENTITY_IRONGOLEM_DEATH, 1f, 0.8f);
                    for (int x = 0; x < 7; x++) ((Player) ne).playSound(ne.getLocation(), Sound.BLOCK_GRASS_BREAK, 1f, 0.75f);
                    for (int x = 0; x < 3; x++) ((Player) ne).playSound(ne.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 1f, 0.3f);
                }
            }

            Location chunkCenter = totens.get(entity.getUniqueId()).chunkCenter;
            ClanLand.removeClanAt(chunkCenter);
            ClanLand.setClanAt(chunkCenter, "RUIN");
            Ruin ruin = new Ruin(chunkCenter.getChunk().getX(), chunkCenter.getChunk().getZ(), (System.currentTimeMillis() + 1800000));

            ruin.runId = Bukkit.getScheduler().runTaskLater(KoM._instance, () -> {
                ClanLand.setClanAt(Bukkit.getWorld(CFG.mundoGuilda).getChunkAt(ruin.x, ruin.z).getBlock(0, 0, 0).getLocation(), "WILD");
                Ruin.removeRuinAt(ruin.x, ruin.z);
            }, (ruin.when - System.currentTimeMillis())).getTaskId();

            ruins.add(ruin);
            remove(entity.getUniqueId());
            entity.getWorld().dropItemNaturally(entity.getLocation(), CustomItem.getItem(PedraDoPoder.class).generateItem());
            entity.remove();
        }

    }

    public static void doDamage(EntityDamageByEntityEvent ev) {
        IronGolem entity = (IronGolem) ev.getDamager();
        if (!totens.containsKey(entity.getUniqueId())) return;
        ev.setDamage(totens.get(entity.getUniqueId()).totemStatus.damage);
        addRage(entity.getUniqueId(), -ev.getDamage());
    }

    private static boolean addRage(UUID totemUuid, double damage) {
        double rage = totens.get(totemUuid).totemStatus.rageLevel;

        IronGolem ironGolem = (IronGolem) Bukkit.getEntity(totemUuid);
        if (ironGolem == null) return false;

        rage += damage;

        boolean valueToReturn = false;
        if (rage < 0) totens.get(totemUuid).totemStatus.rageLevel = 0;
        else if (rage > 50) valueToReturn = doRage(totemUuid);
        else totens.get(totemUuid).totemStatus.rageLevel = rage;

        rage = totens.get(totemUuid).totemStatus.rageLevel;

        if (rage >= 40) ironGolem.setCustomName("§7Totem de §nIncursão §8*§4" + (int) rage + "§8*");
        else if (rage >= 25) ironGolem.setCustomName("§7Totem de §nIncursão §8*§c" + (int) rage + "§8*");
        else if (rage >= 15) ironGolem.setCustomName("§7Totem de §nIncursão §8*§e" + (int) rage + "§8*");
        else ironGolem.setCustomName("§7Totem de §nIncursão §8*§a" + (int) rage + "§8*");

        ironGolem.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(totens.get(totemUuid).totemStatus.movementSpeed + (rage / 500));

        return valueToReturn;
    }

    private static boolean doRage(UUID totemUuid) {
        IronGolem entity = (IronGolem) Bukkit.getEntity(totemUuid);
        if (entity == null) return false;

        double vida = entity.getHealth();
        if (vida < 0) return false;

        vida += totens.get(totemUuid).totemStatus.rageLevel;
        if (vida > entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
            vida = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

        entity.setHealth(vida);
        Fireworks.doFirework(entity.getLocation().add(0, 2, 0), FireworkEffect.Type.BURST, Color.ORANGE, Color.RED, 2);

        for (org.bukkit.entity.Entity nearby : entity.getNearbyEntities(7, 7, 7)) {
            if (!(nearby instanceof Player)) continue;
            ClanPlayer nearbyCP = ClanLand.manager.getAnyClanPlayer(nearby.getUniqueId());
            if (nearbyCP == null || !nearbyCP.getTag().equalsIgnoreCase(totens.get(totemUuid).clan)) {
                DamageListener.darDano(entity, (double) 10, (LivingEntity) nearby, EntityDamageEvent.DamageCause.FIRE);
                nearby.setFireTicks(150);
            }
        }
        RageRunnable run = new RageRunnable(entity);
        run.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(KoM._instance, run, 2, 2);
        entity.setTarget(null);
        totens.get(totemUuid).totemStatus.rageLevel = 0;
        return true;
    }

    public static void remove(UUID totemUuid) {
        totens.remove(totemUuid);
    }

    public static void register() {
        try {
            MinecraftKey key = new MinecraftKey("invasion_totem");
            EntityTypes.b.a(99, key, IncursionIronTotem.class);
            if (!EntityTypes.d.contains(key)) EntityTypes.d.add(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class Ruin {
        private int x;
        private int z;
        private long when;
        private int runId;

        private Ruin(int x, int z, long when) {
            this.x = x;
            this.z = z;
            this.when = when;
        }

        private static void removeRuinAt(int x, int z) {
            for (Ruin ruin : ruins)
                if (ruin.x == x && ruin.z == z) ruins.remove(ruin);
        }

    }

    public static void reloadRuins() {
        try {
            ConfigManager cfg = new ConfigManager(KoM._instance.getDataFolder() + "/ruinCache");
            if (!cfg.getConfig().contains("cache")) return;
            for (String cache : cfg.getConfig().getStringList("cache")) {
                String[] split = cache.split(";");
                Ruin ruin = new Ruin(Integer.valueOf(split[0]), Integer.valueOf(split[1]), Long.valueOf(split[2]));
                Long when = ruin.when - System.currentTimeMillis();
                if (when < 0) when = 10L;
                ruin.runId = Bukkit.getScheduler().runTaskLater(KoM._instance, () -> {
                    ClanLand.setClanAt(Bukkit.getWorld(CFG.mundoGuilda).getChunkAt(ruin.x, ruin.z).getBlock(0, 0, 0).getLocation(), "WILD");
                    Ruin.removeRuinAt(ruin.x, ruin.z);
                }, when).getTaskId();
                ruins.add(ruin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cacheRuins() {
        try {
            ConfigManager cfg = new ConfigManager(KoM._instance.getDataFolder() + "/ruinCache");
            List<String> ruinsL = new ArrayList<>();
            for (Ruin ruin : ruins) {
                Bukkit.getScheduler().cancelTask(ruin.runId);
                ruinsL.add(ruin.x + ";" + ruin.z + ";" + ruin.when);
            }
            cfg.getConfig().set("cache", ruinsL);
            cfg.saveConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //TODO DAO
    private TotemStatus getTotemStatus(String clan) {
        return new TotemStatus(250, 7, 0.31, 1);
    }

}