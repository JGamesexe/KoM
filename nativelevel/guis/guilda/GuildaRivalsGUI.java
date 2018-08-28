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

public class GuildaRivalsGUI extends GUI {

    private static HashMap<String, ArrayList<String>> solicitacaoCease = new HashMap<>();

    private Clan clan;
    private ClanPlayer clanPlayer;
    int page = 0;

    public GuildaRivalsGUI(Clan clan, ClanPlayer clanPlayer) {
        super(Bukkit.createInventory(null, 54, "§f§l" + clan.getTag().toUpperCase() + "§3 Lista de Inimigos"));
        this.clan = clan;
        this.clanPlayer = clanPlayer != null && clanPlayer.getTag().equalsIgnoreCase(clan.getTag()) ? clanPlayer : null;
        cria();
    }

    private void cria() {
        botaVidros();

        inventory.setItem(10, geraItem(Material.STAINED_GLASS, (short) 14));
        inventory.setItem(11, geraItem(Material.CONCRETE_POWDER, (short) 14));
        inventory.setItem(12, inimigos());
        inventory.setItem(13, geraItem(Material.CONCRETE_POWDER, (short) 14));
        inventory.setItem(14, geraItem(Material.STAINED_GLASS, (short) 14));

        inventory.setItem(16, geraItem(Material.FEATHER));

        pocaRivals();

    }

    private final byte[] slots = new byte[]{28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

    private void pocaRivals() {

        byte index = 0;
        byte pass = (byte) (page * 14);

        if (solicitacaoCease.containsKey(clan.getTag().toUpperCase())) {
            solicitacaoCease.get(clan.getTag().toUpperCase()).removeIf((String how) -> ClanLand.getClan(how) == null);
            for (String how : solicitacaoCease.get(clan.getTag().toUpperCase())) {
                if (pass > 0) {
                    pass--;
                    continue;
                }

                if (index > 13) return;
                inventory.setItem(slots[index], inimigo(how, true));
                index++;
            }
        }

        for (String rival : clan.getRivals()) {
            if (hasSend(rival, clan.getTag())) continue;
            if (pass > 0) {
                pass--;
                continue;
            }

            if (index > 13) return;
            inventory.setItem(slots[index], inimigo(rival, false));
            index++;
        }

        for (byte slot : slots) {
            if (inventory.getItem(slot).getType().equals(Material.STAINED_GLASS_PANE))
                inventory.setItem(slot, Jobs.dado(2) == 2 ? geraItem(Material.CONCRETE) : geraItem(Material.WOOL));
        }

    }

    private ItemStack inimigo(String how, boolean cease) {
        ItemStack ss = geraItem((Jobs.dado(2) == 2 ? Material.CONCRETE : Material.WOOL), ((short) (cease ? 7 : 14)));
        ItemMeta meta = ss.getItemMeta();

        if (cease) {
            meta.setDisplayName("§7§l" + how.toUpperCase());
            if (clanPlayer != null && clanPlayer.isLeader()) meta.setLore(Arrays.asList("", "§8Clique com o botão de DROP para aceitar o cessar-fogo", ""));
            else meta.setLore(Collections.singletonList(""));
        } else {
            meta.setDisplayName("§a§l" + how.toUpperCase());
            if (clanPlayer != null && clanPlayer.isLeader()) meta.setLore(Arrays.asList("", "§8Pressione o botão [5] para solicitar um cessar-fogo", ""));
            else meta.setLore(Collections.singletonList(""));
        }
        ss.setItemMeta(meta);

        return ss;
    }

    private ItemStack inimigos() {
        ItemStack itemStack = geraItem(Material.RED_SHULKER_BOX);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§eLista de Inimigos");
        if (clanPlayer != null && clanPlayer.isLeader()) itemMeta.setLore(Arrays.asList("", "§8Clique para entrar em guerra com outra guilda", ""));
        else itemMeta.setLore(Collections.singletonList(""));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private static void removeSolicitacao(String own, String how) {
        if (!solicitacaoCease.containsKey(own.toUpperCase())) return;
        solicitacaoCease.get(own.toUpperCase()).removeIf(soli -> soli.equalsIgnoreCase(how));
        if (solicitacaoCease.get(own.toUpperCase()).size() == 0) solicitacaoCease.remove(own);
    }

    private static void addSolicitacao(String own, String how) {
        if (solicitacaoCease.containsKey(own)) {
            solicitacaoCease.get(own.toUpperCase()).add(how.toUpperCase());
        } else {
            ArrayList<String> list = new ArrayList<>(Collections.singletonList(how.toUpperCase()));
            solicitacaoCease.put(own.toUpperCase(), list);
        }

    }

    private static boolean  hasSend(String how, String to) {
        if (solicitacaoCease.containsKey(to.toUpperCase())) {
            for (String s : solicitacaoCease.get(to.toUpperCase()))
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
            InternalStringPrompts.createConversation((Player) event.getWhoClicked(), "addRival").begin();
            return;
        }

        if (!ss.getType().equals(Material.CONCRETE) && !ss.getType().equals(Material.WOOL)) return;

        if (ss.getDurability() == 7) {
            if (event.getAction().equals(InventoryAction.DROP_ONE_SLOT) || event.getAction().equals(InventoryAction.DROP_ALL_SLOT)) {
                Clan clanRequest = ClanLand.manager.getClan(ChatColor.stripColor(ss.getItemMeta().getDisplayName()));
                removeSolicitacao(clan.getTag(), ChatColor.stripColor(ss.getItemMeta().getDisplayName()));
                if (clanRequest != null) {
                    if (clan.isRival(clanRequest.getTag())) clan.removeRival(clanRequest);
                } else {
                    event.getWhoClicked().sendMessage("§cEstá guilda já não existe mais");
                }
                cria();
            }
        } else if (ss.getDurability() == 14) {
            if (event.getClick().isKeyboardClick() && event.getHotbarButton() == 4) {
                Clan clanRival = ClanLand.manager.getClan(ChatColor.stripColor(ss.getItemMeta().getDisplayName()));
                if (clanRival.isRival(clan.getTag())) {
                    if (!hasSend(clan.getTag(), ChatColor.stripColor(ss.getItemMeta().getDisplayName()))) {
                        addSolicitacao(clanRival.getTag(), clan.getTag());
                        event.getWhoClicked().sendMessage("§eCessar-fogo solicitado a guilda " + clanRival.getTag().toUpperCase());
                    } else {
                        event.getWhoClicked().sendMessage("§cJá foi solicitado um cessar-fogo a está guilda");
                    }
                } else {
                    event.getWhoClicked().sendMessage("§aVocês já não são mais inimigos =D");
                }
                cria();
            }
        }

    }

}
