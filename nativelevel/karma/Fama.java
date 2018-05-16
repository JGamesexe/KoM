package nativelevel.karma;

import nativelevel.KoM;
import nativelevel.MetaShit;
import nativelevel.sisteminhas.ClanLand;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

//import org.bukkit.event.player.PlayerBeaconEvent;

public class Fama {

    public static void manoloMata(Player matador, LivingEntity morreu) {

        if (matador.getWorld().getName().equalsIgnoreCase("WoE") || matador.getWorld().getName().equalsIgnoreCase("Arena"))
            return;

        int famaMatou = KoM.database.getFama(matador.getUniqueId());
        int famaMorreu = (ClanLand.getMobLevel(morreu.getLocation()) * 5) * 320 / 2;

        if (morreu.getType() == EntityType.PLAYER) {
            famaMorreu = KoM.database.getFama(morreu.getUniqueId());
        } else {
            if (morreu.hasMetadata("nivel")) {
                int nivelMob = (Integer) MetaShit.getMetaObject("nivel", morreu) + 1 * 3;
                famaMorreu = famaMorreu * nivelMob;
            } else if (KoM.mm.getMobManager().isActiveMob(morreu.getUniqueId())) {
                famaMorreu = 500000;
            }
        }


        int famaGanho = (famaMorreu - famaMatou) / 100 / 2;
        if (famaGanho == 0)
            famaGanho = 1;

        if (famaGanho > 100)
            famaGanho = 100;
        int famaFinal = famaMatou + famaGanho;

        if (matador.isOp() && KoM.debugMode) {
            matador.sendMessage("Fama Mob: " + famaMorreu);
        }


        if (famaFinal < 0) {
            famaFinal = 0;
        }
        if (famaFinal > 32000) {
            famaFinal = 32000;
        }

        int diferencaFama = famaFinal - famaMatou;

        if (diferencaFama > 0) {
            matador.sendMessage(ChatColor.GREEN + "Fama: " + famaMatou + " " + ChatColor.GOLD + "+" + diferencaFama);
            KoM.database.setFama(matador.getUniqueId(), famaMatou + diferencaFama);
        }
    }

}
