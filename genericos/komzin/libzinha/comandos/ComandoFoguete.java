package genericos.komzin.libzinha.comandos;

import genericos.komzin.libzinha.InstaMCLibKom;
import genericos.komzin.libzinha.PlayerInfo;
import nativelevel.MetaShit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

public class ComandoFoguete implements CommandExecutor {
    
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage("somente players pode usar este comando");
            return false;
        }
        if (args.length != 0) {
            cs.sendMessage(ChatColor.RED + "Utilize /foguete somente");
            return false;
        }
        if (!cs.hasPermission("manialib.foguete")) {
            cs.sendMessage(ChatColor.RED + "Voce precisa ser §6vip para utilizar o §f/foguete");
            return false;
        }
        Player p = (Player) cs;
        PlayerInfo info = InstaMCLibKom.getinfo(p);
        if (System.currentTimeMillis() < info.TempoFoguete) {
            cs.sendMessage("§cAguarde alguns segundos para utilizar o comando novamente!");
            return true;
        }
        SoltaFoguete(p);
        p.sendMessage("§cSoltou um foguete =D");
        if (!p.isOp()) {
            info.TempoFoguete = (System.currentTimeMillis() + 15000L);
        }
        return true;
    }

        private void SoltaFoguete(Player player) {
        Firework fw = (Firework) player.getWorld().spawn(player.getLocation(), Firework.class);
        FireworkMeta fm = fw.getFireworkMeta();
        int fType = (int) (Math.random() * 6.0D);
        FireworkEffect.Type type = null;
        Random r = new Random();
        switch (fType) {
            case 1:
            default:
                type = FireworkEffect.Type.BALL;
                break;
            case 2:
                type = FireworkEffect.Type.BALL_LARGE;
                break;
            case 3:
                type = FireworkEffect.Type.BURST;
                break;
            case 4:
                type = FireworkEffect.Type.CREEPER;
                break;
            case 5:
                type = FireworkEffect.Type.STAR;
        }
        int c1i = (int) (Math.random() * 17.0D);
        int c2i = (int) (Math.random() * 17.0D);
        Color c1 = getColour(c1i);
        Color c2 = getColour(c2i);
        FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
        fm.addEffect(effect);
        int power = (int) (Math.random() * 3.0D);
        fm.setPower(power);
        fw.setFireworkMeta(fm);
        MetaShit.setMetaObject("visual", fw, true);
        fw.setCustomName("Foguete VIP");
    }

        public Color getColour(int c) {
        switch (c) {
            case 1:
            default:
                return Color.AQUA;
            case 2:
                return Color.BLACK;
            case 3:
                return Color.BLUE;
            case 4:
                return Color.FUCHSIA;
            case 5:
                return Color.GRAY;
            case 6:
                return Color.GREEN;
            case 7:
                return Color.LIME;
            case 8:
                return Color.MAROON;
            case 9:
                return Color.NAVY;
            case 10:
                return Color.OLIVE;
            case 11:
                return Color.PURPLE;
            case 12:
                return Color.RED;
            case 13:
                return Color.SILVER;
            case 14:
                return Color.TEAL;
            case 15:
                return Color.WHITE;
        }
    }
}

/* Location:              C:\Users\User\Desktop\REPO\InstaMCLibKom.jar!\instamc\coders\libkom\comandos\ComandoFoguete.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */