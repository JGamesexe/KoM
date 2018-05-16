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
import nativelevel.Menu.netMenu;
import nativelevel.MetaShit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PedraReborn extends CustomItem {

    public PedraReborn() {
        super(Material.PAPER, L.m("Reset de Classe Mitico"), L.m("Reseta classe sem perder reborn"), CustomItem.EPICO);
    }

    @Override
    public boolean onItemInteract(Player p) {
        MetaShit.setMetaObject("rebornGratiz", p, "1");
        netMenu.escolheClasse(p);
        if (p.getInventory().getItemInMainHand().getAmount() > 1) {
            p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
        } else {
            p.setItemInHand(null);
        }
        return true;
    }

}
