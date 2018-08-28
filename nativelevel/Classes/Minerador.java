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
import nativelevel.Attributes.Stamina;
import nativelevel.CFG;
import nativelevel.CustomEvents.BlockHarvestEvent;
import nativelevel.Equipment.EquipManager;
import nativelevel.Equipment.ItemAttributes;
import nativelevel.Jobs;
import nativelevel.KoM;
import nativelevel.Menu.Menu;
import nativelevel.MetaShit;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.skills.Skill;
import nativelevel.skills.SkillMaster;
import nativelevel.spec.PlayerSpec;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class Minerador {

    public static final Jobs.Classe classe = Jobs.Classe.Minerador;

    public static void onHit(EntityDamageByEntityEvent ev) {
        Player attacker = (Player) ev.getDamager();
        ItemStack weapon = attacker.getInventory().getItemInMainHand();

        if (weapon.getType().toString().contains("PICKAXE")) {

            if (weapon.getType().name().contains("GOLD")) ev.setDamage(ev.getDamage() + 0.5);

            ev.setDamage(ev.getDamage() + (ev.getDamage() * 0.10));

        }

    }

    public static void axaItems(BlockBreakEvent ev) {
        int lvl = Jobs.getJobLevel("Minerador", ev.getPlayer());
        if (lvl == 1) {
            int random = Jobs.rnd.nextInt(100);
            if (random <= 1) {
                ev.getPlayer().getWorld().dropItemNaturally(ev.getBlock().getLocation(), new ItemStack(Material.SULPHUR, 1));
            } else if (random == 2) {
                ev.getPlayer().getWorld().dropItemNaturally(ev.getBlock().getLocation(), new ItemStack(Material.MOSSY_COBBLESTONE, 1));
            } else if (random == 9) {
                ev.getPlayer().getWorld().dropItemNaturally(ev.getBlock().getLocation(), new ItemStack(Material.GLOWSTONE_DUST, 1));
            }
        }
    }

    public static Material materiaPrima(Material m) {
        if (m == Material.IRON_ORE) {
            return Material.IRON_INGOT;
        }
        if (m == Material.COAL_ORE) {
            return Material.COAL;
        }
        if (m == Material.GOLD_ORE) {
            return Material.GOLD_INGOT;
        }
        return null;
    }

    private static final PotionEffect escala = new PotionEffect(PotionEffectType.LEVITATION, 11, 1, false, false);

    public static void escala(PlayerInteractEvent ev) {
        if (ev.getPlayer().getWorld().getName().equalsIgnoreCase(CFG.mundoDungeon)) return;
        if (!ev.getHand().equals(EquipmentSlot.HAND)) return;

        if (SkillMaster.temSkill(ev.getPlayer(), Skills.Escalar.skill)) {
            if (ev.getAction() == Action.RIGHT_CLICK_BLOCK) {

                Location playerLoc = ev.getPlayer().getLocation();
                playerLoc.setY(0);

                Location blockLoc = ev.getClickedBlock().getLocation();
                double x = blockLoc.getX() + 0.5;
                double z = blockLoc.getZ() + 0.5;

                float looking = ((ev.getPlayer().getLocation().getYaw() - 90) % 360);
                if (looking < 0) looking += 360;

//              KoM.announce("Looking " + looking);

                if (looking >= 315 || looking <= 45) z = playerLoc.getZ(); //WEST
                else if (looking >= 45 && looking <= 135) x = playerLoc.getX(); //NORTH
                else if (looking >= 135 && looking <= 225) z = playerLoc.getZ(); //EAST
                else x = playerLoc.getX(); //SHOUT

                blockLoc.setX(x);
                blockLoc.setY(0);
                blockLoc.setZ(z);

                double distance = playerLoc.distance(blockLoc);

//              KoM.announce("Distancia [" + distance + "]" + " (" + x + ")" + " (" + z + ")");

                if (distance > 0.85) return;

                if (ev.getClickedBlock().getRelative(BlockFace.UP).getType() == Material.AIR) return;
                if (ev.getClickedBlock().getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType() == Material.AIR) return;

                if (Stamina.spendStamina(ev.getPlayer(), 1)) {
                    if (Jobs.dado(10) == 10) return;
                    ev.getPlayer().addPotionEffect(escala, true);

                    if (Jobs.dado(3) != 3) return;

                    blockLoc.setY(ev.getPlayer().getLocation().getY());
                    blockLoc.getWorld().spawnParticle(Particle.BLOCK_CRACK, blockLoc, 15, Jobs.rnd.nextDouble(), Jobs.rnd.nextDouble(), Jobs.rnd.nextDouble(), new MaterialData(blockLoc.getBlock().getType()));
                    blockLoc.getWorld().playSound(blockLoc, Sound.BLOCK_ANVIL_BREAK, 0.2f, 0.85f);

                    ItemStack mainHand = ev.getPlayer().getInventory().getItemInMainHand();
                    ItemStack offHand = ev.getPlayer().getInventory().getItemInOffHand();

                    mainHand.setDurability((short) (mainHand.getDurability() + 1));
                    offHand.setDurability((short) (offHand.getDurability() + 1));

                    if (mainHand.getDurability() >= mainHand.getType().getMaxDurability()) ev.getPlayer().getInventory().setItemInMainHand(null);
                    if (offHand.getDurability() >= offHand.getType().getMaxDurability()) ev.getPlayer().getInventory().setItemInOffHand(null);

                }
            }
        }
    }

    public static void cobrePicareta(InventoryClickEvent ev) {
        if (ev.getCursor() == null || ev.getCursor().getType() != Material.FLINT) return;
        if (ev.getCurrentItem() == null || !ev.getCurrentItem().getType().name().contains("_PICKAXE")) return;
        ev.setCancelled(true);

        ItemStack pick = ev.getCurrentItem();
        ItemMeta pickMeta = pick.getItemMeta();

        ItemStack flint = ev.getCursor();
        if (flint.getAmount() == 1) flint = null;
        else flint.setAmount(flint.getAmount() - 1);

        ev.getWhoClicked().setItemOnCursor(flint);

        List<String> lore = pickMeta.getLore() != null ? pickMeta.getLore() : new ArrayList<>();

        int silexIndex = whereSilexLore(lore);
        if (silexIndex >= 0) {
            lore.set(silexIndex, " §7Esta picareta está coberta de Sílex §0" + (quantoSilex(lore) + 1));
        } else {
            lore.add(ItemAttributes.lastSlot(lore), "");
            lore.add(ItemAttributes.lastSlot(lore), " §7Esta picareta está coberta de Sílex §01");
        }

        pickMeta.setLore(lore);
        pick.setItemMeta(pickMeta);
    }

    private static int whereSilexLore(List<String> lore) {
        if (lore == null) return -1;

        int index = 0;
        for (String s : lore) {
            if (s.contains("coberta de Sílex")) return index;
            index++;
        }

        return -1;
    }

    private static int quantoSilex(List<String> lore) {
        if (lore == null) return -1;

        for (String s : lore) {
            if (s.contains("coberta de Sílex")) {
                String[] split = ChatColor.stripColor(s).split(" ");
                return Integer.valueOf(ChatColor.stripColor(split[split.length - 1]));
            }
        }

        return -1;
    }

    private static boolean gastaSilex(ItemStack ss) {
        if (ss == null) return false;
        if (!ss.getType().name().contains("_PICKAXE")) return false;
        if (ss.getItemMeta() == null) return false;
        ItemMeta meta = ss.getItemMeta();
        if (meta.getLore() == null || meta.getLore().size() == 0) return false;

        int quanto = quantoSilex(meta.getLore());
        if (quanto <= 0) return false;
        quanto--;

        List<String> lore = meta.getLore();

        int silexIndex = whereSilexLore(lore);
        if (quanto == 0) {
            lore.remove(silexIndex - 1);
            lore.remove(silexIndex - 1);
        } else {
            lore.set(silexIndex, " §7Esta picareta está coberta de Sílex §0" + quanto);
        }

        meta.setLore(lore);
        ss.setItemMeta(meta);
        return true;
    }

    private static final PotionEffect fadiga = new PotionEffect(PotionEffectType.SLOW_DIGGING, 10, 2);

    public static void desarma(Player miner, LivingEntity alvo) {
        if (!miner.isSneaking()) return;
        if (miner.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) return;
        ItemStack pick = miner.getInventory().getItemInMainHand();
        if (pick == null || !pick.getType().name().contains("_PICKAXE")) return;
        if (!gastaSilex(pick)) return;
        miner.playSound(miner.getLocation(), Sound.BLOCK_GRAVEL_FALL, 0.025f, 0.85f);
        miner.addPotionEffect(fadiga, true);

        if (!SkillMaster.temSkill(miner, Skills.Desarmar.skill)) return;

        if (alvo == null) {
            Stamina.spendStamina(miner, 35);
            return;
        }

        if (!(alvo instanceof Player)) {
            Stamina.spendStamina(miner, 20);
            return;
        }

        Player playerAlvo = (Player) alvo;
        if (!bracoAdormecido(playerAlvo)) {
            if (!Stamina.spendStamina(miner, 30)) return;
            miner.sendMessage("§aVocê adormeceu o braço do seu alvo!");
            MetaShit.setMetaObject("bCansado", playerAlvo, System.currentTimeMillis() + 3000);
            playerAlvo.sendMessage("§cVocê levou uma pancada tão forte que seu braço adormeceu");
            Bukkit.getScheduler().runTaskLater(KoM._instance, () -> {
                if (playerAlvo.isOnline()) playerAlvo.sendMessage("§aSeu braço já responde a comandos");
            }, 62);
            return;
        }

        Mana.spendMana(miner, 5);

    }

    public static boolean bracoAdormecido(Player player) {
        if (!player.hasMetadata("bCansado")) return false;
        long time = (long) MetaShit.getMetaObject("bCansado", player);

        if (time > System.currentTimeMillis()) return true;

        player.removeMetadata("bCansado", KoM._instance);
        return false;
    }

// !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=!

    public static final List<Skill> skillList = Arrays.asList(
            new Skill(classe, "Desarmar", 2, false, new String[]{"§9Permite adormecer o braço dos oponentes usando picaretas"}),
            new Skill(classe, "Escalar", 3, false, new String[]{"§9Permite escalar paredes com uma picareta"}),
            new Skill(classe, "Obter Minérios", 5, false, new String[]{"§9Aumenta chances de sucesso ao minerar.", "§9Quanto maior seu nivel, maior a chance"}, true),
            new Skill(classe, "Escavador Experiente", 10, false, new String[]{"§9Chance de encontrar items raros minerando"}),
            new Skill(classe, "Craftar Blocos", 20, false, new String[]{"§9Aumenta chances de sucesso ao criar blocos.", "§9Quanto maior seu nivel, maior a chance"}, true)
    );

    public enum Skills {
        Desarmar(skillList.get(0)),
        Escalar(skillList.get(1)),
        Obter_Minerios(skillList.get(2)),
        Escavadir_Experiente(skillList.get(3)),
        Craftar_Blocos(skillList.get(4));

        public Skill skill;

        Skills(Skill skill) {
            this.skill = skill;
        }
    }

// !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=!

}
