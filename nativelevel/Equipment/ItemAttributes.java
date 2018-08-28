/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.Equipment;

import nativelevel.KoM;
import nativelevel.phatloots.loot.Item;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.lang.model.element.QualifiedNameable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author vntgasl
 */
public class ItemAttributes {

    public static void subAttribute(ItemStack ss, Atributo attribute, int qtd) {
        ItemMeta meta = ss.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<String>();
        }
        // if already have this attribute, we sum
        for (String loreIn : lore) {
            if (loreIn.startsWith("§9+ ") && loreIn.endsWith(attribute.name().replace("_", " "))) {
                int hasAttribute = Integer.valueOf(loreIn.split(" ")[1]);
                hasAttribute -= qtd;
                if (hasAttribute < 0) {
                    hasAttribute = 0;
                }
                lore.remove(loreIn);
                String pct = attribute.pct ? "%" : "";
                lore.add(0, "§9+ " + hasAttribute + pct + " " + attribute.name().replace("_", " "));
                meta.setLore(lore);
                ss.setItemMeta(meta);
                return;
            }
        }
        return;
    }

    public static EquipMeta getAttributes(ItemStack ss) {
        HashMap<Atributo, Double> attrs = new HashMap();
        ItemMeta meta = ss.getItemMeta();
        if (meta == null) {
            return new EquipMeta(attrs);
        }
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<String>();
        }
        // if already have this attribute, we sum
        for (String loreIn : lore) {
            if (loreIn.startsWith("§9+")) {
                String attrName = "";
                //+9 Physical Damage
                for (int x = 1; x < loreIn.split(" ").length; x++) {
                    attrName += loreIn.split(" ")[x];
                    if (x != loreIn.split(" ").length - 1) {
                        attrName += "_";
                    }
                }
                try {
                    Atributo attr = Atributo.valueOf(attrName);
                    double value = Integer.valueOf(loreIn.split("\\+")[1].split(" ")[0].replace("%", ""));

                    attrs.put(attr, value);
                } catch (Exception e) {

                }
            }
        }
        return new EquipMeta(attrs);
    }
    //is: AA0069KI74

    public static int getAttribute(ItemStack ss, Atributo attribute) {
        ItemMeta meta = ss.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<String>();
        }
        // if already have this attribute, we sum
        for (String loreIn : lore) {
            if (loreIn.startsWith("§9+") && loreIn.endsWith(attribute.name())) {
                return Integer.valueOf(loreIn.split("\\+")[1].split(" ")[0].replace("%", ""));
            }
        }
        return 0;
    }

    public static void addAttribute(ItemStack ss, Atributo attribute, int qtd) {
        ItemMeta meta = ss.getItemMeta();
        KoM.debug("addando " + qtd + " " + attribute.name());
        List<String> lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();

        // if already have this attribute, we sum
        int index = 0;
        for (String loreIn : lore) {
            KoM.debug("Ja tinha lore: " + lore);
            if (loreIn.startsWith("§9+") && loreIn.endsWith(attribute.name().replace("_", " "))) {
                int hasAttribute = Integer.valueOf(ChatColor.stripColor(loreIn.split(" ")[0].replace("%", "")));
                hasAttribute += qtd;
                String pct = attribute.pct ? "%" : "";
                lore.set(index, "§9+" + hasAttribute + (pct) + " " + attribute.name().replace("_", " "));
                meta.setLore(lore);
                ss.setItemMeta(meta);
                return;
            }
            index++;
        }
        String pct = attribute.pct ? "%" : "";

        int attrSlot = getLastAtributoSlot(lore);
        if (attrSlot >= 0) {
            lore.add(attrSlot + 1,"§9+" + qtd + pct + " " + attribute.name().replace("_", " "));
        } else {
            lore.add(ItemAttributes.lastSlot(lore), "");
            lore.add(ItemAttributes.lastSlot(lore),"§9+" + qtd + pct + " " + attribute.name().replace("_", " "));
        }

        meta.setLore(lore);
        ss.setItemMeta(meta);
        return;
    }

    public static void addAttribute(ItemStack ss, QuantoAtributo quantoAtributo) {
        addAttribute(ss, quantoAtributo.atributo, quantoAtributo.quanto);
    }

    public static int lastSlot(List<String> lore) {
        if (lore == null || lore.size() == 0) return 0;
        int lastSlot = -666;
        int slot = (lore.size() - 1);
        boolean found = false;
        while (slot > 0) {
            if (lore.get(slot).contains("§0:") || lore.get(slot).contains("§aCriado por ") || lore.get(slot).contains("§9+") || lore.get(slot).contains("§9-") || lore.get(slot).contains("§c-")) {
                found = true;
                lastSlot = (slot - 1);
            }
            slot--;
        }

        if (found) {
            if (lastSlot == -1) return 0;
            else return lastSlot;
        }

        return lore.size();
    }

    private static int getLastAtributoSlot(List<String> lore) {
        int lastSlot = -666;
        if (lore == null) return lastSlot;

        int slot = 0;
        for (String s : lore) {
            if (s.contains("§9+") || s.contains("§9-") || s.contains("§c-")) lastSlot = slot;
            slot++;
        }

        return lastSlot;
    }

    public static class QuantoAtributo {

        private Atributo atributo;
        private int quanto;

        public QuantoAtributo(Atributo atributo, int quanto) {
            this.atributo = atributo;
            this.quanto = quanto;
        }

    }

}
