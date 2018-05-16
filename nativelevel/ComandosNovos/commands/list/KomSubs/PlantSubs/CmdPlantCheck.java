/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.ComandosNovos.commands.list.KomSubs.PlantSubs;


import nativelevel.ComandosNovos.SubCmd;
import nativelevel.Lang.L;
import nativelevel.Planting.PlantCache;
import nativelevel.Planting.Plantable;
import nativelevel.utils.ExecutorType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashSet;

/**
 * @author vntgasl
 */
public class CmdPlantCheck extends SubCmd {

    public CmdPlantCheck() {
        super("check", ExecutorType.OP);
    }

    public void display(Player p, HashSet<Plantable> harvestables) {
        Inventory i = Bukkit.createInventory(p, 6 * 9, "Plantables");
        for (Plantable h : harvestables) {
            i.addItem(h.getIcon());
        }
        p.openInventory(i);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (args.length < 3) {
            cs.sendMessage(L.m("Use /kom colocavel check <classe>"));
            cs.sendMessage(L.m("Or /kom colocavel check <material>"));
        } else {
            String skill = null;
            try {
                skill = args[2];
            } catch (Exception e) {

            }
            if (skill != null) {
                HashSet<Plantable> harvestables = PlantCache.getPlantable(skill);
                if (harvestables == null) {
                    cs.sendMessage(ChatColor.RED + L.m("Nao achei coletaveis para esta classe !"));
                } else {
                    display((Player) cs, harvestables);
                }
            } else {
                Material m = null;
                try {
                    m = Material.valueOf(args[2]);
                } catch (Exception e) {

                }
                if (m == null) {
                    m = Material.getMaterial(args[2]);
                }
                HashSet<Plantable> harvestables = PlantCache.getPlantable(m);
                if (harvestables == null || harvestables.size() == 0) {
                    cs.sendMessage(ChatColor.RED + L.m("Nao achei coletaveis ") + args[2]);
                } else {
                    display((Player) cs, harvestables);
                }
            }
        }
    }

}
