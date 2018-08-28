/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.Equipment;

import org.bukkit.inventory.ItemStack;

/**
 * @author vntgasl
 */
public enum Atributo {

    Chance_Critico(true, 1, 20), //

    Penetr_Armadura(false, 1, 20), //-

    Dano_Critico(true, 1, 25), //

    Chance_Stun(true, 1, 15), //

    Tempo_Stun(false, 1, 40), //

    Chance_Esquiva(true, 1, 15), //

    Vida(true, 1, 15), //

    Armadura(false, 1, 13), //

    Dano_Fisico(true, 1, 20), //

    Regen_Mana(true, 2, 20), //

    Regen_Stamina(true, 2, 20), //

    Mana(false, 5, 50), //

    Stamina(false, 5, 50), //

    Dano_Magico(true, 1, 20),//

    Dano_Distancia(true, 1, 20);

    public boolean pct = false;
    public int min;
    public int max;

    Atributo(boolean pct, int min, int max) {
        this.pct = pct;
        this.min = min;
        this.max = max;
    }

    public String getName() {
        return name().replaceAll("\\_", " ");
    }

    public static boolean hasSense(Atributo attr,ItemStack ss) {


        return false;
    }

}
