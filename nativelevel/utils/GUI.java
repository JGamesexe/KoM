package nativelevel.utils;

import nativelevel.KoM;
import nativelevel.MetaShit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public abstract class GUI {

    public Inventory inventory;
    public boolean cancelaInteract = true;
    protected Object objPrincipal;

    public GUI(Inventory inventory) {
        this.inventory = inventory;
    }

    public GUI(Inventory inventory, Object objPrincipal) {
        this(inventory);
        this.objPrincipal = objPrincipal;
    }

    public static void open(Player player, GUI gui) {

        MetaShit.setMetaObject("guiAberta", player, gui);
        player.openInventory(gui.inventory);

    }

    public void interage(Player player, int slot, InventoryAction inventoryAction) {

    }

    protected void botaVidros() {

        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§0Vazio " + Math.random());
        itemStack.setDurability((short) 8);
        itemStack.setItemMeta(itemMeta);

        for (int x = 0; x < this.inventory.getSize(); x++) {
            inventory.setItem(x, new ItemStack(itemStack));
        }
    }


}
