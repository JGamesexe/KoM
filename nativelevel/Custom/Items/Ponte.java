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

import me.fromgate.playeffect.PlayEffect;
import me.fromgate.playeffect.VisualEffect;
import nativelevel.Custom.CustomItem;
import nativelevel.KoM;
import nativelevel.Lang.L;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class Ponte extends CustomItem {

    public static HashSet<Block> bloqueios = new HashSet<Block>();

    public Ponte() {
        super(Material.GOLD_BLOCK, L.m("Bloco Hidraulico"), L.m("Coloca um bloco hidraulico"), CustomItem.RARO);
        Runnable r = new Runnable() {
            public void run() {
                for (Block b : bloqueios) {
                    PlayEffect.play(VisualEffect.FLAME, b.getLocation(), "num:3");
                }
            }
        };
        Bukkit.getScheduler().scheduleSyncRepeatingTask(KoM._instance, r, 10, 10);
    }

    @Override
    public boolean onItemInteract(Player player) {
        return false;
    }

}
