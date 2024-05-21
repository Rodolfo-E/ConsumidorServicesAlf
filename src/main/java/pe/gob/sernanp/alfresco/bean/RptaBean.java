package pe.gob.sernanp.alfresco.bean;

import java.util.List;

import pe.gob.sernanp.alfresco.util.Util;

/**
 * Objeto que contiene los atributos de respuesta de los metodos de las operaciones en Alfresco
 * @author Domain Consulting
 * @version 1.0
 */
public class RptaBean {
    
    private Status status;
    private String message;   
    private String time;
    private String server;
    private String exception;
    private List<String> callstack;
    private String result;
    private String code;
    
    private String uuid;
    private String fileName;
    private byte[] content;
    private String mimeType;
    private String size; 
    private String path;
    private List<BeanFile> nodes;
    private String propiedades;
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public String getMessage() {
        if(status!=null)
            if(status.getCode()==200)
                this.message=Util.getMessage("00000");
    	return this.message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getUuid() {
        String ref = "workspace://SpacesStore/";
        if(uuid!=null)
            if (uuid.startsWith(ref))
                uuid = uuid.substring(ref.length(), uuid.length());
        return uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public String getSize() {
        return size;
    }
    
    public void setSize(String size) {
        this.size = size;
    }
    
    public String getMimeType() {
        return mimeType;
    }
    
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public String getServer() {
        return server;
    }
    
    public void setServer(String server) {
        this.server = server;
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
    
    public byte[] getContent() {
        return content;
    }
    
    public void setContent(byte[] content) {
        this.content = content;
    }
    
    public String getCode() {
        if(status!=null)
            if(status.getCode()==200)
                code="00000";
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public List<BeanFile> getNodes() {
        return nodes;
    }
    
    public void setNodes(List<BeanFile> nodes) {
        this.nodes = nodes;
    }
    
    public String getPropiedades() {
        return propiedades;
    }
    
    public void setPropiedades(String propiedades) {
        this.propiedades = propiedades;
    }  
}
