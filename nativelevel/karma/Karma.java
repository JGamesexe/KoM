package nativelevel.karma;


import nativelevel.KoM;
import nativelevel.MetaShit;
import nativelevel.sisteminhas.ClanLand;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Karma {

    public static void main(String[] args) {

        int karmaMorreu = 500;
        int karmaMatou = 100;

        int karmaGanho = (-karmaMorreu - karmaMatou) / 100;

        int karmaFinal = karmaMatou + karmaGanho;
        System.out.println(karmaGanho);
    }

    public static int DELIMITADOR = -50;

    public static void manoloMata(Player matador, LivingEntity morreu) {

        if (matador.getWorld().getName().equalsIgnoreCase("WoE") || matador.getWorld().getName().equalsIgnoreCase("Arena"))
            return;

        int karmaMatou = KoM.database.getKarma(matador.getUniqueId());
        int karmaMorreu = (ClanLand.getMobLevel(morreu.getLocation()) * 5) * 320 / 2;

        boolean player = false;
        if (morreu.getType() == EntityType.PLAYER) {
            player = true;
            karmaMorreu = KoM.database.getKarma(morreu.getUniqueId());
        } else {
            if (morreu.hasMetadata("mobLevel")) {
                int nivelMob = (Integer) MetaShit.getMetaObject("mobLevel", morreu) + 1;
                karmaMorreu = karmaMorreu * nivelMob; // recupera o /5
            } else if (KoM.mm.getMobManager().isActiveMob(morreu.getUniqueId())) {
                karmaMorreu = karmaMorreu * 50000;
            }
        }

        int karmaGanho = (karmaMorreu - karmaMatou) / 100;
        if (player == true) {
            karmaGanho = (-karmaMorreu - karmaMatou) / 75;
            // se to perdendo karma por matar um noob
            if (

                    ((Player) morreu).getLevel() <= 10 && karmaGanho < 0) {
                karmaGanho *= 10;
            }
        } else {
            if (matador.isOp() && KoM.debugMode) {
                matador.sendMessage("Karma do mob: " + karmaMorreu);
            }
            if (karmaGanho < 0) {
                karmaGanho = 0;
            }
            karmaGanho /= 6;
        }

        // se quem morreu for criminoso, não perde karma
        if (morreu.getType() == EntityType.PLAYER && karmaGanho < 0 && Criminoso.isCriminoso((Player) morreu)) {
            return;
        }

        if (karmaGanho > 100)
            karmaGanho = 100;

        int karmaFinal = karmaMatou + karmaGanho;
        if (karmaFinal < -32000) {
            karmaFinal = -32000;
        }
        if (karmaFinal > 32000) {
            karmaFinal = 32000;
        }

        int diferencaKarma = karmaFinal - karmaMatou;

        // se eu sou bom e matei um mau, nao perco karma
        if (karmaMatou > 0 && karmaMorreu < 0 && diferencaKarma < 0) {
            return;
        }

        if (diferencaKarma < 0 && !player) {
            diferencaKarma = 0;
            karmaFinal = karmaMatou;
        }

        if (diferencaKarma < 0) {
            diferencaKarma *= 5;
            matador.sendMessage(ChatColor.GREEN + "Karma: " + karmaMatou + " " + ChatColor.GOLD + "" + diferencaKarma);
            KoM.database.setKarma(matador.getUniqueId(), karmaMatou + diferencaKarma);
            if (!matador.hasMetadata("msgkarma") && matador.getLevel() <= 10) {
                MetaShit.setMetaObject("msgkarma", matador, true);
                matador.sendMessage("§e§l[Dica] §aVoce matou uma pessoa boa, e perdeu Karma. No KoM voce é livre, pode matar, roubar, pilhar, porém suas ações definem seu caráter.");
                matador.sendMessage("§e§l[Dica] §aPessoas um simbolo de caveira mais avermelhado são malvadas, pessoas com simbolos de carinha feliz mais azulados são boas.");
            }
        } else {
            matador.sendMessage(ChatColor.GREEN + "Karma: " + karmaMatou + " " + ChatColor.GOLD + "+" + diferencaKarma);
            KoM.database.setKarma(matador.getUniqueId(), karmaFinal);
        }

        if (karmaFinal != karmaMatou) {
            if (!ClanLand.permission.playerHas(matador, "kom.bom") && !ClanLand.permission.playerHas(matador, "kom.mau")) {
                ClanLand.permission.playerRemove(matador, "kom.bom");
            }
            if (karmaFinal >= DELIMITADOR && karmaMatou < DELIMITADOR) {
                if (ClanLand.permission.playerHas(matador, "kom.mau")) {
                    ClanLand.permission.playerRemove(matador, "kom.mau");
                }
                ClanLand.permission.playerAdd(matador, "kom.bom");
                matador.sendMessage(ChatColor.GREEN + "Você se tornou uma pessoa de bom Karma.");
            } else if (karmaFinal <= DELIMITADOR && karmaMatou > DELIMITADOR) {
                if (ClanLand.permission.playerHas(matador, "kom.bom")) {
                    ClanLand.permission.playerRemove(matador, "kom.bom");
                }
                ClanLand.permission.playerAdd(matador, "kom.mau");
                matador.sendMessage(ChatColor.GREEN + "Você se tornou uma pessoa de mau Karma.");
            }
        }

        Fama.manoloMata(matador, morreu);

    }

}
