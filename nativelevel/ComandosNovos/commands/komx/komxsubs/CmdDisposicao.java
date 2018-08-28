package nativelevel.ComandosNovos.commands.komx.komxsubs;

import nativelevel.ComandosNovos.SubCmd;
import nativelevel.DataBase.SQL;
import nativelevel.KoM;
import nativelevel.utils.ExecutorType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdDisposicao extends SubCmd {

    public CmdDisposicao() {
        super("disposicao", ExecutorType.PLAYER);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {

        Player player = (Player) cs;

        if (!cs.isOp()) {
            player.sendMessage("Você tem " + KoM.database.getDisposicao(player.getUniqueId()) + " de Disposição");
            return;
        }

        if (args.length >= 3 && (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("see"))) {

            UUID uuid = KoM.database.pegaUUID(args[2]);

            if (uuid == null) {
                cs.sendMessage("Player não encontrado");
                return;
            }

            if (args[1].equalsIgnoreCase("add")) {
                if (args.length > 3 && args[3].matches("^[0-9-]*$")) {
                    int quanto = Integer.valueOf(args[3]);
                    KoM.database.addDisposicao(uuid, quanto, false);
                    cs.sendMessage(args[2] + " agora tem " + KoM.database.getDisposicao(uuid) + " de Disposição (" + quanto + ")");
                } else {
                    cs.sendMessage("Terceiro argumento não é um numero valido");
                }
            } else if (args[1].equalsIgnoreCase("see")) {
                cs.sendMessage(args[2] + " tem " + KoM.database.getDisposicao(uuid) + " de Disposição");
            }

        } else {
            cs.sendMessage("§c/kom disposicao {add, see} {player} {quanto}");
        }

    }
}
