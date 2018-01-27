package nativelevel.ComandosNovos.komx;

import nativelevel.ComandosNovos.Comando;
import nativelevel.ComandosNovos.SubCmd;
import nativelevel.ComandosNovos.komx.komxsubs.CmdTeste;
import nativelevel.ComandosNovos.komx.komxsubs.CmdTper;
import org.bukkit.command.CommandSender;

public class KoMx extends Comando {

    public KoMx() {
        super("komx", CommandType.TODOS);
        subs.add(new CmdTper());
        subs.add(new CmdTeste());
    }

    @Override
    public void usouComando(CommandSender cs, String[] args) {
        if (args.length >= 1) {
            for (SubCmd args0 : subs) {
                if (args0.cmd.equalsIgnoreCase(args[0])) {
                    args0.execute(cs, args);
                }
            }
        } else {
            showSubCommands(cs);
        }
    }
}
