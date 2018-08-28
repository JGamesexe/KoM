package nativelevel.ComandosNovos.commands.terreno;

import nativelevel.CFG;
import nativelevel.ComandosNovos.Comando;
import nativelevel.ComandosNovos.commands.terreno.terrenosubs.*;
import nativelevel.KoM;
import nativelevel.sisteminhas.ClanLand;
import nativelevel.utils.ExecutorType;
import nativelevel.utils.GUI;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Terreno extends Comando {

    public Terreno() {
        super("terreno", ExecutorType.PLAYER);
        subs.add(new TerrenoAmigo());
        subs.add(new TerrenoPublico());
        subs.add(new TerrenoDono());
        subs.add(new TerrenoSafe());
        subs.add(new TerrenoInfo());
        subs.add(new TerrenoConquistar());
    }

    @Override
    public void usouComando(CommandSender cs, String[] args) {

        Player p = (Player) cs;
        ClanPlayer cp = ClanLand.manager.getClanPlayer(p);

        if (!p.isOp()) {

            if (!p.getWorld().getName().equalsIgnoreCase(CFG.mundoGuilda) || ClanLand.isSafeZone(p.getLocation()) || ClanLand.isWarZone(p.getLocation()))
                p.sendMessage(KoM.tag + " §cEsse comando não funciona aqui!");

            if (cp == null) p.sendMessage(KoM.tag + " §cVocê não possui guilda para executar esse comando!");
            else subs.get(4).executeSubCmd(cs, args);

        } else {
            usouComandoBase(cs, args);
        }
    }
}
