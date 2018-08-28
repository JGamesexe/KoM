/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.gemas;

import nativelevel.Equipment.Atributo;
import nativelevel.Equipment.ItemAttributes;
import nativelevel.Jobs;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Gema {

    // Amarela(4, "§E", new String[]{"+1 Regeneracao"}, new String[]{"+2 Regeneracao"}),

    Branca(0, "§f",
            new ItemAttributes.QuantoAtributo[]{new ItemAttributes.QuantoAtributo(Atributo.Vida, 1)},
            new ItemAttributes.QuantoAtributo[]{new ItemAttributes.QuantoAtributo(Atributo.Vida, 2)}),
    Laranja(1, "§6",
            new ItemAttributes.QuantoAtributo[]{new ItemAttributes.QuantoAtributo(Atributo.Chance_Critico, 3), new ItemAttributes.QuantoAtributo(Atributo.Dano_Critico, 1)},
            new ItemAttributes.QuantoAtributo[]{new ItemAttributes.QuantoAtributo(Atributo.Chance_Critico, 7), new ItemAttributes.QuantoAtributo(Atributo.Dano_Critico, 5)}),
    Magenta(2, "§d",
            new ItemAttributes.QuantoAtributo[]{new ItemAttributes.QuantoAtributo(Atributo.Dano_Magico, 2)},
            new ItemAttributes.QuantoAtributo[]{new ItemAttributes.QuantoAtributo(Atributo.Dano_Magico, 8)}),
    Aqua(3, "§b",
            new ItemAttributes.QuantoAtributo[]{new ItemAttributes.QuantoAtributo(Atributo.Dano_Distancia, 3)},
            new ItemAttributes.QuantoAtributo[]{new ItemAttributes.QuantoAtributo(Atributo.Dano_Distancia, 8)}),
    Cinza(8, "§8",
            new ItemAttributes.QuantoAtributo[]{new ItemAttributes.QuantoAtributo(Atributo.Penetr_Armadura, 1)},
            new ItemAttributes.QuantoAtributo[]{new ItemAttributes.QuantoAtributo(Atributo.Penetr_Armadura, 4)}),
    Ciana(9, "§3",
            new ItemAttributes.QuantoAtributo[]{new ItemAttributes.QuantoAtributo(Atributo.Armadura, 1)},
            new ItemAttributes.QuantoAtributo[]{new ItemAttributes.QuantoAtributo(Atributo.Armadura, 4)}),
    Azul(11, "§1",
            new ItemAttributes.QuantoAtributo[]{new ItemAttributes.QuantoAtributo(Atributo.Tempo_Stun, 1)},
            new ItemAttributes.QuantoAtributo[]{new ItemAttributes.QuantoAtributo(Atributo.Tempo_Stun, 4)}),
    Vermelha(14, "§c",
            new ItemAttributes.QuantoAtributo[]{new ItemAttributes.QuantoAtributo(Atributo.Dano_Fisico, 2)},
            new ItemAttributes.QuantoAtributo[]{new ItemAttributes.QuantoAtributo(Atributo.Dano_Fisico, 8)});


    private int loreIndex = 1;

    public static String iconeSlot = "[+]"; // só aparece na resource pack
    public int id;
    public String cor;
    public ItemAttributes.QuantoAtributo[] buffNormal;
    public ItemAttributes.QuantoAtributo[] buffRaro;

    Gema(int idCor, String cor, ItemAttributes.QuantoAtributo[] buffNormal, ItemAttributes.QuantoAtributo[] buffRaro) {
        this.id = idCor;
        this.buffNormal = buffNormal;
        this.buffRaro = buffRaro;
        this.cor = cor;
    }

    public static Gema getGemaRandom(ItemStack ss) {
        return Gema.values()[Jobs.rnd.nextInt(Gema.values().length)];
    }

    public static Gema getGema(ItemStack ss) {
        if (ss.getType() != Material.STAINED_GLASS) {
            return null;
        }
        ItemMeta meta = ss.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null || lore.size() == 0) {
            return null;
        }
        if (!lore.get(0).contains("Gema")) {
            return null;
        }
        for (Gema g : Gema.values()) {
            if ((byte) g.id == ss.getData().getData()) {
                return g;
            }
        }
        return null;
    }

    public static ItemStack gera(Gema gem, Raridade r) {
        ItemStack gema = new ItemStack(Material.STAINED_GLASS, 1, (short) 0, (byte) gem.id);
        int qtd = 1;
        ItemMeta meta = gema.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<String>();
        lore.add("§9§lIsto e uma Gema " + gem.cor + "§l " + gem.name());
        lore.add(" ");
        lore.add("§9§lColoque isto em um item");
        lore.add("§9§lQue tenha um encaixe " + gem.cor + "§l " + gem.name());
        lore.add("  ");
        lore.add("§6Raridade: " + r.cor + "" + r.name());
        meta.setLore(lore);
        String raridade = ChatColor.BLUE + "♦ ";
        if (r == Raridade.Epico) {
            raridade = ChatColor.LIGHT_PURPLE + "♦ ";
        }
        meta.setDisplayName(raridade + gem.cor + "§l Gema " + gem.name());
        gema.setItemMeta(meta);
        return gema;
    }

    public static Raridade getRaridade(ItemStack gema) {
        ItemMeta meta = gema.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore != null) {
            for (String s : lore) {
                if (s.contains("§6Raridade")) {
                    return Raridade.valueOf(ChatColor.stripColor(s.split(":")[1]).trim());
                }
            }
        }
        return Raridade.Comum;
    }

    public static void addGemaToItem(ItemStack ss, Gema g, ItemStack gemaItem) {
        ItemMeta meta = ss.getItemMeta();
        List<String> lore = meta.getLore();

        Raridade rar = getRaridade(gemaItem);
        String novaLore = rar.cor + "[*] " + g.cor + "Gema " + g.name();
        lore.set(g.loreIndex, novaLore);
        meta.setLore(lore);
        ss.setItemMeta(meta);

        ItemAttributes.QuantoAtributo[] attrs;
        if (rar == Raridade.Epico) attrs = g.buffRaro;
        else attrs = g.buffNormal;

        for (ItemAttributes.QuantoAtributo attr : attrs) ItemAttributes.addAttribute(ss, attr);

    }

    public static int quantosSockets(ItemStack ss) {
        int quantosSockets = 0;
        ItemMeta meta = ss.getItemMeta();
        if (meta == null) return quantosSockets;

        List<String> lore = meta.getLore();
        if (lore == null) return quantosSockets;

        for (String s : lore) {
            s = ChatColor.stripColor(s);
            if (s.contains(iconeSlot) || s.contains("[*]")) quantosSockets++;
        }

        return quantosSockets;
    }

    public static boolean temSocketUsado(ItemStack ss) {
        ItemMeta meta = ss.getItemMeta();
        if (meta == null) return false;

        List<String> lore = meta.getLore();
        if (lore == null) return false;

        for (String s : lore) {
            if (s.contains("[*]"))
                return true;
        }

        return false;
    }

    public static int getLastSocketsSlot(ItemStack ss) {
        int lastSlot = -666;
        ItemMeta meta = ss.getItemMeta();
        if (meta == null) return lastSlot;

        List<String> lore = meta.getLore();
        if (lore == null) return lastSlot;

        int slot = 0;
        for (String s : lore) {
            if (s.contains(iconeSlot) || s.contains("[*]")) lastSlot = slot;
            slot++;
        }

        return lastSlot;
    }

    public static List<Gema> getSocketsLivres(ItemStack ss) {
        List<Gema> sockets = new ArrayList<Gema>();
        ItemMeta meta = ss.getItemMeta();
        if (meta == null) {
            return sockets;
        }
        List<String> lore = meta.getLore();
        int index = 0;
        if (lore != null)
            for (String s : lore) {
                if (s.contains(iconeSlot)) {
                    String[] split = s.split(" ");
                    String ultimo = split[split.length - 1];
                    Gema cor = Gema.valueOf(ultimo);
                    cor.loreIndex = index;
                    sockets.add(cor);
                }
                index++;
            }

        return sockets;
    }

    public static void addSocket(ItemStack item, Gema g) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();

        Gema gema;

        int slotSocket = getLastSocketsSlot(item);
        if (slotSocket >= 0) {
            lore.add(slotSocket + 1, g.cor + iconeSlot + " Encaixe " + g.name());
        } else {
            lore.add(ItemAttributes.lastSlot(lore), "");
            lore.add(ItemAttributes.lastSlot(lore), g.cor + iconeSlot + " Encaixe " + g.name());
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

}
