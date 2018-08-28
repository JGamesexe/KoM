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

package nativelevel.Classes;

import nativelevel.CFG;
import nativelevel.Jobs;
import nativelevel.KoM;
import nativelevel.Lang.L;
import nativelevel.MetaShit;
import nativelevel.skills.Skill;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Engineer {

    public static HashMap<UUID, UUID> leashed = new HashMap<UUID, UUID>();

    public static final Jobs.Classe classe = Jobs.Classe.Engenheiro;
    public static final String name = "Engenheiro";

    public static void onDamaged(EntityDamageEvent ev) {
        if (ev.getCause().equals(EntityDamageEvent.DamageCause.LIGHTNING)) ev.setCancelled(true);
    }

    public static boolean validaPrisao(PlayerInteractEvent ev) {
        if (ev.getPlayer().hasMetadata("prendeu")) {
            if (ev.getPlayer().hasMetadata("euPrendi")) {
                if (ev.getPlayer().hasMetadata("cabouDePrender")) {
                    ev.getPlayer().removeMetadata("cabouDePrender", KoM._instance);
                    return false;
                }
                UUID prendeu = (UUID) MetaShit.getMetaObject("prendeu", ev.getPlayer());
                Player preso = Bukkit.getPlayer(prendeu);
                if (preso != null) {
                    preso.removeMetadata("prendeu", KoM._instance);
                    preso.sendMessage(ChatColor.GREEN + L.m("Voce foi solto !"));
                }
                ev.getPlayer().removeMetadata("euPrendi", KoM._instance);
                ev.getPlayer().removeMetadata("prendeu", KoM._instance);
                ev.getPlayer().sendMessage(ChatColor.GREEN + L.m("Voce soltou o alvo !"));
                if (preso.getPassenger() != null) {
                    ((Bat) preso.getPassenger()).setLeashHolder(null);
                    preso.getPassenger().remove();
                }
            } else {
                ev.setCancelled(true);
                UUID prendeu = (UUID) MetaShit.getMetaObject("prendeu", ev.getPlayer());
                Player quemPrendeu = Bukkit.getPlayer(prendeu);
                if (quemPrendeu == null) {
                    ev.getPlayer().removeMetadata("prendeu", KoM._instance);
                    ev.getPlayer().sendMessage(ChatColor.GREEN + L.m("Voce se soltou !"));
                    if (ev.getPlayer().getPassenger() != null) {
                        ((Bat) ev.getPlayer().getPassenger()).setLeashHolder(null);
                        ev.getPlayer().getPassenger().remove();
                    }
                    return false;
                } else {
                    if (quemPrendeu.getLocation().distance(ev.getPlayer().getLocation()) > 5) {
                        quemPrendeu.removeMetadata("euPrendi", KoM._instance);
                        quemPrendeu.removeMetadata("prendeu", KoM._instance);
                        ev.getPlayer().removeMetadata("prendeu", KoM._instance);
                        if (ev.getPlayer().getPassenger() != null) {
                            ev.getPlayer().getPassenger().remove();
                        }
                        quemPrendeu.sendMessage(ChatColor.GREEN + L.m("O Alvo se soltou !"));
                        ev.getPlayer().sendMessage(ChatColor.GREEN + L.m("Voce se soltou !"));
                        if (ev.getPlayer().getPassenger() != null) {
                            ((Bat) ev.getPlayer().getPassenger()).setLeashHolder(null);
                            ev.getPlayer().getPassenger().remove();
                        }
                        return false;
                    }
                }
                ev.setCancelled(true);
                ev.setUseItemInHand(Event.Result.DENY);
                ev.setUseInteractedBlock(Event.Result.DENY);
                ev.getPlayer().sendMessage(ChatColor.RED + L.m("Voce esta amarrado e nao consegue usar as maos !"));
                return true;
            }
        }
        return false;
    }

    public static void prende(PlayerInteractEntityEvent ev) {
        if (!CFG.mundoGuilda(ev.getPlayer().getLocation()) && !ev.getPlayer().getWorld().getName().equalsIgnoreCase("woe") && !ev.getPlayer().getWorld().getName().equalsIgnoreCase("arena")) {
            return;
        }
        if (ev.getPlayer().getInventory().getItemInMainHand() != null && ev.getPlayer().getInventory().getItemInMainHand().getType() == Material.LEASH) {
            if (Jobs.getJobLevel("Engenheiro", ev.getPlayer()) == 1) {
                if (ev.getRightClicked() != null && ev.getRightClicked().getType() == EntityType.PLAYER) {

                    if (!KoM.tem(ev.getPlayer().getInventory(), Material.REDSTONE)) {
                        ev.getPlayer().sendMessage(ChatColor.RED + L.m("Voce precisa redstones para ativar a corrente !"));
                        return;
                    }
                    KoM.removeInventoryItems(ev.getPlayer().getInventory(), Material.REDSTONE, 1);
                    Player alvo = (Player) ev.getRightClicked();
                    if (Jobs.getJobLevel("Ladino", alvo) == 1) {
                        ev.getPlayer().sendMessage(ChatColor.RED + L.m("O alvo rapidamente se desenrolou da corda !"));
                        return;
                    }
                    // prendendo
                    Bat porco = (Bat) ev.getPlayer().getWorld().spawnEntity(ev.getPlayer().getLocation(), EntityType.BAT);
                    porco.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 1));
                    // alvo.addPassenger(porco);
                    MetaShit.setMetaObject("cabouDePrender", ev.getPlayer(), "sim");
                    MetaShit.setMetaObject("prendeu", ev.getRightClicked(), ev.getPlayer().getUniqueId());
                    MetaShit.setMetaObject("prendeu", ev.getPlayer(), ev.getRightClicked().getUniqueId());
                    MetaShit.setMetaObject("euPrendi", ev.getPlayer(), ev.getRightClicked().getUniqueId());
                    porco.setLeashHolder(ev.getPlayer());

                    alvo.addPassenger(porco);

                    ev.getPlayer().sendMessage(ChatColor.GREEN + L.m("Voce prendeu o alvo !"));
                    ev.getPlayer().sendMessage(ChatColor.GREEN + L.m("Se voce fizer algo ou ir muito longe ira soltar o alvo !"));
                    alvo.sendMessage(ChatColor.RED + ev.getPlayer().getName() + L.m(" Te segurou numa corrente eletrica"));
                }
            }
        }
    }

