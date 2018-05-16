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
package nativelevel.Classes;

import me.fromgate.playeffect.PlayEffect;
import me.fromgate.playeffect.VisualEffect;
import nativelevel.Custom.CustomItem;
import nativelevel.Custom.Items.Adaga;
import nativelevel.Custom.Items.Ank;
import nativelevel.Jobs;
import nativelevel.KoM;
import nativelevel.Menu.Menu;
import nativelevel.skills.SkillMaster;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Paladin {

    private static final Material[] lootsExtras = {Material.GOLDEN_APPLE, Material.CAKE, Material.EMERALD, Material.GLOWSTONE_DUST, Material.COOKIE, Material.COOKIE, Material.COOKIE, Material.COOKIE, Material.COOKIE, Material.COOKIE};
    public static final String name = "Paladino";

    //  ლ(ಠ益ಠლ) paladinos...odeiam....magos... !!!

    public static void onHit(EntityDamageByEntityEvent ev) {

        Player attacker = (Player) ev.getDamager();
        ItemStack weapon = attacker.getInventory().getItemInMainHand();


        if (weapon.getType().toString().contains("SWORD") && !Adaga.isAdaga(weapon)) {

            int chance;
            if (SkillMaster.temSkill(attacker, name, "Usar Espadas")) chance = attacker.getLevel();
            else chance = 0;

            if (Jobs.hasSuccess(-35, name, attacker)) {
                ev.setCancelled(true);
                attacker.sendMessage("§cVocê errou o ataque.");
                return;
            }

            if (weapon.getType().name().contains("GOLD")) ev.setDamage(ev.getDamage() + 0.5);

            ev.setDamage(ev.getDamage() * 1.10);

        }
    }

    public static void onHitSec(EntityDamageByEntityEvent ev) {

        Player attacker = (Player) ev.getDamager();
        ItemStack weapon = attacker.getInventory().getItemInMainHand();

        if (!Adaga.isAdaga(weapon) && weapon.getType().toString().contains("SWORD")) {

            int chance;
            if (SkillMaster.temSkill(attacker, name, "Usar Espadas")) chance = (attacker.getLevel() / 2);
            else chance = 0;

            if (Jobs.hasSuccess(-35, name, attacker)) {
                ev.setCancelled(true);
                attacker.sendMessage("§cVocê errou o ataque.");
                return;
            }
        }

    }

    public static void noHit(EntityDamageByEntityEvent ev) {

        Player attacker = (Player) ev.getDamager();
        ItemStack weapon = attacker.getInventory().getItemInMainHand();

        if (!Adaga.isAdaga(weapon) && weapon.getType().toString().contains("SWORD")) {
            int chance = (attacker.getLevel() / 3);
            if (Jobs.hasSuccess(-35, name, attacker)) {
                ev.setCancelled(true);
                attacker.sendMessage("§cVocê errou o ataque.");
            }
        }
    }

    public static void onDamaged(EntityDamageEvent ev) {

        Player damaged = (Player) ev.getEntity();

        if (SkillMaster.temSkill(damaged, "Paladino", "Jabu está entre nós")) {
            if (Jobs.rnd.nextInt(21) == 1) {
                ev.setDamage(ev.getDamage() * 0.10);
                damaged.playSound(damaged.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1.2f);
                damaged.spawnParticle(Particle.FLAME, damaged.getLocation().add(0, 0.3, 0), 20);
                damaged.sendMessage(ChatColor.AQUA + Menu.getSimbolo("Paladino") + " " + ChatColor.GOLD + "Deus Jabu te protege do mal !");
            }
        }

        if (ev.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) ev.setDamage(ev.getDamage() * 0.90);

    }

    public static void onDamagedSec(EntityDamageEvent ev) {
        Player damaged = (Player) ev.getEntity();

        if (SkillMaster.temSkill(damaged, "Paladino", "Jabu está entre nós")) {
            if (Jobs.rnd.nextInt(51) == 1) {
                ev.setDamage(ev.getDamage() * 0.10);
                damaged.playSound(damaged.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1.2f);
                damaged.spawnParticle(Particle.FLAME, damaged.getLocation().add(0, 0.3, 0), 20);
                damaged.sendMessage(ChatColor.AQUA + Menu.getSimbolo("Paladino") + " " + ChatColor.GOLD + "Deus Jabu te protege do mal !");
            }
        }

    }

    public static void usaAnk(EntityDamageEvent ev, Player p) {
        // dano matador

        if (KoM.debugMode) {
            KoM.log.info(p.getName() + " ativando ressurrenct");
        }
        if (ev.getDamage() > p.getHealth()) {
            if (Ank.protegidos.contains(p.getName())) {
                if (KoM.debugMode) {
                    KoM.log.info(p.getName() + " removendo hashmap");
                }
                Ank.protegidos.remove(p.getName());
                p.sendMessage(ChatColor.GREEN + "Ativando luz divina !");
                if (KoM.debugMode) {
                    KoM.log.info(p.getName() + " criando item");
                }
                ItemStack ank = CustomItem.getItem(Ank.class).generateItem(1);
                KoM.gastaCustomItem(p, ank);
                if (KoM.debugMode) {
                    KoM.log.info(p.getName() + " verificando se tem custom item");
                }
                if (KoM.temCustomItem(p, ank)) {
                    p.sendMessage(ChatColor.AQUA + Menu.getSimbolo("Paladino") + " " + ChatColor.GOLD + "A outra Luz que voce tem evita que voce volte a vida !");
                } else {
                    PlayEffect.play(VisualEffect.FIREWORKS_EXPLODE, p.getLocation(), "type:star color:white");
                    p.setHealth(p.getMaxHealth());
                    p.getWorld().createExplosion(p.getLocation(), 0);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 1));
                    ev.setDamage(1D);
                    ev.setCancelled(true);
                    return;
                }

            }
        }
    }

    public static void pegaDropsExtrasDeMobs(Entity e, Player p) {
        if (!p.getInventory().getItemInMainHand().getType().name().contains("SWORD")) {
            return;
        }
        if (e instanceof Creature) {
            int level = Jobs.getJobLevel("Paladino", p);
            int rnd = 0;
            if (level == 1) // primaria 
            {
                rnd = 30;
            }
            if (level == 2) {
                rnd = 80;
            }
            if (rnd != 0) {
                if (Jobs.rnd.nextInt(rnd) == 1) {
                    p.sendMessage(ChatColor.AQUA + Menu.getSimbolo("Paladino") + " " + ChatColor.YELLOW + "Voce foi abencoado pelo deus Jabu !");
                    // dropando coisa boa
                    int item = Jobs.rnd.nextInt(lootsExtras.length);
                    p.getWorld().dropItem(e.getLocation(), new ItemStack(lootsExtras[item], 1));
                    // dropando reagentes de alquimia
                    item = 370 + Jobs.rnd.nextInt(382 - 370);
                    p.getWorld().dropItem(e.getLocation(), new ItemStack(item, 1));
                }
            }
        }
    }

    public static void tomaMenosDano(EntityDamageEvent ev, Player p) {

        int level = Jobs.getJobLevel("Paladino", p);
        int rnd = 0;
        // chance de evitar dano
        if (level == 1) // primaria
        {
            rnd = 10;

            // BLOQUEIO COM PORTA (retirado)
            /*
             if (p.getInventory().getItemInMainHand().getType() == Material.IRON_DOOR && p.getLocation().getBlock().getType() == Material.WEB) {
             p.sendMessage(ChatColor.RED + "Voce nao conseguiu bloquear o ataque por estar preso em teias");
             } else {
             if (p.getInventory().getItemInMainHand().getType() == Material.IRON_DOOR && (ev.getCause() == DamageCause.ENTITY_ATTACK || ev.getCause() == DamageCause.CONTACT || ev.getCause() == DamageCause.PROJECTILE)) {
             Entity causador = null;
             if (ev instanceof EntityDamageByEntityEvent) {
             causador = ((EntityDamageByEntityEvent) ev).getDamager();
             Vector chave = causador.getLocation().getDirection();
             if (ev.getCause() == DamageCause.PROJECTILE) {
             chave = causador.getVelocity();
             chave.setY(1);
             chave = chave.normalize();
             }
             double angle = Tralhas.getAngle(p.getLocation().getDirection(), chave);
             if (KoM.debugMode) {
             KoM.log.info("Angle: " + angle);
             }
             if (angle < 50) {
             p.sendMessage(ChatColor.RED + "Voce tem que estar de frente para poder bloquear o ataque !");
             return;
             }
             }
             int mana = 30;
             if (PlayerSpec.temSpec(p, PlayerSpec.Guardiao)) {
             mana = 10;
             } else if (PlayerSpec.temSpec(p, PlayerSpec.Crusador)) {
             mana = 60;
             }
             if (Mana.spendMana(p, mana)) {
             p.sendMessage(ChatColor.GOLD + "Voce bloqueou o ataque");
             PlayEffect.play(VisualEffect.FIREWORKS_SPARK, p.getLocation(), "num:10");
             PlayEffect.play(VisualEffect.SOUND, p.getLocation(), "type:zombie_metal");
             ev.setDamage(0f);
             ev.setCancelled(true);
             p.setNoDamageTicks(20);
             if(ev instanceof EntityDamageByEntityEvent) {
             EntityDamageByEntityEvent ev2 = (EntityDamageByEntityEvent)ev;
             if(ev2.getDamager().getType()==EntityType.PLAYER) {
             Player bateu = (Player) ev2.getDamager();
             if(Thief.taInvisivel(bateu))
             Thief.revela(bateu);
             }
             }
             }
             return;
             }
                   
             }
             */
        }
        if (level == 2) {
            rnd = 50;
        }
        if (rnd != 0) {
            if (Jobs.rnd.nextInt(rnd) == 1) {
                ev.setDamage(0);
                return;
            }
        } else {
            return;
        }
        // se tomar, reduz o dano
        double reducao = 0;
        if (level == 1) // primaria
        {
            reducao = 1;
        }
        if (level == 2) {
            reducao = 0;
        }

        if (ev.getDamage() <= reducao) {
            ev.setDamage(1D);
        } else {
            ev.setDamage(ev.getDamage() - reducao);
        }
    }

}
