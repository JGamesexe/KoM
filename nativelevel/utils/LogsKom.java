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
package nativelevel.utils;

import nativelevel.KoM;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class LogsKom {

    private boolean logON = false;

    private final Logger logger = Logger.getLogger(KoM.class
            .getName());
    private FileHandler fh = null;

    public LogsKom() {
        /*
        //just to make our log file nicer :)
        SimpleDateFormat format = new SimpleDateFormat("d/M HH:mm:ss M-d_HHmmss");
        try {
            fh = new FileHandler(KnightsOfMania._instance.getDataFolder() + "LogsKom.log");
        } catch (Exception e) {
            e.printStackTrace();
        }

        fh.setFormatter(new SimpleFormatter());
        logger.addHandler(fh);
                */
        // TODO LOGS
    }

    public void logEspecial(String msg) {
        if (logON)
            logger.info(msg);
    }

}
