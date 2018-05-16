/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.ComandosNovos.commands.list.KomSubs;


import nativelevel.ComandosNovos.SubCmd;
import nativelevel.ComandosNovos.commands.list.KomSubs.PlantSubs.CmdPlantAdd;
import nativelevel.ComandosNovos.commands.list.KomSubs.PlantSubs.CmdPlantCheck;
import nativelevel.utils.ExecutorType;
import org.bukkit.command.CommandSender;

/**
 * @author vntgasl
 */

public class CmdPlants extends SubCmd {

    public CmdPlants() {
        super("colocavel", ExecutorType.OP);
        subs.add(new CmdPlantCheck());
        subs.add(new CmdPlantAdd());
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if (args.length <= 1) {
            this.showSubCommands(cs, "colocavel");
        } else {
            boolean executed = false;
            for (SubCmd cmd : subs) {
                if (cmd.cmd.equalsIgnoreCase(args[1])) {
                    cmd.executeSubCmd(cs, args);
                    executed = true;
                }
            }
            if (!executed)
                this.showSubCommands(cs, "colocavel");
        }
    }

}
