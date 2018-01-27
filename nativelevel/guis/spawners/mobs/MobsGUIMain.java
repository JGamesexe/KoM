package nativelevel.guis.spawners.mobs;

import nativelevel.guis.spawners.mobs.tipos.TiposGUIAnimals;
import nativelevel.guis.spawners.mobs.tipos.TiposGUIMonsters;
import nativelevel.guis.spawners.mobs.tipos.TiposGUIOutros;
import nativelevel.utils.GUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MobsGUIMain extends GUI {

    private CreatureSpawner spawner;

    public MobsGUIMain(CreatureSpawner spawner) {
        super(Bukkit.createInventory(null, 9, "§3MobSPAWNAER §l| §3Mobs"), spawner);

        this.spawner = (CreatureSpawner) spawner.getBlock().getState();
        cria();

    }

    private void cria() {

        botaVidros();

        inventory.setItem(2, animais());
        inventory.setItem(4, monstros());
        inventory.setItem(6, outros());

    }

    private ItemStack animais() {
        ItemStack itemStack = new ItemStack(Material.RAW_BEEF);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§eAnimais");

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack monstros() {
        ItemStack itemStack = new ItemStack(Material.ROTTEN_FLESH);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§eMonstros");

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack outros() {
        ItemStack itemStack = new ItemStack(Material.CLAY_BALL);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§eOutros");

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public void interage(Player player, int slot, InventoryAction inventoryAction) {
        super.interage(player, slot, inventoryAction);

        if (inventory.getItem(slot) == null || inventory.getItem(slot).getType().equals(Material.STAINED_GLASS_PANE)) return;

        switch (slot) {
            case 2:
                open(player, new TiposGUIAnimals(spawner));
                break;
            case 4:
                open(player, new TiposGUIMonsters(spawner));
                break;
            case 6:
                open(player, new TiposGUIOutros(spawner));
                break;
        }
    }

}
