package nativelevel.guis.terreno;

import nativelevel.KoM;
import nativelevel.conversations.InternalStringPrompt;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.GUI;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.InactivityConversationCanceller;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class TerrenoPrivadoGUI extends GUI {

    private Location location;
    private Player playerBase;
    private ClanPlayer clanPlayer;
    private String[] owner;

    public TerrenoPrivadoGUI(Player player) {
        super(Bukkit.createInventory(null, 54, "§f§l" + ClanLand.manager.getClanPlayer(player).getTag().toUpperCase() + " §3" + ClanLand.tipoTerreno(player.getLocation()) + ", Privado"));
        this.location = player.getLocation();
        this.playerBase = player;
        this.clanPlayer = ClanLand.manager.getClanPlayer(player);
        this.owner = ClanLand.getOwnerAt(location);
        cria();
    }

    public void cria() {

        botaVidros();

        inventory.setItem(10, new ItemStack(Material.STAINED_GLASS, 1, (short) 3));
        inventory.setItem(11, new ItemStack(Material.STAINED_GLASS, 1, (short) 3));
        inventory.setItem(12, new ItemStack(Material.STAINED_GLASS, 1, (short) 9));
        inventory.setItem(13, donoItem());
        inventory.setItem(14, new ItemStack(Material.STAINED_GLASS, 1, (short) 9));
        inventory.setItem(15, new ItemStack(Material.STAINED_GLASS, 1, (short) 3));
        inventory.setItem(16, new ItemStack(Material.STAINED_GLASS, 1, (short) 3));

        ItemStack[] amigos = amigosItens();
        byte[] slots = new byte[]{28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

        byte index = 0;

        for (byte slot : slots) {
            inventory.setItem(slot, amigos[index]);
            index++;
        }

        Bukkit.getPlayer(clanPlayer.getUniqueId()).updateInventory();

    }

    private ItemStack donoItem() {

        ItemStack itemStack = new ItemStack(Material.SEA_LANTERN);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§eTerreno Privado");

        ArrayList<String> lore = new ArrayList<>();

        lore.add("");
        lore.add("§7 - Dono, " + owner[1]);
        lore.add("");

        if (clanPlayer.isLeader()) {
            lore.add("§8  Pressione 'Q' para tornar o");
            lore.add("§8terreno público, ou clique com");
            lore.add("§8o SHIFT para alterar o dono.");
            lore.add("");
        }

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack[] amigosItens() {

        ItemStack[] itemStacks = new ItemStack[14];

        byte index = 0;

        for (Map.Entry<String, UUID> amigo : ClanLand.getMembersAt(location).entrySet()) {

            ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

            skullMeta.setOwner(amigo.getKey());
            skullMeta.setDisplayName("§e" + amigo.getKey());

            if (owner[0].equalsIgnoreCase(clanPlayer.getUniqueId().toString())) {
                ArrayList<String> lore = new ArrayList<>();

                lore.add("");
                lore.add("§8  Clique aqui para remover");
                lore.add("§8 este amigo do seu terreno.");
                lore.add("");
                skullMeta.setLore(lore);
            }

            itemStack.setItemMeta(skullMeta);
            itemStacks[index] = itemStack;
            index++;
        }

        while (index < 14) {

            ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

//          skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString("606e2ff0-ed77-4842-9d6c-e1d3321c7838")));
            skullMeta.setOwner("MHF_Question");
            skullMeta.setDisplayName("§eSem amigo...");

            if (owner[0].equalsIgnoreCase(clanPlayer.getUniqueId().toString())) {
                ArrayList<String> lore = new ArrayList<>();

                lore.add("");
                lore.add("§8  Clique aqui para adicionar");
                lore.add("§8 um amigo ao seu terreno.");
                lore.add("");
                skullMeta.setLore(lore);
            }

            itemStack.setItemMeta(skullMeta);
            itemStacks[index] = itemStack;
            index++;
        }

        return itemStacks;
    }

    public void interage(InventoryClickEvent event) {
        super.interage(event);

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        InventoryAction inventoryAction = event.getAction();

        if (owner[0].equals(player.getUniqueId().toString())) {
            if (inventory.getItem(slot).getType().equals(Material.SKULL_ITEM)) {
                SkullMeta skullMeta = (SkullMeta) inventory.getItem(slot).getItemMeta();
                if (skullMeta.getDisplayName().contains("Sem amigo...")) {

                    Conversation conv = KoM.conversationFactory.
                            withFirstPrompt(new InternalStringPrompt()).
                            withLocalEcho(false).
                            withConversationCanceller(new InactivityConversationCanceller(KoM._instance, 25)).
                            buildConversation(player);

                    conv.getContext().setSessionData("id", "addTerrenoAmigo");
                    conv.getContext().setSessionData("location", location);
                    conv.begin();
                    player.closeInventory();

                } else {
                    ClanLand.removeMemberAt(location, ClanLand.getMembersAt(location).get(skullMeta.getOwner()));
                    cria();
                    player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1f, 1f);
                }
            }
        }

        if (clanPlayer.isLeader()) {
            if (slot == 13) {
                if (inventoryAction.equals(InventoryAction.DROP_ONE_SLOT) || inventoryAction.equals(InventoryAction.DROP_ALL_SLOT)) {

                    ClanLand.setOwnerAt(location, null);
                    GUI gui = new TerrenoPublicoGUI(playerBase);
                    GUI.open(player, gui);

                } else if (event.isShiftClick()) {

                    Conversation conv = KoM.conversationFactory.
                            withFirstPrompt(new InternalStringPrompt()).
                            withLocalEcho(false).
                            withConversationCanceller(new InactivityConversationCanceller(KoM._instance, 25)).
                            buildConversation(player);

                    conv.getContext().setSessionData("id", "setTerrenoOwner");
                    conv.getContext().setSessionData("location", location);
                    conv.begin();
                    player.closeInventory();
                    player.updateInventory();

                } else if (event.getClick().isKeyboardClick() && event.getHotbarButton() == 8) {

                    Conversation conv = KoM.conversationFactory.
                            withFirstPrompt(new InternalStringPrompt()).
                            withLocalEcho(false).
                            withConversationCanceller(new InactivityConversationCanceller(KoM._instance, 25)).
                            buildConversation(player);

                    conv.getContext().setSessionData("id", "terrenoDesconquistar");
                    conv.getContext().setSessionData("location", location);
                    conv.begin();
                    player.closeInventory();
                    player.updateInventory();

                }
            }
        }
    }

}
