package nativelevel.guis.guilda;

import nativelevel.KoM;
import nativelevel.MetaShit;
import nativelevel.conversations.InternalStringPrompts;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.GUI;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class GuildaGUI extends GUI {

    private ClanPlayer clanPlayer;
    private UUID founder;
    private boolean isFounder;
    private Clan clan;

    //TODO NÃO IR POR BANCO SEMPRE QUE ABRIR GUI
    public GuildaGUI(Player player, Clan clan) {
        super(Bukkit.createInventory(null, 45, "§3§l" + clan.getTag().toUpperCase() + ", §3Menu de Guilda."));
        this.clanPlayer = ClanLand.manager.getClanPlayer(player) != null && ClanLand.manager.getClanPlayer(player).getTag().equalsIgnoreCase(clan.getTag()) ? ClanLand.manager.getClanPlayer(player) : null;
        this.founder = ClanLand.getFounder(clan.getTag());
        this.isFounder = clanPlayer != null && founder.equals(player.getUniqueId());
        this.clan = clan;
        cria();
    }

    private void cria() {

        botaVidros();

        inventory.setItem(10, poder());
        inventory.setItem(12, guilda());
        if (clanPlayer != null) inventory.setItem(13, sair());
        inventory.setItem(15, membros());
        inventory.setItem(28, terreno());
        if (clanPlayer != null) inventory.setItem(30, home());
        inventory.setItem(32, alidos());
        inventory.setItem(34, inimigos());

    }

    private ItemStack membros() {

        ItemStack itemStack = new ItemStack(Material.ITEM_FRAME);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§eMembros");

        itemMeta.setLore(Arrays.asList(
                "",
                "§7Clique para visualizar os membros de sua Guilda",
                ""));

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    //TODO INIMIGOS ALIADOS RIVALS AND ALIES
    private ItemStack alidos() {

        ItemStack itemStack = new ItemStack(Material.CONCRETE_POWDER, 1, (short) 5);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§eAliados");

        itemMeta.setLore(Arrays.asList("", "§7Clique para visualizar os aliados de sua Guilda", ""));

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack inimigos() {

        ItemStack itemStack = new ItemStack(Material.CONCRETE_POWDER, 1, (short) 14);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§eInimigos");

        itemMeta.setLore(Arrays.asList("", "§7Clique para visualizar os inimigos de sua Guilda", ""));

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack poder() {

        ItemStack itemStack = new ItemStack(Material.CLAY);
        ItemMeta itemMeta = itemStack.getItemMeta();

        int poder = ClanLand.getPoder(clan.getTag());

        if (poder == 0) itemMeta.setDisplayName("§7§l?§e Poder Disponivel");
        else itemMeta.setDisplayName("§6§l?§e Poder Disponivel");

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack terreno() {

        ItemStack itemStack = new ItemStack(Material.BOOK);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§eInfo's Territoriais");

        itemMeta.setLore(Arrays.asList(
                "",
                "§7Total de Terrenos, §f?",
                "§7Terrenos Primários, §f?",
                "§7Terrenos de Poder, §f?",
                ""));

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack guilda() {

        ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§a" + clan.getName());

        itemMeta.setLore(Arrays.asList(
                "",
                "§7TAG: " + clan.getColorTag(),
                "§7KDR:§f " + clan.getTotalKDR(),
                "§7Membros:§f " + clan.getAllMembers().size() + " de 15",
                "§7Fundada em:§f " + clan.getFounded(),
                "§7Fundada por:§f " + KoM.database.pegaNick(founder),
                ""));

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack terreno(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        int terrenosPrimarios = ClanLand.getQtdTerrenos(clan.getTag(), true);
        int terrenosPoder = ClanLand.getQtdTerrenos(clan.getTag(), false);
        int terrenosTotal = terrenosPoder + terrenosPrimarios;

        itemMeta.setLore(Arrays.asList(
                "",
                "§7Total de Terrenos, §f" + terrenosTotal,
                "§7Terrenos Primários, §f" + terrenosPrimarios,
                "§7Terrenos de Poder, §f" + terrenosPoder,
                ""));

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack poder(ItemStack itemStack) {

        ItemMeta itemMeta = itemStack.getItemMeta();

        int poder = ClanLand.getPoder(clan.getTag());

        if (poder == 0) itemMeta.setDisplayName("§7§l§n" + poder + "§e Poder Disponivel");
        else itemMeta.setDisplayName("§6§l§n" + poder + "§e Poder Disponivel");

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack sair() {

        ItemStack itemStack = new ItemStack(Material.IRON_DOOR);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§eDeixar a Guilda");

        if (isFounder) itemMeta.setLore(Arrays.asList("", "§8Clique com o botão [7] para desmanchar está guilda", ""));
        else itemMeta.setLore(Arrays.asList("", "§8Clique com o botão [5] para deixar a Guilda", ""));

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack home() {

        ItemStack itemStack = new ItemStack(Material.EYE_OF_ENDER);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§eTeleporte para Guilda");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§7Clique para começar um Teleporte para sua guilda");
        if (clanPlayer.isLeader()) {
            lore.add("");
            lore.add("§8Aperte o botão de DROP para definir um ponto de teleporte da guilda");
        }
        lore.add("");

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public void interage(InventoryClickEvent event) {
        super.interage(event);

        Player player = (Player) event.getWhoClicked();
        byte slot = (byte) event.getSlot();

        if (slot == 13) {
            Conversation conv = null;
            if (isFounder && event.getClick().isKeyboardClick() && event.getHotbarButton() == 6) conv = InternalStringPrompts.createConversation(player, "desbandarGuilda");
            else if (event.getClick().isKeyboardClick() && event.getHotbarButton() == 4) conv = InternalStringPrompts.createConversation(player, "sairDaGuilda");
            if (conv != null) {
                conv.begin();
                player.closeInventory();
                player.updateInventory();
            }
        } else if (slot == 15) {
            GUI.open(player, new GuildaMembrosGUI(player, clan));
        } else if (slot == 10 && poder().equals(inventory.getItem(10))) {
            inventory.setItem(10, poder(inventory.getItem(10)));
        } else if (slot == 28 && terreno().equals(inventory.getItem(28))) {
            inventory.setItem(28, terreno(inventory.getItem(28)));
        } else if (slot == 30 && clanPlayer != null) {
            if (clanPlayer.isLeader() && (event.getAction().equals(InventoryAction.DROP_ALL_SLOT) || event.getAction().equals(InventoryAction.DROP_ONE_SLOT))) {
                Clan clanAt = ClanLand.getClanAt(player.getLocation());
                if (clan != null && clan.getTag().equalsIgnoreCase(clanAt.getTag())) {
                    clan.addBb(player.getName(), "Local de Teleporte da guilda alterado por " + player.getName());
                    clan.setHomeLocation(player.getLocation());
                } else {
                    player.sendMessage("§bVocê só pode setar um local de teleporte em um terreno de sua guilda.");
                }
            } else if (clan.getHomeLocation() != null) {
                boolean teleport = true;
                if (player.hasMetadata("teleportGuilda")) teleport = ((long) MetaShit.getMetaObject("teleportGuilda", player)) < System.currentTimeMillis();
                if (teleport) {
                    TeleportToGuilda run = new TeleportToGuilda(player, clan.getHomeLocation());
                    run.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(KoM._instance, run, 20, 20);
                } else {
                    player.sendMessage("§bAguarde um pouco para se teleportar...");
                }
            } else {
                player.sendMessage("§bGuilda sem local para teleporte...");
            }
        } else if (slot == 32) {
            GUI.open(player, new GuildaAllysGUI(clan, clanPlayer));
        } else if (slot == 34) {
            GUI.open(player, new GuildaRivalsGUI(clan, clanPlayer));
        }

    }

    public static class TeleportToGuilda implements Runnable {
        private UUID uuid;
        private int taskId;

        private int seconds;
        private Location startLocation;
        private Location to;

        private TeleportToGuilda(Player player, Location to) {
            seconds = 1;
            this.uuid = player.getUniqueId();
            this.startLocation = player.getLocation();
            this.to = to;
            MetaShit.setMetaObject("teleportGuilda", player, (System.currentTimeMillis() + (20 * 1000)));
            player.sendMessage("§BCanalizando teleporte, aguarde 10 segundos...");
        }

        @Override
        public void run() {

            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                Bukkit.getScheduler().cancelTask(taskId);
                return;
            }

            if (player.isDead()) {
                Bukkit.getScheduler().cancelTask(taskId);
                return;
            }

            if (player.getLocation().distance(startLocation) > 0.3) {
                Bukkit.getScheduler().cancelTask(taskId);
                player.sendMessage("§bVocê perdeu o foco do seu teleporte se mexendo...");
                return;
            }

            if (seconds <= 10) {
                if (seconds == 10) {
                    Bukkit.getScheduler().cancelTask(taskId);
                    player.sendMessage("§bVocê foi teleportado para sua guilda!");
                    player.teleport(to);
                } else {
                    player.sendMessage("§BCanalizando teleporte, aguarde " + (10 - seconds) + " segundos...");
                    seconds++;
                }
            }

        }


    }

}
