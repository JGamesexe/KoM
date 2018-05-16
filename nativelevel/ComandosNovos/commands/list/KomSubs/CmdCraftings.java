/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.ComandosNovos.commands.list.KomSubs;

import nativelevel.ComandosNovos.SubCmd;
import nativelevel.Custom.CustomItem;
import nativelevel.Custom.Items.MostraReceita;
import nativelevel.KoM;
import nativelevel.utils.ExecutorType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.Recipe;

/**
 * @author vntgasl
 */

public class CmdCraftings extends SubCmd {

    public CmdCraftings() {
        super("customcrafting", ExecutorType.OP);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {

        Player p = (Player) cs;
        Inventory i = Bukkit.createInventory(p, 9 * 4, "Receitas de Crafting do KoM");
        for (Recipe r : KoM.receitasCustom) {
            i.addItem(CustomItem.getItem(MostraReceita.class).fazReceita(r));
        }
        p.openInventory(i);
    }

}
