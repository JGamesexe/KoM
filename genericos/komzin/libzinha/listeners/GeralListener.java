package genericos.komzin.libzinha.listeners;


import genericos.komzin.libzinha.utils.Efeitos;
import me.asofold.bpl.simplyvanish.SimplyVanish;
import me.asofold.bpl.simplyvanish.api.events.SimplyVanishStateEvent;
import nativelevel.KoM;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashSet;

public class GeralListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public static void Comandos(PlayerCommandPreprocessEvent ev) {

        if (ev.getMessage().startsWith("/gamemode")) {

            ev.setCancelled(true);

            ev.getPlayer().sendMessage("§cUse §f/gm §cpara alterar seu modo de jogo ;)");

        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void JoinEvent(PlayerJoinEvent ev) {


        if (ev.getPlayer().isOp()) {

            ev.getPlayer().sendMessage("§c§lTotal de pessoas com OP §f(§2" + Bukkit.getOperators().size() + "§f) §c§lUse §f/ops");

        }

//
//        if (ev.getPlayer().getName().equalsIgnoreCase("Camila")) {
//
//            ev.getPlayer().sendMessage("Oi camila, bem vinda de volta ao KOM !!");
//
//            ev.getPlayer().sendMessage("Sentimos muita falta sua... muita mesmo !");
//
//            ev.getPlayer().sendMessage("O dev do server, gosta pakarawlhos de vc !");
//            for (Player pl : Bukkit.getOnlinePlayers())
//                pl.sendMessage(ChatColor.GREEN + "Camila entrou lindamente no servidor");
//
//        }

        //TitleAPI.sendTabHeader(ev.getPlayer(), ChatColor.GOLD + "Bem-Vindo ao §c§lKnights Of Minecraft");

        ev.getPlayer().sendMessage("");

        ev.getPlayer().sendMessage(" §6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄");

        int online = Bukkit.getOnlinePlayers().size();

        ev.getPlayer().sendMessage("");

        String msgOnline;
        if (online > 1) msgOnline = "§aTemos §c§l" + online + " §aaventureiros pelo mundo de Aden!";
        else msgOnline = "§aTemos §c§l" + online + " §aaventureiro solitário pelo mundo de Aden!";


//        if (ev.getPlayer().getLevel() < 101) {
//
//            ev.getPlayer().sendMessage("   §aBem-vindo ao " + ChatColor.GREEN + ChatColor.BOLD + "KoM §a(RPG), Pequeno Aprendiz!");
//
//            ev.getPlayer().sendMessage(msgOnline);
//
//            ev.getPlayer().sendMessage("");
//
//            ev.getPlayer().sendMessage("   §aSe tiver alguma dúvida no servidor ou achar difícil aprender, não desista!");
//
//            ev.getPlayer().sendMessage("   §aAcesse: §cwww.knightsofminecraft.com.br/ para ver a WIKI");
//
//        } else if (ev.getPlayer().getLevel() < 110) {

        ev.getPlayer().sendMessage("             §aBem-vindo ao " + KoM.tag + "§a (RPG), Aventureiro!");

        ev.getPlayer().sendMessage("      " + msgOnline);

        ev.getPlayer().sendMessage("");

        ev.getPlayer().sendMessage("            §aÉ importante participar da comunidade do KoM");

        ev.getPlayer().sendMessage("");

        ev.getPlayer().sendMessage("                  §aAcesse: §cwww.knightsofminecraft.com");

//        } else {
//
//
//            ev.getPlayer().sendMessage("   §aBem-vindo ao " + ChatColor.GREEN + "KoM (RPG), Nobre Guerreiro!");
//
//            ev.getPlayer().sendMessage(msgOnline);
//
//            ev.getPlayer().sendMessage("");
//
//            ev.getPlayer().sendMessage("   §aFacilite sua vida, upe rapidamete ate o 100 e resete!!");
//
//            ev.getPlayer().sendMessage("   §aCompre cash em §cwww.knightsofminecraft.com");
//
//        }

        ev.getPlayer().sendMessage("");

        ev.getPlayer().sendMessage(" §6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄§e§l▄§6§l▄");

    }


    public static HashSet<String> ListaEfeitos = new HashSet();


    @EventHandler(priority = EventPriority.NORMAL)
    public void VanishEvent(SimplyVanishStateEvent event) {

        if (!event.getPlayer().hasPermission("manialibkom.vanish.efeito")) {

            return;

        }

        if (!event.getPlayer().isOp() && !ListaEfeitos.contains(event.getPlayer().getName())) {

            return;

        }

        if (!event.getVisibleAfter()) {

            if (SimplyVanish.isVanished(event.getPlayer())) {
            }


        } else if (SimplyVanish.isVanished(event.getPlayer())) {

            return;

        }

        if (event.getPlayer().getName().equalsIgnoreCase("ZidenVentania")) {

            Location loc = event.getPlayer().getLocation();

            Efeitos.effectBats(loc);

            Efeitos.effectExplosion(loc);

            Efeitos.effectFlames(loc);

            Efeitos.effectLightning(loc);

            Efeitos.effectSmoke(loc);
        }


    }

}


/* Location:              C:\Users\User\Desktop\REPO\InstaMCLibKom.jar!\instamc\coders\libkom\listeners\GeralListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
