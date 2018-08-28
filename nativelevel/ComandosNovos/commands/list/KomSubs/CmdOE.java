/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.ComandosNovos.commands.list.KomSubs;

import nativelevel.ComandosNovos.SubCmd;
import nativelevel.DataBase.SQL;
import nativelevel.KoM;
import nativelevel.MetaShit;
import nativelevel.guis.BancoGUI;
import nativelevel.utils.ExecutorType;
import nativelevel.utils.GUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.UUID;

/**
 * @author vntgasl
 */
public class CmdOE extends SubCmd {

    public CmdOE() {
        super("echest", ExecutorType.OP);
    }

    // TODO evitar acessar banco qnd xeretar

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (!(cs instanceof Player)) return;
        Player player = (Player) cs;

        if (args.length < 2) {
            cs.sendMessage(ChatColor.RED + "Digite /kom echest <player>");
            return;
        } else {
            String playerAlvo = args[1];
            UUID uuidAlvo = KoM.database.pegaUUID(playerAlvo);

            if (uuidAlvo == null) {
                cs.sendMessage("Não achei esse cara naum o.O");
                return;
            }

            KoM.log.info("Abrindo inv de " + player + " uuid " + uuidAlvo);

            BancoGUI.openBanco(player, uuidAlvo);

//            ItemStack[] items = KoM.database.getBanco(objetoPlayer.getUniqueId());
//            int slots = KoM.database.getSlotsBanco(objetoPlayer.getUniqueId().toString());
//            int linhas = slots + 1;
//            if (linhas > 5) {
//                linhas = 5;
//            }
//
//            Inventory i = Bukkit.createInventory((Player) cs, linhas * 9, "Xeretando Banco");
//            for (ItemStack item : items) {
//                if (item != null && item.getType() != Material.AIR) {
//                    i.addItem(item);
//                }
//            }
//            ((Player) cs).openInventory(i);
        }
    }

}
