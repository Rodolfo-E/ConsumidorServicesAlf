package pe.gob.sernanp.alfresco.bean;

/**
 * Clase que se conecta directamente con los servicios del Gestor Documental  
 * @author Domain Consulting
 * @version 1.0
 */
public class Status {
	
    private int code;
    private String name;
    private String description;
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    } 
}