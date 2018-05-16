/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.ComandosNovos.commands.list.KomSubs;


import nativelevel.ComandosNovos.SubCmd;
import nativelevel.ComandosNovos.commands.list.KomSubs.CraftSubs.CmdCraftAdd;
import nativelevel.ComandosNovos.commands.list.KomSubs.CraftSubs.CmdCraftCheck;
import nativelevel.utils.ExecutorType;
import org.bukkit.command.CommandSender;

/**
 * @author vntgasl
 */
public class CmdCrafts extends SubCmd {

    public CmdCrafts() {
        super("crafts", ExecutorType.OP);
        subs.add(new CmdCraftCheck());
        subs.add(new CmdCraftAdd());
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (args.length <= 1) {
            this.showSubCommands(cs, "crafts");
        } else {
            boolean executed = false;
            for (SubCmd cmd : subs) {
                if (cmd.cmd.equalsIgnoreCase(args[1])) {
                    cmd.executeSubCmd(cs, args);
                    executed = true;
                }
            }
            if (!executed)
                this.showSubCommands(cs, "crafts");
        }
    }

}
