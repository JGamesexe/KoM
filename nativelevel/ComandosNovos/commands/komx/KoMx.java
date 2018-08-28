package nativelevel.ComandosNovos.commands.komx;

import nativelevel.ComandosNovos.Comando;
import nativelevel.ComandosNovos.commands.komx.komxsubs.*;
import nativelevel.utils.ExecutorType;
import org.bukkit.command.CommandSender;

public class KoMx extends Comando {

    public KoMx() {
        super("kom", ExecutorType.TODOS);
        subs.add(new CmdTper());
        subs.add(new CmdTeste());
        subs.add(new CmdAutoClaim());
        subs.add(new CmdDisposicao());
        subs.add(new CmdSuicidio());
        subs.add(new CmdSkills());
        subs.add(new CmdMostraXP());
        subs.add(new CmdSetLevel());
        subs.add(new CmdBoss());
        subs.add(new CmdVerClasses());
    }

    @Override
    public void usouComando(CommandSender cs, String[] args) {
        usouComandoBase(cs, args);
    }

}
