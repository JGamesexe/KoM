//package nativelevel.ComandosNovos.commands;
//
//import genericos.komzin.libzinha.InstaMCLibKom;
//import genericos.komzin.libzinha.PlayerInfo;
//import nativelevel.ComandosNovos.Comando;
//import nativelevel.KoM;
//import nativelevel.utils.ExecutorType;
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.command.CommandSender;
//import org.bukkit.command.ConsoleCommandSender;
//import org.bukkit.entity.Player;
//
//public class ChatBB extends Comando {
//
//    public ChatBB() {
//        super("chatbb", ExecutorType.PERMISSION);
//        this.permission = "kom.staff";
//    }
//
//    @Override
//    public void usouComando(CommandSender cs, String[] args) {
//        if (args.length == 0) {
//            if (cs instanceof Player) {
//                Player p = (Player) cs;
//                PlayerInfo meta = InstaMCLibKom.getinfo(p);
//                if (meta.inChannel == null || !meta.inChannel.equalsIgnoreCase("staff")) {
//                    meta.inChannel = "staff";
//                    p.sendMessage(ChatColor.GOLD + "Voce esta falando dos bigboss (privado), pegue um whisky e sirva-se de um charuto");
//                } else {
//                    meta.inChannel = null;
//                    p.sendMessage(ChatColor.GOLD + "Voce saiu do chat bigboss");
//                }
//            } else if (cs instanceof ConsoleCommandSender) {
//                cs.sendMessage("Faz favor ai e usa /chatbb [MENSAGEM AQUI =D]");
//            }
//        } else {
//            if (cs instanceof Player) {
//                StringBuilder message = new StringBuilder();
//                for (String arg : args) message.append(arg).append(" ");
//
//                sendMessage(ChatListener.getFormatedName((Player) cs), message.toString());
//            } else if (cs instanceof ConsoleCommandSender) {
//                StringBuilder message = new StringBuilder();
//                for (String arg : args) message.append(arg).append(" ");
//
//                sendMessage(ChatListener.consoleName, message.toString());
//            }
//        }
//    }
//
//    public static void sendMessage(String howSend, String message) {
//        message = ("§9[BigBoss] " + howSend + "§7: §e" + message);
//        KoM.console.sendMessage(message);
//        for (Player px : Bukkit.getOnlinePlayers()) {
//            if (px.hasPermission("kom.staff")) {
//                px.sendMessage(message);
//            }
//        }
//    }
//
//}
