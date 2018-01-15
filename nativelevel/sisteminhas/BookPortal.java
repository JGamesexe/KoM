/*

 ╭╮╭━╮╱╱╭━╮╭━╮
 ┃┃┃╭╯╱╱┃┃╰╯┃┃
 ┃╰╯╯╭━━┫╭╮╭╮┃
 ┃╭╮┃┃╭╮┃┃┃┃┃┃
 ┃┃┃╰┫╰╯┃┃┃┃┃┃
 ╰╯╰━┻━━┻╯╰╯╰╯

 Desenvolvedor: ZidenVentania
 Colaboradores: NeT32, Gabripj, Feldmann
 Patrocionio: InstaMC

 */
package nativelevel.sisteminhas;

import nativelevel.utils.BungLocation;
import nativelevel.utils.BookUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BookPortal {

    public static ItemStack criaLivroPortal(Player p) {
        ItemStack livro = new ItemStack(Material.BOOK_AND_QUILL, 1);
        BookUtil.setTitle(livro, "TP");
        BookUtil.setAuthor(livro, "Senhor Dos Portais");
        String[] page = {
            p.getWorld().getName(),
            "" + p.getLocation().getBlockX(),
            "" + p.getLocation().getBlockY(),
            "" + p.getLocation().getBlockZ(),
            "" + p.getLocation().getPitch(),
            "" + p.getLocation().getYaw()

        };
        BookUtil.setPages(livro, Arrays.asList(page));
        return livro;
    }

    public static BungLocation getLocationFromBook(ItemStack livro) {
        BungLocation l = null;
        List<String> pages = BookUtil.getPages(livro);
        if (pages == null) {
            return null;
        }
        try {
            String mundo = pages.get(0);
            double x = Double.valueOf(pages.get(1))+0.5;
            double y = Double.valueOf(pages.get(2));
            double z = Double.valueOf(pages.get(3))+0.5;
            float pitch = Float.valueOf(pages.get(4));
            float yam = Float.valueOf(pages.get(5));
            l = new BungLocation(mundo,x,y,z,pitch,yam);
//            Todos livros tem pitch e yam =V
//            if(pages.size() > 4) {
//                int pitch = Integer.valueOf(pages.get(4));
//                int yaw = Integer.valueOf(pages.get(5));
//                l.pitch = pitch;
//                l.yaw = yaw;
//            }
        } catch (Exception e) {

        }
        return l;

    }

    public static String getTitle(ItemStack livro){
        List<String> pages = BookUtil.getPages(livro);
        if (pages.size() > 6) {
            return ChatColor.translateAlternateColorCodes('&', pages.get(6));
        }
        return "";
    }

    public static String getSubtitle(ItemStack livro){
        List<String> pages = BookUtil.getPages(livro);
        if (pages.size() > 7) {
            return ChatColor.translateAlternateColorCodes('&', pages.get(7));
        }
        return "";
    }

}
