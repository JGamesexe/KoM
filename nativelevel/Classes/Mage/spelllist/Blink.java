/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.Classes.Mage.spelllist;

import nativelevel.Classes.Mage.Elements;
import nativelevel.Classes.Mage.MageSpell;
import nativelevel.Listeners.GeneralListener;
import org.bukkit.entity.Player;

/**
 * @author User
 */
public class Blink extends MageSpell {

    public Blink() {
        super("Lampejo");
    }

    @Override
    public void cast(Player p) {
        GeneralListener.wizard.blink(p);
    }

    @Override
    public double getManaCost() {
        return 8;
    }

    @Override
    public double getExpRatio() {
        return 1;
    }

    @Override
    public int getMinSkill() {
        return 30;
    }

    @Override
    public Elements[] getElements() {
        return new Elements[]{Elements.Terra, Elements.Raio, Elements.Raio};
    }

    @Override
    public int getCooldownInSeconds() {
        return 1;
    }

}
