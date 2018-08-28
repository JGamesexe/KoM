package nativelevel.scores;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import nativelevel.KoM;
import nativelevel.karma.Criminoso;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.sisteminhas.KomSystem;
import nativelevel.titulos.TituloDB;
import nativelevel.titulos.Titulos;
import net.minecraft.server.v1_12_R1.*;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import pixelmc.ServerReboot;
import pixelmc.utils.CoisasDeTempo;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author NeT32
 */
public class SBCoreListener extends KomSystem {

    private static boolean change = false;

//    public SBCoreListener() {
//
//        Bukkit.getScheduler().scheduleSyncRepeatingTask(KoM._instance, new Runnable() {
//            @Override
//            public void run() {
//                for (Player pl : Bukkit.getOnlinePlayers()) {
//                    for (Player observer : Bukkit.getOnlinePlayers()) {
//
//                        PacketPlayOutPlayerInfo pf = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, ((CraftPlayer) pl).getHandle());
//                        // PacketPlayOutPlayerInfo pf = PacketPlayOutPlayerInfo.updateDisplayName(((CraftPlayer) pl).getHandle(), (CraftScoreboard) observer.getScoreboard());
//                        ((CraftPlayer) observer).getHandle().playerConnection.sendPacket(pf);
//                    }
//                }
//            }
//        }, 20 * 60, 20 * 60);
//
//    }

    @EventHandler(priority = EventPriority.HIGH)
    public void Aoentrar(final PlayerJoinEvent ev) {

        ev.setJoinMessage(null);
        KoM.sb.onJoin(ev);

//        final Player nego = ev.getPlayer();
//
//        Bukkit.getScheduler().runTaskLater(KoM._instance, () -> {
//
//            if (nego != null && nego.isOnline()) {
//
//                SBCoreListener.AdicionarPlayerScores(nego);
//                SBCore.setLevelPoints(nego, nego.getLevel());
//                SBCore.AtualizaObjetivos(nego);
//
////              SBCore.CriarObjetivos();
//
////              Bukkit.getScheduler().scheduleSyncDelayedTask(KoM._instance, new Runnable() {
////
////                  @Override
////                  public void run() {
////                      if (ev.getPlayer() != null) {
////                          ScoreboardManager.makeScore(ev.getPlayer(), DisplaySlot.BELOW_NAME, ev.getPlayer().getName(), (int) ev.getPlayer().getHealth());
////                      }
////                  }
////              }, 40);
//
//            }
//        }, 20);
    }

//    public static void addPlayerToPlayer(Player zoiando, Player sendovisto) {
//
//        Cargo cargo = getCargo(sendovisto);
//        //TODO ISSO AQUI EM BAIXO NÃO TA FUNCIONANDO \/ (Dois espaços em players karma**nick)
//        String prefix = !cargo.equals(Cargo.PLAYER) ? getIconeKarma(sendovisto) + " " + cargo.icon + "§f" : getIconeKarma(sendovisto) + " §f";
//        String corNome = ChatColor.WHITE + "";
//
//        ClanPlayer cp1 = ClanLand.manager.getClanPlayer(zoiando);
//        ClanPlayer cp2 = ClanLand.manager.getClanPlayer(sendovisto);
//
//        if (cp2 != null) {
//
//            if (cp1 != null && cp1.isAlly(sendovisto)) corNome = ChatColor.AQUA + "";
//            else if (cp1 != null && cp1.getTag().equalsIgnoreCase(cp2.getTag())) corNome = ChatColor.GREEN + "";
//            else if (cp1 != null && cp1.isRival(sendovisto)) corNome = ChatColor.RED + "";
//
//        }
//
//        if (sendovisto.isOp() || sendovisto.hasPermission("kom.staff")) corNome = "§6";
//
//        if (Criminoso.isCriminoso(sendovisto)) corNome = "§n";
//
//        String prefixo = prefix + " " + corNome;
//
//        String level = "" + sendovisto.getLevel();
//        if (staffs.contains(cargo)) level = "000";
//
//        int levelSize = level.length();
//
//        if (levelSize == 1) level = "§e" + "  " + level + " ";
//        else if (levelSize == 2) level = "§e" + " " + level + "§8.";
//        else level = "§e" + level;
//
//        String customNick = level + prefixo + sendovisto.getName();
//
//        addToTeam(zoiando, sendovisto, cargo.teamName);
//        sendToObserver(zoiando, sendovisto, customNick);
//    }

//    public static String getIconeKarma(Player p) {
//        int karma = KoM.database.getKarma(p.getUniqueId());
//        String icone = "§7ʘ";
//        if (karma < 0) {
//
//            icone = "☠";
//            if (karma < -20000) icone = "§4" + icone;
//            else if (karma < -10000) icone = "§c" + icone;
//            else if (karma < -2000) icone = "§6" + icone;
//            else if (karma < -100) icone = "§e" + icone;
//
//        } else if (karma > 0) {
//
//            icone = "☺";
//            if (karma > 20000) icone = "§9" + icone;
//            else if (karma > 10000) icone = "§b" + icone;
//            else if (karma > 2000) icone = "§a" + icone;
//            else if (karma > 100) icone = "§2" + icone;
//
//        }
//
//        return icone;
//    }

