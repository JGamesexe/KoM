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

import nativelevel.Classes.Thief;
import nativelevel.Custom.CustomItem;
import nativelevel.Jobs;
import nativelevel.KoM;
import nativelevel.Lang.L;
import nativelevel.MetaShit;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class Atadura extends CustomItem {

    public static final int DISTANCIA_ANDAR = 6;

    public Atadura() {
        super(Material.CARPET, L.m("Atadura"), L.m("Para simples curativos"), CustomItem.INCOMUM);
    }

    @Override
    public void posCriacao(ItemStack ss) {
        ss.getData().setData(DyeColor.YELLOW.getDyeData());
    }

    private class AtaduraRun2 implements Runnable {

        //Cura a cada 0.2 segundos          5ticks
        //Demora 10 segundos                200ticks
        //Cura no total metade da vida

        private UUID uuid;
        private int taskId;

        private double howManyCura;
        private int howManyTimes = 40;
        private int whereIs;

        private Location loc;

        private AtaduraRun2(Player player) {
            this.uuid = player.getUniqueId();
            this.howManyCura = ((player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() / 2) / howManyTimes);
            this.whereIs = 0;
            this.loc = player.getLocation().clone();
        }

        @Override
        public void run() {

            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                para(player);
                return;
            }

            if (player.isDead()) {
                para(player);
                return;
            }


            if (!player.getLocation().getWorld().getName().equalsIgnoreCase(loc.getWorld().getName())) {
                para(player);
                player.sendMessage("§cA Atadura se perdeu no caminho...");
                return;
            }

            if (player.getLocation().distance(loc) > 0.5) {
                para(player);
                player.sendMessage("§cA Atadura se perdeu em quanto você se movia...");
                return;
            }

            if (whereIs <= 40) {

                if ((player.getHealth() + howManyCura) > player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
                    player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                    para(player);
                    player.sendMessage("§e[ : : ] §aA ferida foi tratada o suficiente!");
                    return;
                }

                whereIs++;
                player.setHealth(player.getHealth() + howManyCura);

                if ((whereIs % 5) == 0) {
                    player.playSound(player.getLocation(), Sound.BLOCK_WATERLILY_PLACE, 1, 2);
                    player.spawnParticle(Particle.BLOCK_CRACK, player.getLocation().add(0, 1, 0), 4, Jobs.rnd.nextDouble(), Jobs.rnd.nextDouble(), Jobs.rnd.nextDouble(), new MaterialData(Material.WOOL));
                    loc = player.getLocation();
                }

                if (whereIs == 40) {
                    Bukkit.getScheduler().cancelTask(taskId);
                    player.sendMessage("§e[ : : ] §aA Atadura que você usava acabou!");
                }

            }

        }

        private void para(Player player) {
            Bukkit.getScheduler().cancelTask(taskId);
            if (player != null) player.removeMetadata("atadura", KoM._instance);
        }

    }

    public static void para(Player p) {
        AtaduraRun2 run = seCurando(p);
        if (run != null) {
            p.sendMessage("§e[ : : ] §cVocê para de aplicar as Ataduras");
            Bukkit.getScheduler().cancelTask(run.taskId);
            p.removeMetadata("atadura", KoM._instance);
        }
    }

    private static AtaduraRun2 seCurando(Player p) {
        return p.hasMetadata("atadura") ? (AtaduraRun2) MetaShit.getMetaObject("atadura", p) : null;
    }

    private static boolean podeInterromper(Player p) {
        if (p.hasPotionEffect(PotionEffectType.POISON) || p.hasPotionEffect(PotionEffectType.WITHER)) {
            p.sendMessage(ChatColor.RED + "Você não consegue aplicar as ataduras estando envenenado.");
            return true;
        }

        if (p.getFireTicks() > 0) {
            p.sendMessage(ChatColor.RED + "Voce nao consegue se curar pegando fogo !");
            return true;
        }

        if (p.getLocation().add(0, 1, 0).getBlock().getType() == Material.WATER) {
            p.sendMessage(ChatColor.RED + "Você nao consegue aplicar as ataduras na agua.");
            return true;
        }

        if (p.getLocation().add(0, 1, 0).getBlock().getType() == Material.WEB) {
            p.sendMessage(ChatColor.RED + "Você nao consegue aplicar as ataduras em um emaranhado de teias.");
            return true;
        }

        return false;
    }

    @Override
    public boolean onItemInteract(Player p) {
        AtaduraRun2 run = seCurando(p);
        if (run != null) {
            p.sendMessage(ChatColor.RED + "Você já está com ataduras.");
            return true;
        }

        if (p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() == p.getHealth()) {
            p.sendMessage(ChatColor.RED + "Você não tem nenhum ferimento.");
            return true;
        }

        if (podeInterromper(p)) return true;


        int stack = p.getInventory().getItemInMainHand().getAmount();
        stack--;

        if (stack == 0) p.getInventory().setItemInMainHand(null);
        else p.getInventory().getItemInMainHand().setAmount(stack);

        if (Thief.taInvisivel(p)) Thief.revela(p);

        for (Entity e : p.getNearbyEntities(10, 4, 10)) {
            if (e.getType() == EntityType.PLAYER) {
                e.sendMessage(ChatColor.AQUA + "* " + p.getName() + " começou a aplicar ataduras *");
            }
        }

        run = new AtaduraRun2(p);
        MetaShit.setMetaObject("atadura", p, run);
        run.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(KoM._instance, run, 5, 5);
        p.sendMessage(ChatColor.YELLOW + "[ : : ] " + ChatColor.GREEN + "Aplicando Ataduras");
        return true;
    }
}
