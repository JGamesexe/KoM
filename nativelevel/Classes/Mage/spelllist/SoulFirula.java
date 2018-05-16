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
public class SoulFirula extends MageSpell {

    public SoulFirula() {
        super("Benção de Jabu");
    }

    @Override
    public void cast(Player p) {
        GeneralListener.wizard.conversaoDeAlma(p, p.getLevel());
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
        return 80;
    }

    @Override
    public Elements[] getElements() {
        return new Elements[]{Elements.Fogo, Elements.Raio, Elements.Terra};
    }

    @Override
    public int getCooldownInSeconds() {
        return 1;
    }

}
