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

public class ItemRaroAleatorio extends CustomItem {

    public ItemRaroAleatorio() {
        super(Material.CHEST, L.m("Item Raro Aleatorio"), L.m("Um item bom aleatorio"), CustomItem.RARO);

    }

    @Override
    public boolean onItemInteract(Player p) {
        p.getInventory().setItemInMainHand(EquipGenerator.gera(Raridade.Raro, p.getLevel()));
        return true;
    }

}
