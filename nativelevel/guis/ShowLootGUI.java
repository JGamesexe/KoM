package nativelevel.guis;

import com.mysql.fabric.xmlrpc.base.Array;
import nativelevel.KoM;
import nativelevel.phatloots.PhatLoots;
import nativelevel.phatloots.loot.*;
import nativelevel.utils.GUI;
import net.minecraft.server.v1_12_R1.LootItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ShowLootGUI extends GUI {

    private String lootName;

    private List<Item> lootItems = new ArrayList<>();
    private List<LootCollection> lootCollections = new ArrayList<>();
    private List<Experience> lootExperiences = new ArrayList<>();
    private List<Money> lootMoneys = new ArrayList<>();

    public ShowLootGUI(List<Loot> lootList, String topName, String lootName) {
        super(Bukkit.createInventory(null, 54, "§8" + topName + "§0§n" + lootName));
        this.lootName = lootName;

        for (Loot loot : lootList) {
            if (loot instanceof Item) lootItems.add((Item) loot);
            else if (loot instanceof LootCollection) lootCollections.add((LootCollection) loot);
            else if (loot instanceof Experience) lootExperiences.add((Experience) loot);
            else if (loot instanceof Money) lootMoneys.add((Money) loot);
        }

        cria();
    }

    private void cria() {

        int slot = 0;

        for (LootCollection collection : lootCollections) {
            if (slot > 53) return;
            inventory.setItem(slot, collecttion(collection));
            slot++;
        }

        for (Experience experience : lootExperiences) {
            if (slot > 53) return;
            inventory.setItem(slot, experience(experience));
            slot++;
        }

        for (Money money : lootMoneys) {
            if (slot > 53) return;
            inventory.setItem(slot, money(money));
            slot++;
        }

        for (Item item : lootItems) {
            if (slot > 53) return;
            inventory.setItem(slot, item(item));
            slot++;
        }

    }

    private ItemStack collecttion(LootCollection lootCollection) {
        ItemStack ss = new ItemStack(Material.ENDER_CHEST);
        ItemMeta meta = ss.getItemMeta();

        meta.setDisplayName("§dColeção de Itens");

        List<String> lore = new ArrayList<>();

        lore.add("");
        lore.add("§8Probabilidade: §f" + ((int) lootCollection.getProbability()) + "%");
        lore.add("§8Quantidade Max. de Loots: §f" + lootCollection.upperNumberOfLoots);
        lore.add("§8Quantidade Min. de Loots: §f" + lootCollection.lowerNumberOfLoots);

        meta.setLore(lore);

        ss.setItemMeta(meta);
        return ss;
    }

    private ItemStack experience(Experience experience) {

        ItemStack ss = new ItemStack(Material.EXP_BOTTLE);
        ItemMeta meta = ss.getItemMeta();

        meta.setDisplayName("§eExperiência");

        List<String> lore = new ArrayList<>();

        lore.add("");
        lore.add("§8Probabilidade: §f" + ((int) experience.getProbability()) + "%");
        lore.add("§8Quantidade Máxima: §f" + experience.upperAmount);
        lore.add("§8Quantidade Mínima: §f" + experience.lowerAmount);

        meta.setLore(lore);

        ss.setItemMeta(meta);
        return ss;

    }

    private ItemStack money(Money money) {

        ItemStack ss = new ItemStack(Material.EMERALD);
        ItemMeta meta = ss.getItemMeta();

        meta.setDisplayName("§aEsmeraldas");

        List<String> lore = new ArrayList<>();

        lore.add("");
        lore.add("§8Probabilidade: §f" + ((int) money.getProbability()) + "%");
        lore.add("§8Quantidade Máxima: §f" + money.upperAmount);
        lore.add("§8Quantidade Mínima: §f" + money.lowerAmount);

        meta.setLore(lore);

        ss.setItemMeta(meta);
        return ss;

    }

    private ItemStack item(Item item) {

        ItemStack ss = new ItemStack(item.item.getType());
        ItemMeta meta = item.item.getItemMeta() != null ? item.item.getItemMeta() : ss.getItemMeta();

        List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();

        lore.add("");
        lore.add("§8Probabilidade: §f" + ((int) item.getProbability()) + "%");
        lore.add("§8Quantidade Máxima: §f" + item.item.getAmount());
        lore.add("§8Quantidade Mínima: §f" + (item.item.getAmount() + item.amountBonus));

        meta.setLore(lore);

        ss.setItemMeta(meta);
        return ss;

    }

    private boolean isCollection(ItemStack ss) {
        if (ss == null || ss.getType() != Material.ENDER_CHEST) return false;
        if (!ss.getItemMeta().getDisplayName().contains("Coleção")) return false;
        return true;
    }

    @Override
    public void interage(InventoryClickEvent event) {
        super.interage(event);

        if (isCollection(inventory.getItem(event.getSlot())))
            GUI.open((Player) event.getWhoClicked(), new ShowLootGUI(lootCollections.get(event.getSlot()).getLootList(), "Ex. de Coleção do LooT ", lootName));

    }
}
