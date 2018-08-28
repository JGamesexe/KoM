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
import nativelevel.Attributes.Stamina;
import nativelevel.CustomEvents.BlockHarvestEvent;
import nativelevel.Jobs;
import nativelevel.KoM;
import nativelevel.Lang.L;
import nativelevel.Menu.Menu;
import nativelevel.MetaShit;
import nativelevel.bencoes.TipoBless;
import nativelevel.sisteminhas.XP;
import nativelevel.skills.Skill;
import nativelevel.spec.PlayerSpec;
import nativelevel.utils.Fireworks;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

public class Lumberjack {

    public static final Jobs.Classe classe = Jobs.Classe.Lenhador;

    public static final Material[] dropsBonus = {Material.SULPHUR, Material.GOLD_INGOT, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.GOLDEN_APPLE, Material.GLOWSTONE_DUST};

    public static void onHit(EntityDamageByEntityEvent ev) {

        Player attacker = (Player) ev.getDamager();
        ItemStack weapon = attacker.getInventory().getItemInMainHand();

        if (weapon.getType().toString().contains("_AXE")) {

            if (attacker.hasMetadata("machadadaEpica")) {
                long dife = (System.currentTimeMillis() - ((long) MetaShit.getMetaObject("machadadaEpica", attacker)));
                if (dife >= 4000 && dife <= 5750) {

                    if (ev.getEntity() instanceof Player) {
                        if (attacker.getLocation().add(0, -0.1, 0).getBlock().getType().isSolid()) ev.setDamage(ev.getDamage() * 5.0);
                        else ev.setDamage(ev.getDamage() * 3.3);
                    } else {
                        ev.setDamage(ev.getDamage() * 2.6);
                    }

                    Fireworks.doFirework(ev.getEntity().getLocation(), FireworkEffect.Type.STAR, Color.ORANGE, Color.ORANGE, 3);
                    attacker.sendMessage("§6Você acerta a Machadada Épica!");

                } else if (dife < 4000) {
                    attacker.sendMessage("§cVocê afobou sua machada epica.");
                }
                attacker.removeMetadata("machadadaEpica", KoM._instance);
            }

            if (weapon.getType().name().contains("GOLD")) ev.setDamage(ev.getDamage() + 0.5);

            ev.setDamage(ev.getDamage() + (ev.getDamage() * 0.10));

        }
    }

    public static void preparaMachadada(Player p) {

        // 40 Ticks para preparar   2000
        // 40 Preparando            2000
        // 35 Pra acertar           1750
        //                          5750

        if (p.hasMetadata("machadadaEpica")) {
            long dife = (System.currentTimeMillis() - ((long) MetaShit.getMetaObject("machadadaEpica", p)));
            if (dife < 5850) return;
        }

        if (!Stamina.spendStamina(p, 40)) return;

        MetaShit.setMetaObject("machadadaEpica", p, System.currentTimeMillis());

        if (cancelMachadada(p)) return;

        p.sendMessage("§aVocê segurou firmemente o machado e o colocou de lado");

        if (!p.hasPotionEffect(PotionEffectType.SPEED)) p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));

        Runnable last = () -> {

            if (cancelMachadada(p)) return;
            p.sendMessage("§cO machado ja se abaixou, voce passou do ponto...");

        };

        Runnable hitTime = () -> {

            if (cancelMachadada(p)) return;
            p.sendMessage("§aVoce coloca força no machado e joga seu corpo de lado ! A machada epica esta pronta !");

            Bukkit.getScheduler().runTaskLater(KoM._instance, last, 35);

        };

        Runnable middle = () -> {

            if (cancelMachadada(p)) return;
            p.sendMessage("§aVoce levanta o machado e se prepara para a machadada epica !");

            for (Entity e : p.getNearbyEntities(8, 2, 8))
                if (e.getType() == EntityType.PLAYER) e.sendMessage("§6" + p.getName() + " esta levantando o machado lentamente !");

            Bukkit.getScheduler().runTaskLater(KoM._instance, hitTime, 40);

        };

        Bukkit.getScheduler().runTaskLater(KoM._instance, middle, 40);

    }

    private static boolean cancelMachadada(Player p) {
        if (!p.isOnline()) return true;
        if (!p.hasMetadata("machadadaEpica")) return true;
        if (p.getLocation().getBlock() != null && p.getLocation().getBlock().getType() == Material.WEB) {
            if (p.hasPotionEffect(PotionEffectType.SPEED)) p.removePotionEffect(PotionEffectType.SPEED);
            p.sendMessage("§₢Voce se enrosca nas teias e nao consegue preparar a Machadada Épica.");
            return true;
        }
        ItemStack off = p.getInventory().getItemInOffHand();
        if (off != null && off.getType() != Material.AIR) {
            p.sendMessage(ChatColor.RED + "Voce precisa das 2 mãos para preparar a Machadada Épica.");
            return true;
        }
        return false;
    }

