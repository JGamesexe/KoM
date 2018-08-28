package nativelevel.guis.guilda;

import nativelevel.KoM;
import nativelevel.conversations.InternalStringPrompts;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.GUI;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.InactivityConversationCanceller;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class GuildaMembrosGUI extends GUI {

    private String viewer;
    private ClanPlayer clanPlayer;
    private Clan clan;
    private boolean isFounder;

    public GuildaMembrosGUI(Player player, Clan clan) {
        super(Bukkit.createInventory(null, 45, "§3§l" + clan.getTag().toUpperCase() + ", §3Menu de Guilda, Membros"));
        this.clanPlayer = ClanLand.manager.getClanPlayer(player) != null && ClanLand.manager.getClanPlayer(player).getTag().equalsIgnoreCase(clan.getTag()) ? ClanLand.manager.getClanPlayer(player) : null;
        this.viewer = clanPlayer != null ? player.getName() : "";
        this.clan = clan;
        this.isFounder = clanPlayer != null && ClanLand.isFounder(player.getUniqueId(), clan.getTag());
        cria();
    }

    private void cria() {

        botaVidros();

        ItemStack[] membros = membros();
        byte[] slots = new byte[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 28, 29, 30};
        byte index = 0;

        for (byte slot : slots) {
            inventory.setItem(slot, membros[index]);
            index++;
        }

        inventory.setItem(34, new ItemStack(Material.PAPER));

    }

    private ItemStack[] membros() {

        ItemStack[] itemStacks = new ItemStack[15];

        ArrayList<String> online = new ArrayList<>();
        ArrayList<String> offline = new ArrayList<>();

        ArrayList<ClanPlayer> onlineCP = new ArrayList<>();
        ArrayList<ClanPlayer> offlineCP = new ArrayList<>();

        for (ClanPlayer cp : clan.getOnlineMembers()) {
            online.add(cp.getName());
            onlineCP.add(cp);
        }

        for (ClanPlayer cp : clan.getAllMembers()) {
            if (!online.contains(cp.getName())) {
                offline.add(cp.getName());
                offlineCP.add(cp);
            }
        }

        int index = 0;

        for (String name : online) {
            ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

            ClanPlayer membro = null;

            for (ClanPlayer cp : onlineCP) {
                if (cp.getName().equalsIgnoreCase(name)) membro = cp;
            }

            skullMeta.setOwner(name);

            if (membro.isLeader()) skullMeta.setDisplayName("§2§n" + name);
            else if (membro.isTrusted()) skullMeta.setDisplayName("§a§n" + name);
            else skullMeta.setDisplayName("§7§n" + name);

            if (name.equalsIgnoreCase(viewer)) {
                skullMeta.setLore(Arrays.asList("","§7Este é você, um pedaço importante para está guilda!",""));
            } else if (clanPlayer != null && clanPlayer.isLeader()) {
                if (membro.isLeader()) {
                    if (isFounder) skullMeta.setLore(Arrays.asList("", "§8Clique, para rebaixar este membro", "§8Pressione com botão de DROP para tirar o membro da Guilda", ""));
                    else skullMeta.setLore(Arrays.asList("", "§8Você não pode fazer nada com outro Lider", ""));
                } else if (membro.isTrusted()) {
                    skullMeta.setLore(Arrays.asList("", "§8Clique, para desconfiar este membro", "§8Clique com Shift, para promover este membro", "§8Pressione com  botão de DROP para tirar o membro da Guilda", ""));
                } else {
                    skullMeta.setLore(Arrays.asList("", "§8Clique, para confiar este membro", "§8Pressione com botão de DROP para tirar o membro da Guilda", ""));
                }
            }

            itemStack.setItemMeta(skullMeta);
            itemStacks[index] = itemStack;

            index++;
        }

        for (String name : offline) {
            ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

            skullMeta.setOwner(name);

            ClanPlayer membro = null;

            for (ClanPlayer cp : offlineCP) {
                if (cp.getName().equalsIgnoreCase(name)) membro = cp;
            }

            if (membro.isLeader()) skullMeta.setDisplayName("§2" + name);
            else if (membro.isTrusted()) skullMeta.setDisplayName("§a" + name);
            else skullMeta.setDisplayName("§7" + name);

            if (name.equalsIgnoreCase(viewer)) {
                skullMeta.setLore(Arrays.asList("","§7Este é você, um pedaço importante para está guilda!",""));
            } else if (clanPlayer != null && clanPlayer.isLeader()) {
                if (membro.isLeader()) {
                    if (isFounder) skullMeta.setLore(Arrays.asList(" ", "§8Clique, para rebaixar este membro", "§8Pressione com botão de DROP para tirar o membro da Guilda", ""));
                    else skullMeta.setLore(Arrays.asList(" ", "§8Você não pode fazer nada com outro Lider", ""));
                } else if ((membro.isTrusted())) {
                    skullMeta.setLore(Arrays.asList("", "§8Clique, para desconfiar este membro", "§8Clique com Shift, para promover este membro", "§8Pressione com botão de DROP para tirar o membro da Guilda", ""));
                } else {
                    skullMeta.setLore(Arrays.asList("", "§8Clique, para confiar este membro", "§8Pressione com botão de DROP para tirar o membro da Guilda", ""));
                }
            }

            itemStack.setItemMeta(skullMeta);
            itemStacks[index] = itemStack;

            index++;
        }

        index = 0;

        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null) {

                itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

//              skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString("606e2ff0-ed77-4842-9d6c-e1d3321c7838")));
                skullMeta.setOwner("MHF_Question");
                skullMeta.setDisplayName("§eSem membro...");

                if (clanPlayer != null && clanPlayer.isLeader()) skullMeta.setLore(Arrays.asList("", "§8Clique aqui para convidar um membro para sua guilda", ""));

                itemStack.setItemMeta(skullMeta);

                itemStacks[index] = itemStack;
            }
            index++;
        }


        return itemStacks;
    }

    @Override
    public void interage(InventoryClickEvent event) {
        super.interage(event);

        Player player = (Player) event.getWhoClicked();
        byte slot = (byte) event.getSlot();
        ItemStack itemStack = inventory.getItem(slot);

        if (itemStack == null || itemStack.getType() == Material.AIR) return;

        if (clanPlayer == null || !clanPlayer.isLeader()) return;

        if (slot == 34) return;

        if (itemStack.getType().equals(Material.SKULL_ITEM)) {
            if (itemStack.getItemMeta().getDisplayName().contains("Sem membro...")) {
                Conversation conv = InternalStringPrompts.createConversation(player, "addMembroGuilda");

                conv.getContext().setSessionData("clan", clan);
                conv.begin();
                player.closeInventory();
            } else {
                String nick = ((SkullMeta) itemStack.getItemMeta()).getOwner();

                for (ClanPlayer cp : clan.getAllMembers()) {
                    if (cp.getName().equalsIgnoreCase(nick)) {

                        if (cp.getCleanName().equalsIgnoreCase(viewer)) return;
                        if (cp.isLeader() && !isFounder) return;

                        InventoryAction action = event.getAction();

                        if (action == InventoryAction.PICKUP_ALL || action == InventoryAction.PICKUP_HALF) {

                            if (cp.isLeader()) cp.setLeader(false);
                            else if (cp.isTrusted()) cp.setTrusted(false);
                            else cp.setTrusted(true);

                        } else if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {

                            if (cp.isLeader()) cp.setLeader(false);
                            else cp.setLeader(true);

                        } else if (action == InventoryAction.DROP_ONE_SLOT || action == InventoryAction.DROP_ALL_SLOT) {
                            clan.removePlayerFromClan(cp.getUniqueId());
                        } else {
                            return;
                        }

                        player.closeInventory();

                        GUI.open(player, new GuildaMembrosGUI(player, clan));

                    }
                }
            }
        }

    }

}
