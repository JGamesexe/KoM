package nativelevel.guis.terreno;

import nativelevel.Comandos.Terreno;
import nativelevel.Lang.L;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.GUI;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TerrenoWildGUI extends GUI {

    private Location location;
    private ClanPlayer clanPlayer;

    public TerrenoWildGUI(Player player) {
        super(Bukkit.createInventory(null, 27, "§3Terras Sem Dono"));
        this.location = player.getLocation();
        this.clanPlayer = ClanLand.manager.getClanPlayer(player);
        cria();
    }

    public void cria() {

        botaVidros();

        inventory.setItem(11, terrenoItem());
        inventory.setItem(15, conquistarItem());

    }

    private ItemStack terrenoItem() {

        ItemStack itemStack = new ItemStack(Material.GRASS);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§eTerreno Sem Dono");

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack conquistarItem() {

        ItemStack itemStack = new ItemStack(Material.BOOK_AND_QUILL);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§eConquistar Terreno");

        ArrayList<String> lore = new ArrayList<>();

        lore.add("");

        if (clanPlayer != null && clanPlayer.isLeader()) {
            lore.add("§7Recursos para conquistar terrenos:");
            lore.add("§8 - §71 Poder");
            lore.add("§8 - §7" + ClanLand.priceOfTerreno(clanPlayer.getTag()) + " Esmeraldas");
            lore.add("");
            lore.add("§7Clique com o 'Q' para conquistar");
        } else {
            lore.add("§7Apenas lideres de guildas");
            lore.add("§7podem conquistar terrenos");
        }

        lore.add("");

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public void interage(InventoryClickEvent event) {
        super.interage(event);

        if (clanPlayer == null || !clanPlayer.isLeader()) return;

        if (!(event.getAction().equals(InventoryAction.DROP_ALL_SLOT) || event.getAction().equals(InventoryAction.DROP_ONE_SLOT)))
            return;

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();


        if (slot == 6) {

            ArrayList<String> emvolta = Terreno.getGuildasPerto(location);

            if (Terreno.temGuildaPerto(player, location, false)) {
                ClanLand.msg(player, L.m("Um terreno que não pertence a sua guilda está muito proximo para conquistar este terreno."));
                return;
            }

            if (emvolta.get(1).equalsIgnoreCase(clanPlayer.getTag()) || emvolta.get(2).equalsIgnoreCase(clanPlayer.getTag()) ||
                    emvolta.get(3).equalsIgnoreCase(clanPlayer.getTag()) || emvolta.get(4).equalsIgnoreCase(clanPlayer.getTag())) {

                int poder = ClanLand.getPoder(clanPlayer.getTag());

                if (poder > 0) {
                    int preco = ClanLand.priceOfTerreno(clanPlayer.getTag());
                    if (ClanLand.econ.has(player, preco)) {

                        if (preco == 0) {
                            ClanLand.setClanAt(location, clanPlayer.getTag());
                            ClanLand.msg(player, L.m("Terreno conquistado, como este é o primeiro terreno de sua guilda, ele foi colocado como Terreno Primário, os proximos terrenos vão precisar de uma melhoria para torna-los Terrenos Primários"));
                        } else {
                            ClanLand.setClanAt(location, "#" + clanPlayer.getTag());
                            ClanLand.msg(player, L.m("Terreno conquistado, este terreno é um Terreno de Poder e pode ser dominado a qualquer momento, para melhorar este terreno torne-o um Terreno Primário"));
                            ClanLand.econ.withdrawPlayer(player, preco);
                        }

                        ClanLand.msg(player, L.m("Para modificicar este terreno utilize, /terreno"));
                        ClanLand.setPoder(clanPlayer.getTag(), poder - 1);
                        if (location.getChunk().equals(player.getLocation().getChunk()))
                            ClanLand.update(player, location);

                    } else {
                        ClanLand.msg(player, L.m("Você não possui " + preco + " esmeraldas"));
                    }
                } else {
                    ClanLand.msg(player, L.m("Sua guilda não tem poder para conquistar este terreno"));
                }
            } else {
                ClanLand.msg(player, L.m("Você só pode conquistar terrenos ao lado de sua guilda."));
            }

        }

    }

}
