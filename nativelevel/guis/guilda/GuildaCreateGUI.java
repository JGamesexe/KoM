package nativelevel.guis.guilda;

import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import nativelevel.Custom.Items.CriaGuilda;
import nativelevel.KoM;
import nativelevel.conversations.InternalStringPrompts;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.GUI;
import net.md_5.bungee.api.ChatColor;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class GuildaCreateGUI extends GUI {

    private String name;
    private String tag;

    public GuildaCreateGUI(String name, String tag) {
        super(Bukkit.createInventory(null, 36, "§3§lFundação de Guilda!"));
        this.name = name;
        this.tag = tag;
        cria();
    }

    private void cria() {
        botaVidros();

        inventory.setItem(10, setName());
        inventory.setItem(16, setTag());
        inventory.setItem(19, geraItem(Material.STAINED_GLASS_PANE, (short) 15));

        if (name.equalsIgnoreCase("")) inventory.setItem(20, geraItem(Material.STAINED_GLASS_PANE, (short) 14));
        else inventory.setItem(20, geraItem(Material.STAINED_GLASS_PANE, (short) 5));

        inventory.setItem(22, createGuilda());

        if (tag.equalsIgnoreCase("")) inventory.setItem(24, geraItem(Material.STAINED_GLASS_PANE, (short) 14));
        else inventory.setItem(24, geraItem(Material.STAINED_GLASS_PANE, (short) 5));

        inventory.setItem(25, geraItem(Material.STAINED_GLASS_PANE, (short) 15));
    }

    private ItemStack setName() {
        ItemStack itemStack = new ItemStack(Material.EMPTY_MAP);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§eDefinir Nome");
        itemMeta.setLore(name.equalsIgnoreCase("") ? Arrays.asList("", "§7Não Aceita Código de Cores", "") : Arrays.asList("§b" + name, "", "§7Não Aceita Código de Cores", ""));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack setTag() {
        ItemStack itemStack = new ItemStack(Material.NAME_TAG);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§eDefinir TAG");
        itemMeta.setLore(tag.equalsIgnoreCase("") ? Arrays.asList("", "§7Aceita Código de §3C§eO§cR§6E§5S", "") : Arrays.asList("", ChatColor.translateAlternateColorCodes('&', tag), "", "§7Aceita Código de §3C§eO§cR§6E§5S", ""));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack createGuilda() {
        ItemStack itemStack = new ItemStack(Material.BOOK_AND_QUILL);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§eCriar Guilda!");

        if (!name.equalsIgnoreCase("") && !tag.equalsIgnoreCase("")) {
            itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        itemMeta.setLore(Arrays.asList(
                "",
                "§7Nome: §b" + (name.equalsIgnoreCase("") ? "§cINDEFINIDO" : name),
                "§7TAG: §8" + (tag.equalsIgnoreCase("") ? "§cINDEFINIDO" : ChatColor.translateAlternateColorCodes('&', tag)),
                ""));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public void interage(InventoryClickEvent event) {
        super.interage(event);

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        if (slot == 10 || slot == 16) {
            Conversation conv = InternalStringPrompts.createConversation(player, "createGuilda");

            if (slot == 10) conv.getContext().setSessionData("tipo", "name");
            else conv.getContext().setSessionData("tipo", "tag");

            conv.getContext().setSessionData("name", name);
            conv.getContext().setSessionData("tag", tag);
            conv.begin();
            player.closeInventory();

        } else if (slot == 22) {
            if (!name.equalsIgnoreCase("") && !tag.equalsIgnoreCase("")) {
                Clan clan = ClanLand.getClan(tag.replaceAll("[&§][0-9a-fk-or]", ""));
                if (clan == null) {
                    if (CriaGuilda.createGuilda(player)) {
                        ClanLand.manager.createClan(player, tag, name);
                        clan = ClanLand.getClan(tag.replaceAll("[&§][0-9a-fk-or]", ""));
                        if (clan != null) clan.verifyClan();
                        ClanLand.setPoder(tag.replaceAll("[&§][0-9a-fk-or]", ""), 5);
                        ClanLand.setFounder(tag.replaceAll("[&§][0-9a-fk-or]", ""), player.getUniqueId());
                        KoM.announce("§bA guilda " + name + " foi fundada por " + player.getName());
                        doObjective(player);
                    } else {
                        player.sendMessage("§cAcho que não em =D");
                    }
                } else {
                    player.sendMessage("§cInfelizmente criaram uma guilda com a sua TAG antes de você...");
                }
                player.closeInventory();
            }
        }
    }

    private void doObjective(Player player) {
        Quester quester = KoM.quests.getQuester(player.getUniqueId());
        if (quester == null) return;
        for (Quest quest : quester.currentQuests.keySet())
            if (quester.hasCustomObjective(quest, "Chega em Zona"))
                CustomObjective.incrementObjective(player, objective, 1, quest);
    }

    private static final CustomObjective objective = makeCustomObjective();

    private static CustomObjective makeCustomObjective() {
        CustomObjective objective = new CustomObjective() {
        };
        objective.setName("Cria guilda");
        return objective;
    }

}
