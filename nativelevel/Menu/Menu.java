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
package nativelevel.Menu;

import nativelevel.Lang.L;
import org.bukkit.Material;

public class Menu {

    public static String getToolTip(int n) {
        if (n == 0) {
            return L.m("Minerar, Disarmar, Arremecar");
        }
        if (n == 1) {
            return L.m("Forjar Equips, Imune a Fogo");
        }
        if (n == 2) {
            return L.m("Cortar lenha, alto dano, construir torres");
        }
        if (n == 3) {
            return L.m("Lobos, Animais, Couro, Plantas");
        }
        if (n == 4) {
            return L.m("Espada, Tanker, Loots");
        }
        if (n == 5) {
            return L.m("Pocoes, Tnt, Chant Table");
        }
        if (n == 6) {
            return L.m("Chants, Clones, Magias");
        }
        if (n == 7) {
            return L.m("Arco, Adaga, Invisivel, Backstab");
        }
        if (n == 8) {
            return L.m("Redstone, Armadilhas, Armas de Fogo");
        }
        if (n == 9) {
            return "To-DO";
        }
        return null;
    }

    public static String getNome(int n) {
        if (n == 0) {
            return "Minerador";
        }
        if (n == 1) {
            return "Ferreiro";
        }
        if (n == 2) {
            return "Lenhador";
        }
        if (n == 3) {
            return "Fazendeiro";
        }
        if (n == 4) {
            return "Paladino";
        }
        if (n == 5) {
            return L.get("Classes.Alchemist");
        }
        if (n == 6) {
            return "Mago";
        }
        if (n == 7) {
            return "Ladino";
        }
        if (n == 8) {
            return "Engenheiro";
        }
        if (n == 9) {
            return "Druida";
        }
        return null;
    }

    public static String getSimbolo(String nome) {
        int id = Menu.getId(nome);
        return getSimbolo(id);
    }

    public static String getSimbolo(int n) {
        if (n == 0) {
            return "♢"; // minerador
        }
        if (n == 1) {
            return "☭"; // ferreiro
        }
        if (n == 2) {
            return "♧"; // lenhador
        }
        if (n == 3) {
            return "☮"; // fazendeiro
        }
        if (n == 4) {
            return "☥"; // paladino
        }
        if (n == 5) {
            return "✡"; // alquimista
        }
        if (n == 6) {
            return "☯"; // mago
        }
        if (n == 7) {
            return "➹"; // ladino
        }
        if (n == 8) {
            return "⌘"; // engenheiro
        }
        if (n == 9) {
            return "D";
        }
        return null;
    }

    public static int getSkillsComNivel(int[] skills, int n) {
        int tem = 0;
        for (int skill : skills) {
            if (n == skill) {
                tem++;
            }
        }
        return tem;
    }

    public static void avancaClasse(int[] classes, int id) {
        int primarias = getSkillsComNivel(classes, 2);
        int secundarias = getSkillsComNivel(classes, 1);

        int proxLevel = classes[id];
        proxLevel++;
        if ((proxLevel == 1) && (secundarias >= 2)) {
            proxLevel++;
        }
        if ((proxLevel == 2) && (primarias >= 2)) {
            proxLevel++;
        }
        if (proxLevel > 2) {
            proxLevel = 0;
        }
        classes[id] = proxLevel;
    }

    public static int getId(String nome) {
        if (nome.equals("Minerador")) return 0;
        else if (nome.equals("Ferreiro")) return 1;
        else if (nome.equals("Lenhador")) return 2;
        else if (nome.equals("Fazendeiro")) return 3;
        else if (nome.equals("Paladino")) return 4;
        else if (nome.equals(L.get("Classes.Alchemist"))) return 5;
        else if (nome.equals("Mago")) return 6;
        else if (nome.equals("Ladino")) return 7;
        else if (nome.equals("Engenheiro")) return 8;
        else if (nome.equals("Druida")) return 9;
        else return -1;
    }

    public static Material getDesenho(int n) {
        if (n == 0) return Material.DIAMOND_PICKAXE;
        else if (n == 1) return Material.ANVIL;
        else if (n == 2) return Material.GOLD_AXE;
        else if (n == 3) return Material.MONSTER_EGG;
        else if (n == 4) return Material.IRON_SWORD;
        else if (n == 5) return Material.POTION;
        else if (n == 6) return Material.BOOK;
        else if (n == 7) return Material.BOW;
        else if (n == 8) return Material.REDSTONE;
        else if (n == 9) return Material.DEAD_BUSH;
        else return Material.BARRIER;
    }
}
