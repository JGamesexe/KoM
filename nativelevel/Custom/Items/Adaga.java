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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Adaga extends CustomItem {

    public Adaga() {
        super(Material.IRON_SWORD, L.m("Adaga da Ponta Diamantada"), L.m("Ataque furtivo"), CustomItem.RARO);
    }

    @Override
    public boolean onItemInteract(Player p) {
        return true;
    }

    public static boolean isAdaga(ItemStack itemStack) {

        if (itemStack != null
                && itemStack.getType() == Material.IRON_SWORD
                && itemStack.getItemMeta() != null
                && itemStack.getItemMeta().getLore() != null
                && !itemStack.getItemMeta().getLore().isEmpty()) {
            for (String lore : itemStack.getItemMeta().getLore()) {
                if (lore.contains(":Adaga da Ponta Diamantada")) return true;
            }
        }

        return false;

    }
}
