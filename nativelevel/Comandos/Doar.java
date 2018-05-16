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

import nativelevel.config.Config;
import nativelevel.sisteminhas.ClanLand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Doar implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if (cs instanceof Player) {
            Player p = (Player) cs;
            if (args.length == 0) {
                p.sendMessage("§4§lComo Funciona ?");
                p.sendMessage("§aVoce digita §4§l/doar <esmeraldas>§a e doa esmeraldas para o Deus Jabu.");
                p.sendMessage("§a§lQuando alguem ativar um Double XP Jabu o recompensará com tudo que foi doado !");
                p.sendMessage("§6§lSaldo do Deus Jabu: §e§l" + Config.getDoacoes() + " Moedas");
            } else if (args.length == 1) {
                try {
                    int qtd = Integer.valueOf(args[0]);
                    if (qtd < 32) {
                        p.sendMessage("§a§lA Humildade faz o homem, porém não o Deus. Jabu só aceita mais de 32 Moedas");
                        return true;
                    }
                    if (!ClanLand.econ.has(p.getName(), qtd)) {
                        p.sendMessage("§a§lJabu gosta do seu amor no coração, mas as Moedas que voce quer doar voce não tem :(");
                        return true;
                    }
                    ClanLand.econ.withdrawPlayer(p.getName(), qtd);
                    int doacoes = Config.getDoacoes() + qtd;
                    Config.setDoacao(doacoes);
                    p.sendMessage(ChatColor.GREEN + "Voce fez sua doação a Jabu.");
                    for (Player pl : Bukkit.getOnlinePlayers()) {
                        if (pl != p) {
                            pl.sendMessage("§a§l" + p.getName() + " foi a §6§l/doar§9§l " + qtd + " para Deus Jabu.");
                        }
                    }
                } catch (Exception e) {
                    p.sendMessage("§aUse §4§l/doar <Esmeraldas>§a.");
                }
            }
        }
        return true;
    }

}
