package nativelevel.Custom.Items;

import me.fromgate.playeffect.PlayEffect;
import me.fromgate.playeffect.VisualEffect;
import nativelevel.Custom.CustomItem;
import nativelevel.KoM;
import nativelevel.Lang.L;
import nativelevel.Listeners.DeathEvents;
import nativelevel.MetaShit;
import nativelevel.integration.BungeeCordKom;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class CidadeScroll extends CustomItem {
    public CidadeScroll() {
        super(Material.PAPER, "Pergaminho para Cidade", "Não é pra isso funcionar não...", INCOMUM);
    }

    @Override
    public ItemStack generateItem() {
        return makeItem(10);
    }

    public static ItemStack makeItem(int cargas) {
        ItemStack scroll = new ItemStack(Material.PAPER);
        ItemMeta smeta = scroll.getItemMeta();
        smeta.setLore(Arrays.asList(
                "§7Leva para cidade que você pertence",
                "",
                "§6Cargas: §a" + cargas + "§6/§a" + cargas,
                "",
                "§0:Pergaminho para Cidade"));
        smeta.setDisplayName("§a♦ §6Pergaminho para Cidade");
        scroll.setItemMeta(smeta);

        return scroll;
    }

    @Override
    public boolean onItemInteract(Player p) {
        ItemStack scroll = p.getInventory().getItemInMainHand();
        if (scroll == null || !scroll.getType().equals(Material.PAPER)) return false;

        ItemMeta meta = scroll.getItemMeta();

        if (!meta.getLore().get((meta.getLore().size() - 1)).contains(":Pergaminho para Cidade")) return false;

        if (scroll.getAmount() > 1) {
            p.sendMessage(ChatColor.RED + L.m("Tire o item do stack para usar !"));
            return false;
        }
        if (p.hasMetadata("Pergaminho")) {
            p.sendMessage(ChatColor.RED + L.m("Aguarde para usar outro pergaminho !"));
            return false;
        }


        String cargas = ChatColor.stripColor(meta.getLore().get(1).split(":")[1]);
        String[] cargaTotal = cargas.split("/");
        int tem = Integer.valueOf(cargaTotal[0].replaceAll(" ", ""));
        int max = Integer.valueOf(cargaTotal[1]);

        Runnable runnable = () -> {
            p.teleport(DeathEvents.pertenceAVila(p));
            p.sendMessage(ChatColor.DARK_PURPLE + "* poof *");
            PlayEffect.play(VisualEffect.FIREWORKS_SPARK, p.getLocation(), "color:purple type:burst");
            p.removeMetadata("Pergaminho", KoM._instance);
        };

        if (tem == 1) {
            p.getInventory().setItemInMainHand(null);
            p.sendMessage(ChatColor.AQUA + L.m("* O pergaminho se desfez em farelos *"));
        } else {
            if (!p.hasPermission("kom.lord")) {
                tem = tem - 1;
                List<String> lore = meta.getLore();
                lore.set(1, "§6Cargas: §a" + tem + "§6/§a" + max);
                p.sendMessage("§b* O papel do pergaminho envelheceu e ficou com  " + tem + " cargas *");
                meta.setLore(lore);
                scroll.setItemMeta(meta);
            }
        }

        int idTask = Bukkit.getScheduler().scheduleSyncDelayedTask(KoM._instance, runnable, 20 * 5);
        MetaShit.setMetaString("Pergaminho", p, "" + idTask);
        p.sendMessage(ChatColor.GREEN + L.m("Aguarde 5 segundos enquanto voce foca no teleporte !"));

        return true;
    }

}
