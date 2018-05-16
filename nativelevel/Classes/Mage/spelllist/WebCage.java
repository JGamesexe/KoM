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
public class WebCage extends MageSpell {

    public WebCage() {
        super("Prisão de Teias");
    }

    @Override
    public void cast(Player p) {
        GeneralListener.wizard.prende(p);
    }

    @Override
    public double getManaCost() {
        return 20;
    }

    @Override
    public double getExpRatio() {
        return 1;
    }

    @Override
    public int getMinSkill() {
        return 40;
    }

    @Override
    public Elements[] getElements() {
        return new Elements[]{Elements.Terra, Elements.Terra, Elements.Raio};
    }

    @Override
    public int getCooldownInSeconds() {
        return 1;
    }

}
