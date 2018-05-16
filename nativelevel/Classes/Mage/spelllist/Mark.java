/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.Classes.Mage.spelllist;

import nativelevel.Classes.Mage.Elements;
import nativelevel.Classes.Mage.MageSpell;
import nativelevel.Classes.Mage.Wizard;
import org.bukkit.entity.Player;

/**
 * @author User
 */
public class Mark extends MageSpell {

    public Mark() {
        super("Marcar Runa");
    }

    @Override
    public void cast(Player p) {
        Wizard.markRecall(p);
    }

    @Override
    public double getManaCost() {
        return 75;
    }

    @Override
    public double getExpRatio() {
        return 1;
    }

    @Override
    public int getMinSkill() {
        return 95;
    }

    @Override
    public Elements[] getElements() {
        return new Elements[]{Elements.Terra, Elements.Terra, Elements.Terra};
    }

    @Override
    public int getCooldownInSeconds() {
        return 1;
    }

}
