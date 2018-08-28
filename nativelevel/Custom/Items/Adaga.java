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
import nativelevel.Equipment.WeaponDamage;
import nativelevel.Lang.L;
import nativelevel.gemas.Raridade;
import nativelevel.utils.MetaUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Adaga extends CustomItem {

    public Adaga() {
        super(Material.IRON_SWORD, L.m("Adaga"), L.m("Uma arma perfeita para os ladinos"), CustomItem.RARO);
    }

    public enum Type {
        WOOD(Material.WOOD_SWORD, "Adaga de Pinheiro"),
        STONE(Material.STONE_SWORD, "Adaga de Pedra Laminada"),
        IRON(Material.IRON_SWORD, "Adaga Cortante"),
        DIAMOND(Material.IRON_SWORD, "Adaga da Ponta Diamantada");

        Material material;
        String nome;

        Type(Material material, String nome) {
            this.material = material;
            this.nome = nome;
        }

    }

    @Override
    public boolean onItemInteract(Player p) {
        return true;
    }

    @Override
    public ItemStack generateItem() {
        return generateItem(Type.IRON);
    }

    public ItemStack generateItem(Type type) {
        ItemStack item = new ItemStack(type.material, 1, (short) 0);
        item.setType(type.material);
        item = WeaponDamage.checkForMods(item);
        MetaUtils.setItemNameAndLore(item, this.pegaCorPelaRaridade(this.raridade) + "♦ " + ChatColor.UNDERLINE + ChatColor.GOLD + type.nome, this.lore.toArray(new String[this.lore.size()]));
        this.posCriacao(item);

        return item;
    }

    public static boolean isAdaga(ItemStack itemStack) {

        if (itemStack != null
                && itemStack.getType() == Material.IRON_SWORD
                && itemStack.getItemMeta() != null
                && itemStack.getItemMeta().getLore() != null
                && !itemStack.getItemMeta().getLore().isEmpty()) {
            for (String lore : itemStack.getItemMeta().getLore()) {
                if (lore.contains(":Adaga")) return true;
            }
        }

        return false;

    }
}
