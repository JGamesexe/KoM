/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.ComandosNovos.commands.list.KomSubs;


import nativelevel.ComandosNovos.SubCmd;
import nativelevel.ComandosNovos.commands.list.KomSubs.HarvestSubs.CmdHarvestAdd;
import nativelevel.ComandosNovos.commands.list.KomSubs.HarvestSubs.CmdHarvestCheck;
import nativelevel.utils.ExecutorType;
import org.bukkit.command.CommandSender;

/**
 * @author vntgasl
 */
public class CmdHarvest extends SubCmd {

    public CmdHarvest() {
        super("coletavel", ExecutorType.OP);
        subs.add(new CmdHarvestCheck());
        subs.add(new CmdHarvestAdd());
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (args.length <= 1) {
            this.showSubCommands(cs, "coletavel");
        } else {
            boolean executed = false;
            for (SubCmd cmd : subs) {
                if (cmd.cmd.equalsIgnoreCase(args[1])) {
                    cmd.executeSubCmd(cs, args);
                    executed = true;
                }
            }
            if (!executed)
                this.showSubCommands(cs, "coletavel");
        }
    }

}
