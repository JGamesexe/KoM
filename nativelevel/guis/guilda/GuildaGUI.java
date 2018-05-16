package nativelevel.guis.guilda;

import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.GUI;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GuildaGUI extends GUI {

    private ClanPlayer clanPlayer;
    private Clan clan;

    public GuildaGUI(Player player, Clan clan) {
        super(Bukkit.createInventory(null, 45, "§3§l" + clan.getTag().toUpperCase() + ", §3Menu de Guilda."));
        this.clanPlayer = ClanLand.manager.getClanPlayer(player);
        this.clan = clan;
        cria();
    }

    private void cria() {

        botaVidros();

        inventory.setItem(10, poder());
        inventory.setItem(15, membros());
        inventory.setItem(21, guilda());
        inventory.setItem(28, terreno());
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

    private ItemStack alidos() {

        ItemStack itemStack = new ItemStack(Material.CONCRETE_POWDER, 1, (short) 5);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§eAliados");

        itemMeta.setLore(Arrays.asList(
                "",
                "§7Clique para visualizar os aliados de sua Guilda",
                ""));

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack inimigos() {

        ItemStack itemStack = new ItemStack(Material.CONCRETE_POWDER, 1, (short) 14);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§eInimigos");

        itemMeta.setLore(Arrays.asList(
                "",
                "§7Clique para visualizar os inimigos de sua Guilda",
                ""));

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
                "§7Fundada por:§f JGamesexe",
                ""));

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    //Não ir pro banco sempre que abre a gui
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

    //Não ir pro banco sempre que abre a gui
    private ItemStack poder(ItemStack itemStack) {

        ItemMeta itemMeta = itemStack.getItemMeta();

        int poder = ClanLand.getPoder(clan.getTag());

        if (poder == 0) itemMeta.setDisplayName("§7§l§n" + poder + "§e Poder Disponivel");
        else itemMeta.setDisplayName("§6§l§n" + poder + "§e Poder Disponivel");

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    //TODO INIMIGOS ALIADOS RIVALS AND ALIES
    @Override
    public void interage(InventoryClickEvent event) {
        super.interage(event);

        Player player = (Player) event.getWhoClicked();
        byte slot = (byte) event.getSlot();

        if (slot == 15) GUI.open(player, new GuildaMembrosGUI(player, clan));
        else if (slot == 10 && poder().equals(inventory.getItem(10)))
            inventory.setItem(10, poder(inventory.getItem(10)));
        else if (slot == 28 && terreno().equals(inventory.getItem(28)))
            inventory.setItem(28, terreno(inventory.getItem(28)));


    }
}
