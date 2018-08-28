package nativelevel.ComandosNovos.commands.komx.komxsubs;

import com.nisovin.shopkeepers.Shopkeeper;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import nativelevel.ComandosNovos.SubCmd;
import nativelevel.KoM;
import nativelevel.Lang.LangMinecraft;
import nativelevel.Listeners.DamageListener;
import nativelevel.MetaShit;
import nativelevel.guis.ShowLootGUI;
import nativelevel.guis.guilda.GuildaAllysGUI;
import nativelevel.phatloots.PhatLoot;
import nativelevel.phatloots.PhatLoots;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.*;
import net.minecraft.server.v1_12_R1.*;
import net.minecraft.server.v1_12_R1.Entity;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.ShulkerBox;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import pixelmc.packets.PacketListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class CmdTeste extends SubCmd {

    public CmdTeste() {
        super("teste", ExecutorType.OPCONSOLE);
    }

    public static ArrayList<UUID> shopKeppers = new ArrayList<>();

//    public static int x;
//    public static int z;
//
//    public static Runtime runtime = Runtime.getRuntime();
//    public static ArrayList<Integer> scanIds = new ArrayList<>();

    @Override
    public void execute(CommandSender cs, String[] args) {

        GUI.open((Player) cs, new ShowLootGUI(PhatLoots.getPhatLoot(args[1]).lootList, "Exemplo do LooT ", args[1]));

//        cs.sendMessage("§7!=- §3Packets RUNNING §7-=!");
//
//        for (Map.Entry<String, Integer> entry : PacketListener.writePackets.entrySet())
//            cs.sendMessage("§8[§cWRITE§8] §7" + entry.getKey() + " §8(§e" + entry.getValue() + "§8)");
//
//        for (Map.Entry<String, Integer> entry : PacketListener.readPackets.entrySet())
//            cs.sendMessage("§8[§aREAD§8] §7" + entry.getKey() + " §8(§e" + entry.getValue() + "§8)");

//        Player p = (Player) cs;
//        EntityPlayer ep = ((CraftPlayer) p).getHandle();
//        WorldServer worldServer = ep.getWorld().getWorld().getHandle();
//        PlayerChunk playerChunk = worldServer.getPlayerChunkMap().getChunk(ep.getChunkX(), ep.getChunkZ());
//        if (playerChunk == null) return;
//
//        for (EntityPlayer epx : playerChunk.c) {
//            p.sendMessage(epx.getName());
//        }

//        Player p = (Player) cs;
//
//        if (args.length > 1) {
//            if (args[1].equalsIgnoreCase("1")) {
//                KoM.announce("" + p.getLocation().getChunk().isLoaded());
//            } else if (args[1].equalsIgnoreCase("2")) {
//                p.getLocation().getChunk().unload(true);
//                p.getLocation().getChunk().load(true);
//            }
//            KoM.announce(LocUtils.loc2str(p.getWorld().getHighestBlockAt(p.getLocation()).getLocation()));
//        }

//        joga(p, Bukkit.getPlayer(args[1]).getLocation(), Double.valueOf(args[2]), Double.valueOf(args[3]));

//        Fireworks.doFirework(entity.getLocation(), FireworkEffect.Type.BURST, Color.GREEN, Color.GREEN, 50);

//        for (ActiveMob mob : KoM.mm.getMobManager().getActiveMobs()) p.sendMessage(mob.getUniqueId().toString());

//        Player alvo = Bukkit.getPlayer(args[1]);
//
//        Vector toDamager = alvo.getLocation().toVector().subtract(p.getLocation().toVector());
//
//        p.setVelocity(toDamager.normalize().multiply(0.5));

//        DamageListener.actionDamage(p);

//        for (Entity entity : p.getNearbyEntities(20, 20, 20)) {
//            p.sendMessage(entity.getClass().getName());
//        }

//        p.sendMessage(RecipeBook.isRecipeBook(p.getInventory().getItemInMainHand()) + "");

//        p.getInventory().addItem(CidadeScroll.makeItem(Integer.valueOf(args[1])));

//        if (scanIds.size() == 0)
//            scan(Bukkit.getWorld("NewWorld"), 100);
//        else {
//            cs.sendMessage("§7RAM §e" + (runtime.totalMemory() / 1048576) + " §f/ §c" + ((runtime.totalMemory() - runtime.freeMemory()) / 1048576));
//            cs.sendMessage("Já ta no CHUNK [" + x + "," + z + "]");
//        }

//        Chunk ck = p.getLocation().getChunk();
//        for (BlockState bs : ck.getTileEntities()) {
//            if (bs.getLocation().getY() != 0)
//                p.sendMessage("[" + ck.getX() + "," + ck.getZ() + "]" + bs.getType() + " " + LocUtils.loc2str(bs.getLocation()));
//        }

//      cs.sendMessage(Jobs.hasSuccess(Integer.valueOf(args[1]), args[2], (Player) cs) + "");
//      DeathEvents.dropaItens((Player) cs, 0.5d);

//      p.getInventory().addItem(SeguroDeItems.geraSeguro(3, 0.25));
//      p.sendMessage(SeguroDeItems.getPorcent(p) + "");
//      GUI.open(p, new GuildaCreateGUI(p, "", ""));

//        SQL.addDisposicao(p.getUniqueId(), -10, false);
//        cs.sendMessage("" + SQL.getDisposicao(((Player) cs).getUniqueId()));

    }

//    private static void joga(Entity throwed, Location target, double x, double y) {
//
//        Vector toDamager = target.toVector().subtract(throwed.getLocation().toVector());
//        KoM.announce("0 - " + toDamager.getX() + " " + toDamager.getY() + " " + toDamager.getZ());
//        toDamager.setY(y);
//        KoM.announce("1 - " + toDamager.getX() + " " + toDamager.getY() + " " + toDamager.getZ());
//        throwed.setVelocity(toDamager.normalize().multiply(x));
//
//    }

//    private static void scan(World world, int perTick) {
//
//        x = 800;
//        z = 800;
//
//        int scanId;
//
//        Runnable runnable0 = () -> {
//            if (!scanChunk(world)) {
//                for (int id : scanIds)
//                    Bukkit.getScheduler().cancelTask(id);
//            }
//        };
//
//        while (perTick > 0) {
//            if (perTick == 1) scanId = Bukkit.getScheduler().scheduleSyncRepeatingTask(KoM._instance, runnable0, 1, 1);
//            else scanId = Bukkit.getScheduler().scheduleSyncRepeatingTask(KoM._instance, () -> scanChunk(world), 1, 1);
//            scanIds.add(scanId);
//            perTick--;
//        }
//
//    }
//
//    private static boolean scanChunk(World world) {
//        if (x >= 0) {
//            if (z >= 0) {
//                for (BlockState bs : world.getChunkAt((x - 400), (z - 400)).getTileEntities())
//                    if (bs instanceof InventoryHolder)
//                        if (bs.getY() != 0) {
//                            if (bs instanceof ShulkerBox) {
//                                Block bloco = world.getBlockAt(bs.getLocation());
//                                ((ShulkerBox) bs).getInventory().clear();
//                                DyeColor cor = ((ShulkerBox) bloco.getState()).getColor();
//                                bloco.setType(Material.WOOL);
//                                bloco.setData(cor.getWoolData());
//                            } else {
//                                String type = ClanLand.getTypeAt(bs.getLocation());
//                                int size = 0;
//                                for (ItemStack itemStack : ((InventoryHolder) bs).getInventory().getStorageContents())
//                                    if (!bs.getLocation().add(0, -1, 0).getBlock().getType().equals(Material.SPONGE) &&
//                                            !bs.getLocation().add(0, 1, 0).getBlock().getType().equals(Material.SPONGE) &&
//                                            itemStack != null &&
//                                            !itemStack.getType().equals(Material.AIR) &&
//                                            !itemStack.getType().equals(Material.BOOK_AND_QUILL) &&
//                                            !itemStack.getType().equals(Material.WRITTEN_BOOK))
//                                        size++;
//                                if (type.equalsIgnoreCase("WILD") || size > 0)
//                                    Bukkit.broadcastMessage("[" + (x - 400) + "," + (z - 400) + "] " + bs.getType() + " " + size + " " + LocUtils.loc2str(bs.getLocation()) + ":" + ClanLand.getTypeAt(bs.getLocation()));
////                                Block block = bs.getBlock();
////                                InventoryHolder inv = (InventoryHolder) bs;
////                                inv.getInventory().clear();
////                                block.setType(Material.AIR);
////                                block = Bukkit.getWorld(CFG.mundoGuilda).getBlockAt(block.getX(), block.getY(), block.getZ());
////                                inv = (InventoryHolder) block.getState();
////                                inv.getInventory().clear();
////                                block.setType(Material.AIR);
//                            }
//                        }
//                world.unloadChunk((x - 400), (x - 400));
//                z--;
//                if (z == 0) {
//                    z = 800;
//                    x--;
//                }
//                return true;
//            }
//        }
//        return false;
//    }

}
