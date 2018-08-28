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

import me.asofold.bpl.simplyvanish.SimplyVanish;
import me.asofold.bpl.simplyvanish.config.VanishConfig;
import me.fromgate.playeffect.PlayEffect;
import me.fromgate.playeffect.VisualEffect;
import nativelevel.Attributes.Mana;
import nativelevel.*;
import nativelevel.Custom.CustomItem;
import nativelevel.Custom.Items.Adaga;
import nativelevel.Custom.Items.Lock;
import nativelevel.Lang.L;
import nativelevel.Listeners.DamageListener;
import nativelevel.Listeners.GeneralListener;
import nativelevel.Menu.Menu;
import nativelevel.sisteminhas.ChaveCadiado;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.sisteminhas.KomSystem;
import nativelevel.sisteminhas.Tralhas;
import nativelevel.skills.Skill;
import nativelevel.skills.SkillMaster;
import nativelevel.utils.MetaUtils;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Thief extends KomSystem {

    public static final Jobs.Classe classe = Jobs.Classe.Ladino;
    public static final String name = "Ladino";

    public static void onHit(EntityDamageByEntityEvent ev) {

        Player attacker = (Player) ev.getDamager();
        ItemStack weapon = attacker.getInventory().getItemInMainHand();

        if (!Adaga.isAdaga(weapon)) return;

        if (SkillMaster.temSkill(attacker, Skills.Backstab.skill)) {
            if (taInvisivel(attacker) && Mana.spendMana(attacker, 35) && Tralhas.getAngle(ev.getEntity().getLocation().getDirection(), attacker.getLocation().getDirection()) <= 65) {
                ev.setDamage(ev.getDamage() + 5 + (attacker.getLevel() / 10));
                attacker.getWorld().playSound(ev.getEntity().getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1, 0.9f);
                ((LivingEntity) ev.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 0), true);
                ((LivingEntity) ev.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 25, 1), true);
            } else {
                attacker.getWorld().playSound(ev.getEntity().getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.5f, 1.2f);
                attacker.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 80, 2), true);
            }
        }

        if (weapon.getType().name().contains("GOLD")) ev.setDamage(ev.getDamage() + 0.5);

        ev.setDamage(ev.getDamage() * 1.10);

    }

    public static void noHit(EntityDamageByEntityEvent ev) {

        Player attacker = (Player) ev.getDamager();
        ItemStack weapon = attacker.getInventory().getItemInMainHand();

        if (Adaga.isAdaga(weapon)) {
            ev.setCancelled(true);
            attacker.sendMessage("§cApenas ladinos conseguem conseguem utilizar Adagas");
        }

    }

    public static void onDamaged(EntityDamageEvent ev) {

        Player damaged = (Player) ev.getEntity();

        if (ev.getCause().equals(EntityDamageEvent.DamageCause.POISON)) {
            if (!((ev.getDamage() * 1.75) >= damaged.getHealth())) ev.setDamage(ev.getDamage() * 1.75);
        }

        if (ev.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || ev.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
            if (SkillMaster.temSkill(damaged, Skills.Esquiva_Perfeita.skill)) {
                if (Jobs.rnd.nextInt(201 - damaged.getLevel()) == 1) ev.setCancelled(true);
            }
        }

    }

    public static void onDamagedSec(EntityDamageEvent ev) {

        Player damaged = (Player) ev.getEntity();

        if (ev.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || ev.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
            if (SkillMaster.temSkill(damaged, Skills.Esquiva_Perfeita.skill)) {
                if (Jobs.rnd.nextInt(351 - damaged.getLevel()) == 1) ev.setCancelled(true);
            }
        }

    }

    public static void onFlechada(EntityDamageByEntityEvent ev) {

        //TODO MELHORAR ISSO AQUI NÉ

        Player damager = DamageListener.getPlayerDamager(ev.getDamager());

        if (Jobs.getPrimarias(damager).contains("Ladino")) ev.setDamage(ev.getDamage() * 2);
        else if (Jobs.getPrimarias(damager).contains("Ladino")) ev.setDamage(ev.getDamage() * 1.3);
        else ev.setDamage(ev.getDamage() * 0.75);

    }

    public static void pegaItemAleatorio(Player p, Block bau) {
        Chest c = (Chest) bau.getState();
        if (c.getBlockInventory().getSize() == 0) {
            p.sendMessage(ChatColor.RED + L.m("Este bau está vazio !"));
            return;
        }

        if (!bau.hasMetadata("aberto")) {
            p.sendMessage(ChatColor.RED + L.m("Um ladino precisa abrir o bau com uma lockpick antes de rouba-lo !"));
            return;
        }

        if (Jobs.getJobLevel("Ladino", p) != 1) {
            p.sendMessage(ChatColor.RED + L.m("Apenas ladinos sabem furtar baus !"));
            return;
        }

        Clan donoDoBau = ClanLand.getClanAt(bau.getLocation());
        Clan meu = ClanLand.manager.getClanByPlayerUniqueId(p.getUniqueId());
        if (meu != null && donoDoBau != null) {
            int pontosPilhagem = ClanLand.getPtosPilagem(meu.getTag(), donoDoBau.getTag());
            if (pontosPilhagem < CFG.custoPegarItemRandom) {
                p.sendMessage(ChatColor.RED + L.m("Sua guilda precisa de % PPs para roubar o bau !", CFG.custoPegarItemRandom + ""));
                p.sendMessage(ChatColor.RED + L.m("Mate inimigos para ganhar pilhagem sobre eles !"));
                return;
            }
            int random = Jobs.rnd.nextInt(c.getBlockInventory().getSize());
            ItemStack sorteado = c.getBlockInventory().getItem(random);

            pontosPilhagem -= CFG.custoPegarItemRandom;
            int ct = 0;
            while (sorteado == null) {
                random = Jobs.rnd.nextInt(c.getBlockInventory().getSize());
                sorteado = c.getBlockInventory().getItem(random);
                ct++;
                if (ct > 25) {
                    break;
                }
            }

            if (sorteado == null) {
                p.sendMessage(ChatColor.RED + L.m("Voce nao conseguiu roubar nada !"));
                return;
            }

            p.sendMessage(ChatColor.GREEN + L.m("Voce enfiou a mão no bau e puxou um item ! "));
            //p.getInventory().addItem(sorteado);
            c.getWorld().dropItemNaturally(c.getLocation().add(0, 1, 0), sorteado);
            c.getBlockInventory().remove(sorteado);
            ClanLand.setPtosPilhagem(meu.getTag(), donoDoBau.getTag(), pontosPilhagem);

            ItemStack nota = new ItemStack(Material.PAPER, 1);
            ItemMeta meta = nota.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + L.m("Nota de Furto"));
            meta.setLore(Arrays.asList(new String[]{ChatColor.GREEN + L.m("Bau roubado por:"), ChatColor.YELLOW + p.getName(), ChatColor.GREEN + "Guilda:", ChatColor.YELLOW + meu.getTag()}));
            nota.setItemMeta(meta);
            if (!c.getBlockInventory().contains(nota)) {
                c.getBlockInventory().addItem(nota);
            }

        }
    }

    public static void stealFullChest(Player p, Block bau) {
        Chest c = (Chest) bau.getState();
        if (c.getBlockInventory().getSize() == 0) {
            p.sendMessage(ChatColor.RED + L.m("Este bau está vazio !"));
            return;
        }

        if (!bau.hasMetadata("aberto")) {
            p.sendMessage(ChatColor.RED + L.m("Um ladino precisa abrir o bau com uma lockpick antes de rouba-lo !"));
            return;
        }

        if (Jobs.getJobLevel("Ladino", p) != 1) {
            p.sendMessage(ChatColor.RED + L.m("Apenas ladinos sabem furtar baus !"));
            return;
        }

        Clan donoDoBau = ClanLand.getClanAt(bau.getLocation());
        Clan meu = ClanLand.manager.getClanByPlayerUniqueId(p.getUniqueId());
        if (meu != null && donoDoBau != null) {
            int pontosPilhagem = ClanLand.getPtosPilagem(meu.getTag(), donoDoBau.getTag());
            if (pontosPilhagem < CFG.custoPegarBau) {
                p.sendMessage(ChatColor.RED + L.m("Sua guilda precisa de % PPs para roubar o bau !", CFG.custoPegarBau + ""));
                p.sendMessage(ChatColor.RED + L.m("Mate inimigos para ganhar pilhagem sobre eles !"));
                return;
            }
            int random = Jobs.rnd.nextInt(c.getBlockInventory().getSize());
            ItemStack sorteado = c.getBlockInventory().getItem(random);

            pontosPilhagem -= CFG.custoPegarBau;
            int ct = 0;

            p.sendMessage(ChatColor.GREEN + L.m("Voce sugou os items do bau ! ! "));

            for (ItemStack ss : c.getBlockInventory().getContents()) {
                if (ss != null) {
                    c.getWorld().dropItemNaturally(c.getLocation().add(0, 1, 0), ss);
                }
            }
            c.getBlockInventory().clear();

            ClanLand.setPtosPilhagem(meu.getTag(), donoDoBau.getTag(), pontosPilhagem);
            ItemStack nota = Thief.getNota(p);
            if (!c.getBlockInventory().contains(nota)) {
                c.getBlockInventory().addItem(nota);
            }
            p.getInventory().getItemInMainHand().setDurability((short) (p.getInventory().getItemInMainHand().getDurability() + 80));
        }
    }

    private static class Lockpicker implements Runnable {

        public UUID player;
        public Chest chest;
        public int step;
        public int id;
        public int dif;

        @Override
        public void run() {

            Player p = Bukkit.getPlayer(player);
            if (p == null) {
                cancel();
            } else {

                if (!p.getWorld().getName().equalsIgnoreCase(chest.getWorld().getName()) || p.getLocation().distance(chest.getLocation()) > 3) {
                    cancel();
                    p.sendMessage(ChatColor.RED + "Voce ficou muito longe do baú");
                }

                if (step > 6) {

                    if (Jobs.hasSuccess(dif, "Ladino", p)) {

                        MetaShit.setMetaString("aberto", chest, "sim");

                        boolean quebrou = false;
                        ItemStack[] coisas = chest.getBlockInventory().getContents();
                        for (int x = 0; x < coisas.length; x++) {
                            ItemStack ss = chest.getBlockInventory().getContents()[x];
                            if (ss != null) {
                                if (ss.getType() == Material.OBSERVER) {
                                    CustomItem item = CustomItem.getItem(ss);
                                    // it has a lock !
                                    if (item != null && item instanceof Lock) {
                                        coisas[x] = new ItemStack(Material.AIR);
                                        quebrou = true;
                                    }
                                }
                            }
                        }
                        p.sendMessage(Menu.getSimbolo("Ladino") + ChatColor.GREEN + L.m("Voce conseguiu quebrar o trinco do baú !"));

                        ItemStack nota = Thief.getNota(p);

                        chest.getBlockInventory().setContents(coisas);

                        if (!chest.getBlockInventory().contains(nota)) {
                            chest.getBlockInventory().addItem(nota);
                        }
                        cancel();
                    } else {
                        p.sendMessage(Menu.getSimbolo("Ladino") + ChatColor.RED + L.m("Voce nao conseguiu abrir o baú !"));
                    }
                    cancel();
                } else {
                    step++;
                    p.sendMessage(ChatColor.GREEN + "Voce está tentando abrir o baú...");
                    p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
                    KoM.efeitoBlocos(chest.getBlock(), Material.CHEST);
                }

            }

        }

        public void cancel() {
            Bukkit.getScheduler().cancelTask(id);
            Player p = Bukkit.getPlayer(player);
            if (p != null) {
                p.removeMetadata("arrombando", KoM._instance);
            }
        }

    }

    public static void zoiudo(Player p, Chest chest) {

        int lockLevel = ChaveCadiado.getLockLevel(chest.getBlock());

        if (lockLevel > 0) {
            p.sendMessage(ChatColor.RED + "Este baú possui uma tranca.");
            if (Jobs.getJobLevel(Jobs.Classe.Ladino, p) == Jobs.TipoClasse.PRIMARIA) {
                p.sendMessage(ChatColor.RED + L.m("Voce pode tentar arrombar baús com lockpicks !!"));
            }
            return;
        }

        Inventory bauIlusorio = Bukkit.createInventory(p, chest.getBlockInventory().getSize(), L.m("Zoiudo !"));
        for (int i = 0; i < chest.getBlockInventory().getSize(); i++) {
            if (chest.getBlockInventory().getItem(i) == null) {
                continue;
            }
            ItemStack item = chest.getBlockInventory().getItem(i).clone();
            if (item != null) {
                MetaUtils.appendLore(item, ChatColor.RED + "!");
                bauIlusorio.setItem(i, item);
            }
        }
        p.openInventory(bauIlusorio);
    }

    public static void bisbilhota(Player p, Block b) {

        if (p.hasMetadata("arrombando")) {
            p.sendMessage(ChatColor.RED + "Voce ja está arrombando o baú, aguarde terminar.");
            return;
        }

        if (b.getType() == Material.CHEST) {
            Chest c = (Chest) b.getState();
            int dif = 5;
            boolean gasta = true;
            int lvl = Jobs.getJobLevel("Ladino", p);
            if (ClanLand.permission.has(p, "kom.verbaus")) {
                lvl = 1;
                dif = 0;
                gasta = false;
            }
            if (lvl == 0) {
                p.sendMessage(Menu.getSimbolo("Ladino") + ChatColor.RED + L.m("Apenas ladinos podem usar isto !"));
                return;
            }

            if (!Mana.spendMana(p, 40)) {
                return;
            }

            int lockLevel = ChaveCadiado.getLockLevel(b);
            if (lockLevel != 0) {
                dif = lockLevel;
            }

            if (p.getInventory().getItemInMainHand().getAmount() > 1) {
                p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
            } else {
                p.setItemInHand(null);
            }

            Lockpicker lock = new Lockpicker();
            MetaShit.setMetaObject("arrombando", p, 1);
            lock.chest = c;
            lock.dif = dif;
            lock.player = p.getUniqueId();
            lock.step = 0;
            lock.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(KoM._instance, lock, 20 * 2, 20 * 2);
            p.sendMessage(ChatColor.GREEN + "Voce começou a tentar arrombar o baú.");

        }
    }

    public static ItemStack getNota(Player p) {
        ItemStack nota = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = nota.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + L.m("Nota de Furto"));
        meta.setLore(Arrays.asList(new String[]{ChatColor.GREEN + L.m("Bau roubado por:"), ChatColor.YELLOW + p.getName()}));
        nota.setItemMeta(meta);
        return nota;
    }

    public static String getLadrao(ItemStack nota) {
        if (nota == null) {
            return null;
        }
        ItemMeta meta = nota.getItemMeta();
        if (meta.getDisplayName() != null && ChatColor.stripColor(meta.getDisplayName()).equalsIgnoreCase("Nota de Furto")) {
            if (meta != null) {
                List<String> lore = meta.getLore();
                if (lore != null && lore.size() >= 2) {
                    return ChatColor.stripColor(lore.get(1));
                }
            }
        }
        return null;
    }

    @EventHandler
    public void invClick(InventoryClickEvent ev) {
        if (ev.getCurrentItem() != null && ev.getCurrentItem().getType() == Material.PAPER) {
            String ladrao = getLadrao(ev.getCurrentItem());
            if (ladrao != null && ladrao.equalsIgnoreCase(ev.getWhoClicked().getName())) {
                ev.setCancelled(true);
            }
        }
    }

    public static void desviaTiro(Entity p, int qtd) {
        Vector destino = p.getVelocity();
        if (Jobs.rnd.nextBoolean()) {
            destino.add(new Vector(Jobs.rnd.nextInt(qtd) - (qtd / 2) / 10, Jobs.rnd.nextInt(qtd) - (qtd / 2) / 10, Jobs.rnd.nextInt(qtd) - (qtd / 2) / 10));
        } else {
            destino.subtract(new Vector(Jobs.rnd.nextInt(qtd) - (qtd / 2) / 10, Jobs.rnd.nextInt(qtd) - (qtd / 2) / 10, Jobs.rnd.nextInt(qtd) - (qtd / 2) / 10));
        }
        p.setVelocity(destino);
    }

    public static boolean taInvisivel(Player p) {
        VanishConfig cfg = SimplyVanish.getVanishConfig(p.getName(), true);
        // KnightsOfMinecraft.log.info("VANISHED: "+p.getName()+" "+cfg.vanished.state);
        return (SimplyVanish.isVanished(p) || cfg.vanished.state);

    }

    public static void ficaInvisivel(final Player p, int tempo) {
        PlayEffect.play(VisualEffect.SMOKE, p.getLocation(), "num:1");
        int idRevela = Bukkit.getScheduler().scheduleSyncDelayedTask(KoM._instance, new Runnable() {
            public void run() {
                if (SimplyVanish.isVanished(p) && !p.isOp()) {
                    SimplyVanish.setVanished(p, false);
                    p.removePotionEffect(PotionEffectType.INVISIBILITY);
                    p.sendMessage(ChatColor.RED + L.m("Voce se revelou !"));
                }
            }
        }, tempo);
        SimplyVanish.setVanished(p, true);
        MetaShit.setMetaObject("idRevela", p, idRevela);
        VanishConfig cfg = SimplyVanish.getVanishConfig(p.getName(), true);
        cfg.damage.state = true;
        cfg.notify.state = false;
        cfg.interact.state = true;
        cfg.attack.state = true;
        cfg.target.state = true;
        cfg.vanished.state = true;
        SimplyVanish.setVanishConfig(p.getName(), cfg, true);
        //SimplyVanish.setVanishConfig(null, null, true);
        //p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, tempo, 1));
    }

    public static void revela(Player p) {

        if (!taInvisivel(p)) {
            return;
        }

        try {
            if (ClanLand.permission.has(p, "vanish.vanish")) {
                return;
            }
            if (!p.isOp()) {

                if (p.hasMetadata("idRevela")) {

                    int task = (int) MetaShit.getMetaObject("idRevela", p);
                    Bukkit.getScheduler().cancelTask(task);
                }
                if (Thief.taInvisivel(p)) {
                    PlayEffect.play(VisualEffect.SPELL, p.getLocation(), "num:1");
                    p.sendMessage(ChatColor.AQUA + Menu.getSimbolo("Ladino") + " " + ChatColor.RED + L.m("Voce foi revelado !"));
                }
                SimplyVanish.setVanished(p, false);
                VanishConfig cfg = SimplyVanish.getVanishConfig(p.getName(), true);
                cfg.notify.state = false;
                cfg.vanished.state = false;
                SimplyVanish.setVanished(p, false);
                SimplyVanish.setVanishConfig(p.getName(), cfg, true);
                // SimplyVanish.setVanished(p, false);
            }
            p.removeMetadata("idRevela", KoM._instance);
        } catch (Exception e) {
            KoM.log.info("ERRO NO VANISH: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void atiraFlecha(Player p, Projectile flecha) {
        if (!(flecha.getShooter() instanceof Player)) return;

        if (taInvisivel(p))
            if (Jobs.rnd.nextInt(5) == 1) Thief.revela(p);
            else p.spawnParticle(Particle.SMOKE_LARGE, p.getLocation(), 3);

        //AttributeInfo info = KnightsOfMania.database.getAtributos(p);
        //MetaShit.setMetaObject("modDano", flecha, (Attributes.calcArcheryDamage(info.attributes.get(Attr.dexterity))));
        if (Jobs.getJobLevel("Ladino", p) != 1)
            if (!Jobs.hasSuccess(65, "Ladino", p)) Thief.desviaTiro(flecha, 2);


        if (p.getLevel() < 10) GeneralListener.givePlayerExperience(1, p);

        //p.getWorld().spawn(p.getLocation(), ExperienceOrb.class).setExperience(1);
    }

    public static void throwEnderPearl(PlayerInteractEvent ev) {
        if (!Mana.spendMana(ev.getPlayer(), 35)) {
            ev.setCancelled(true);
            return;
        }
        if (ev.getPlayer().getWorld().getName().equalsIgnoreCase(CFG.mundoDungeon)) {
            ev.getPlayer().sendMessage(ChatColor.AQUA + Menu.getSimbolo("Ladino") + " " + ChatColor.GOLD + L.m("Aqui nao"));
            ev.setCancelled(true);
            return;
        }
        int arc = Jobs.getJobLevel("Ladino", ev.getPlayer());
        if (arc != 1) {
            ev.getPlayer().sendMessage(ChatColor.AQUA + Menu.getSimbolo("Ladino") + " " + ChatColor.GOLD + L.m("Voce falhou em usar a Ender Pearl !"));
            ev.setCancelled(true);
            return;
        }
    }

// !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=!

    public static final List<Skill> skillList = Arrays.asList(
            new Skill(classe,"Esquiva Perfeita", 2, false, new String[]{"§9Aumenta chance de esquiva a ataques."}),
            new Skill(classe,"Pé de Cabra", 5, true, new String[]{"§9Permite arrombar e roubar items de baús inimigos"}),
            new Skill(classe,"Caçador de Mobs", 6, false, new String[]{"§9Ganha mais XP ao matar Monstros"}),
            new Skill(classe,"Ender Pearl", 7, true, new String[]{"§9Permite usar perolas do Ender."}),
            new Skill(classe,"Sniper Shot", 9, true, new String[]{"§9Se atirar a longa distância causa grande dano"}),
            new Skill(classe,"Mira com Arcos", 10, true, new String[]{"§9Aumenta a precisão com arcos.", "Aumentar o nível aumenta as chances."}),
            new Skill(classe,"Backstab", 13, true, new String[]{"§9Pode atacar por traz para causar grande dano."}),
            new Skill(classe,"Bomba de Fumaça", 15, false, new String[]{"§9Permite usar bomba de fumaça para ficar invisivel."}),
            new Skill(classe,"Lockpick", 20, true, new String[]{"§9Pode espiar baús de guildas inimigas"})
    );

    public enum Skills {
        Esquiva_Perfeita(skillList.get(0)),
        Pe_de_Cabra(skillList.get(1)),
        Cacador_de_Mobs(skillList.get(2)),
        Ender_Pearl(skillList.get(3)),
        Sniper_Shot(skillList.get(4)),
        Mira_com_Arcos(skillList.get(5)),
        Backstab(skillList.get(6)),
        Bomba_de_Fumaca(skillList.get(7)),
        Lockpick(skillList.get(8));

        public Skill skill;

        Skills(Skill skill) {
            this.skill = skill;
        }

    }

// !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=!

}
