/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nativelevel.precocabeca;

/**
 * @author lko
 */
public class Principal {


    public static Recompensas recom = new Recompensas();

    public Principal() {
        onEnable();
    }

    public void onEnable() {
        // Bukkit.getPluginCommand("recompensa").setExecutor(new CmdRecompensa());
        //Bukkit.getPluginManager().registerEvents(new Events(), NativeLevel.instanciaDoPlugin);
    }

}
