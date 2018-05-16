package nativelevel.guis;

import nativelevel.MetaShit;
import nativelevel.guis.spawners.SpawnerGUIMain;
import nativelevel.utils.GUI;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class GUIsHelp {

    public static void clickNoInv(InventoryClickEvent event) {

        Player p = (Player) event.getWhoClicked();

        if (event.getClickedInventory() != null) {
            if (MetaShit.getMetaObject("guiAberta", p) != null) {
                if (event.getClickedInventory().getName().equals(((GUI) (MetaShit.getMetaObject("guiAberta", p))).inventory.getName())) {
                    event.setCancelled(true);
                    GUI gui = (GUI) MetaShit.getMetaObject("guiAberta", p);
                    gui.interage(event);
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

    public static void interact(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (player.isOp() || player.hasPermission("kom.editspawner")) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (event.getHand().equals(EquipmentSlot.HAND)) {
                    if (event.getClickedBlock().getType().equals(Material.MOB_SPAWNER)) {
                        event.setCancelled(true);
                        GUI gui = new SpawnerGUIMain((CreatureSpawner) event.getClickedBlock().getState());
                        GUI.open(player, gui);
                    }
                }
            }
        }
    }

}
