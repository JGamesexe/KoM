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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class SeguroDeItems extends CustomItem {

    public SeguroDeItems() {
        super(Material.PAPER, L.m("Seguro de Itens"), L.m("Pegar esse item por aqui n funciona."), CustomItem.EPICO);
    }

    @Override
    public ItemStack generateItem() {
        return generateItem(3, 0.15);
    }

    public ItemStack generateItem(int cargas, double porc) {

        ItemStack papel = new ItemStack(m, 1, (short) 0);

        ItemMeta meta = papel.getItemMeta();
        meta.setDisplayName("§5♦ §6Seguro de Itens [§e+" + (int) (porc * 100) + "%§6]");
        meta.setLore(Arrays.asList("§7Segura uma porcentagem maior de itens quando você morrer",
                "",
                "§bCargas : " + cargas,
                "",
                "§0:Seguro de Itens " + porc));
        papel.setItemMeta(meta);
        return papel;
    }

    public static double getPorcent(Player player) {
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (SeguroDeItems.isSeguro(itemStack)) {
                double porc = Double.valueOf(itemStack.getItemMeta().getLore().get(2).split(" ")[3].replace("%", ""));
                gastaSeguro(player, itemStack);
                return porc;
            }
        }
        return 0;
    }

    public static boolean isSeguro(ItemStack itemStack) {
        return itemStack != null &&
                itemStack.hasItemMeta() &&
                itemStack.getItemMeta().getLore() != null &&
                itemStack.getItemMeta().getLore().size() == 3 &&
                itemStack.getItemMeta().getLore().get(2).contains(":Seguro de Itens");
    }

    public static boolean gastaSeguro(Player p, ItemStack seguro) {
        ItemMeta meta = seguro.getItemMeta();
        List<String> lore = meta.getLore();
        try {
            int qtd = Integer.valueOf(lore.get(1).split(":")[1].trim());
            qtd--;
            if (qtd <= 0) {
                meta.setDisplayName("§8Seguro vencido");
                lore.clear();
            } else {
                lore.remove(1);
                lore.add(1, "§bCargas : " + qtd);
            }
            meta.setLore(lore);
            seguro.setItemMeta(meta);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean onItemInteract(Player p) {
        //Terrenos.permission.
        return true;
    }

}
