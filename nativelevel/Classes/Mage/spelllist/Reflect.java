package nativelevel.Classes.Mage.spelllist;

import nativelevel.Classes.Mage.Elements;
import nativelevel.Classes.Mage.MageSpell;
import nativelevel.Equipment.Atributo;
import nativelevel.Equipment.EquipManager;
import nativelevel.KoM;
import nativelevel.Lang.L;
import nativelevel.MetaShit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author User
 */
public class Reflect extends MageSpell {

    public Reflect() {
        super("Escudo Reflexor");
    }

    @Override
    public void cast(final Player p) {
        double magia = EquipManager.getPlayerAttribute(Atributo.Dano_Magico, p);
        int segundos = (int) Math.round(5 + (magia / 100));
        p.sendMessage(ChatColor.GREEN + L.m("Voce se protegeu com um escudo reflexor por " + segundos + " segundos"));
        MetaShit.setMetaString("shield", p, "1");
        Runnable r = new Runnable() {

            public void run() {
                if (p.hasMetadata("shield")) {
                    p.sendMessage(ChatColor.RED + L.m("Seu escudo se dissipou"));
                    p.removeMetadata("shield", KoM._instance);
                }
            }
        };
        Bukkit.getScheduler().scheduleSyncDelayedTask(KoM._instance, r, 20 * segundos);
    }

    @Override
    public double getManaCost() {
        return 30;
    }

    @Override
    public double getExpRatio() {
        return 1;
    }

    @Override
    public int getMinSkill() {
        return 65;
    }

    @Override
    public Elements[] getElements() {
        return new Elements[]{Elements.Fogo, Elements.Fogo, Elements.Terra};
    }

    @Override
    public int getCooldownInSeconds() {
        return 6;
    }

}
