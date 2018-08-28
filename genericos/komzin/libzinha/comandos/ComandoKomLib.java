package genericos.komzin.libzinha.comandos;

import genericos.komzin.libzinha.listeners.GeralListener;
import genericos.komzin.libzinha.utils.Efeitos;
import nativelevel.utils.GeneralUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;

public class ComandoKomLib
        implements CommandExecutor {
    HashSet<Material> fodase = new HashSet<Material>();

    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if (!cs.hasPermission("manialibkom.komlib")) {
            return true;
        }
        Player p = (Player) cs;
        if (args.length == 0) {
            cs.sendMessage("§cUtilize: §f/komlib efeito");
            cs.sendMessage("§cUtilize §f/komlib bauraio <Bau com items do seu inv onde aponta caindo raio>");
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("efeito")) {
                if (GeralListener.ListaEfeitos.contains(cs.getName())) {
                    GeralListener.ListaEfeitos.remove(cs.getName());

                    cs.sendMessage("§cEfeitos desativados!");

                    return true;

                }
                GeralListener.ListaEfeitos.add(cs.getName());

                cs.sendMessage("§cEfeitos ativados!");
                return true;

            }
            if (args[0].equalsIgnoreCase("bauraio")) {
                Location novloc = p.getTargetBlock(fodase, 600).getLocation().clone();
                novloc.setY(GeneralUtils.getHighestBlockYAt(novloc));
                novloc.getBlock().setType(Material.CHEST);
                Chest chest = (Chest) novloc.getBlock().getState();
                for (int i = 0; i < 36; i++) {
                    if (p.getInventory().getItem(i) != null) {
                        if (chest.getBlockInventory().firstEmpty() != -1) {
                            chest.getBlockInventory().addItem(new ItemStack[]{p.getInventory().getItem(i)});
                        }
                    }
                }
                Efeitos.effectBats(novloc);
                Efeitos.effectExplosion(novloc);
                Efeitos.effectFlames(novloc);
                Efeitos.effectLightning(novloc);
                Efeitos.effectSmoke(novloc);
                cs.sendMessage("§cColocado!");
                return true;
            }
        }
        return true;
    }
}


/* Location:              C:\Users\User\Desktop\REPO\InstaMCLibKom.jar!\instamc\coders\libkom\comandos\ComandoKomLib.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */