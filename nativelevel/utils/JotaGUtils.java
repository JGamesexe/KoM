package nativelevel.utils;

import me.blackvein.quests.Quest;
import nativelevel.KoM;
import nativelevel.sisteminhas.ClanLand;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JotaGUtils {

    public static boolean onQuest(UUID uuid, String nomeQuest) {

        for (Quest quest : KoM.quests.getQuester(uuid).currentQuests.keySet()) {
            Bukkit.broadcastMessage(quest.getName());
            if (quest.name.equalsIgnoreCase(nomeQuest)) {
                return true;
            }
        }

        return false;
    }

    public static boolean onQuestStage(UUID uuid, String nomeQuest, int stage) {

        for (Map.Entry<Quest, Integer> quest : KoM.quests.getQuester(uuid).currentQuests.entrySet()) {
            Bukkit.broadcastMessage(stage + " ;" + quest.getKey().getName() + " - " + quest.getValue());
            if (quest.getKey().name.equalsIgnoreCase(nomeQuest) && quest.getValue() == stage) {
                return true;
            }
        }

        return false;
    }

    public static boolean betweenQuestStages(UUID uuid, String nomeQuest, int stageMenor, int stageMaior) {

        for (Map.Entry<Quest, Integer> quest : KoM.quests.getQuester(uuid).currentQuests.entrySet()) {
            Bukkit.broadcastMessage(stageMenor + ":" + stageMaior + " ; " + quest.getKey().getName() + " - " + quest.getValue());
            Bukkit.broadcastMessage((quest.getValue() >= stageMenor) + ":" + (quest.getValue() <= stageMaior) + " ; " + quest.getKey().getName() + " - " + quest.getValue());
            if (quest.getKey().name.equalsIgnoreCase(nomeQuest) && (quest.getValue() >= stageMenor) && (quest.getValue() <= stageMaior)) {
                Bukkit.broadcastMessage("foi ué .-.");
                return true;
            }
        }

        return false;
    }

    public static boolean regenerateChunk(Chunk chunk) {

        int x = chunk.getX();
        int z = chunk.getZ();

        CraftWorld cw = (CraftWorld) chunk.getWorld();
        WorldServer ws = cw.getHandle();

        WorldServer wsBKP;

        try {
            wsBKP = ((CraftWorld) Bukkit.getWorld("NewWorldBKP")).getHandle();
        } catch (NullPointerException ex) {
            Bukkit.getServer().broadcastMessage("§cZezinho que estava tentando regenrar chunk chama o JotaG e para de usar esse comando =D");
            throw ex;
        }

        ws.getChunkProviderServer().unloadChunk(ws.getChunkAt(x, z), false);

        long chunkKey = ChunkCoordIntPair.a(x, z);
        ws.getChunkProviderServer().unloadQueue.remove(chunkKey);
        net.minecraft.server.v1_12_R1.Chunk ck;
        wsBKP.getChunkProviderServer().loadChunk(x, z);
        ck = wsBKP.getChunkAt(x, z);
        PlayerChunk playerChunk = ws.getPlayerChunkMap().getChunk(x, z);
        if (playerChunk != null) {
            playerChunk.chunk = ck;
        }

        ws.getChunkProviderServer().chunks.put(chunkKey, ck);
        ck.addEntities();
        ck.loadNearby(ws.getChunkProviderServer(), ws.getChunkProviderServer().chunkGenerator, true);
        cw.refreshChunk(x, z);

        ws.getChunkProviderServer().unloadChunk(ws.getChunkAt(x, z), true);
        ws.getChunkProviderServer().loadChunk(x, z);

        cw.refreshChunk(x, z);

        ClanLand.removeClanAt(chunk.getBlock(0, 0, 0).getLocation());
        ClanLand.setClanAt(chunk.getBlock(0, 0, 0).getLocation(), "WILD");

        wsBKP.getChunkProviderServer().unload(ck);
        return ck != null;
    }

    public static boolean changeChunkTypeWithItem(Player who, Location to, ItemStack itemstack) {

        if (itemstack.getItemMeta() != null) {

            String tipoTerreno;

            if (itemstack.getItemMeta().getDisplayName().equalsIgnoreCase("§2§lWILDERNESS")) {
                tipoTerreno = "WILD";
            } else if (itemstack.getItemMeta().getDisplayName().equalsIgnoreCase("§6§lSAFEZONE")) {
                tipoTerreno = "SAFE";
            } else if (itemstack.getItemMeta().getDisplayName().equalsIgnoreCase("§4§lWARZONE")) {
                tipoTerreno = "WARZ";
            } else if (who.getName().equalsIgnoreCase("JGamesexe") && itemstack.getItemMeta().getDisplayName().equalsIgnoreCase("§3§lREGEN")) {
                JotaGUtils.regenerateChunk(to.getChunk());
                TitleAPI.sendTitle(who, 10, 30, 10, "§3Regenerado", "");
                return true;
            } else {
                return false;
            }

            if (ClanLand.getTypeAt(to).equalsIgnoreCase(tipoTerreno)) return false;

            if (ClanLand.getTypeAt(to).equals("CLAN")) {
                if (to.getPitch() < -88) {
                    ClanLand.removeClanAt(to);
                    ClanLand.setClanAt(to, tipoTerreno);
                    ClanLand.update(who, to);
                } else {
                    who.sendMessage("Tem guilda nesse chunk...");
                    return false;
                }
            }

            ClanLand.setClanAt(to, tipoTerreno);

            if (tipoTerreno.equals("SAFE")) {
                List<String> lore = itemstack.getItemMeta().getLore();
                if (lore != null) {
                    if (lore.size() == 1) ClanLand.setCoisasSafe(to, lore.get(0), "");
                    else if (lore.size() >= 2)
                        ClanLand.setCoisasSafe(to, lore.get(0), lore.get(1));
                }
            }

            ClanLand.update(who, to);

            return true;
        }

        return true;

    }

    public static double random() {
        double rd = Math.random() * 2;
        if (rd > 1) rd -= 2;
        return rd;
    }

    public static boolean hasItem(org.bukkit.Material m) {
        return Item.getById(m.getId()) != null;
    }


}
