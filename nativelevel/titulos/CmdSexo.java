/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.titulos;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author User
 */

public class CmdSexo implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        EscolheSexo.open((Player) cs);
        return true;
    }

}
