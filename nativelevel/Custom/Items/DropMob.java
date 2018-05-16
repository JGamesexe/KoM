/*

 ╭╮╭━╮╱╱╭━╮╭━╮
 ┃┃┃╭╯╱╱┃┃╰╯┃┃
 ┃╰╯╯╭━━┫╭╮╭╮┃
 ┃╭╮┃┃╭╮┃┃┃┃┃┃
 ┃┃┃╰┫╰╯┃┃┃┃┃┃
 ╰╯╰━┻━━┻╯╰╯╰╯

 Desenvolvedor: ZidenVentania
 Colaboradores: NeT32, Gabripj, Feldmann
 Patrocionio: InstaMC

 */
package nativelevel.Custom.Items;

import nativelevel.Custom.CustomItem;
import nativelevel.Lang.L;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class DropMob extends CustomItem {

    public DropMob() {
        super(Material.CHEST, L.m("Bau Envelhecido"), L.m("Um velho bau empoeirado"), CustomItem.RARO);
    }

    @Override
    public boolean onItemInteract(Player p) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "loot give " + p.getName() + " DropMob");
        if (p.getInventory().getItemInMainHand().getAmount() == 1) {
            p.setItemInHand(null);
        } else {
            p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
        }
        p.updateInventory();
        return true;
    }

}
