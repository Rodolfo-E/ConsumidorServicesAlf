package pe.gob.sernanp.alfresco.main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import pe.gob.sernanp.alfresco.bean.RptaBean;
import pe.gob.sernanp.alfresco.caller.CallServiceRest;
import pe.gob.sernanp.alfresco.util.Util;

public class UploadDocument {

	public static void main(String[] args) throws IOException {

		final String HOST = "10.10.14.62";
		final String PORT = "8080";
		final String USER = "admin";
		final String PASS = "admin";
		final String RUTA_DOC_LOCAL = "C:/Users/Domain5/Desktop/sernanp-workispace/sgd_folder1.csv";
		final String NOMBRE_DOC = "doc_name223.csv";
		final String TIPO_DOC = "rsag:archivoGeneral";
		final String FOLDER_DOC_ALF = "SGD/2020/0010/Documento 01/Anexos";

		HashMap<String, String> metadatos = new HashMap<String, String>();
		metadatos.put("rsag:usuarioSGD", "Joel Caleb");

		System.out.println("Archivo a cargar: " + RUTA_DOC_LOCAL);

//		final String FOLDER_DOC_ALF = "SGD/2019/0010/Documento 01/Anexos";
		
//		String carpetaLocal = "C:/Anexos";
//		File anexos = new File(carpetaLocal);
//		File[] directoryListing = anexos.listFiles();
//		for (File child : directoryListing) {
//			if (child.isDirectory()) {
//				// volver a llamar metodo
//			}
//			RptaBean rpta = CallServiceRest.ServiceUpload(HOST, PORT, USER,
//					PASS, Util.getBytes(child.getCanonicalPath()), NOMBRE_DOC, TIPO_DOC,
//					metadatos, FOLDER_DOC_ALF);
//		}

		RptaBean rpta = CallServiceRest.ServiceUpload(HOST, PORT, USER, PASS,
				Util.getBytes(RUTA_DOC_LOCAL), NOMBRE_DOC, TIPO_DOC, metadatos,
				FOLDER_DOC_ALF);

		System.out.println("CODIGO: " + rpta.getCode());
		System.out.println("MENSAJE: " + rpta.getMessage());
		if (rpta.getCode().equals("00000")) {
			System.out.println("UUID: " + rpta.getUuid());
		} else {
			System.out.println("EXCEPTION: " + rpta.getException());
		}

	}

}
