package nativelevel.guis.spawners.mobs.tipos;

import nativelevel.guis.spawners.SpawnerGUIMain;
import nativelevel.utils.EntityHelp;
import nativelevel.utils.GUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.ArrayList;

public class TiposGUIMonsters extends GUI {

    private CreatureSpawner spawner;

    public TiposGUIMonsters(CreatureSpawner spawner) {
        super(Bukkit.createInventory(null, EntityHelp.tamanhoDoBau(EntityHelp.mobs[1]), "§3MobSPAWNAER, Mobs, Monstros "), spawner);
        this.spawner = spawner;
        cria();

    }

    private void cria() {

        botaVidros();

        ArrayList<ItemStack> monstros = EntityHelp.chocadeira(EntityHelp.mobs[1]);

        for (int x = 0; x < monstros.size(); x++) {
            inventory.setItem(x, monstros.get(x));

            if (((SpawnEggMeta) monstros.get(x).getItemMeta()).getSpawnedType().equals(EntityType.WITHER_SKELETON) ||
                    ((SpawnEggMeta) monstros.get(x).getItemMeta()).getSpawnedType().equals(EntityType.CAVE_SPIDER)) {

                ArrayList<String> itemLore = new ArrayList<>();
                itemLore.add("");
                itemLore.add("§4§lEsse bixo tem problema na hora de spawnar...");
                itemLore.add("");
                ItemStack itemBatata = inventory.getItem(x);
                ItemMeta itemMetaBatata = itemBatata.getItemMeta();
                itemMetaBatata.addEnchant(Enchantment.MENDING, 1, true);
                itemMetaBatata.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                itemMetaBatata.setLore(itemLore);
                itemBatata.setItemMeta(itemMetaBatata);
                inventory.setItem(x, itemBatata);

            }
        }
    }

    @Override
    public void interage(InventoryClickEvent event) {
        super.interage(event);

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        if (inventory.getItem(slot) == null || inventory.getItem(slot).getType().equals(Material.STAINED_GLASS_PANE))
            return;

        spawner.setSpawnedType(((SpawnEggMeta) inventory.getItem(slot).getItemMeta()).getSpawnedType());
        spawner.update(true);

        SpawnerGUIMain.tiraBebe(spawner);

        GUI.open(player, new SpawnerGUIMain(spawner));

    }

}
