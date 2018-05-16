/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.ComandosNovos.commands.list.KomSubs;


import nativelevel.ComandosNovos.SubCmd;
import nativelevel.Custom.CustomPotion;
import nativelevel.utils.ExecutorType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author User
 */

public class CmdPotions extends SubCmd {

    public CmdPotions() {
        super("potions", ExecutorType.OP);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        Player p = (Player) cs;
        CustomPotion.mostra(p);
    }

}
