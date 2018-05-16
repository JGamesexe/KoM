/*    */
package nativelevel.anuncios;
/*    */

import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;

/*    */
/*    */
/*    */
/*    */
/*    */

/*    */
/*    */ public class AnnouncerPerm
/*    */ {
    /*  9 */   private static Permission Permissions = null;
    /* 11 */   private boolean permissionsEnabled = false;

    /*    */
/*    */
    public AnnouncerPerm(Announcer plugin) {/*    */
    }

    /*    */
/*    */
    public void enablePermissions() {

/* 24 */
        AnnouncerLog.warning("Permission system not found!");
/*    */
    }

    /*    */
/*    */
    public boolean has(Player player, String line, Boolean op) {
/* 28 */
        if (this.permissionsEnabled) {
/* 29 */
            return Permissions.has(player, line);
/*    */
        }
/* 31 */
        return op.booleanValue();
/*    */
    }

    /*    */
/*    */
    public boolean group(Player player, String group)
/*    */ {
/* 36 */
        if (this.permissionsEnabled) {
/* 37 */
            return Permissions.playerInGroup(player, group);
/*    */
        }
/* 39 */
        return false;
/*    */
    }
/*    */
}