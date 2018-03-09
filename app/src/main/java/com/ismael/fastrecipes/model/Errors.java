package com.ismael.fastrecipes.model;

/**
 * Errors.class -> Clase para el mapeo de errores
 * @author Ismael Garcia
 */

public class Errors {

    public static final int EMPTYFIELD_EXCEPTION = 10;
    public static final int INVALIDEMAIL_EXCEPTION = 11;
    public static final int USERISPASS_EXCEPTION = 12;
    public static final int PASSWORDLENGHT_EXCEPTION = 13;
    public static final int PASSCAPLETTER_EXCEPTION = 14;
    public static final int PASSNUMBER_EXCEPTION = 15;
    public static final int PHONE_EXCEPTION = 16;
    public static final int EMPTYTIME_EXCEPTION = 101;
    public static final int EMPTYDIF_EXCEPTION = 102;
    public static final int EMPTYNAME_EXCEPTION = 103;
    public static final int EMPTYING_EXCEPTION = 104;
    public static final int EMPTYELABORATION_EXCEPTION = 105;


    public static String message;
    public static int code;

    static {

    }
}
