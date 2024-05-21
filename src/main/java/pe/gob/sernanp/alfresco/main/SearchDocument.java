package pe.gob.sernanp.alfresco.main;

import java.util.HashMap;
import java.util.Map;

import pe.gob.sernanp.alfresco.bean.BeanFile;
import pe.gob.sernanp.alfresco.bean.RptaBean;
import pe.gob.sernanp.alfresco.caller.CallServiceRest;

public class SearchDocument {

	public static void main(String[] args) {

		// datos de conexión
		String HOST = "10.10.14.62";
		String PORT = "8080";
		String USER = "admin";
		String PASS = "admin";

		// Parametros de busqueda
		String rutaInterna = null;
		String palabras = null;
		String frase = null;
		String paraIgnorar = null;
		String queryLibre = null;
		String tipoDocumental = null;
		// busqueda por metadato nombre
		HashMap<String, String> metadatos = new HashMap<String, String>();
		metadatos.put("rsag:usuarioSGD", "Joel Caleb");

		RptaBean rpta = CallServiceRest.ServiceSearch(HOST, PORT, USER, PASS, rutaInterna, palabras, frase, paraIgnorar,
				queryLibre, tipoDocumental, metadatos);

		System.out.println("CODIGO: " + rpta.getCode());
		System.out.println("MENSAJE: " + rpta.getMessage());
		if (rpta.getCode() == "00000") {
			System.out.println("\nSe encontraron " + rpta.getNodes().size() + " resultados.");
			for (BeanFile bean : rpta.getNodes()) {
				System.out.println("\nELEMENTO ENCONTRADO: " + bean.getUuid() + " - " + bean.getArchivo());
				HashMap<String, String> values = bean.getProperties();
				for (Map.Entry<String, String> entry : values.entrySet()) {
					System.out.println("\tPROPIEDAD: " + entry.getKey().substring(entry.getKey().lastIndexOf("}") + 1)
							+ " - " + entry.getValue());
				}
			}
		} else {
			System.out.println("EXCEPTION: " + rpta.getException());
		}
	}

}
