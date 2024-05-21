package pe.gob.sernanp.alfresco.main;

import pe.gob.sernanp.alfresco.bean.RptaBean;
import pe.gob.sernanp.alfresco.caller.CallServiceRest;
import pe.gob.sernanp.alfresco.util.Util;

public class UpdateDocument {

	public static void main(String[] args) {

		// datos de conexión
		String HOST = "10.10.14.62";
		String PORT = "8080";
		String USER = "admin";
		String PASS = "admin";

		// uuid del documento a actualizar
		String UUID_DOC = "b8b22076-7568-4529-8588-dde86dd3374c";
		// nombre actualizado del documento
		String NOMBRE_DOC = "new_name.pdf";
		// ruta del documento para obtener el array de bytes
		String RUTA_DOC_LOCAL = "D:/WorkSpaces/SERNANP/doc_name223.pdf";

		RptaBean rpta = CallServiceRest.ServiceUpdate(HOST, PORT, USER, PASS, UUID_DOC, NOMBRE_DOC,
				Util.getBytes(RUTA_DOC_LOCAL), null);

		System.out.println("Archivo a actualizar: " + RUTA_DOC_LOCAL);

		System.out.println("CODIGO: " + rpta.getCode());
		System.out.println("MENSAJE: " + rpta.getMessage());
		if (rpta.getCode().equals("00000")) {
			System.out.println("UUID: " + rpta.getUuid());
		} else {
			System.out.println("EXCEPTION: " + rpta.getException());
		}

	}

}
