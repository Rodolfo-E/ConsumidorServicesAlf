package pe.gob.sernanp.alfresco.main;

import pe.gob.sernanp.alfresco.bean.RptaBean;
import pe.gob.sernanp.alfresco.caller.CallServiceRest;

public class DeleteDocument {

	public static void main(String[] args) {

		// datos de conexión
		String HOST = "10.10.14.62";
		String PORT = "8080";
		String USER = "admin";
		String PASS = "admin";

		// uuid del documento a eliminar
		String UUID_DOC = "b8b22076-7568-4529-8588-dde86dd3374c";

		RptaBean rpta = CallServiceRest.ServiceDelete(HOST, PORT, USER, PASS, UUID_DOC);

		System.out.println("CODIGO: " + rpta.getCode());
		System.out.println("MENSAJE: " + rpta.getMessage());
		if (rpta.getCode().equals("00000"))
			System.out.println("UUID: " + rpta.getUuid());
		else
			System.out.println("EXCEPTION: " + rpta.getException());

	}

}
