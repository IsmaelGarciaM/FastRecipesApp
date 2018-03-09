package com.ismael.fastrecipes.exceptions;

import android.content.Context;

import com.ismael.fastrecipes.utils.ErrorMapUtils;

import static com.ismael.fastrecipes.model.Errors.EMPTYFIELD_EXCEPTION;
import static com.ismael.fastrecipes.model.Errors.INVALIDEMAIL_EXCEPTION;
import static com.ismael.fastrecipes.model.Errors.PASSCAPLETTER_EXCEPTION;
import static com.ismael.fastrecipes.model.Errors.PASSNUMBER_EXCEPTION;
import static com.ismael.fastrecipes.model.Errors.PASSWORDLENGHT_EXCEPTION;
import static com.ismael.fastrecipes.model.Errors.USERISPASS_EXCEPTION;

/**
 * DataEntryException -> Excepción personalizada para la gestión de errores en la interfaz
 * @author Ismael Garcia
 */

public class DataEntryException extends RuntimeException {

    int exceptionType;
    String exceptionMessage;



    public DataEntryException(int exceptionType, Context context){


        String msg = ErrorMapUtils.getErrorMap(context).get(String.valueOf(exceptionType));

        String error = context.getResources().getString(context.getResources().getIdentifier(msg, "string", context.getPackageName()));
        this.exceptionMessage = error;
        this.exceptionType = exceptionType;
/*
        switch (exceptionType){
            case EMPTYFIELD_EXCEPTION:
                this.exceptionMessage = msg;
                break;
            case INVALIDEMAIL_EXCEPTION:
                this.exceptionMessage = ErrorMapUtils.getErrorMap(context).get(exceptionType);;
                break;
            case PASSWORDLENGHT_EXCEPTION:
                this.exceptionMessage = ErrorMapUtils.getErrorMap(context).get(exceptionType);;
                break;
            case USERISPASS_EXCEPTION:
                this.exceptionMessage = ErrorMapUtils.getErrorMap(context).get(exceptionType);;
                break;
            case PASSCAPLETTER_EXCEPTION:
                this.exceptionMessage = ErrorMapUtils.getErrorMap(context).get(exceptionType);;
                break;
            case PASSNUMBER_EXCEPTION:
                this.exceptionMessage = ErrorMapUtils.getErrorMap(context).get(exceptionType);;
                break;

        }
*/
    }

    @Override
    public String getMessage() {
        return exceptionMessage;
    }
    public int getType() {
        return exceptionType;
    }

}
