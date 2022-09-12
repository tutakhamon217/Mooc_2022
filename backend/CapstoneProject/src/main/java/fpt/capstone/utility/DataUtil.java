package fpt.capstone.utility;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

public class DataUtil {

    public static Double safeToDouble(Object obj1, Double defaultValue) {
        Double result = defaultValue;
        if (obj1 != null) {
            try {
                result = Double.parseDouble(obj1.toString());
            } catch (Exception ignored) {
                Logging.getLogger().info(ignored.getMessage(), ignored);
            }
        }

        return result;
    }

    public static Double safeToDouble(Object obj1) {
        return safeToDouble(obj1, 0.0);
    }

    public static Short safeToShort(Object obj1, Short defaultValue) {
        Short result = defaultValue;
        if (obj1 != null) {
            try {
                result = Short.parseShort(obj1.toString());
            } catch (Exception ignored) {
                Logging.getLogger().info(ignored.getMessage(), ignored);
            }
        }

        return result;
    }

    /**
     * @param obj1
     * @param defaultValue
     * @return
     * @author phuvk
     */
    public static int safeToInt(Object obj1, int defaultValue) {
        int result = defaultValue;
        if (obj1 != null) {
            try {
                result = Integer.parseInt(obj1.toString());
            } catch (Exception ignored) {
                Logging.getLogger().info(ignored.getMessage(), ignored);
            }
        }

        return result;
    }

    /**
     * @param obj1 Object
     * @return int
     */
    public static int safeToInt(Object obj1) {
        return safeToInt(obj1, 0);
    }

    /**
     * @param obj1 Object
     * @return String
     */
    public static String safeToString(Object obj1, String defaultValue) {
        if (obj1 == null || obj1.toString().isEmpty()) {
            return defaultValue;
        }

        return obj1.toString();
    }

    public static Boolean safeToBoolean(Object obj1) {
        if (obj1 == null || obj1 instanceof Boolean) {
            return (Boolean) obj1;
        }
        return false;
    }

    /**
     * @param obj1 Object
     * @return String
     */
    public static String safeToString(Object obj1) {
        return safeToString(obj1, null);
    }

    public static Instant safeToInstant(Object obj) {
        if (obj != null) {
            if (obj instanceof Instant) {
                return (Instant) obj;
            } else if (obj instanceof Timestamp) {
                return ((Timestamp) obj).toInstant();
            }
        }
        return null;
    }

    public static Date safeToDate(Object obj) {
        if (obj instanceof Date) {
            return (Date) obj;
        }
        return null;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
