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
package nativelevel.Custom.Items;

import nativelevel.Attributes.Mana;
import nativelevel.Custom.CustomItem;
import nativelevel.Jobs;
import nativelevel.KoM;
import nativelevel.Lang.L;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class Ank extends CustomItem {

    public static HashSet<String> protegidos = new HashSet<String>();

    public Ank() {
        super(Material.NETHER_STAR, L.m("Luz da Graca"), L.m("Ressusita uma vez"), CustomItem.RARO);
    }

    @Override
    public boolean onItemInteract(Player player) {
        if (Jobs.getJobLevel("Paladino", player) != 1) {
            player.sendMessage(ChatColor.GOLD + L.m("Apenas bons paladinos sabem usar isto !"));
            return true;
        } else {
            if (protegidos.contains(player.getName())) {
                player.sendMessage(ChatColor.GOLD + L.m("Voce ja esta protegido !"));
                return true;
            }
            if (!Mana.spendMana(player, 50)) {
                return true;
            }
            player.sendMessage(ChatColor.GOLD + L.m("A luz comeca a brilhar te circulando por 5 segundos !"));
            player.playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 10, 0);
            player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 10);
            protegidos.add(player.getName());
            Bukkit.getScheduler().scheduleSyncDelayedTask(KoM._instance, new tiraNome(player.getName()), 20 * 5l);
            player.setItemInHand(null);
        }
        return false;
    }

    public class tiraNome implements Runnable {

        String nome;

        public tiraNome(String nome) {
            this.nome = nome;
        }

        @Override
        public void run() {
            protegidos.remove(nome);
            Player p = Bukkit.getPlayer(nome);
            if (p != null) {
                p.sendMessage(ChatColor.GOLD + L.m("A luz se desfaz"));
            }
        }

    }
}
