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

        if (CmdOE.xeretando.contains(p.getUniqueId())) {
            return true;
        }

        ItemStack[] items = KoM.database.getBanco(p.getUniqueId());
        int slots = KoM.database.getSlotsBanco(p.getUniqueId().toString());
        int linhas = slots + 1;
        if (linhas > 5) {
            linhas = 5;
        }
        Inventory banco = Bukkit.createInventory(p, 9 * linhas, "Banco");
        if (items != null) {
            banco.setContents(items);
        }
        p.openInventory(banco);

        return true;
    }
}
