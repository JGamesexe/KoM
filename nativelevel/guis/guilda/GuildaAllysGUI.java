package nativelevel.guis.guilda;

import nativelevel.Jobs;
import nativelevel.KoM;
import nativelevel.conversations.InternalStringPrompts;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.GUI;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class GuildaAllysGUI extends GUI {

    private static HashMap<String, ArrayList<String>> solicitacaoAlly = new HashMap<>();

    private Clan clan;
    private ClanPlayer clanPlayer;
    int page = 0;

    public GuildaAllysGUI(Clan clan, ClanPlayer clanPlayer) {
        super(Bukkit.createInventory(null, 54, "§f§l" + clan.getTag().toUpperCase() + "§3 Lista de Alidos"));
        this.clan = clan;
        this.clanPlayer = clanPlayer != null && clanPlayer.getTag().equalsIgnoreCase(clan.getTag()) ? clanPlayer : null;
        cria();
    }

    private void cria() {
        botaVidros();

        inventory.setItem(10, geraItem(Material.STAINED_GLASS, (short) 5));
        inventory.setItem(11, geraItem(Material.CONCRETE_POWDER, (short) 5));
        inventory.setItem(12, aliados());
        inventory.setItem(13, geraItem(Material.CONCRETE_POWDER, (short) 5));
        inventory.setItem(14, geraItem(Material.STAINED_GLASS, (short) 5));

        inventory.setItem(16, geraItem(Material.FEATHER));

        pocaAllys();

    }

    private final byte[] slots = new byte[]{28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

    private void pocaAllys() {

        byte index = 0;
        byte pass = (byte) (page * 14);

        if (solicitacaoAlly.containsKey(clan.getTag().toUpperCase())) {
            solicitacaoAlly.get(clan.getTag().toUpperCase()).removeIf(how -> ClanLand.getClan(how) == null);
            for (String how : solicitacaoAlly.get(clan.getTag().toUpperCase())) {
                if (pass > 0) {
                    pass--;
                    continue;
                }
                if (index > 13) return;

                inventory.setItem(slots[index], ally(how, true));
                index++;
            }
        }

        for (String ally : clan.getAllies()) {
            if (pass > 0) {
                pass--;
                continue;
            }
            if (index > 13) return;

            inventory.setItem(slots[index], ally(ally, false));
            index++;
        }

        for (byte slot : slots) {
            if (inventory.getItem(slot).getType().equals(Material.STAINED_GLASS_PANE)) {
                ItemStack ss = Jobs.dado(2) == 2 ? geraItem(Material.CONCRETE_POWDER) : geraItem(Material.WOOL);
                inventory.setItem(slot, ss);
            }
        }

    }

    private ItemStack ally(String how, boolean ask) {
        ItemStack ss = geraItem((Jobs.dado(2) == 2 ? Material.CONCRETE : Material.WOOL), ((short) (ask ? 8 : 5)));
        ItemMeta meta = ss.getItemMeta();

        if (ask) {
            meta.setDisplayName("§7§l" + how.toUpperCase());
            if (clanPlayer != null && clanPlayer.isLeader()) meta.setLore(Arrays.asList("", "§8Clique com o botão de DROP para aceitar o pacto", ""));
            else meta.setLore(Collections.singletonList(""));
        } else {
            meta.setDisplayName("§a§l" + how.toUpperCase());
            if (clanPlayer != null && clanPlayer.isLeader()) meta.setLore(Arrays.asList("", "§8Pressione o botão [5] para quebrar o pacto com está guilda", ""));
            else meta.setLore(Collections.singletonList(""));
        }
        ss.setItemMeta(meta);

        return ss;
    }

    private ItemStack aliados() {
        ItemStack itemStack = geraItem(Material.LIME_SHULKER_BOX);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§eLista de Aliados");
        if (clanPlayer != null && clanPlayer.isLeader()) itemMeta.setLore(Arrays.asList("", "§8Clique para solocitar um pacto com uma guilda", ""));
        else itemMeta.setLore(Collections.singletonList(""));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private static void removeSolicitacao(String own, String how) {
        if (!solicitacaoAlly.containsKey(own.toUpperCase())) return;
        solicitacaoAlly.get(own.toUpperCase()).removeIf(soli -> soli.equalsIgnoreCase(how));
        if (solicitacaoAlly.get(own.toUpperCase()).size() == 0) solicitacaoAlly.remove(own);
    }

    public static void addSolicitacao(String own, String how) {
        if (solicitacaoAlly.containsKey(own.toUpperCase())) {
            solicitacaoAlly.get(own.toUpperCase()).add(how.toUpperCase());
        } else {
            ArrayList<String> list = new ArrayList<>(Collections.singletonList(how.toUpperCase()));
            solicitacaoAlly.put(own.toUpperCase(), list);
        }

    }

    public static boolean hasSend(String how, String to) {
        if (solicitacaoAlly.containsKey(to.toUpperCase())) {
            for (String s : solicitacaoAlly.get(to.toUpperCase()))
                if (s.equalsIgnoreCase(how)) return true;
        }
        return false;
    }

    @Override
    public void interage(InventoryClickEvent event) {
        super.interage(event);

        ItemStack ss = inventory.getItem(event.getSlot());

        if (clanPlayer == null || !clanPlayer.isLeader()) return;

        if (event.getSlot() == 12) {
            event.getWhoClicked().closeInventory();
            InternalStringPrompts.createConversation((Player) event.getWhoClicked(), "addAlly").begin();
            return;
        }

        if (!ss.getType().equals(Material.CONCRETE) && !ss.getType().equals(Material.WOOL)) return;

        if (ss.getDurability() == 8) {
            if (event.getAction().equals(InventoryAction.DROP_ONE_SLOT) || event.getAction().equals(InventoryAction.DROP_ALL_SLOT)) {
                Clan clanRequest = ClanLand.manager.getClan(ChatColor.stripColor(ss.getItemMeta().getDisplayName()));
                removeSolicitacao(clan.getTag(), ChatColor.stripColor(ss.getItemMeta().getDisplayName()));
                if (clanRequest != null) {
                    if (!clanRequest.isAlly(clan.getTag()) && !clanRequest.isRival(clan.getTag())) clan.addAlly(clanRequest);
                } else {
                    event.getWhoClicked().sendMessage("§cEstá guilda já não existe mais");
                }
                cria();
            }
        } else if (ss.getDurability() == 5) {
            if (event.getClick().isKeyboardClick() && event.getHotbarButton() == 4) {
                Clan clanAlly = ClanLand.manager.getClan(ChatColor.stripColor(ss.getItemMeta().getDisplayName()));
                clan.removeAlly(clanAlly);
                cria();
            }
        }

    }

}
