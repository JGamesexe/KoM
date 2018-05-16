/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nativelevel.Custom.Items;

import nativelevel.Custom.CustomItem;
import nativelevel.Lang.L;
import nativelevel.sisteminhas.ClanLand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author usuario
 */
public class TituloCavaleiro extends CustomItem {

    public TituloCavaleiro() {
        super(Material.PAPER, L.m("Titulo de Cavaleiro"), L.m("Dias : 3"), CustomItem.EPICO);
    }

    @Override
    public boolean onItemInteract(Player p) {
        if (ClanLand.permission.has(p, "kom.vip")) {
            p.sendMessage(ChatColor.RED + "Voce ja e um cavaleiro !");
            return true;
        }
        ItemMeta m = p.getInventory().getItemInMainHand().getItemMeta();
        if (m.getLore().size() > 0) {
            int dias = Integer.valueOf(m.getLore().get(0).split(":")[1]);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/darvip " + p.getName() + " kom-cavaleiro " + dias + " false");
            p.sendMessage(ChatColor.GREEN + L.m("Voce agora é um Cavaleiro por % dias !", dias));
            for (Player p2 : Bukkit.getOnlinePlayers()) {
                if (!p2.getName().equalsIgnoreCase(p.getName()))
                    p2.sendMessage(ChatColor.GREEN + p.getName() + L.m(" ativou seu Cavaleiro !"));
            }
        }
        return true;
    }
}
