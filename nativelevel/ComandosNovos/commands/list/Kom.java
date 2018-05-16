/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.ComandosNovos.commands.list;

import nativelevel.ComandosNovos.Comando;
import nativelevel.ComandosNovos.commands.list.KomSubs.*;
import nativelevel.utils.ExecutorType;
import org.bukkit.command.CommandSender;

/**
 * @author vntgasl
 */
public class Kom extends Comando {

    public Kom() {
        super("kom2", ExecutorType.OP);
        subs.add(new CmdTeste());
        subs.add(new CmdHarvest());
        subs.add(new CmdCrafts());
        subs.add(new CmdPlants());
        subs.add(new CmdRecipes());
        subs.add(new CmdPotions());
        subs.add(new CmdCreateBook());
        subs.add(new CmdMercado());
        subs.add(new CmdOE());
        subs.add(new CmdItem());
        subs.add(new CmdLocal());
        subs.add(new CmdCraftings());
    }

    @Override
    public void usouComando(CommandSender cs, String[] args) {
        usouComandoBase(cs, args);
    }
}
