package nativelevel.Listeners;

import nativelevel.KoM;
import nativelevel.utils.StructureAPI;
import org.bukkit.Bukkit;
import org.bukkit.block.Structure;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

public class ArvoreCresce implements Listener {

    public static String[][][] arvoreTREE;
    public static String[][][] arvoreBIG_TREE;
    public static String[][][] arvoreMEGA_REDWOOD;
    public static String[][][] arvoreREDWOOD;
    public static String[][][] arvoreBIRCH;
    public static String[][][] arvoreJUNGLE;
    public static String[][][] arvoreSMALL_JUNGLE;
    public static String[][][] arvoreDARK_OAK;
    public static String[][][] arvoreACACIA;

    StructureAPI st = KoM.structureAPI;

    @EventHandler
    public void arvoreCresce(StructureGrowEvent e) {

        e.setCancelled(true);

        switch (e.getSpecies()){
            default:
                Bukkit.getLogger().info("Specie " + e.getSpecies() + ", Não foi encontrada, deixei crescer normalmente...");
                e.setCancelled(false);
                break;
            case TREE:
                st.paste(arvoreTREE, e.getLocation(), false);
                break;
            case BIG_TREE:
                st.paste(arvoreBIG_TREE, e.getLocation(), false);
                break;
            case MEGA_REDWOOD:
                st.paste(arvoreMEGA_REDWOOD, e.getLocation(), false);
                break;
            case REDWOOD:
                st.paste(arvoreREDWOOD, e.getLocation(), false);
                break;
            case BIRCH:
                st.paste(arvoreBIRCH, e.getLocation(), false);
                break;
            case JUNGLE:
                st.paste(arvoreJUNGLE, e.getLocation(), false);
                break;
            case SMALL_JUNGLE:
                st.paste(arvoreSMALL_JUNGLE, e.getLocation(), false);
                break;
            case DARK_OAK:
                st.paste(arvoreDARK_OAK, e.getLocation(),false);
                break;
            case ACACIA:
                st.paste(arvoreACACIA, e.getLocation(), false);
                break;

        }
    }

    public static void pegaArvores() {

        StructureAPI st = KoM.structureAPI;

        arvoreTREE = st.load("ArvoreTREE");
        arvoreBIG_TREE = st.load("ArvoreBIG_TREE");
        arvoreMEGA_REDWOOD = st.load("ArvoreTALL_REDWOOD");
        arvoreREDWOOD = st.load("ArvoreREDWOOD");
        arvoreBIRCH = st.load("ArvoreBIRCH");
        arvoreJUNGLE = st.load("ArvoreJUNGLE");
        arvoreSMALL_JUNGLE = st.load("ArvoreSMALL_JUNGLE");
        arvoreDARK_OAK = st.load("ArvoreDARK_OAK");
        arvoreACACIA = st.load("ArvoreACACIA");
    }

}
