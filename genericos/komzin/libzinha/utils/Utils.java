/*    */
package genericos.komzin.libzinha.utils;
/*    */ 
/*    */

import nativelevel.KoM;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */

/*    */
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Utils
/*    */ {
    /*    */
    public static void TeleportarTPBG(String server, Player sender)
/*    */ {
/* 24 */
        Bukkit.getMessenger().registerOutgoingPluginChannel(KoM._instance, "BungeeCord");
/* 25 */
        ByteArrayOutputStream b = new ByteArrayOutputStream();
/* 26 */
        DataOutputStream out = new DataOutputStream(b);
/*    */
        try {
/* 28 */
            out.writeUTF("Connect");
/* 29 */
            out.writeUTF(server);
/*    */
        }
/*    */ catch (IOException localIOException) {
        }
/* 32 */
        sender.sendPluginMessage(KoM._instance, "BungeeCord", b.toByteArray());
/*    */
    }
/*    */
}
