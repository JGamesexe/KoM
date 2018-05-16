/*

 ╭╮╭━╮╱╱╭━╮╭━╮
 ┃┃┃╭╯╱╱┃┃╰╯┃┃
 ┃╰╯╯╭━━┫╭╮╭╮┃
 ┃╭╮┃┃╭╮┃┃┃┃┃┃
 ┃┃┃╰┫╰╯┃┃┃┃┃┃
 ╰╯╰━┻━━┻╯╰╯╰╯

 Desenvolvedor: ZidenVentania
 Colaboradores: NeT32, Gabripj, Feldmann
 Patrocionio: InstaMC

 */
package nativelevel.Comandos;

import nativelevel.sisteminhas.XP;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KomExp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if (cs instanceof Player) {
            Player p = (Player) cs;
            if (!p.isOp())
                return true;
            if (args.length == 0) {
                p.sendMessage("/verexp <nivel>");
                p.sendMessage("/verexp ganhar <xp>");
            } else if (args.length == 1) {
                try {
                    int nivel = Integer.valueOf(args[0]);
                    if (nivel > 100) {
                        p.sendMessage("Nivel max eh 100 manolo...");
                        return true;
                    } else if (nivel < 1) {
                        p.sendMessage("Nivel minimo eh 1 manolo...");
                        return true;
                    }
                    XP.debugLevel(p, nivel - 1);
                } catch (Exception e) {
                    p.sendMessage("Numero invalido");
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("ganhar")) {
                    int xp = Integer.valueOf(args[1]);
                    XP.changeExp(p, xp, 1);
                }
            }
        }
        return true;
    }
}
