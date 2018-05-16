package nativelevel.ComandosNovos.commands.komx;

import nativelevel.ComandosNovos.Comando;
import nativelevel.ComandosNovos.commands.komx.komxsubs.CmdAutoClaim;
import nativelevel.ComandosNovos.commands.komx.komxsubs.CmdTeste;
import nativelevel.ComandosNovos.commands.komx.komxsubs.CmdTper;
import nativelevel.utils.ExecutorType;
import org.bukkit.command.CommandSender;

public class KoMx extends Comando {

    public KoMx() {
        super("kom", ExecutorType.TODOS);
        subs.add(new CmdTper());
        subs.add(new CmdTeste());
        subs.add(new CmdAutoClaim());
    }

    @Override
    public void usouComando(CommandSender cs, String[] args) {
        usouComandoBase(cs, args);
    }
}