//    public static void preparaMachadadaEpica(final Player p) {
//
//        if (p.hasMetadata("epichax")) {
//            return;
//        }
//        if (Jobs.getJobLevel("Lenhador", p) != 1) {
//            return;
//        }
//
//        int custo = 35;
//        int modTempo = 0;
//        boolean cansa = false;
//        if (PlayerSpec.temSpec(p, PlayerSpec.Barbaro)) {
//            modTempo = -20;
//            custo = 45;
//        }
//        final int finalMod = modTempo;
//
//        if (!Stamina.spendStamina(p, custo)) {
//            return;
//        }
//
//        ItemStack off = p.getInventory().getItemInOffHand();
//        if (off != null && off.getType() != Material.AIR) {
//            p.sendMessage(ChatColor.RED + "Voce precisa das 2 mãos para conseguir usar a Machadada Épica");
//            return;
//        }
//
//        if (Thief.taInvisivel(p)) {
//            Thief.revela(p);
//        }
//
//        p.sendMessage(ChatColor.GREEN + L.m("Voce segurou firmemente o machado e o colocou de lado"));
//
//        if (!p.hasPotionEffect(PotionEffectType.SPEED)) {
//            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5 + finalMod, 0));
//        }
//
//        final Runnable r3 = new Runnable() {
//            public void run() {
//                if (p.hasMetadata("epichax")) {
//                    p.sendMessage(ChatColor.RED + L.m("O machado ja se abaixou, voce passou do ponto..."));
//                }
//                p.removeMetadata("epichaxpronta", KoM._instance);
//                p.removeMetadata("epichax", KoM._instance);
//                if (p.hasPotionEffect(PotionEffectType.SPEED)) {
//                    p.removePotionEffect(PotionEffectType.SPEED);
//                }
//            }
//        };
//
//        final Runnable r2 = new Runnable() {
//            public void run() {
//                if (p.getLocation().getBlock().getType() == Material.WEB) {
//                    if (p.hasPotionEffect(PotionEffectType.SPEED)) {
//                        p.removePotionEffect(PotionEffectType.SPEED);
//                    }
//                    p.sendMessage(ChatColor.RED + "Voce se enrosca nas teias e nao consegue preparar a machadada");
//                    p.removeMetadata("epichaxpronta", KoM._instance);
//                    p.removeMetadata("epichax", KoM._instance);
//                    return;
//                }
//                p.sendMessage(ChatColor.GREEN + "Voce coloca força no machado e joga seu corpo de lado ! A machada epica esta pronta !");
//                int id = Bukkit.getScheduler().scheduleSyncDelayedTask(KoM._instance, r3, 35 + finalMod);
//                MetaShit.setMetaObject("epichax", p, id);
//                MetaShit.setMetaObject("epichaxpronta", p, 0);
//            }
//        };
//
//        final Runnable r = new Runnable() {
//            public void run() {
//                if (p.getLocation().getBlock().getType() == Material.WEB) {
//                    if (p.hasPotionEffect(PotionEffectType.SPEED)) {
//                        p.removePotionEffect(PotionEffectType.SPEED);
//                    }
//                    p.sendMessage(ChatColor.RED + "Voce se enrosca nas teias e nao consegue preparar a machadada");
//                    p.removeMetadata("epichaxpronta", KoM._instance);
//                    p.removeMetadata("epichax", KoM._instance);
//                    return;
//                }
//                p.sendMessage(ChatColor.GREEN + "Voce levanta o machado e se prepara para a machadada epica !");
//                for (Entity e : p.getNearbyEntities(8, 2, 8)) {
//                    if (e.getType() == EntityType.PLAYER) {
//                        ((Player) e).sendMessage(ChatColor.GOLD + p.getName() + " esta levantando o machado lentamente !");
//                    }
//                }
//                int id = Bukkit.getScheduler().scheduleSyncDelayedTask(KoM._instance, r2, 40 + finalMod);
//                MetaShit.setMetaObject("epichax", p, id);
//            }
//        };
//
//        if (p.getLocation().getBlock().getType() == Material.WEB) {
//            if (p.hasPotionEffect(PotionEffectType.SPEED)) {
//                p.removePotionEffect(PotionEffectType.SPEED);
//            }
//            p.sendMessage(ChatColor.RED + "Voce se enrosca nas teias e nao consegue preparar a machadada");
//            p.removeMetadata("epichaxpronta", KoM._instance);
//            p.removeMetadata("epichax", KoM._instance);
//            return;
//        }
//        int sched = Bukkit.getScheduler().scheduleSyncDelayedTask(KoM._instance, r, 40 + finalMod);
//        MetaShit.setMetaObject("epichax", p, sched);
//    }

    public static void salta(PlayerInteractEvent ev) {
        if (ev.getPlayer().getLocation().getBlock().getType() == Material.STATIONARY_WATER || ev.getPlayer().isSneaking() || ev.getPlayer().getWorld().getName().equalsIgnoreCase("NewDungeon")) {
            return;
        }

        if (ev.getPlayer().getInventory().getItemInMainHand().getType() == Material.IRON_AXE || ev.getPlayer().getInventory().getItemInMainHand().getType() == Material.GOLD_AXE || ev.getPlayer().getInventory().getItemInMainHand().getType() == Material.DIAMOND_AXE || ev.getPlayer().getInventory().getItemInMainHand().getType() == Material.WOOD_AXE || ev.getPlayer().getInventory().getItemInMainHand().getType() == Material.STONE_AXE) {

            if (!Stamina.spendStamina(ev.getPlayer(), 25)) {
                return;
            }

            PlayEffect.play(VisualEffect.SMOKE_LARGE, ev.getPlayer().getLocation(), "num:1");
            Vector jumpDir = ev.getPlayer().getLocation().getDirection().normalize().multiply(3);
            jumpDir.divide(new Vector(1, 5, 1));
            jumpDir.setY(0.6D);

            ev.getPlayer().setVelocity(jumpDir);
        }
    }

    public static void corta(BlockHarvestEvent ev) {
        ev.getPlayer().playSound(ev.getBlock().getLocation(), Sound.ENTITY_IRONGOLEM_ATTACK, 1, 1);

        TipoBless ativo = TipoBless.save.getTipo(ev.getPlayer());
        if (ativo != null && ativo == TipoBless.Serralheria) {
            int exp = XP.getExpPorAcao(ev.getHarvestable().difficulty);
            exp = exp * 10;
            XP.changeExp(ev.getPlayer(), exp, 1);
        }

        if (Jobs.rnd.nextInt(10000) == 1) {
            ev.getPlayer().sendMessage(ChatColor.GREEN + "Ao dar a cortada final, farpas da madeira saltaram e pegaram no seu olho");
            ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 60, 2));
        }
    }

