/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.Comandos;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vntgasl
 */
public class Cmdlimpaitem implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if (cs instanceof Player) {
            Player pl = (Player) cs;
            if (!pl.hasMetadata("NPC") && !pl.isOp())
                return true;
        }
        if (args.length == 1) {
            Player p = Bukkit.getPlayer(args[0]);
            if (p != null) {
                ItemStack naMao = p.getInventory().getItemInMainHand();
                if (naMao == null) {
                    return true;
                }
                List<String> novaLore = new ArrayList<String>();
                ItemMeta meta = naMao.getItemMeta();
                if (meta == null) {
                    return true;
                }
                if (meta.getLore() == null) {
                    return true;
                }
                for (String s : meta.getLore()) {
                    if (!s.contains("Criado Por")) {
                        novaLore.add(s);
                    }
                }
                meta.setLore(novaLore);
                naMao.setItemMeta(meta);
            }
        }
        return true;
    }

}
