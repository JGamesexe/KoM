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
import nativelevel.Equipment.Generator.EquipGenerator;
import nativelevel.Lang.L;
import nativelevel.gemas.Raridade;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ItemIncomum extends CustomItem {

    public ItemIncomum() {
        super(Material.CHEST, L.m("Item Incomum Sortido"), L.m("Um item legal aleatorio"), CustomItem.INCOMUM);
    }

    @Override
    public boolean onItemInteract(Player p) {
        p.getInventory().setItemInMainHand(EquipGenerator.gera(Raridade.Incomum, p.getLevel()));
        return true;
    }

}
