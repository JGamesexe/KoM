package nativelevel.guis.terreno;

import nativelevel.conversations.InternalStringPrompts;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.GUI;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TerrenoPublicoGUI extends GUI {

    private Location location;
    private ClanPlayer clanPlayer;

    public TerrenoPublicoGUI(Player player) {
        super(Bukkit.createInventory(null, 27, "§f§l" + ClanLand.manager.getClanPlayer(player).getTag().toUpperCase() + "§3 " + ClanLand.tipoTerreno(player.getLocation()).text + ", Públi"));
        this.location = player.getLocation();
        this.clanPlayer = ClanLand.manager.getClanPlayer(player);
        cria();
    }

    public void cria() {

        botaVidros();

        inventory.setItem(11, permItem());
        if (ClanLand.isTerrenoPoder(location)) inventory.setItem(13, new ItemStack(Material.ANVIL));
        inventory.setItem(15, terrenoItem());

    }

    private ItemStack terrenoItem() {

        ItemStack itemStack = new ItemStack(Material.NOTE_BLOCK);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§eTerreno Público");

        if (clanPlayer.isLeader()) {
            ArrayList<String> lore = new ArrayList<>();
            lore.add("");
            lore.add("§8Clique para tornar alguém dono deste terreno.");
            lore.add("");
            lore.add("§8Para desconquistar pressione o número [9]");
            lore.add("");
            itemMeta.setLore(lore);
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack permItem() {

        ItemStack itemStack = new ItemStack(Material.SLIME_BALL);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§ePermissões do Terreno");

        ArrayList<String> lore = new ArrayList<>();

        int level = ClanLand.getPermLevel(location);

        switch (level) {
            default:
                lore.add("§c§l§n§oChama a staff ai doido...");
                lore.add("§c§l§n§oChama a staff ai doido...");
                lore.add("§c§l§n§oChama a staff ai doido...");
                break;
            case 0:
                itemStack.setType(Material.MAGMA_CREAM);
                lore.add("");
                lore.add("§7Confiáveis, não podem fazer nada.");
                lore.add("");
                lore.add("§7Desconfiáveis, não podem fazer nada.");
                lore.add("");
                break;
            case 1:
                itemStack.setType(Material.ENDER_PEARL);
                lore.add("");
                lore.add("§7Confiáveis, podem interagir com");
                lore.add("§7redstone e portas.");
                lore.add("");
                lore.add("§7Desconfiáveis, não podem fazer nada.");
                lore.add("");
                break;
            case 2:
                itemStack.setType(Material.EYE_OF_ENDER);
                lore.add("");
                lore.add("§7Confiáveis, podem interagir com");
                lore.add("§7redstone e portas.");
                lore.add("");
                lore.add("§7Desconfiáveis, podem interagir com");
                lore.add("§7redstone e portas.");
                lore.add("");
                break;
            case 3:
                itemStack.setType(Material.ENDER_PEARL);
                lore.add("");
                lore.add("§7Confiáveis, podem interagir com");
                lore.add("§7redstone, portas, baús, fornalhas");
                lore.add("§7e estantes de poção.");
                lore.add("");
                lore.add("§7Desconfiáveis, podem interagir com");
                lore.add("§7redstone e portas.");
                lore.add("");
                break;
            case 4:
                itemStack.setType(Material.EYE_OF_ENDER);
                lore.add("");
                lore.add("§7Confiáveis, podem interagir com");
                lore.add("§7redstone, portas, baús, fornalhas");
                lore.add("§7e estantes de poção.");
                lore.add("");
                lore.add("§7Desconfiáveis, podem interagir com");
                lore.add("§7redstone, portas, baús, fornalhas");
                lore.add("§7e estantes de poção.");
                lore.add("");
                break;
            case 5:
                itemStack.setType(Material.ENDER_PEARL);
                lore.add("");
                lore.add("§7Confiáveis, podem fazer tudo.");
                lore.add("");
                lore.add("§7Desconfiáveis, podem interagir com");
                lore.add("§7redstone, portas, baús, fornalhas");
                lore.add("§7e estantes de poção.");
                lore.add("");
                break;
            case 6:
                itemStack.setType(Material.EYE_OF_ENDER);
                lore.add("");
                lore.add("§7Confiáveis, podem fazer tudo.");
                lore.add("");
                lore.add("§7Desconfiáveis, podem fazer tudo.");
                lore.add("");
                break;
        }

        if (clanPlayer.isLeader()) {
            lore.add("§8Clique para alterar as permissões.");
            lore.add("");
        }

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public void interage(InventoryClickEvent event) {
        super.interage(event);

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        if (!clanPlayer.isLeader()) return;

        switch (slot) {
            case 11:
                ClanLand.changePermLevel(location);
                inventory.setItem(11, permItem());
                player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 0.5f, 2f);

                return;
            case 15:
                Conversation conv;

                if (event.getClick().isKeyboardClick() && event.getHotbarButton() == 8)
                    conv = InternalStringPrompts.createConversation(player, "terrenoDesconquistar");
                else
                    conv = InternalStringPrompts.createConversation(player, "setTerrenoOwner");

                conv.getContext().setSessionData("location", location);
                conv.begin();
                player.closeInventory();
                player.updateInventory();
        }
    }

}
