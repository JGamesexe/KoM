/*

 ╭╮╭━╮╱╱╭━╮╭━╮
 ┃┃┃╭╯╱╱┃┃╰╯┃┃
 ┃╰╯╯╭━━┫╭╮╭╮┃
 ┃╭╮┃┃╭╮┃┃┃┃┃┃
 ┃┃┃╰┫╰╯┃┃┃┃┃┃
 ╰╯╰━┻━━┻╯╰╯╰╯

 Desenvolvedor: ZidenVentania
 Colaboradores: NeT32, Gabripj, Feldmann, JGamesexe
 Patrocionio: InstaMC > PixelMC

 */
package nativelevel.sisteminhas;

import nativelevel.KoM;
import nativelevel.integration.BungeeCordKom;
import nativelevel.utils.BookUtil;
import nativelevel.utils.BungLocation;
import nativelevel.utils.Fireworks;
import nativelevel.utils.JotaGUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookPortal {

    public static void teleport(Chest chest, Player player) {
        ItemStack livro = chest.getBlockInventory().getItem(0);
        if (livro != null && livro.getType().equals(Material.BOOK_AND_QUILL)) {
            BookMeta m = (BookMeta) livro.getItemMeta();
            if (m.getTitle() != null && m.getTitle().equalsIgnoreCase("TP")) {
                BungLocation l = BookPortal.getLocationFromBook(livro);

                ItemStack podeTp = chest.getBlockInventory().getItem(8);

                if (podeTp != null && !podeTp.getType().equals(Material.AIR) && !podeTeleporta(player, podeTp)) return;

                if (m.getPageCount() == 6) BungeeCordKom.tp(player, l);
                else BungeeCordKom.tp(player, l, BookPortal.getTitle(livro), BookPortal.getSubtitle(livro));

                ItemStack setaSpawn = chest.getBlockInventory().getItem(4);

                if (setaSpawn != null && setaSpawn.getType().equals(Material.WOOL) && setaSpawn.getDurability() == 14) ;
                else if (l.mundo.equalsIgnoreCase("NewDungeon"))
                    player.setBedSpawnLocation(BungLocation.toLocation(l), true);

                player.sendMessage(ChatColor.LIGHT_PURPLE + "* poof *");
                Fireworks.doFirework(
                        player.getLocation().add(0, -0.6, 0),
                        FireworkEffect.Type.BURST,
                        Color.PURPLE,
                        Color.AQUA, 2);
            }
        }
    }

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

    public static ItemStack criaLivroPortal(Player p, String title) {

        ItemStack livro = criaLivroPortal(p);
        List<String> pages = new ArrayList<>(BookUtil.getPages(livro));

        pages.add("" + title.replaceAll("_", " "));

        BookUtil.setPages(livro, pages);

        return livro;
    }

    public static ItemStack criaLivroPortal(Player p, String title, String subtitle) {

        ItemStack livro = criaLivroPortal(p, title);
        List<String> pages = new ArrayList<>(BookUtil.getPages(livro));

        pages.add("" + subtitle.replaceAll("_", " "));

        BookUtil.setPages(livro, pages);

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
            double x = Double.valueOf(pages.get(1)) + 0.5;
            double y = Double.valueOf(pages.get(2));
            double z = Double.valueOf(pages.get(3)) + 0.5;
            float pitch = Float.valueOf(pages.get(4));
            float yam = Float.valueOf(pages.get(5));
            l = new BungLocation(mundo, x, y, z, pitch, yam);
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

    public static String getTitle(ItemStack livro) {
        List<String> pages = BookUtil.getPages(livro);
        if (pages.size() > 6) {
            return ChatColor.translateAlternateColorCodes('&', pages.get(6));
        }
        return "";
    }

    public static String getSubtitle(ItemStack livro) {
        List<String> pages = BookUtil.getPages(livro);
        if (pages.size() > 7) {
            return ChatColor.translateAlternateColorCodes('&', pages.get(7));
        }
        return "";
    }

    private static boolean podeTeleporta(Player player, ItemStack itemStack) {
        if (itemStack.getType().equals(Material.WRITTEN_BOOK)) {
            BookMeta bm = (BookMeta) itemStack.getItemMeta();
            if (bm.getTitle().equalsIgnoreCase("hasQuest*")) {
                if (!KoM.quests.getQuester(player.getUniqueId()).completedQuests.contains(bm.getPage(1)) && !JotaGUtils.onQuest(player.getUniqueId(), bm.getPage(1))) {
                    if (bm.getPageCount() >= 2)
                        player.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', bm.getPage(2)));
                    else
                        player.sendMessage("§cVocê precisar estar fazendo ou ter concluido a quest: §n" + bm.getPage(1));
                    return false;
                }
            } else if (bm.getTitle().equalsIgnoreCase("onQuest")) {
                if (!JotaGUtils.onQuest(player.getUniqueId(), bm.getPage(1))) {
                    if (bm.getPageCount() >= 2)
                        player.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', bm.getPage(2)));
                    else player.sendMessage("§cVocê precisar estar fazendo a quest: §n" + bm.getPage(1));
                    return false;
                }
            } else if (bm.getTitle().equalsIgnoreCase("onQuestStage")) {
                if (!JotaGUtils.onQuestStage(player.getUniqueId(), bm.getPage(1), Integer.valueOf(bm.getPage(2)))) {
                    if (bm.getPageCount() >= 3)
                        player.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', bm.getPage(3)));
                    else
                        player.sendMessage("§cVocê precisar estar em um momento especifico da quest: §n" + bm.getPage(1));
                    return false;
                }
            } else if (bm.getTitle().equalsIgnoreCase("btwQuestStages")) {
                if (!JotaGUtils.betweenQuestStages(player.getUniqueId(), bm.getPage(1), Integer.valueOf(bm.getPage(2)), Integer.valueOf(bm.getPage(3)))) {
                    if (bm.getPageCount() >= 4)
                        player.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', bm.getPage(4)));
                    else
                        player.sendMessage("§cVocê precisar estar em um momento especifico da quest: §n" + bm.getPage(1));
                    return false;
                }
            } else if (bm.getTitle().equalsIgnoreCase("completedQuest")) {
                if (!KoM.quests.getQuester(player.getUniqueId()).completedQuests.contains(bm.getPage(1))) {
                    if (bm.getPageCount() >= 2)
                        player.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', bm.getPage(2)));
                    else player.sendMessage("§cVocê precisar ter concluido a quest: §n" + bm.getPage(1));
                    return false;
                }
            }
        } else {
            boolean temItemChave = false;
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null && !item.getType().equals(Material.AIR)) {
                    temItemChave = itemStack.getType().equals(item.getType());
                    if (temItemChave && itemStack.getItemMeta() != null)
                        temItemChave = itemStack.getItemMeta().equals(item.getItemMeta());
                    if (temItemChave) break;
                }
            }
            if (!temItemChave) {
                player.sendMessage("§cEsse teleporte aparenta precisar de algo mais para funcionar...");
                return false;
            }
        }
        return true;
    }

}
