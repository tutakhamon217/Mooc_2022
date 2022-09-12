package fpt.capstone.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Logging {
    private static Logger logger = LogManager.getLogger(Logging.class);

    public static Logger getLogger() {
        return logger;
    }

}
