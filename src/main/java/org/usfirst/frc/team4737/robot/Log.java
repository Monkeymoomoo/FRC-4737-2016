package org.usfirst.frc.team4737.robot;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Outputs formatted information.
 *
 * @author Brian Semrau
 * @version 1/10/2016
 */
public class Log {

    private static final SimpleDateFormat format = new SimpleDateFormat("[yyyy-MM-dd-HH:mm:ss.SS]");

    public static void info(Object o) {
        System.out.println(format.format(new Date()) + " [INFO]: " + o);
    }

    public static void debug(Object o) {
        System.out.println(format.format(new Date()) + " [DEBUG]: " + o);
    }

    public static void warn(Object o) {
        System.out.println(format.format(new Date()) + " [WARNING]: " + o);
    }

    public static void err(Object o) {
        System.err.println(format.format(new Date()) + " [ERROR]: " + o);
    }

}
