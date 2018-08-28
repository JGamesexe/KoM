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
import nativelevel.Attributes.Mana;
import nativelevel.Custom.Items.Adaga;
import nativelevel.Custom.Items.Ank;
import nativelevel.Jobs;
import nativelevel.KoM;
import nativelevel.Menu.Menu;
import nativelevel.skills.Skill;
import nativelevel.skills.SkillMaster;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Paladin {

    private static final Material[] lootsExtras = {Material.GOLDEN_APPLE, Material.CAKE, Material.EMERALD, Material.GLOWSTONE_DUST, Material.COOKIE, Material.COOKIE, Material.COOKIE, Material.COOKIE, Material.COOKIE, Material.COOKIE};
    public static final Jobs.Classe classe = Jobs.Classe.Paladino;
    public static final String name = "Paladino";

    //  ლ(ಠ益ಠლ) paladinos...odeiam....magos... !!!

    public static void onHit(EntityDamageByEntityEvent ev) {
        Player attacker = (Player) ev.getDamager();
        ItemStack weapon = attacker.getInventory().getItemInMainHand();

        if (weapon.getType().toString().contains("SWORD") && !Adaga.isAdaga(weapon)) {
            if (!Jobs.hasSuccess(-35, name, attacker)) {
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

        if (!Adaga.isAdaga(weapon) && weapon.getType().toString().contains("SWORD"))
            if (!Jobs.hasSuccess(-35, name, attacker)) {
                ev.setCancelled(true);
                attacker.sendMessage("§cVocê errou o ataque.");
            }

    }

    public static void noHit(EntityDamageByEntityEvent ev) {
        Player attacker = (Player) ev.getDamager();
        ItemStack weapon = attacker.getInventory().getItemInMainHand();

        if (!Adaga.isAdaga(weapon) && weapon.getType().toString().contains("SWORD"))
            if (Jobs.hasSuccess(-35, name, attacker)) {
                ev.setCancelled(true);
                attacker.sendMessage("§cVocê errou o ataque.");
            }

    }

    public static void onDamaged(EntityDamageEvent ev) {
        Player damaged = (Player) ev.getEntity();

        if (ev.getCause().equals(EntityDamageEvent.DamageCause.LIGHTNING)) ev.setDamage(ev.getDamage() * 1.4);

        if (SkillMaster.temSkill(damaged, Skills.Resistencia_Divina.skill))
            if (ev.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) ev.setDamage(ev.getDamage() * 0.90);

        if (SkillMaster.temSkill(damaged, Skills.Jabu_esta_entre_nos.skill)) {
            if (Jobs.rnd.nextInt(21) == 1) {
                ev.setDamage(ev.getDamage() * 0.05);
                damaged.playSound(damaged.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1.2f);
                damaged.spawnParticle(Particle.FLAME, damaged.getLocation().add(0, 0.3, 0), 20);
                damaged.sendMessage(ChatColor.AQUA + Menu.getSimbolo("Paladino") + " " + ChatColor.GOLD + "Deus Jabu te protege do mal !");
            }
        }

        if (ev.getDamage() > damaged.getHealth())
            if (Ank.protegidos.contains(damaged.getUniqueId()))
                if (SkillMaster.temSkill(damaged, Skills.Luz_Divina.skill)) usaAnk(ev);

    }

    public static void onDamagedSec(EntityDamageEvent ev) {
        Player damaged = (Player) ev.getEntity();

        if (SkillMaster.temSkill(damaged, Skills.Jabu_esta_entre_nos.skill))
            if (Jobs.rnd.nextInt(51) == 1) {
                ev.setDamage(ev.getDamage() * 0.10);
                damaged.playSound(damaged.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1.2f);
                damaged.spawnParticle(Particle.FLAME, damaged.getLocation().add(0, 0.3, 0), 20);
                damaged.sendMessage(ChatColor.AQUA + Menu.getSimbolo("Paladino") + " " + ChatColor.GOLD + "Deus Jabu te protege do mal !");
            }

    }

    public static void onBlocking(EntityDamageEvent ev) {
        Player blocker = (Player) ev.getEntity();

        if (Jobs.getJobLevel(Jobs.Classe.Paladino, blocker) != Jobs.TipoClasse.PRIMARIA || !Mana.spendMana(blocker, 15)) {

            int firstEmpty = blocker.getInventory().firstEmpty();

            blocker.sendMessage(ChatColor.RED + "Voce nao consegue defender direito e quase perdeu seu escudo");

            if (firstEmpty != -1) {

                if (blocker.getInventory().getItemInMainHand().getType() == Material.SHIELD) {

                    blocker.getInventory().addItem(blocker.getInventory().getItemInMainHand().clone());
                    blocker.getInventory().setItemInMainHand(new ItemStack(Material.AIR, 1));

                } else if (blocker.getInventory().getItemInOffHand().getType() == Material.SHIELD) {

                    blocker.getInventory().addItem(blocker.getInventory().getItemInOffHand().clone());
                    blocker.getInventory().setItemInOffHand(new ItemStack(Material.AIR, 1));
                }

            } else {

                if (blocker.getInventory().getItemInMainHand().getType() == Material.SHIELD) {

                    blocker.getInventory().addItem(blocker.getInventory().getItemInMainHand().clone());
                    blocker.getWorld().dropItem(blocker.getLocation(), blocker.getInventory().getItemInMainHand());

                } else if (blocker.getInventory().getItemInMainHand().getType() == Material.SHIELD) {

                    blocker.getWorld().dropItem(blocker.getLocation(), blocker.getInventory().getItemInOffHand());
                    blocker.getInventory().addItem(blocker.getInventory().getItemInOffHand().clone());
                }

            }
            ev.setCancelled(true);
            double vida = blocker.getHealth() - ev.getDamage();
            if (vida < 0) {
                vida = 0;
            }
            blocker.setHealth(vida);
        } else {
            PlayEffect.play(VisualEffect.FIREWORKS_SPARK, blocker.getLocation(), "num:10");
            PlayEffect.play(VisualEffect.SOUND, blocker.getLocation(), "type:zombie_metal");
        }

    }

    public static void usaAnk(EntityDamageEvent ev) {
        Player p = (Player) ev.getEntity();
        KoM.debug(p.getName() + "Ativando PalaRessurect");

        Ank.protegidos.remove(p.getUniqueId());
        p.sendMessage(ChatColor.GREEN + "Ativando luz divina !");

        PlayEffect.play(VisualEffect.FIREWORKS_EXPLODE, p.getLocation(), "type:star color:white");
        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        p.getWorld().createExplosion(p.getLocation(), 0);
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 1));
        p.damage(1);
        ev.setCancelled(true);

    }

    public static void pegaDropsExtrasDeMobs(Entity e, Player p) {
        if (!p.getInventory().getItemInMainHand().getType().name().contains("SWORD")) {
            return;
        }
        if (e instanceof Creature) {
            int level = Jobs.getJobLevel(name, p);
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
                    p.sendMessage(ChatColor.AQUA + Menu.getSimbolo(name) + " " + ChatColor.YELLOW + "Voce foi abencoado pelo deus Jabu !");
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

// !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=!

    public static final List<Skill> skillList = Arrays.asList(
            new Skill(classe, "Usar Espadas", 2, false, new String[]{"§9Aumenta a precisão com espadas.", "§9§oAumentar o nível aumenta as chances."}),
            new Skill(classe, "Usar Escudos", 5, false, new String[]{"§9Permite bloquear ataques em frente usando uma porta de ferro ou escudos."}),
            new Skill(classe, "Recompensa Divina", 4, false, new String[]{"§9Ganha items extras ao matar mobs."}),
            new Skill(classe, "Caçador de Mobs", 6, true, new String[]{"§9Ganha mais XP ao matar Monstros"}),
            new Skill(classe, "Grito de guerra", 7, true, new String[]{"§9Da um grito olhando pra cima com uma espada na mao chamando atenção de mobs."}),
            new Skill(classe, "Resistência Divina", 10, true, new String[]{"§9Recebe menos dano de ataques físicos."}),
            new Skill(classe, "Jabu está entre nós", 15, false, new String[]{"Ao levar dano há uma chance de Jabu lhe abençoar."}),
            new Skill(classe, "Luz Divina", 30, true, new String[]{"§9Pode usar Luz da Graça para evitar a morte."}));


    public enum Skills {
        Usar_Espada(skillList.get(0)),
        Usar_Escudos(skillList.get(1)),
        Recompensa_Divina(skillList.get(2)),
        Cacador_de_Mobs(skillList.get(3)),
        Grito_de_guerra(skillList.get(4)),
        Resistencia_Divina(skillList.get(5)),
        Jabu_esta_entre_nos(skillList.get(6)),
        Luz_Divina(skillList.get(7));

        public Skill skill;

        Skills(Skill skill) {
            this.skill = skill;
        }
    }

// !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=!


}
