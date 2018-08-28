package nativelevel.Custom.Items;

import nativelevel.Custom.CustomItem;
import nativelevel.Custom.Mobs.IncursionIronTotem;
import nativelevel.sisteminhas.ClanLand;
import net.minecraft.server.v1_12_R1.World;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pixelmc.ServerReboot;

public class IncursionTotem extends CustomItem {

    public IncursionTotem() {
        super(Material.TOTEM, "Totem de Incursão", "Usado para arruinar terrenos inimigos", EPICO);
    }

    @Override
    public boolean onItemInteract(Player p) {
        ItemStack hand = p.getInventory().getItemInMainHand();
        if (hand == null || !hand.getType().equals(Material.TOTEM)) return false;
        if (hand.getItemMeta() == null || hand.getItemMeta().getLore() == null || hand.getItemMeta().getLore().size() != 3) return false;
        if (!hand.getItemMeta().getLore().get(2).contains(":Totem de Incursão")) return false;

        ClanPlayer cp = ClanLand.manager.getClanPlayer(p);
        if (cp == null) {
            p.sendMessage("§eVocê precisa de uma guilda para utilizar isto.");
            return false;
        } else if (!cp.isLeader()) {
            p.sendMessage("§eApenas lideres podem utilizar este item.");
            return false;
        }

        Clan clanAt = ClanLand.getClanAt(p.getLocation());
        if (clanAt == null || !cp.getClan().isRival(clanAt.getTag())) {
            p.sendMessage("§eVocê só pode utilizar isso em um territórios inimigos.");
            return false;
        }

        if (ClanLand.isTerrenoPoder(p.getLocation())) {
            p.sendMessage("§eTotens de incursão só podem ser invocados em terrenos de poder.");
            return false;
        }

        if ((System.currentTimeMillis() + 600000) > ServerReboot.reinicio && !(p.getLocation().getPitch() < 88)) {
            p.sendMessage("§cNão vamos deixar você perder tempo atoa, espere o servidor reinciar para invocar o totem...");
            return false;
        }

        if ((System.currentTimeMillis() + 1800000) > ServerReboot.reinicio && !(p.getLocation().getPitch() < 88)) {
            p.sendMessage("§cO Servidor irá reiniciar logo mais, tem certeza que deseja invocar o Totem de Incursão?\n" +
                    "§cCaso não derrote ele até o reinicio todo progesso de luta será perdido.\n" +
                    "§cSe você ainda quer invocar o totem, use o item agachado e olhando totalmente para baixo.");
            return false;
        }

        p.getInventory().setItemInMainHand(null);

        clanAt.addBb(cp.getTag().toUpperCase(), "§eA Guilda " + cp.getTag().toUpperCase() + " invocou um Totem de Incursão em sua guilda, vá defender o terreno que está sendo atacado!");

        World world = ((CraftWorld) p.getLocation().getWorld()).getHandle();
        IncursionIronTotem totem = new IncursionIronTotem(p.getLocation(), clanAt.getTag(), cp.getTag());
        world.addEntity(totem);

        return false;

    }

}
