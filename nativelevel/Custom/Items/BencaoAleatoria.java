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

import nativelevel.Custom.CustomItem;
import nativelevel.Jobs;
import nativelevel.Lang.L;
import nativelevel.bencoes.TipoBless;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BencaoAleatoria extends CustomItem {

    public BencaoAleatoria() {
        super(Material.NETHER_STAR, L.m("Benção Suprema de Jabu"), L.m("Lhe concede uma benção super forte"), CustomItem.LENDARIO);
    }

    @Override
    public boolean onItemInteract(final Player p) {

        TipoBless sorteado = TipoBless.values()[Jobs.rnd.nextInt(TipoBless.values().length)];

        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl != p) {
                pl.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "" + p.getName() + " ganhou uma benção suprema de " + sorteado.name());
            }
        }
        p.sendMessage(ChatColor.GREEN + "Voce ganhou uma bencao suprema de " + sorteado.name());
        p.sendMessage(ChatColor.GREEN + "Use ela com muita sabedoria...pois seu poder eh grande.");

        ItemStack item = TipoBless.cria(sorteado);

        p.getInventory().addItem(item);

        if (p.getInventory().getItemInMainHand().getAmount() > 1) {
            p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
        } else {
            p.setItemInHand(null);
        }
        return true;
    }
}
