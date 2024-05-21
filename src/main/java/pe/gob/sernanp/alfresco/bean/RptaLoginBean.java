package pe.gob.sernanp.alfresco.bean;

import java.util.List;

/**
 * Objeto que contiene los atributos de respuesta del metodo de inicio de sesion en Alfresco
 * @author Domain Consulting
 * @version 1.0
 */
public class RptaLoginBean {

    private int status;
    private String message;
    private String data;
    private String exception;
    private List<String> callstack;
    private String code;

    public RptaLoginBean(){
    }
    
    public RptaLoginBean(int status, String message, String data, String exception, List<String> callstack, String code){
        this.status = status;
        this.message = message;
        this.data = data;
        this.exception = exception;
        this.callstack = callstack;
        this.code = code;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public List<String> getCallstack() {
        return callstack;
    }

    public void setCallstack(List<String> callstack) {
        this.callstack = callstack;
    }

    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
}


