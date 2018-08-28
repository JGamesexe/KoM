package nativelevel.Custom.Items;

import nativelevel.Custom.CustomItem;
import nativelevel.KoM;
import nativelevel.guis.guilda.GuildaCreateGUI;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.GUI;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CriaGuilda extends CustomItem {

    public CriaGuilda() {
        super(Material.ENCHANTED_BOOK, "Livro da Instituição", "Utilizado para criar uma guilda", CustomItem.EPICO);
        this.cancelaInteract = true;
    }

//    @Override
//    public void interage(PlayerInteractEvent ev) {
//        ev.setCancelled(true);
//    }

    @Override
    public boolean onItemInteract(Player p) {
        ClanPlayer cp = ClanLand.manager.getClanPlayer(p);
        if (cp != null) {
            p.sendMessage("§cVocê já possui uma guilda, se precisar de outra tem que sair desta primeiro.");
        } else {
            GUI.open(p, new GuildaCreateGUI("", ""));
        }
        return false;
    }

    public static boolean createGuilda(Player p) {
        for (ItemStack itemStack : p.getInventory().getContents()) {
            if (itemStack != null && itemStack.getType().equals(Material.ENCHANTED_BOOK))
                if (itemStack.getItemMeta() != null && itemStack.getItemMeta().getLore() != null && itemStack.getItemMeta().getLore().size() == 3)
                    if (itemStack.getItemMeta().getLore().get(2).contains(":Livro da Instituição")) {
                        p.getInventory().remove(itemStack);
                        return true;
                    }
        }
        return false;
    }

}
