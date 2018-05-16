/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.integration;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nativelevel.CFG;
import nativelevel.Classes.Thief;
import nativelevel.Comandos.Terreno;
import nativelevel.Custom.Buildings.Construcao;
import nativelevel.Custom.CustomItem;
import nativelevel.KoM;
import nativelevel.Lang.L;
import nativelevel.karma.Criminoso;
import nativelevel.sisteminhas.ClanLand;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SimpleClanKom {

    public static boolean canDamage(EntityDamageEvent ev) {

        //if(ev.getEntity() instanceof Monster) return;
        if (ev.getEntity().getWorld().getName().equalsIgnoreCase("NewDungeon")) {
            return true;
        }

        String type = ClanLand.getTypeAt(ev.getEntity().getLocation());
        if (ev.getEntity() instanceof Monster && !(ev.getEntity() instanceof Tameable) && type.equalsIgnoreCase("SAFE")) {
            ev.getEntity().getWorld().playEffect(ev.getEntity().getLocation(), Effect.SMOKE, 0);
            ev.getEntity().remove();
            ev.setCancelled(true);
            return false;
        }
        if (ev.getEntity() instanceof Horse && ev.getEntity().getPassenger() != null && type.equalsIgnoreCase("SAFE")) {
            ev.setCancelled(true);
            return false;
        }
        return true;
    }

    public static boolean canPvp(Player atacante, Player defensor) {

        if (atacante.getWorld().getName().equalsIgnoreCase("NewDungeon")) {
            // if (!Criminoso.isCriminoso(defensor)) {
            return false;
            // }
        }

        ClanPlayer atk = ClanLand.manager.getClanPlayer(atacante);
        ClanPlayer def = ClanLand.manager.getClanPlayer(defensor);

        if (WorldGuardKom.ehSafeZone(defensor.getLocation())) {
            // se quem ta tomando não é criminoso nao pode pvp
            if (!Criminoso.isCriminoso(defensor)) {
                KoM.debug("NO CAN PVP CRIM");
                return false;
            } else {
                // criminoso tomando
                if (Criminoso.isCriminoso(atacante)) {
                    // criminoso batendo em alguem, nop
                    KoM.debug("NO CAN PVP");
                    return false;
                }
            }
        }
        return true;
    }

    //public static HashSet<Location> verificados = new HashSet<Location>();
    public List<Material> permitidosMineradorRetirar = Arrays.asList(
            Material.COBBLESTONE,
            Material.STONE,
            Material.BRICK,
            Material.BRICK_STAIRS,
            Material.COBBLESTONE_STAIRS,
            Material.WOOD,
            Material.LOG,
            Material.LOG_2);

    public static List<Material> liberados = Arrays.asList(
            Material.SUGAR_CANE_BLOCK,
            Material.PUMPKIN,
            Material.DEAD_BUSH,
            Material.LONG_GRASS,
            Material.CACTUS,
            Material.RED_MUSHROOM,
            Material.BROWN_MUSHROOM,
            Material.SUGAR_CANE,
            Material.RED_ROSE,
            Material.YELLOW_FLOWER,
            Material.LOG,
            Material.LEAVES,
            Material.SAPLING,
            Material.IRON_ORE,
            Material.LEAVES,
            Material.LOG_2,
            Material.LEAVES_2,
            Material.PUMPKIN,
            Material.COCOA,
            Material.DOUBLE_PLANT);

    public static void interact(PlayerInteractEvent ev) {
        if (ev.getPlayer().getWorld().getName().equalsIgnoreCase("NewDungeon")) {
            return;
        }

        if (ev.getPlayer().getInventory().getItemInMainHand() != null && ev.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR) {
            if (ev.getPlayer().getInventory().getItemInMainHand().getType() == Material.FLINT_AND_STEEL
                    && (ev.getClickedBlock() != null && ev.getClickedBlock().getType() != Material.NETHERRACK && ev.getBlockFace() == BlockFace.UP)) {
                ev.setCancelled(true);
                return;
            }
        }
        if (!ev.getPlayer().isOp() && !ClanLand.permission.has(ev.getPlayer(), "kom.build") && ev.getClickedBlock() != null && ev.getClickedBlock().getType() != Material.AIR) {
            if (!podeMexer(ev.getPlayer(), ev.getClickedBlock().getLocation(), ev.getClickedBlock())) {
                ev.setCancelled(true);
                KoM.debug("parei o interact com simpleclans");
            } else {
                if (ev.getClickedBlock().getType() == Material.BEACON) {
                    ev.getClickedBlock().breakNaturally();
                }
            }
        }
        if (ev.getClickedBlock() != null)

        {
            String type = ClanLand.getTypeAt(ev.getClickedBlock().getLocation());
            if (type.equalsIgnoreCase("WARZ")) {
                if (ev.getPlayer().getInventory().getItemInMainHand() != null) {
                    if (ev.getPlayer().getInventory().getItemInMainHand().getType() == Material.BUCKET || ev.getPlayer().getInventory().getItemInMainHand().getType() == Material.WATER_BUCKET) {
                        ev.getPlayer().sendMessage(ChatColor.RED + L.m("Voce nao pode fazer isto !"));
                        ev.setCancelled(true);
                    }
                }
            }
        }

    }

    // metodo principal de controle de permissões
    public static boolean podeMexer(Player p, Location l, Block b) {

        if (l.getY() <= 45) return true;
        if (p.isOp() || ClanLand.permission.has(p, "kom.interact")) return true;

        String type = ClanLand.getTypeAt(l);

        if (type.equalsIgnoreCase("CLAN")) {
            Clan aqui = ClanLand.getClanAt(l);
            Clan meu = ClanLand.manager.getClanByPlayerName(p.getName());
            if (b != null && b.getType() == Material.CHEST) {
                Location origem = b.getLocation().getChunk().getBlock(0, 0, 0).getLocation();
                //Terrenos.getClanAt(l);
            }

            //  if(Terrenos.  ev.getPlayer().)
            if ((aqui != null) && (meu == null || !meu.getTag().equalsIgnoreCase(aqui.getTag()))) {

                //// TENTANDO MEXER EM TERRA DE INIMIGO

                if (p.getInventory().getItemInMainHand().getType() == Material.WATER_BUCKET) {
                    p.sendMessage(ChatColor.RED + L.m("Ninguem pediu pra voce lavar a guilda dos outros !"));
                    return false;
                }

                if ((p.getInventory().getItemInMainHand() != null
                        && p.getInventory().getItemInMainHand().getType().isBlock()
                        && !p.getInventory().getItemInMainHand().getType().equals(Material.AIR))
                        || b.getType().name().contains("DOOR")
                        || b.getType().name().contains("TRAP_DOOR")
                        || b.getType().name().contains("FENCE_GATE")
                        || b.getType() == Material.ENDER_CHEST
                        || b.getType() == Material.FURNACE
                        || b.getType() == Material.BURNING_FURNACE
                        || b.getType() == Material.BREWING_STAND
                        || b.getType() == Material.LEVER
                        || b.getType().name().contains("BUTTON")
                        || b.getType().name().contains("PLATE")) {

                    p.sendMessage(ChatColor.RED + L.m("Voce nao pode fazer isto em terras dos outros"));
                    if ((p.getInventory().getItemInMainHand() != null
                            && p.getInventory().getItemInMainHand().getType().isBlock()
                            && !p.getInventory().getItemInMainHand().getType().equals(Material.AIR))
                            || b.getType().name().contains("DOOR")
                            || b.getType().name().contains("FENCE_GATE")) p.damage(5);
                    else p.damage(1);

                    return false;
                } else if (b.getType() == Material.CHEST) {
                    if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() != Material.AIR) {
                        String customItem = CustomItem.getCustomItem(p.getInventory().getItemInMainHand());
                        if (customItem != null && customItem.equalsIgnoreCase(L.m("Lockpick"))) {
                            Thief.bisbilhota(p, b);
                        }
                        if (customItem != null && customItem.equalsIgnoreCase(L.m("Pe de Cabra"))) {
                            Thief.stealFullChest(p, b);
                        }
                    } else {
                        if (!p.isSneaking()) {
                            Thief.zoiudo(p, (Chest) b.getState());
                            if (p.getOpenInventory() != null) {
                                p.sendMessage(ChatColor.GREEN + "Voce está espiando o baú. Voce pode usar shift + click sem nada na mão para roubar um item aleatório. Custa " + CFG.custoPegarItemRandom + " PPS");
                            }
                        } else {
                            Thief.pegaItemAleatorio(p, b);
                        }
                    }
                    return false;
                }
                /////// SE ELE EH DO CLAN
            } else if (meu != null && aqui != null && meu.getTag().equalsIgnoreCase(aqui.getTag())) {
                ClanPlayer cp = ClanLand.manager.getClanPlayer(p);
                String[] owner = ClanLand.getOwnerAt(l);
                if (owner.length != 0 && owner[0].equalsIgnoreCase("none")) {

                    int level = ClanLand.getPermLevel(p.getLocation());

                    KoM.debug("Terreno publico modo " + level);

                    switch (level) {
                        default:
                            return false;
                        case 1:
                            return cp.isTrusted()
                                    && (b.getType().toString().contains("DOOR")
                                    || b.getType().toString().contains("BUTTON")
                                    || b.getType().toString().contains("PLATE")
                                    || b.getType().toString().contains("LEVER"));
                        case 2:
                            return (cp.isTrusted()
                                    && (b.getType().toString().contains("DOOR")
                                    || b.getType().toString().contains("BUTTON")
                                    || b.getType().toString().contains("PLATE")
                                    || b.getType().toString().contains("LEVER")))

                                    || ((b.getType().toString().contains("DOOR")
                                    || b.getType().toString().contains("BUTTON")
                                    || b.getType().toString().contains("PLATE")
                                    || b.getType().toString().contains("LEVER")));
                        case 3:
                            return (cp.isTrusted())

                                    || (b.getType().toString().contains("DOOR")
                                    || b.getType().toString().contains("BUTTON")
                                    || b.getType().toString().contains("PLATE")
                                    || b.getType().toString().contains("LEVER"));
                        case 4:
                            return true;
                        case 5:
                            return true;
                        case 6:
                            return true;
                    }
                }
                if (owner[0].equalsIgnoreCase(p.getUniqueId().toString()) || ClanLand.isMemberAt(l, p.getUniqueId())) {
                    return true; // tem permissão
                }
//                if (!cp.isLeader()) {
//                    // se nao eh membro confiável só mexe em terra q eh dele
//                    if (!cp.isTrusted()) {
//
//                        if (owner != null && owner.equalsIgnoreCase(p.getUniqueId().toString())) {
//                            return true;
//                        }
//                        if (ClanLand.isMemberAt(l, p.getUniqueId())) {
//                            return true;
//                        }
//                        // nao mexe em chest se nao for trusted
//                        if (b.getType() == Material.CHEST) {
//                            return false;
//                        }
//                        if (b.getType() == Material.WOODEN_DOOR || b.getType() == Material.FURNACE || b.getType() == Material.TRAP_DOOR || b.getType() == Material.WORKBENCH) {
//                            return false;
//                        }
//                        return false;
//                        // ele eh confiável mexe em tudo menos onde tem dono
//                    } else {
//                        if (owner != null && owner.equalsIgnoreCase(p.getUniqueId().toString())) {
//                            return true;
//                        }
//                        if (members != null && members.contains(p.getUniqueId().toString())) {
//                            return true;
//                        }
//                        // se ele nao for o dono
//                        if (owner != null && !owner.equalsIgnoreCase("none")) {
//                            return false;
//                        } else {
//                            return true;
//                        }
//                    }
//                }
                else {
                    KoM.debug("podeMexer ! ?.?.? !");
                    return false;
                }
            }
        } else {
            KoM.debug("Autorizei com interact do simpleClans");
            return true;
        }
        KoM.debug("Chegou no final de podeMexer");
        return false;
    }

    public static boolean canBuild(Player p, Location l, Block b, boolean placing) {
        if (l.getWorld().getEnvironment() == Environment.THE_END) {
            return true;
        }
        if (p.getWorld().getName().equalsIgnoreCase("NewDungeon")) {
            if (placing && b.getType() == Material.TORCH && l.getBlock().getType() != Material.STATIONARY_WATER) {
                return true;
            }
            ApplicableRegionSet set = KoM.worldGuard.getRegionManager(Bukkit.getWorld("NewDungeon")).getApplicableRegions(b.getLocation());
            if (set.size() >= 1) {
                Iterator<ProtectedRegion> i = set.iterator();
                while (i.hasNext()) {
                    ProtectedRegion regiao = i.next();
                    //if (regiao.getId().contains("tutorial")) {
                    // return true;
                    if (!p.isOp() && !ClanLand.permission.has(p, "kom.build")) {
                        p.damage(5D);
                        p.sendMessage(ChatColor.RED + L.m("Voce apenas pode mecher em dungeons em locais específicos !!"));
                        return false;
                    }
                }
            }
            if ((!p.isOp() && !ClanLand.permission.has(p, "kom.build")) && b.getType() != Material.TORCH) {
                p.damage(5D);
                p.sendMessage(ChatColor.RED + L.m("Voce apenas pode mecher em dungeons em locais específicos !!"));
                return false;
            }
            return p.isOp() || ClanLand.permission.has(p, "kom.build");
        } else if (p.getWorld().getName().equalsIgnoreCase("woe")) {
            ApplicableRegionSet set = KoM.worldGuard.getRegionManager(l.getWorld()).getApplicableRegions(l);
            if (set.size() > 0) {
                return true;
            }
            if (p.isOp() && ClanLand.permission.has(p, "kom.build")) {
                return true;
            }
            p.sendMessage(ChatColor.RED + L.m("Voce apenas pode construir/destruir as areas marcadas !"));
            return false;
        }
        String type = ClanLand.getTypeAt(l);
        if (KoM.debugMode) {
            KoM.log.info("tentando construir em " + type);
        }
        // building in wilderness
        if (type.equalsIgnoreCase("WILD")) {

            if (Construcao.chunkConstruido(l.getChunk())) {
                p.sendMessage(ChatColor.RED + "Você não pode construir aqui");
                return false;
            }

            if (Terreno.temGuildaPerto(p, b.getLocation(), true)) {
                p.sendMessage(L.m("§cVoce está muito próximo de outra guilda para mexer nesta terra !"));
                p.damage(3);
                return false;
            }

            if (l.getBlockY() >= 50) {
                if (b.getType().equals(Material.SAPLING) || !placing && liberados.contains(b.getType())) return true;
                else
                    p.sendMessage("§cEm terras sem dono você só pode plantar mudas de árvores e quebrar arvores e recursos naturais");
            } else {
                return true;
            }

//            Block gambs = l.getChunk().getBlock(5, 0, 5);
//            if (gambs.getType() == GeneralListener.gambiarra) {
//                l.getChunk().getBlock(5, 0, 5).setType(Material.GLOWSTONE);
//                MetaShit.setMetaObject("temporegen", l.getChunk().getBlock(5, 0, 5), (System.currentTimeMillis() / 1000) + (60 * 30));
//                if (!p.hasMetadata("msgwild")) {
//                    p.sendMessage(ChatColor.RED + "Voce esta em terras sem dono !");
//                    p.sendMessage(ChatColor.RED + "Este terreno será regenerado em breve e tudo que for construido vai ser perdido !");
//                    p.sendMessage(ChatColor.RED + "Para construir voce precisa de uma " + ChatColor.GREEN + "guilda " + ChatColor.RED + " !");
//                    p.sendMessage(ChatColor.RED + "Use o comando " + ChatColor.GREEN + "/f criar <tag> <nome> " + ChatColor.RED + " !");
//                    MetaShit.setMetaString("msgwild", p, "");
//                }
//            }

            /*
             if (l.getBlockY() > 50) {
             if (b != null && !placing && liberados.contains(b.getType()) || b.getType() == Material.SAPLING) {
             if (placing && b != null && b.getType() == Material.SAPLING) {
             if (p.hasPotionEffect(PotionEffectType.WATER_BREATHING)) {
             return false;
             }
             p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 20 * 10, 0));
             ClanPlayer cp = ClanLand.manager.getClanPlayer(p);
             if (cp == null) {
             p.sendMessage(ChatColor.RED + L.m("Voce precisa de uma guilda para poder colocar arvores !"));
             return false;
             } else {
             if (Terreno.temGuildaPerto(p, cp, b.getLocation())) {
             p.sendMessage(L.m("Voce nao pode plantar arvores colada em outras guildas que nao sejam a sua !"));
             return false;
             }
             }
             }
             return true;
             }
             if (!p.isOp() && !ClanLand.permission.has(p, "kom.build")) {
             p.sendMessage(ChatColor.RED + L.m("Voce nao sabe mecher em terras sem dono !"));
             return false;
             }
             }
             if (b.getType() == Material.LONG_GRASS && Jobs.rnd.nextInt(2000) == 1) {
             b.getWorld().dropItemNaturally(l, new ItemStack(Material.EMERALD, 1));
             p.playSound(l, Sound.ITEM_PICKUP, 10, 10);
             }
             */
        } else if (!p.isOp() && !ClanLand.permission.has(p, "kom.build") && type.equalsIgnoreCase("SAFE")) {
            // regions have privileges!!!
            ApplicableRegionSet set = KoM.worldGuard.getRegionManager(l.getWorld()).getApplicableRegions(l);
            if (set.size() > 0 && set.canBuild(KoM.worldGuard.wrapPlayer(p))) {
                return true;
            }
            p.sendMessage(ChatColor.RED + L.m("Voce nao pode fazer isto em vilas !"));
            return false;
        } else if (type.equalsIgnoreCase("WARZ")) {
            p.sendMessage(ChatColor.RED + L.m("Voce nao pode mecher em uma zona de guerra !"));
            return p.isOp() || ClanLand.permission.has(p, "kom.build");
        } else if (type.equalsIgnoreCase("CLAN") && !p.isOp()) {
            Clan aqui = ClanLand.getClanAt(l);

            KoM.debug("tentando construir nas terras de " + aqui.getTag());

            if (aqui.isMember(p)) {
                KoM.debug("sou membro de " + aqui.getTag());

                if (aqui.isLeader(p)) return true; // lider constroi no clan inteiro

                String[] owner = ClanLand.getOwnerAt(l);
                if (owner.length == 0 || owner[0].equalsIgnoreCase("none")) {
                    KoM.debug("to em terra publica");
                    ClanPlayer cp = ClanLand.manager.getClanPlayer(p);

                    int level = ClanLand.getPermLevel(l);
                    KoM.debug("Terreno publico no modo " + level);

                    switch (level) {
                        default:
                            return false;
                        case 5:
                            return cp.isTrusted();
                        case 6:
                            return true;
                    }

                } else {
                    if (owner[0].equalsIgnoreCase(p.getUniqueId().toString()) || ClanLand.isMemberAt(l, p.getUniqueId())) {
                        return true; // tem permissão
                    }
                }
            }
            if (placing) p.damage(5);
            p.sendMessage(ChatColor.RED + L.m("Estas terras nao sao suas para voce poder construir aqui !"));
            return false;
        }
        return true;
    }

    public static void setupFactionForBeginner(Player p) {
        for (Player pOnline : Bukkit.getOnlinePlayers()) {
            Clan c = ClanLand.manager.getClanByPlayerName(p.getName());
            if (c.getHomeLocation() == null) {
                continue;
            }
            if (c.getOnlineMembers().size() > 2 && ClanLand.getPoder(c.getTag()) < ClanLand.getQtdTerrenos(c.getTag())) {
                p.sendMessage(ChatColor.GREEN + "Voce entrou para a guilda " + c.getName());
                p.sendMessage(ChatColor.GREEN + "Lembre-se, este e um jogo cooperativo !");
                p.sendMessage(ChatColor.GREEN + "Tente " + ChatColor.YELLOW + "fazer bons amigos" + ChatColor.GREEN + " !!");
                //c.addBb("Jabu", p.getName() + " eh um iniciante, e entrou em sua guilda !");
                //c.addBb("Jabu", "Ele se encontra na home de sua faccao ! Tente ajuda-lo !");
                //c.addBb("Jabu", "Assim" + ChatColor.YELLOW + "faccao " + ChatColor.GREEN + "pode ficar " + ChatColor.YELLOW + "maior e mais forte" + ChatColor.GREEN + " !");
                //BungeeCordKom.tp(p, c.getHomeLocation());
                // p.teleport(c.getHomeLocation());
                return;
            }
        }
        p.sendMessage(ChatColor.RED + "Infelizmente nenhuma guilda disponivel " + ChatColor.YELLOW + ":(");
        p.sendMessage(ChatColor.RED + "Tente ir a " + ChatColor.YELLOW + "Rhodes" + ChatColor.RED + " e ache uma ou " + ChatColor.YELLOW + "faca um dinheiro e crie uma" + ChatColor.RED + " !");
    }
}
