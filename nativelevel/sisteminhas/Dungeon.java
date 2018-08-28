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
package nativelevel.sisteminhas;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nativelevel.CFG;
import nativelevel.KoM;
import nativelevel.Lang.L;
import nativelevel.MetaShit;
import nativelevel.integration.SimpleClanKom;
import nativelevel.utils.BookUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Dungeon extends KomSystem {

    public static int LUZ_DO_ESCURO = 2;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncChat(AsyncPlayerChatEvent event) {
        Dungeon.textoAtivaRedstone(event);
    }

    public static void blockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().isOp() && !event.getPlayer().hasPermission("kom.build")) {

            Location l = event.getBlock().getLocation();
            l.setY(0);
            if (l.getBlock().getType() == Material.DIAMOND_BLOCK) return;

            event.setCancelled(true);
        }
    }

    public static void textoAtivaRedstone(final AsyncPlayerChatEvent ev) {
        if (ev.getPlayer().getWorld().getName().equalsIgnoreCase("NewDungeon")) {
            if (ev.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.DIAMOND_BLOCK) {
                if (ev.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN).getType() == Material.CHEST) {
                    KoM.log.info("MSG :" + ev.getMessage());
                    ItemStack book = BookUtil.getBookAt(ev.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN));
                    if (book != null) {
                        BookMeta meta = (BookMeta) book.getItemMeta();
                        if (meta.getPages().size() > 0) {
                            KoM.log.info("MSG LIVRO :" + meta.getPages().get(0));
                            if (ev.getMessage().equalsIgnoreCase(meta.getPages().get(0))) {
                                Runnable r = new Runnable() {
                                    public void run() {
                                        final Block toxa = ev.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN);
                                        if (!toxa.hasMetadata("redstone") && toxa.getType() == Material.REDSTONE_BLOCK) {
                                            toxa.setType(Material.AIR);
                                            ev.setCancelled(true);
                                            return;
                                        }
                                        if (toxa.hasMetadata("redstone")) {
                                            ev.setCancelled(true);
                                            return;
                                        }
                                        KoM.rewind.put(toxa, Material.AIR);
                                        toxa.setType(Material.REDSTONE_BLOCK);

                                        MetaShit.setMetaString("redstone", toxa, "1");
                                        int task = Bukkit.getScheduler().scheduleSyncDelayedTask(KoM._instance,
                                                new Runnable() {

                                                    @Override
                                                    public void run() {
                                                        toxa.setType(Material.AIR);
                                                        toxa.removeMetadata("redstone", KoM._instance);
                                                    }
                                                }, 20 * 10);
                                    }
                                };
                                Bukkit.getScheduler().scheduleSyncDelayedTask(KoM._instance, r, 5);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void itemAtivaRedstone(PlayerInteractEvent ev) {
        // if (ev.getPlayer().getWorld().getName().equalsIgnoreCase("NewDungeon")) {
        if (ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clicado = ev.getClickedBlock();
            if (clicado != null && ev.getPlayer().getInventory().getItemInMainHand() != null && ev.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR) {
                Block emBaixo = clicado.getRelative(BlockFace.DOWN);
                for (int x = 0; x < 3; x++) {
                    emBaixo = emBaixo.getRelative(BlockFace.DOWN);
                    if (emBaixo.getType() == Material.SPONGE) {
                        if (emBaixo.getRelative(BlockFace.DOWN).getType() == Material.CHEST) {
                            Chest c = (Chest) emBaixo.getRelative(BlockFace.DOWN).getState();
                            for (ItemStack ss : c.getBlockInventory().getContents()) {
                                if (ss == null) {
                                    continue;
                                }
                                if (ss.getType() == ev.getPlayer().getInventory().getItemInMainHand().getType()) {
                                    ItemMeta m1 = ss.getItemMeta();
                                    ItemMeta m2 = ev.getPlayer().getInventory().getItemInMainHand().getItemMeta();
                                    if (((m1.getLore() == null && m2.getLore() == null)
                                            || m1.getLore().size() == m2.getLore().size() && m1.getLore().equals(m2.getLore())) && (m1.getDisplayName() != null && m2.getDisplayName() != null && m1.getDisplayName().equalsIgnoreCase(m2.getDisplayName()))) {

                                        if (!m2.getDisplayName().contains("[Quest]")) {
                                            int qtd = ev.getPlayer().getInventory().getItemInMainHand().getAmount();
                                            if (qtd == 1)
                                                ev.getPlayer().setItemInHand(null);
                                            else
                                                ev.getPlayer().getInventory().getItemInMainHand().setAmount((qtd - 1));
                                        }
                                        final Block bbb = emBaixo;
                                        //PlayEffect.play(VisualEffect.SMOKE, clicado.getLocation(), "num:3");
                                        clicado.getWorld().playEffect(clicado.getLocation(), Effect.SMOKE, 3);
                                        emBaixo.setType(Material.REDSTONE_BLOCK);
                                        Runnable r = new Runnable() {
                                            @Override
                                            public void run() {
                                                Block bloco = bbb.getLocation().getBlock();
                                                if (bloco.getType() == Material.REDSTONE_BLOCK) {
                                                    bloco.setType(Material.SPONGE);
                                                }
                                            }
                                        };
                                        Bukkit.getScheduler().scheduleSyncDelayedTask(KoM._instance, r, 20 * 20);
                                        KoM.rewind.put(emBaixo, Material.SPONGE);
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        // }
    }

    public static void empurraBloco(PlayerInteractEvent ev) {
        if (ev.getPlayer().getWorld().getName().equalsIgnoreCase("NewDungeon")) {
            if (ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Block clicked = ev.getClickedBlock();
                if (clicked != null) {

                    if (clicked.getType().getId() == 97) {
                        ev.getPlayer().sendMessage(ChatColor.RED + "Esta pedra e muito pesada para ser empurrada !");
                        return;
                    }

                    if (clicked.getRelative(BlockFace.DOWN).getType() == Material.DIAMOND_BLOCK) {

                        if (ev.getBlockFace() == BlockFace.DOWN || ev.getBlockFace() == BlockFace.UP) {
                            ev.getPlayer().sendMessage(ChatColor.RED + L.m("Voce nao pode empurrar o bloco para la !"));
                            return;
                        }

                        String frase = L.m("Voce empurrou o bloco !");
                        Block novo = clicked.getRelative(ev.getBlockFace().getOppositeFace());
                        if (ev.getPlayer().isSneaking()) {
                            frase = L.m("Voce puxou o bloco !");
                            novo = clicked.getRelative(ev.getBlockFace());
                        }
                        if ((novo.getType() != Material.TORCH && novo.getType() != Material.AIR) || novo.getRelative(BlockFace.DOWN).getType() != Material.DIAMOND_BLOCK) {
                            ev.getPlayer().sendMessage(ChatColor.RED + L.m("Voce nao pode empurrar o bloco para la !"));
                            return;
                        }
                        novo.setType(clicked.getType());
                        novo.getState().setData(clicked.getState().getData());
                        clicked.setType(Material.AIR);
                        clicked.getLocation().getWorld().playEffect(clicked.getLocation(), Effect.SMOKE, 1);
                        clicked.getLocation().getWorld().playEffect(novo.getLocation(), Effect.SMOKE, 1);
                        //PlayEffect.play(VisualEffect.SMOKE, novo.getLocation(), "num:3");
                        ev.getPlayer().sendMessage(ChatColor.GREEN + frase);
                    } else if (clicked.getRelative(BlockFace.DOWN).getType() == Material.ICE) {
                        if (ev.getBlockFace() == BlockFace.DOWN || ev.getBlockFace() == BlockFace.UP) {
                            ev.getPlayer().sendMessage(ChatColor.RED + L.m("Voce nao pode empurrar o bloco para la !"));
                            return;
                        }
                        BlockFace direcao = ev.getBlockFace().getOppositeFace();
                        Block abaixo = ev.getClickedBlock().getRelative(direcao).getRelative(BlockFace.DOWN);
                        Block afrente = ev.getClickedBlock().getRelative(direcao);
                        if (afrente.getType() == Material.AIR && abaixo.getType() == Material.ICE) {
                            int max = 10;
                            List<Block> passar = new ArrayList<Block>();
                            Block ultimo = null;
                            while (max > 0) {
                                abaixo = abaixo.getRelative(direcao);
                                afrente = afrente.getRelative(direcao);
                                if (afrente.getType() == Material.AIR && abaixo.getType() == Material.ICE) {
                                    passar.add(afrente);
                                    ultimo = afrente;
                                } else {
                                    break;
                                }
                                max--;
                            }
                            if (ultimo != null) {
                                ev.getPlayer().sendMessage(ChatColor.GREEN + L.m("Voce empurrou o bloco !"));
                                ultimo.setType(clicked.getType());
                                ultimo.getState().setData(clicked.getState().getData());
                                clicked.setType(Material.AIR);
                                for (Block b : passar) {
                                    b.getLocation().getWorld().playEffect(b.getLocation(), Effect.SMOKE, 1);
                                    //clicked.getLocation().getWorld().playEffect(b.getLocation(), Effect.SMOKE, 1);
                                    //PlayEffect.play(VisualEffect.SMOKE, b.getLocation(), "num:3");
                                    //PlayEffect.play(VisualEffect.SMOKE, clicked.getLocation(), "num:3");
                                }
                            }

                        } else {
                            ev.getPlayer().sendMessage(ChatColor.RED + L.m("Voce nao pode empurrar o bloco para la !"));
                            return;
                        }
                    }
                }
            }
        }
    }

    public static void itemActivatesRedstone(PlayerInteractEvent ev) {
        if (ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clicado = ev.getClickedBlock();
            if (clicado != null && ev.getPlayer().getInventory().getItemInMainHand() != null && ev.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR) {
                Block emBaixo = clicado.getRelative(BlockFace.DOWN);
                for (int x = 0; x < 3; x++) {
                    emBaixo = emBaixo.getRelative(BlockFace.DOWN);
                    if (emBaixo.getType() == Material.GOLD_BLOCK) {
                        if (emBaixo.getRelative(BlockFace.DOWN).getType() == Material.CHEST) {
                            Chest c = (Chest) emBaixo.getRelative(BlockFace.DOWN).getState();
                            for (ItemStack ss : c.getBlockInventory().getContents()) {
                                if (ss == null) {
                                    continue;
                                }
                                if (ss.getType() == ev.getPlayer().getInventory().getItemInMainHand().getType()) {
                                    ItemMeta m1 = ss.getItemMeta();
                                    ItemMeta m2 = ev.getPlayer().getInventory().getItemInMainHand().getItemMeta();
                                    if (((m1.getLore() == null && m2.getLore() == null)
                                            || m1.getLore().size() == m2.getLore().size() && m1.getLore().equals(m2.getLore())) && (m1.getDisplayName() != null && m2.getDisplayName() != null && m1.getDisplayName().equalsIgnoreCase(m2.getDisplayName()))) {

                                        if (!m2.getDisplayName().contains("[Quest]")) {
                                            int qtd = ev.getPlayer().getInventory().getItemInMainHand().getAmount();
                                            if (qtd == 1)
                                                ev.getPlayer().setItemInHand(null);
                                            else
                                                ev.getPlayer().getInventory().getItemInMainHand().setAmount((qtd - 1));
                                        }
                                        final Block bbb = emBaixo;
                                        // PlayEffect.play(VisualEffect.SMOKE, clicado.getLocation(), "num:3");
                                        emBaixo.setType(Material.REDSTONE_BLOCK);
                                        Runnable r = new Runnable() {
                                            @Override
                                            public void run() {
                                                Block bloco = bbb.getLocation().getBlock();
                                                if (bloco.getType() == Material.REDSTONE_BLOCK) {
                                                    bloco.setType(Material.GOLD_BLOCK);
                                                }
                                            }
                                        };
                                        Bukkit.getScheduler().scheduleSyncDelayedTask(KoM._instance, r, 20 * 20);
                                        KoM.rewind.put(emBaixo, Material.GOLD_BLOCK);
                                        //Rewind.add(emBaixo, Material.GOLD_BLOCK);
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    public static boolean canBuild(Player player, Block block, boolean placing) {
        if (player.isOp() || player.hasPermission("kom.build")) return true;
        else if (lorerBuild(player, block)) return true;

        if (placing && block.getType() == Material.TORCH) {

            Material oldBlock = block.getLocation().getBlock().getType();
            if (oldBlock == Material.VINE || oldBlock == Material.STATIONARY_WATER || oldBlock == Material.WATER || oldBlock == Material.STATIONARY_LAVA || oldBlock == Material.LAVA) return false;
            if (block.getWorld().getBlockAt(block.getLocation().add(0, 1, 0)).getType().name().contains("MUSHROOM") ||
                    block.getWorld().getBlockAt(block.getLocation().add(1, 0, 0)).getType().name().contains("MUSHROOM") ||
                    block.getWorld().getBlockAt(block.getLocation().add(-1, 0, 0)).getType().name().contains("MUSHROOM") ||
                    block.getWorld().getBlockAt(block.getLocation().add(0, 0, 1)).getType().name().contains("MUSHROOM") ||
                    block.getWorld().getBlockAt(block.getLocation().add(0, 0, -1)).getType().name().contains("MUSHROOM") ||
                    block.getWorld().getBlockAt(block.getLocation().add(0, -1, 0)).getType().name().contains("MUSHROOM")) {
                return false;
            }

            player.sendMessage(ChatColor.GREEN + L.m("Você colocou uma tocha temporária."));
            KoM.rewind.put(block, Material.AIR);

            Bukkit.getScheduler().runTaskLater(KoM._instance, () -> {

                Block bloco = block.getLocation().getBlock();
                if (!block.getChunk().isLoaded()) {

                    bloco.getChunk().load();
                    bloco.setType(Material.AIR);
                    KoM.rewind.remove(bloco);

                } else {

                    bloco.setType(Material.REDSTONE_TORCH_ON);

                    Bukkit.getScheduler().runTaskLater(KoM._instance, () -> {
                        Block bloco1 = block.getLocation().getBlock();
                        if (!bloco1.getChunk().isLoaded()) bloco1.getChunk().load();
                        bloco1.setType(Material.AIR);
                        KoM.rewind.remove(bloco1);

                    }, 20 * 25);

                }

            }, 20 * 60);

            for (Entity e : block.getWorld().getNearbyEntities(block.getLocation(), 8, 8, 8))
                if (e.getType() == EntityType.PLAYER) {
                    Player nego = (Player) e;
                    if (nego.hasPotionEffect(PotionEffectType.BLINDNESS)) {
                        nego.removePotionEffect(PotionEffectType.BLINDNESS);
                        nego.sendMessage(ChatColor.GREEN + "Voce pode ver novamente");
                    }
                }

        }

        if (placing) {
            player.sendMessage("§CEm dungeons não é permitda a construção!");
            player.damage(2);
        } else if (!SimpleClanKom.easyToBreak.contains(block.getType())) {
            player.sendMessage("§CEm dungeons você não pode quebrar blocos!");
            player.damage(4);
        }

        return false;
    }

    private static boolean lorerBuild(Player player, Block block) {
        if (player.getWorld().getName().equalsIgnoreCase(CFG.mundoDungeon)) return false;
        if (player.hasPermission("kom.lorer")) return false;
        ApplicableRegionSet set = KoM.worldGuard.getRegionManager(block.getWorld()).getApplicableRegions(block.getLocation());
        while (set.iterator().hasNext()) {
            ProtectedRegion region = set.iterator().next();
            if (region.getId().equalsIgnoreCase("lorers")) return true;
        }
        return false;
    }

}
