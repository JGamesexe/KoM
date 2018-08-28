package nativelevel.guis.terreno;

import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.GUI;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class TerrenoOutherGUI extends GUI {

    private Clan clanAt;
    private ClanPlayer clanPlayer;
    private ClanLand.ClanType is;
    private ClanLand.ClaimType type;

    public TerrenoOutherGUI(ClanPlayer clanPlayer, Clan clanAt, Location location) {
        super(Bukkit.createInventory(null, 27, "§3" + ClanLand.tipoTerreno(location).text + " da §f§l" + clanAt.getTag().toUpperCase()));
        this.clanAt = clanAt;
        this.type = ClanLand.tipoTerreno(location);
        this.clanPlayer = clanPlayer;
        this.is = ClanLand.getClanType(clanPlayer.getClan(), clanAt);
        cria();
    }

    public void cria() {

        botaVidros();

        inventory.setItem(11, cover());
        inventory.setItem(12, status());
        inventory.setItem(13, terreno());
        inventory.setItem(14, status());
        inventory.setItem(15, cover());


    }

    private ItemStack status() {
        ItemStack itemStack = new ItemStack(Material.CONCRETE_POWDER);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§f§lStatus");

        if (is.equals(ClanLand.ClanType.NEUTRAL)) {
            itemStack.setDurability((short) 8);
            itemMeta.setLore(Arrays.asList("","§fGuilda Neutra",""));
        } else if (is.equals(ClanLand.ClanType.ALLY)) {
            itemStack.setDurability((short) 5);
            itemMeta.setLore(Arrays.asList("","§aGuilda Aliada",""));
        } else if (is.equals(ClanLand.ClanType.ENEMY)) {
            itemStack.setDurability((short) 14);
            itemMeta.setLore(Arrays.asList("","§cGuilda Inimiga",""));
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack cover() {
        ItemStack itemStack;

        if (is.equals(ClanLand.ClanType.NEUTRAL)) itemStack = geraItem(Material.STAINED_GLASS, (short) 8);
        else if (is.equals(ClanLand.ClanType.ALLY)) itemStack = geraItem(Material.STAINED_GLASS, (short) 5);
        else if (is.equals(ClanLand.ClanType.ENEMY)) itemStack = geraItem(Material.STAINED_GLASS, (short) 14);
        else itemStack = geraItem(Material.STAINED_GLASS);

        return itemStack;
    }

    private ItemStack terreno() {
        ItemStack itemStack;

        if (type.equals(ClanLand.ClaimType.PODER)) itemStack = new ItemStack(Material.CLAY_BRICK);
        else itemStack = new ItemStack(Material.SMOOTH_BRICK);

        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§e" + type.text);

        if (is.equals(ClanLand.ClanType.NEUTRAL)) itemMeta.setLore(Arrays.asList("","§7Esta é uma guilda neutra não há o que fazer aqui...",""));
        else if (is.equals(ClanLand.ClanType.ALLY)) itemMeta.setLore(Arrays.asList("","§7Esta é uma guilda aliada ajude em caso de invasões!",""));
        else if (is.equals(ClanLand.ClanType.ENEMY)) itemMeta.setLore(Arrays.asList("","§7Aqui é uma zona de guerra, inimigos podem estar as espreitas...",""));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
