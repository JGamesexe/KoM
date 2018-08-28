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
package nativelevel;

import nativelevel.Custom.Items.CidadeScroll;
import nativelevel.Custom.Items.TeleportScroll;
import nativelevel.Equipment.WeaponDamage;
import nativelevel.utils.BungLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CFG {

    public static int maxLevel = 100;

    public static boolean mundoGuilda(Location l) {
        return l.getWorld().getName().equalsIgnoreCase(mundoGuilda);
    }

    public static String mundoGuilda = "NewWorld";
    public static String mundoDungeon = "NewDungeon";

    public static List<String> safeMaps = Arrays.asList(new String[]{"vila"});
    public static List<String> warMaps = Arrays.asList(new String[]{"arena", "woe"});

    public static int landMax = 5;
    public static int landPrice = 10;

    public static Location localInicio = new Location(Bukkit.getWorld(CFG.mundoGuilda), 20.5, 60, -24.5, -165, 15);
    public static BungLocation localTutorial = new BungLocation(CFG.mundoDungeon, 539.5, 4, 6199.5, 0, 0);
    public static BungLocation spawnTree = new BungLocation(CFG.mundoGuilda, -200, 153, 438, 0, 0);

    public static ItemStack[] starterItems = {
            new ItemStack(Material.APPLE, 30),
            WeaponDamage.checkForMods(new ItemStack(Material.WOOD_PICKAXE, 1)),
            WeaponDamage.checkForMods(new ItemStack(Material.WOOD_AXE, 1)),
            WeaponDamage.checkForMods(new ItemStack(Material.LEATHER_HELMET, 1)),
            new ItemStack(Material.SAPLING, 10),
            new ItemStack(Material.TORCH, 64),
            WeaponDamage.checkForMods(new ItemStack(Material.LEATHER_BOOTS, 1)),
            WeaponDamage.checkForMods(new ItemStack(Material.LEATHER_CHESTPLATE, 1)),
            WeaponDamage.checkForMods(new ItemStack(Material.LEATHER_LEGGINGS, 1)),
            CidadeScroll.makeItem(10),
            new ItemStack(Material.EMERALD, 10),
            new ItemStack(Material.BOAT, 1)
    };

    // Loot Values --> They do something important..
    public static int custoAbaixarPower = 32;
    public static int custoPegarItemRandom = 1;
    public static int custoPegarBau = 12;
}
