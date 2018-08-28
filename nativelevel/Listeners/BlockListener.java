package nativelevel.Listeners;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import nativelevel.CFG;
import nativelevel.Classes.Farmer;
import nativelevel.Classes.Lumberjack;
import nativelevel.Custom.CustomItem;
import nativelevel.Custom.Items.Ponte;
import nativelevel.Custom.Items.SuperBomba;
import nativelevel.CustomEvents.BlockHarvestEvent;
import nativelevel.Harvesting.HarvestEvents;
import nativelevel.Jobs;
import nativelevel.KoM;
import nativelevel.Lang.L;
import nativelevel.MetaShit;
import nativelevel.Planting.PlantEvents;
import nativelevel.integration.SimpleClanKom;
import nativelevel.rankings.Estatistica;
import nativelevel.rankings.RankDB;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.sisteminhas.Dungeon;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketPlayOutBlockChange;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

import static nativelevel.Listeners.GeneralListener.pistaoNaoEmpurra;
import static nativelevel.Listeners.GeneralListener.wizard;

/**
 * @author Ziden
 */

public class BlockListener implements Listener {

    public static List<Material> passaveis = Arrays.asList(new Material[]{Material.GRASS, Material.DOUBLE_PLANT, Material.RED_ROSE, Material.YELLOW_FLOWER, Material.AIR, Material.LONG_GRASS, Material.WATER});

    @EventHandler
    public void harvest(BlockHarvestEvent ev) {
        if (ev.getHarvestable().classe == Jobs.Classe.Lenhador) {
            Lumberjack.corta(ev);
        } else if (ev.getHarvestable().classe == Jobs.Classe.Fazendeiro) {
            Farmer.blockHarvest(ev);
        }
    }

