/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.ComandosNovos.commands.list.KomSubs.CraftSubs;

import nativelevel.ComandosNovos.SubCmd;
import nativelevel.Crafting.CraftConfig;
import nativelevel.Crafting.Craftable;
import nativelevel.Custom.CustomItem;
import nativelevel.Jobs;
import nativelevel.Lang.L;
import nativelevel.utils.ExecutorType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author vntgasl
 */
public class CmdCraftAdd extends SubCmd {

    public CmdCraftAdd() {
        super("add", ExecutorType.OP);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (args.length < 4) {
            cs.sendMessage("Use /kom crafts add <classe> <dificuldade>");
        } else {
            Jobs.Classe skill = Jobs.Classe.valueOf(args[2]);
            if (skill != null) {
                Player p = (Player) cs;
                ItemStack item = p.getInventory().getItemInMainHand();
                if (item == null) {
                    p.sendMessage(ChatColor.RED + L.m("Coloca um negocio na mao ae ..."));
                    return;
                }
                int minSkill = Integer.valueOf(args[3]);
                CustomItem customitem = CustomItem.getItem(item);
                if (customitem == null) {
                    Material mat = item.getType();
                    byte data = item.getData().getData();
                    Craftable craft = new Craftable(mat, data, minSkill, skill, 1);
                    CraftConfig.add(craft);
                } else {
                    Craftable craft = new Craftable(customitem, minSkill, skill, 1);
                    CraftConfig.add(craft);
                }
                p.sendMessage(ChatColor.GREEN + L.m("Item adicionado !"));
            } else {
                cs.sendMessage(ChatColor.RED + L.m("Classe invalida !"));
            }
        }
    }

}
