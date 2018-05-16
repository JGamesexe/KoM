/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.Custom.Items;

import nativelevel.Custom.CustomItem;
import nativelevel.Lang.L;
import nativelevel.RecipeBooks.BookTypes;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author User
 */

public class RecipeForBook extends CustomItem {

    public RecipeForBook() {
        super(Material.PAPER, L.m("Receita de um Livro"), L.m("Coloque em um livro"), CustomItem.RARO);
    }

    public BookTypes getBookType(ItemStack book) {
        ItemMeta meta = book.getItemMeta();
        String firstLine = meta.getLore().get(0);
        try {
            return BookTypes.valueOf(firstLine.split(" ")[2]);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean displayOnItems() {
        return false;
    }

    @Override
    public boolean onItemInteract(Player p) {
        return true;
    }

}