// !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=!

    public static final List<Skill> skillList = Arrays.asList(
            new Skill(classe, "Bonka Boom", 2, false, new String[]{"§9Permite atirar com a pistola 'Bonka Boom'."}),
            new Skill(classe, "Criar Items com Redstone", 3, false, new String[]{"§9Aumenta chances de sucesso ao craftar items com redstone.", "§9Quanto maior seu nivel, maior a chance"}, true),
            new Skill(classe, "Resistência Elétrica", 5, true, new String[]{"§9Se torna resistente a eletrecidade"}),
            new Skill(classe, "Para Raios", 7, true, new String[]{"§9Permite usar um Para Raio para absorver relampagos."}),
            new Skill(classe, "Auto Dispenser", 9, true, new String[]{"§9Cria um auto dispenser que atira em inimigos"}),
            new Skill(classe, "Coleira Elétrica", 10, true, new String[]{"§9Permite usar uma coleira eletrica em inimigos."}),
            new Skill(classe, "Implantar Mecanicas", 13, true, new String[]{"§9Permite colocar mecanismos de redstone.", "§9Quanto maior seu nivel, maior a chance"}),
            new Skill(classe, "Mina Explosiva", 18, true, new String[]{"§9Pode colocar uma mina explosiva e criar um detonador"}),
            new Skill(classe, "Mega Bomba C4", 20, true, new String[]{"§9Pode explodir guildas com C4"})
    );

    public enum Skills {
        Bonka_Boom(skillList.get(0)),
        Criar_Items_com_Redstone(skillList.get(1)),
        Resistencia_Eletrica(skillList.get(2)),
        Para_Raios(skillList.get(3)),
        Auto_Dispenser(skillList.get(4)),
        Coleira_Eletrica(skillList.get(5)),
        Implantar_Mecanicas(skillList.get(6)),
        Mina_Explosiva(skillList.get(7)),
        Mega_Bomba_C4(skillList.get(8));

        public Skill skill;

        Skills(Skill skill) {
            this.skill = skill;
        }

    }

// !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=! - !=- SKILLS AREA -=!

}
