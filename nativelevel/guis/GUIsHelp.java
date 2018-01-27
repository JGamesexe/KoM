package nativelevel.guis;

import nativelevel.MetaShit;
import nativelevel.utils.GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

public class GUIsHelp {

    public static void clickNoInv(InventoryClickEvent event) {

        Player p = (Player) event.getWhoClicked();

        if (event.getClickedInventory() != null) {
            if (MetaShit.getMetaObject("guiAberta", p) != null) {
                if (event.getClickedInventory().getName().equals(MetaShit.getMetaObject("guiAberta", p))) {
                    event.setCancelled(true);
                    GUI gui = (GUI) MetaShit.getMetaObject("guiAberta", p);
                    gui.interage(p, event.getSlot(), event.getAction());
                    event.setCancelled(gui.cancelaInteract);

                }
            }
        }

    }

    public static void fechaInv(InventoryCloseEvent event) {

        Player p = (Player) event.getPlayer();

        if (event.getInventory().getType().equals(InventoryType.CHEST)) {
            if (MetaShit.getMetaObject("guiAberta", p) != null) {
                MetaShit.setMetaObject("guiAberta", p, null);
            }
        }

    }

}