    @EventHandler
    public void pistao(BlockPistonExtendEvent ev) {
        for (Block b : ev.getBlocks()) {
            if (pistaoNaoEmpurra.contains(b.getType())) {
                ev.setCancelled(true);
            }
            if (b.getType() == Material.MELON_BLOCK || b.getType() == Material.MELON_SEEDS) {
                if (Jobs.rnd.nextInt(100) <= 60) {
                    b.setType(Material.AIR);
                }
            }
            if (!ev.getBlock().getWorld().getName().equalsIgnoreCase("NewDungeon")) {
                if (b.getType() == Material.SLIME_BLOCK) {
                    ev.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void quebraBloco(final BlockBreakEvent ev) {
        if (ev.isCancelled()) return;

        KoM.debug("Block Break event highest");

        if (ev.getPlayer().getGameMode() == GameMode.CREATIVE && ev.getPlayer().hasPermission("kom.build")) {
            return;
        }

        if (Ponte.bloqueios.contains(ev.getBlock())) {
            if (Jobs.rnd.nextInt(10) != 1) {
                ev.setCancelled(true);
                ev.getPlayer().sendMessage(ChatColor.RED + "Voce nao conseguiu remover o bloco estranho, tente novamente.");

                return;
            } else {
                ev.setCancelled(true);
                ev.getBlock().setType(Material.AIR);
                Ponte.bloqueios.remove(ev.getBlock());
                return;
            }
        }

        ev.setExpToDrop(0);

        if (ev.getBlock().getWorld().getName().equalsIgnoreCase(CFG.mundoDungeon)) {
            if (!Dungeon.canBuild(ev.getPlayer(), ev.getBlock(), false)) {
                ev.setCancelled(true);
                return;
            }
        } else if (ev.getBlock().getWorld().getName().equalsIgnoreCase(CFG.mundoGuilda)) {
            if (!SimpleClanKom.canBuild(ev.getPlayer(), ev.getBlock().getLocation(), ev.getBlock(), false)) {
                ev.setCancelled(true);
                return;
            }
        } else if (ev.getBlock().getWorld().getName().equalsIgnoreCase("woe")) {
            if (!woeCanBuild(ev.getPlayer(), ev.getBlock(), false)) {
                ev.setCancelled(true);
                return;
            }
        } else if (ev.getBlock().getWorld().getEnvironment() != World.Environment.THE_END) {
            ev.setCancelled(true);
            KoM.debug("Cancelado por mundo não identificado");
            return;
        }

        if (ev.getBlock().getType() == Material.LEAVES || ev.getBlock().getType() == Material.LEAVES_2) Farmer.cortaFolha(ev.getPlayer(), ev.getBlock());

        if (ev.getBlock().getRelative(BlockFace.DOWN).getType() == Material.DOUBLE_PLANT && ev.getBlock().getType() == Material.DOUBLE_PLANT) {
            ev.getPlayer().sendMessage(ChatColor.RED + L.m("Colha a flor pela raiz !"));
            ev.setCancelled(true);
            return;
        }

        if (ev.getBlock().getRelative(BlockFace.UP).getType() == Material.SUGAR_CANE_BLOCK || ev.getBlock().getRelative(BlockFace.UP).getType() == Material.CACTUS || ev.getBlock().getRelative(BlockFace.UP).getType() == Material.SUGAR_CANE || ev.getBlock().getRelative(BlockFace.UP).getType() == Material.NETHER_WARTS) {
            ev.getPlayer().sendMessage(ChatColor.RED + L.m("Comece colhendo pelas pontas !"));
            ev.setCancelled(true);
            return;
        }

        if (ev.getBlock().getType() == Material.TNT && SuperBomba.explosivos.containsKey(ev.getBlock())) ev.setCancelled(true);

        if (ev.getBlock().getType() == Material.SIGN || ev.getBlock().getType() == Material.SIGN_POST) {
            Sign s = (Sign) ev.getBlock().getState();
            if (!ev.getPlayer().isOp() && s.getLine(0).equalsIgnoreCase("[Server]") || s.getLine(0).equalsIgnoreCase("§l§r[Torres]")) {
                ev.getPlayer().sendMessage(ChatColor.RED + L.m("Esta placa esta presa por um poder maior."));
                ev.setCancelled(true);
                return;
            }
        }

        if (ev.getBlock().getRelative(BlockFace.UP).getType() == Material.SIGN || ev.getBlock().getRelative(BlockFace.UP).getType() == Material.SIGN_POST) {
            Sign s = (Sign) ev.getBlock().getRelative(BlockFace.UP).getState();
            if (!ev.getPlayer().isOp() && s.getLine(0).equalsIgnoreCase("[Server]")) {
                ev.setCancelled(true);
                return;
            }
        }

        // corrigir bug de fazer item sem chegar skil kebrando
        if (ev.getBlock().getType() == Material.FURNACE || ev.getBlock().getType() == Material.BURNING_FURNACE) {
            ((Furnace) ev.getBlock().getState()).getInventory().clear();
            ev.getBlock().setType(Material.AIR);
            ev.getBlock().getWorld().dropItemNaturally(ev.getBlock().getLocation(), new ItemStack(Material.FURNACE, 1));
        } else if (ev.getBlock().getType() == Material.BREWING_STAND) {
            ((BrewingStand) ev.getBlock().getState()).getInventory().clear();
            ev.getBlock().setType(Material.AIR);
            ev.getBlock().getWorld().dropItemNaturally(ev.getBlock().getLocation(), new ItemStack(Material.BREWING_STAND_ITEM, 1));
        }

        if (ev.getBlock().getType() == Material.WEB || ev.getBlock().getType() == Material.GLOWSTONE)
            if (wizard.protegeTeia(ev.getBlock())) {
                ev.setCancelled(true);
                return;
            }


        KoM.generator.breakBlock(ev);

        HarvestEvents.quebraBlock(ev);

        KoM.debug("Fim block break highest");

    }

    private void poeBloco(BlockPlaceEvent ev) {
        if (ev.isCancelled()) return;

        if (ev.getBlock().getType() == Material.MOB_SPAWNER)
            if (ev.getBlock().getY() > 3) {
                ev.getPlayer().sendMessage("§cBota esse bloco mais alto por favor...");
                ev.setCancelled(true);
                return;
            } else {
                ev.getBlock().getWorld().getBlockAt(ev.getBlock().getLocation().add(0, -3, 0)).setType(Material.BEDROCK);
                ev.getBlock().getWorld().getBlockAt(ev.getBlock().getLocation().add(0, -2, 0)).setType(Material.CHEST);
            }

        if (ev.getPlayer().isOp() || ev.getPlayer().hasPermission("kom.build")) return;

        if (ev.getBlock().getType() == Material.BEACON)
            ev.setCancelled(true);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void colocaBloco(final BlockPlaceEvent ev) {

        poeBloco(ev);
        if (ev.isCancelled()) return;

        if (ev.getPlayer().isOp() || ev.getPlayer().hasPermission("kom.build")) return;

        if (ev.getBlock().getType() == Material.COMMAND)
            //KnightsOfMinecraft.komLog.escreve(ev.getPlayer().getName() +L.m( " colocou command block no mundo " + ev.getBlock().getLocation().getWorld().getName() + " na loc " + ev.getBlock().getLocation().getBlockX() + "," + ev.getBlock().getLocation().getBlockY() + "," + ev.getBlock().getLocation().getBlockZ());

            // anti bug de criar redstone_block
            if (ev.getBlock().getType() == Material.STONE_BUTTON) {
                if (ev.getBlockAgainst().getType().getId() == 98 && ev.getBlockAgainst().getData() == 3) {
                    if (!ev.getPlayer().isOp()) {
                        ev.setCancelled(true);
                        return;
                    }
                }
            }

        String customItem = CustomItem.getCustomItem(ev.getPlayer().getInventory().getItemInMainHand());
        if (customItem != null) {
            ev.setCancelled(true);
            return;
        }

        if (ev.getBlock().getWorld().getName().equalsIgnoreCase(CFG.mundoDungeon)) {
            if (!Dungeon.canBuild(ev.getPlayer(), ev.getBlock(), true)) {
                ev.setCancelled(true);
                return;
            }
        } else if (ev.getBlock().getWorld().getName().equalsIgnoreCase(CFG.mundoGuilda)) {
            if (!SimpleClanKom.canBuild(ev.getPlayer(), ev.getBlock().getLocation(), ev.getBlock(), true)) {
                ev.setCancelled(true);
                return;
            }
        } else if (ev.getBlock().getWorld().getName().equalsIgnoreCase("woe")) {
            if (!woeCanBuild(ev.getPlayer(), ev.getBlock(), true)) {
                ev.setCancelled(true);
                return;
            }
        } else if (ev.getBlock().getWorld().getEnvironment() != World.Environment.THE_END) {
            ev.setCancelled(true);
            KoM.debug("Cancelado por mundo não identificado");
            return;
        }

        if (ev.getBlock().getType() == Material.HOPPER || ev.getBlock().getType() == Material.HOPPER_MINECART || ev.getBlock().getType() == Material.IRON_ORE || ev.getBlock().getType() == Material.GOLD_ORE || ev.getBlock().getType() == Material.MELON || ev.getBlock().getType() == Material.PUMPKIN || ev.getBlock().getType() == Material.EMERALD_ORE) {
            if (!ev.getPlayer().isOp()) {
                ev.getPlayer().sendMessage(ChatColor.RED + L.m("Este item esta bloqueado temporariamente !"));
                ev.setCancelled(true);
            }
        }

        KoM.debug("Planting");
        PlantEvents.plantaBlock(ev);

        if (!ev.isCancelled()) ev.getBlock().setMetadata("playerpois", new FixedMetadataValue(KoM._instance, true));

        if (!ev.isCancelled()) RankDB.addPontoCache(ev.getPlayer(), Estatistica.BUILDER, 1);

    }

    private static boolean woeCanBuild(Player player, Block block, boolean placing) {
        if (player.isOp() && player.hasPermission("kom.build")) return true;

        ApplicableRegionSet set = KoM.worldGuard.getRegionManager(block.getWorld()).getApplicableRegions(block.getLocation());
        if (set.size() > 0) return true;

        player.sendMessage(ChatColor.RED + L.m("Voce apenas pode construir/destruir as areas marcadas !"));
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCaiBloco(BlockPhysicsEvent event) {
        if (event.getBlock().getType() == Material.SAND ||
                event.getBlock().getType() == Material.GRAVEL ||
                event.getBlock().getType() == Material.ANVIL ||
                event.getBlock().getType() == Material.CONCRETE_POWDER) {

            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCaiBloco2(EntityChangeBlockEvent event) {
        if (event.getBlock().getType() != Material.SAND &&
                event.getBlock().getType() != Material.GRAVEL &&
                event.getBlock().getType() != Material.CONCRETE_POWDER) return;
        if (event.getEntity() instanceof FallingBlock) {
            event.getEntity().remove();
            event.setCancelled(true);
            for (Entity entity : event.getBlock().getWorld().getNearbyEntities(event.getBlock().getLocation(), 96, 128, 96)) {
                if (entity instanceof Player) {

                    BlockPosition blockPosition = new BlockPosition(event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ());
                    PacketPlayOutBlockChange packetBlock = new PacketPlayOutBlockChange(((CraftWorld) entity.getWorld()).getHandle(), blockPosition);
                    packetBlock.block = CraftMagicNumbers.getBlock(event.getBlock().getType()).fromLegacyData(event.getBlock().getData());
//                    if (event.getBlock().getType().equals(Material.SAND)) packetBlock.block = CraftMagicNumbers.getBlock(Material.STAINED_GLASS).fromLegacyData(4);
//                    else if (event.getBlock().getType().equals(Material.GRAVEL)) packetBlock.block = CraftMagicNumbers.getBlock(Material.STAINED_GLASS).fromLegacyData(7);
//                    else packetBlock.block = CraftMagicNumbers.getBlock(Material.STAINED_GLASS).fromLegacyData(event.getBlock().getData());
                    ((CraftPlayer) ((Player) entity).getPlayer()).getHandle().playerConnection.sendPacket(packetBlock);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void antiBug(BlockPlaceEvent ev) {
        if (!ev.isCancelled()) return;
        ev.getPlayer().updateInventory();

        int blockX = ev.getBlock().getX();
        int blockZ = ev.getBlock().getZ();
        int playerX = (int) ev.getPlayer().getLocation().getX();
        int playerZ = (int) ev.getPlayer().getLocation().getZ();
        if (blockX != playerX || blockZ != playerZ) return;

        MetaShit.setMetaObject("bugandoBloco", ev.getPlayer(), (System.currentTimeMillis() + 100));
    }

}