// !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=!

    public static final List<Skill> skillList = Arrays.asList(
            new Skill(classe, "Coletor de Boas Madeiras", 2, false, new String[]{"§9Coleta recursos extras cortando madeira."}),
            new Skill(classe, "Salto do Lenhador", 9, true, new String[]{"§9Permite dar um salto a frente."}),
            new Skill(classe, "Machadada Epica", 10, true, new String[]{"§9Carrega um machado para um golpe mortal."}),
            new Skill(classe, "Criar Tabuas", 13, true, new String[]{"§9Aumenta chances de sucesso criar tabuas.", "§9Quanto maior seu nivel, maior a chance"}, true),
            new Skill(classe, "Cortar Madeira", 15, false, new String[]{"&9Aumenta chances de sucesso cortar madeira.", "§9Quanto maior seu nivel, maior a chance"})
    );

    public enum Skills {
        Coletor_de_Boas_Madeiras(skillList.get(0)),
        Salto_do_Lenhador(skillList.get(1)),
        Machadada_Epica(skillList.get(2)),
        Criar_Tabuas(skillList.get(3)),
        Cortar_Madeira(skillList.get(4));

        public Skill skill;

        Skills(Skill skill) {
            this.skill = skill;
        }
    }

    // !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=!

//     public static int pegaDificuldadeDe(Material m) {
//     if (m == Material.LOG) {
//     return 25;
//     } else if (m == Material.WOOD) {
//     return 38;
//     } else if (m == Material.LOG_2) {
//     return 70;
//     }
//     return 0;
//     }
//
//     public static int pegaExpDe(Material m, Player p) {
//     int diff = Lumberjack.pegaDificuldadeDe(m);
//     if (m == Material.LOG) {
//     return XP.getExpPorAcao(p.getLevel());
//     } else if (m == Material.WOOD) {
//     return XP.getExpPorAcao(p.getLevel());
//     } else if (m == Material.LOG_2) {
//     return XP.getExpPorAcao(p.getLevel());
//     }
//     return 0;
//     }
//
//    public static boolean cortaLenha(Player p, Block b, int exp) {
//
//        if (b.hasMetadata("playerpois")) {
//            exp = 0;
//        }
//        //int dificuldade = Lumberjack.pegaDificuldadeDe(b.getType());
//
//
//
//        /*
//         if (sucess == Jobs.fail) {
//         b.setType(Material.AIR);
//         p.sendMessage(ChatColor.AQUA + Menu.getSimbolo("Lenhador") + " " + ChatColor.RED + "Voce nao conseguiu cortar a madeira corretamente.");
//         return false;
//         } else {
//         RankDB.addPontoCache(p, Estatistica.LENHADOR, exp);
//         if (!b.hasMetadata("playerpois") && sucess == Jobs.BONUS && Jobs.rnd.nextInt(10) == 1 || (ativo != null && ativo == TipoBless.Serralheria)) {
//         ItemStack drop = new ItemStack(dropsBonus[Jobs.rnd.nextInt(dropsBonus.length)], 1);
//         p.getWorld().dropItem(b.getLocation(), drop);
//         ItemStack madeira = new ItemStack(Material.WOOD, 1);
//         //madeira.setData();
//         p.getWorld().dropItem(b.getLocation(), madeira);
//         p.sendMessage(ChatColor.AQUA + Menu.getSimbolo("Lenhador") + " " + ChatColor.GOLD + "Voce extraiu alguns recursos extras da arvore !");
//         }
//         }
//         if (sucess != Jobs.OVERLEVEL && sucess != Jobs.BONUS) {
//         GeneralListener.givePlayerExperience(exp + (p.getLevel() / 15), p);
//         }
//         return true;
//         */
//
//        return true;
//    }
//
//    /*
//    public static void fazCustomItem(InventoryClickEvent event, ItemStack queFiz) {
//        Player p = ((Player) event.getWhoClicked());
//        if (event.getCursor() != null && event.getCursor().getAmount() > 63) {
//            return;
//        }
//        int dificuldade = 0;
//        //if(queFiz instanceof Tabua)
//        //    dificuldade = 55;
//        //if(queFiz instanceof DeedTorre)
//        //    dificuldade = 75;
//        int suc = Jobs.hasSuccess(dificuldade, "Lenhador", p);
//        if (suc == Jobs.fail) {
//            p.sendMessage(ChatColor.RED + PT.craft_fail);
//            event.setCurrentItem(new ItemStack(Material.STICK, 1));
//            return;
//        }
//        p.sendMessage(ChatColor.GOLD + PT.craft_sucess);
//    }
//    */
//
//    /*
//    public static void transformaTabuas(InventoryClickEvent event, double exp) {
//        Player p = ((Player) event.getWhoClicked());
//        if (event.getCursor() != null && event.getCursor().getAmount() > 63) {
//            return;
//        }
//        int dificuldade = 2;
//
//        TipoBless ativo = TipoBless.save.getTipo(p);
//        if (ativo != null && ativo == TipoBless.Serralheria) {
//            exp = exp * 5;
//            dificuldade = 0;
//        }
//
//        int qtd = 1;
//        int sucesses = 0;
//        int jobLvl = Jobs.getJobLevel("Lenhador", p);
//        if (jobLvl == 1) {
//            qtd = 4;
//        } else if (jobLvl == 2) {
//            qtd = 3;
//            exp = exp * 0.75;
//        } else {
//            exp = exp * 0.25;
//        }
//
//        for (int x = 0; x < qtd; x++) {
//            int suc = Jobs.hasSuccess(dificuldade, "Lenhador", p);
//            if (suc != Jobs.fail) {
//                sucesses += 1;
//            }
//        }
//
//        GeneralListener.givePlayerExperience(exp, p);
//        if (sucesses == 0) {
//            p.sendMessage(ChatColor.AQUA + Menu.getSimbolo("Lenhador") + " " + ChatColor.RED + "Voce falhou ao cortar as madeiras !");
//            event.setCurrentItem(new ItemStack(Material.STICK, 1));
//            return;
//        }
//
//        String plural = sucesses > 1 ? "s" : "";
//
//        if (ativo != null && ativo == TipoBless.Serralheria) {
//            sucesses += 1;
//        }
//
//        event.getCurrentItem().setAmount(sucesses);
//        p.sendMessage(ChatColor.AQUA + Menu.getSimbolo("Lenhador") + " " + ChatColor.GREEN + "Voce conseguiu cortar " + ChatColor.GOLD + sucesses + ChatColor.GREEN + " madeira" + plural + " corretamente !");
//    }
//    */

}
