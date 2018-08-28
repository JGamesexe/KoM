package nativelevel.titulos;

import nativelevel.KoM;
import nativelevel.karma.KarmaFameTables;
import nativelevel.rankings.RankCache;
import nativelevel.scores.SBCoreListener;
import nativelevel.titulos.TituloDB.PData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author Carlos André Feldmann Júnior
 */
public class Titulos {

    public static void setTitulo(Player p, String titulo, ChatColor cor) {
        PData pd = TituloDB.getPlayerData(p);
        pd.setTitulo(titulo, cor);
        update(p);
    }

    public static TreeMap<String, List<ChatColor>> getTitulos(Player p) {

        TreeMap<String, List<ChatColor>> titulos = new TreeMap(TituloDB.getTitulos(p.getUniqueId()));

        if (RankCache.souTopEm.containsKey(p.getUniqueId())) {
            List<ChatColor> cor = new ArrayList<ChatColor>();
            cor.add(ChatColor.AQUA);
            titulos.put(RankCache.souTopEm.get(p.getUniqueId()).titulo, cor);
        }

        if (KarmaFameTables.cacheTitulos.containsKey(p.getUniqueId())) {
            String titulo = KarmaFameTables.cacheTitulos.get(p.getUniqueId());
            if (!titulo.equalsIgnoreCase("")) {
                List<ChatColor> cor = new ArrayList<ChatColor>();
                cor.add(ChatColor.WHITE);
                titulos.put(titulo, cor);
            }
        }
        return titulos;
    }

    public static String trabalhaTitulo(String titulo, Player p, ChatColor cor) {
        if (titulo.equals("clan")) {
            // Clan c = ClanDB.getPlayerWithCache(p.getUniqueId()).getClan();
            // if (c != null) {
            //     return c.getTag();
            // } else {
            Titulos.setTitulo(p, "", ChatColor.WHITE);
            return null;

            // }
        }
        Sexo s = TituloDB.getPlayerData(p).getSexo();
        titulo = cor + s.getPrefix() + " " + s.feminiza(titulo);
        return titulo;

    }

    public static void update(Player p) {
        KoM.sb.updatePlayer(p);
    }

    static HashMap<UUID, ScoreCache> cache = new HashMap();

    public static class ScoreCache {

        public UUID uid;
        public String sufix;
        public String nomeequipe;
        public String prefix;

        public ScoreCache(UUID uid, String sufix, String nomeequipe, String prefix) {
            this.uid = uid;
            this.sufix = sufix;
            this.nomeequipe = nomeequipe;
            this.prefix = prefix;
        }

    }

}
