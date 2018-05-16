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

public class PergaminhoResetClasse extends CustomItem {

    public PergaminhoResetClasse() {
        super(Material.PAPER, L.m("Reset De Classe Lendario"), L.m("Reseta classe sem perder nivel/reborn"), CustomItem.LENDARIO);
    }

    @Override
    public boolean onItemInteract(Player p) {
        MetaShit.setMetaObject("rebornGratiz", p, "1");
        MetaShit.setMetaObject("resetFree", p, "1");
        netMenu.escolheClasse(p);
        if (p.getInventory().getItemInMainHand().getAmount() > 1) {
            p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
        } else {
            p.setItemInHand(null);
        }
        return true;
    }

}
