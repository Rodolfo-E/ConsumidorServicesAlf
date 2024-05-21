package pe.gob.sernanp.alfresco.bean;

import java.util.HashMap;
import java.util.List;

/**
 * Objeto que representa el archivo de Alfresco
 * 
 * @author Domain Consulting
 * @version 1.0
 */
public class BeanFile {

	private String uuid;
	private String archivo;
	private String type;
	private String path;
	private List<BeanFile> nodes;
	private HashMap<String, String> properties;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getArchivo() {
		return archivo;
	}

	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public HashMap<String, String> getProperties() {
		return properties;
	}

	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}

	public List<BeanFile> getNodes() {
        return nodes;
    }
    
    public void setNodes(List<BeanFile> nodes) {
        this.nodes = nodes;
    }

	@Override
	public String toString() {
		return "BeanFile [uuid=" + uuid + ", archivo=" + archivo + ", type="
				+ type + ", path=" + path + ", nodes=" + nodes + "]";
	}
	
}
