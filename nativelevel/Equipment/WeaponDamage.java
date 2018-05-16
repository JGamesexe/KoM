package nativelevel.Equipment;

import me.dpohvar.powernbt.api.NBTCompound;
import me.dpohvar.powernbt.api.NBTList;
import nativelevel.Attributes.Armors;
import nativelevel.KoM;
import nativelevel.utils.itemattributes.ItemAttrAPI;
import nativelevel.utils.itemattributes.Slot;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

/**
 * @author Ziden
 */
public class WeaponDamage {

    public static double getDamage(ItemStack ss) {
        if (ss == null) {
            return 0;
        }

        if (ss.getType().name().contains("SWORD") || ss.getType().name().contains("AXE") || ss.getType().name().contains("SPADE") || ss.getType().name().contains("HOE")) {
            if (ss.getType().name().contains("WOOD")) {
                return 1.5;
            } else if (ss.getType().name().contains("STONE")) {
                return 2.5;
            } else if (ss.getType().name().contains("IRON")) {
                return 3.5;
            } else if (ss.getType().name().contains("GOLD")) {
                return 3.7;
            } else if (ss.getType().name().contains("DIAMOND")) {
                return 5;
            }
        }
        return 0;
    }

    public static ItemStack setDano(ItemStack ss, double dano) {
        nativelevel.utils.itemattributes.ItemAttrAPI attributeModifiers = new ItemAttrAPI();
        KoM.debug("Adicionando dano");
        ItemMeta meta = ss.getItemMeta();
        short durab = ss.getDurability();
        ss = new ItemStack(ss.getType());
        ss = NBTAttrs.removeAttr(ss);
        ss.setDurability(durab);
        nativelevel.utils.itemattributes.AttributeModifier speedModifier = new nativelevel.utils.itemattributes.AttributeModifier(nativelevel.utils.itemattributes.Attribute.ATTACK_DAMAGE, "Dano", Slot.MAIN_HAND, 0, dano, UUID.randomUUID());
        attributeModifiers.addModifier(speedModifier);
        ss.setItemMeta(meta);
        ss = attributeModifiers.apply(ss);
        return ss;
    }

    public static double getItemDamage(ItemStack itemStack) {
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            NBTCompound item = KoM.nbtManager.read(itemStack);
            if (item != null) {
                NBTCompound tag = item.getCompound("tag");
                if (tag != null) {
                    NBTList attrMod = tag.getList("AttributeModifiers");
                    if (attrMod != null || !attrMod.isEmpty()) {
                        for (Object anAttrMod : attrMod) {
                            Bukkit.broadcastMessage(anAttrMod.toString());
                        }
                    }
                }
            }
        }
        return 0;
    }

    public static ItemStack checkForMods(ItemStack ss) {
        if (ss == null) {
            return ss;
        }

        net.minecraft.server.v1_12_R1.ItemStack itemNMS = CraftItemStack.asNMSCopy(ss);
        NBTTagCompound tag = itemNMS.getTag();

        if (tag != null) {
            NBTTagList lista = tag.getList("AttributeModifiers", 10);
            if (lista != null) {
                return ss;
            }
        }

        if (ss.getType() == Material.SHIELD) {
            //ss = NBTAttrs.removeAttr(ss);
            ItemAttributes.addAttribute(ss, Atributo.Armadura, 4);
            return ss;
        }

        double dano = getDamage(ss);
        KoM.debug("tinha dano " + dano);
        if (dano != 0) {
            ss = NBTAttrs.removeAttr(ss);
            return setDano(ss, dano);
        } else {
            int armor = Armors.getBaseArmor(ss);
            if (armor != 0) {
                ss = NBTAttrs.removeAttr(ss);
                int jaTemArmor = ItemAttributes.getAttribute(ss, Atributo.Armadura);
                if (jaTemArmor < armor) {
                    ItemAttributes.addAttribute(ss, Atributo.Armadura, armor);
                }
            }
        }

        return ss;
    }

}
