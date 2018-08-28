package nativelevel.ComandosNovos.commands.list.KomSubs;

import nativelevel.ComandosNovos.SubCmd;
import nativelevel.Crafting.CraftCache;
import nativelevel.Harvesting.HarvestCache;
import nativelevel.Planting.PlantCache;
import nativelevel.utils.ExecutorType;
import org.bukkit.command.CommandSender;

public class CmdReload extends SubCmd {

    public CmdReload() {
        super("reload", ExecutorType.OPCONSOLE);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        CraftCache.reloadCache();
        HarvestCache.reloadCache();
        PlantCache.reloadCache();
        cs.sendMessage("Configs de Craft, Colheita e Plant Reloadadadas");
    }

}
