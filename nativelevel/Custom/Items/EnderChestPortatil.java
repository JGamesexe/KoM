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

import nativelevel.ComandosNovos.commands.list.KomSubs.CmdOE;
import nativelevel.Custom.CustomItem;
import nativelevel.KoM;
import nativelevel.guis.BancoGUI;
import nativelevel.utils.GUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EnderChestPortatil extends CustomItem {

    public EnderChestPortatil() {
        super(Material.ENDER_CHEST, "EnderChest Mochila", "Um velho bau magico, com alças..", CustomItem.EPICO);
    }

    @Override
    public boolean onItemInteract(Player p) {

        BancoGUI.openBanco(p, p.getPlayer().getUniqueId());

        return true;
    }
}
