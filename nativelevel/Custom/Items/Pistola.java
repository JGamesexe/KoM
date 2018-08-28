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
import nativelevel.Attributes.Mana;
import nativelevel.Classes.Thief;
import nativelevel.Custom.CustomItem;
import nativelevel.Jobs;
import nativelevel.KoM;
import nativelevel.Lang.L;
import nativelevel.MetaShit;
import nativelevel.spec.PlayerSpec;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * @author usuario
 */
public class Pistola extends CustomItem {

    public Pistola() {
        super(Material.LEVER, L.m("Bonka Boom"), L.m("Uma velha engenhoca de arma"), CustomItem.INCOMUM);
    }

    @Override
    public boolean onItemInteract(Player player) {

        int job = Jobs.getJobLevel("Engenheiro", player);
        if (job != 1) {
            player.sendMessage(ChatColor.RED + L.m("Apenas bons engenheiros sabem usar isto !"));
            return false;
        }

        if (player.getInventory().getItemInOffHand() != null && player.getInventory().getItemInOffHand().getType() != Material.AIR) {
            player.sendMessage(ChatColor.RED + "Voce precisa das outra mão livre para conseguir mirar e atirar com a Bonka.");
            return false;
        }

        if (!player.hasPotionEffect(PotionEffectType.SLOW)) {

            if (!player.getInventory().contains(Material.SULPHUR)) {
                player.sendMessage(L.m("Voce precisa de polvora para atirar"));
                return false;
            }

            boolean gastaMana = true;
            ItemMeta m = player.getInventory().getItemInMainHand().getItemMeta();
            List<String> lore = m.getLore();
            if (lore.size() > 0) {
                if (lore.get(0).contains("Bonka Versao 2")) {
                    gastaMana = false;
                }
            }

            int mana = 8;
            int random = 2;
            if (PlayerSpec.temSpec(player, PlayerSpec.Fuzileiro)) {
                mana = 15;
                random = 3;
            } else if (PlayerSpec.temSpec(player, PlayerSpec.Inventor)) {
                mana = 5;
                random = 6;
            }
            if (gastaMana && !Mana.spendMana(player, mana)) {
                return false;
            }

            if (Jobs.rnd.nextInt(random) == 1) {
                KoM.removeInventoryItems(player.getInventory(), Material.SULPHUR, 1);
            }

            if (Thief.taInvisivel(player)) {
                Thief.revela(player);
            }

            PlayEffect.play(VisualEffect.SMOKE_LARGE, player.getLocation(), "num:1");
            player.sendMessage(ChatColor.RED + "*boonka*");
            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 10, 2);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5, 1));
//          Vector dir = player.getLocation().getDirection().multiply(10);
//          Location loc = player.getLocation();

//          Location inicio = new Location(player.getWorld(), loc.getX() + (dir.getX() / 5.0), loc.getY() + player.getEyeHeight() - 0.1, loc.getZ() + (dir.getZ() / 5.0));
//          Snowball snowball = (Snowball)player.getWorld().spawnEntity(inicio, EntityType.SNOWBALL);

            Snowball snowball = player.launchProjectile(Snowball.class);

            if (!player.isSneaking()) {
                Vector destino = snowball.getVelocity();
                destino.add(new Vector(((Math.random() - 0.5) / 3), ((Math.random() - 0.5) / 5), ((Math.random() - 0.5) / 3)));
                snowball.setVelocity(destino);
            }

            snowball.setFireTicks(400);
            //snowball.setVelocity(player.getLocation().getDirection().multiply(2f));

            MetaShit.setMetaString("tipoTiro", snowball, "kaboom");
            MetaShit.setMetaString("bonka", snowball, "");
            /*
            AttributeInfo info = KnightsOfMania.database.getAtributos(player);
            double ratio = 0.4 + ((Attributes.calcMagicDamage(PlayerSpec.temSpec(player, PlayerSpec.Sabio), info.attributes.get(Attr.intelligence))) + Attributes.calcArcheryDamage(info.attributes.get(Attr.dexterity)) / 2);
            if(KnightsOfMania.debugMode)
                KnightsOfMania.log.info("RATIO BONKA: "+ratio);
            */
            MetaShit.setMetaObject("modDano", snowball, 1.2);
        }
        return false;
    }

    public static void onHit(EntityDamageByEntityEvent ev) {

        Projectile projectile = (Projectile) ev.getDamager();
        Player atirador = (Player) projectile.getShooter();

        double alturaflecha = projectile.getLocation().getY();
        double diferenca = alturaflecha - ev.getEntity().getLocation().getY();

        double pode = 1.5;
        if (atirador.isSneaking()) pode = 1.35;

        //BOOM PERANHA
        if (diferenca > pode) {
            atirador.sendMessage("§c*!BooM!*");
            ev.setDamage(ev.getDamage() * 3.5);
            if (ev.getEntity() instanceof Player) {
                Player tomou = (Player) ev.getEntity();
                if (tomou.getInventory().getHelmet() != null && tomou.getInventory().getHelmet().getType() == Material.GOLD_HELMET) {
                    ev.setDamage(ev.getDamage() * 0.5);
                    tomou.getInventory().getHelmet().setDurability((short) (tomou.getInventory().getHelmet().getDurability() + 5));
                    if (tomou.getInventory().getHelmet().getDurability() >= tomou.getInventory().getHelmet().getType().getMaxDurability())
                        tomou.getInventory().setHelmet(null);
                }
            }
        }

    }

}
