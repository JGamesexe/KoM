package nativelevel.Equipment;

import nativelevel.KoM;
import nativelevel.Lang.LangMinecraft;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ziden
 */

public class ItemEquipavel {

    public EquipMeta meta;
    public String displayName;
    public Material mat;
    public List<String> lore;

    public ItemEquipavel(ItemStack ss) {
        if (ss == null || ss.getType() == Material.AIR) {
            meta = new EquipMeta();
            return;
        }
        KoM.debug("Criando item equipavel de " + ss.getType().name());
        ItemMeta meta = ss.getItemMeta();
        if (meta.getDisplayName() == null) {
            displayName = LangMinecraft.get().get(ss);
        } else {
            displayName = meta.getDisplayName();
        }
        if (meta.getLore() != null)
            lore = meta.getLore();
        else
            lore = new ArrayList<String>();
        this.meta = ItemAttributes.getAttributes(ss);
        this.mat = ss.getType();
    }

}