    private static final List<Cargo> staffs = Arrays.asList(Cargo.DEVELOPER, Cargo.OPERADOR, Cargo.COORDENADOR, Cargo.ADMIN, Cargo.MESTRE, Cargo.BUILDER, Cargo.MOD, Cargo.LORER);

    public enum Cargo {
        DEVELOPER("§b§lDEV", "00Dev"),
        OPERADOR("§8§lOP", "01Op"),
        COORDENADOR("§5§lCOORD", "02Coord"),
        ADMIN("§4§lADMIN", "03Admin"),
        MESTRE("§3§lMESTRE", "04Mestre"),
        BUILDER("§e§lBUILDER", "05Builder"),
        MOD("§2§lMOD", "06Mod"),
        LORER("§f§lLORER", "07Lorer"),
        YOUTUBER("§f§lYou§c§lTuber", "08Youtuber"),
        AJUDANTE_LORD("§6◂✠▸ §a§lAJD", "09AjudanteL"),
        LORD("§6◂✠▸", "10Lord"),
        AJUDANTE_MARQUES("§9◂✠▸ §a§lAJD", "11AjudanteM"),
        MARQUES("§9◂✠▸", "12Marques"),
        AJUDANTE_CAVALEIRO("§c◂✠▸ §a§lAJD", "13AjudanteC"),
        CAVALEIRO("§c◂✠▸", "14Cavaleiro"),
        AJUDANTE("§a§lAJD", "15Ajudante"),
        PLAYER("", "20Player");

        String icon;
        String teamName;

        Cargo(String icon, String teamName) {
            this.icon = icon;
            this.teamName = teamName;
        }
    }

    public static Cargo getCargo(Player p) {
        if (p.hasPermission("tag.developer")) return Cargo.DEVELOPER;
        else if (p.hasPermission("tag.operador")) return Cargo.OPERADOR;
        else if (p.hasPermission("tag.coordenador")) return Cargo.COORDENADOR;
        else if (p.hasPermission("tag.admin")) return Cargo.ADMIN;
        else if (p.hasPermission("tag.mestre")) return Cargo.MESTRE;
        else if (p.hasPermission("tag.builder")) return Cargo.BUILDER;
        else if (p.hasPermission("tag.mod")) return Cargo.MOD;
        else if (p.hasPermission("tag.lorer")) return Cargo.LORER;
        else if (p.hasPermission("tag.youtuber")) return Cargo.YOUTUBER;
        else if (p.hasPermission("kom.lord")) return p.hasPermission("tag.ajudante") ? Cargo.AJUDANTE_LORD : Cargo.LORD;
        else if (p.hasPermission("kom.templario")) return p.hasPermission("tag.ajudante") ? Cargo.AJUDANTE_MARQUES : Cargo.MARQUES;
        else if (p.hasPermission("kom.vip")) return p.hasPermission("tag.ajudante") ? Cargo.AJUDANTE_CAVALEIRO : Cargo.CAVALEIRO;
        else if (p.hasPermission("tag.ajudante")) return Cargo.AJUDANTE;
        else return Cargo.PLAYER;
    }

//    public static void AdicionarMudancaMeuScore(Player p) {
//        for (Player playerOn : Bukkit.getServer().getOnlinePlayers()) {
//            addPlayerToPlayer(playerOn, p);
//        }
//    }

//    public static void AdicionarPlayerScores(Player player) {
//        for (Player playerOn : Bukkit.getServer().getOnlinePlayers()) {
//            addPlayerToPlayer(player, playerOn);
//            if (playerOn.getUniqueId() != player.getUniqueId()) {
//                addPlayerToPlayer(playerOn, player);
//            }
//        }
//    }

//    public static void sendToObserver(Player observer, Player target, String customNick) {
//        EntityPlayer eTarget = ((CraftPlayer) target).getHandle();
//
//
//        eTarget.listName = new ChatComponentText(customNick);
//        PacketPlayOutPlayerInfo pf = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, eTarget);
//        ((CraftPlayer) observer).getHandle().playerConnection.sendPacket(pf);
//    }

//    public static void addToTeam(Player observer, Player player, String teamName) {
//
//        Scoreboard board = observer.getScoreboard();
//
//        Team team = board.getTeam(teamName);
//        if (team == null) team = board.registerNewTeam(teamName);
//        if (team.getOption(Team.Option.NAME_TAG_VISIBILITY) != Team.OptionStatus.NEVER) team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
//
//        team.addEntry(player.getName());
//
//    }

}
