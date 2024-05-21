package pe.gob.sernanp.alfresco.main;

import java.io.FileOutputStream;
import java.io.IOException;

import pe.gob.sernanp.alfresco.bean.RptaBean;
import pe.gob.sernanp.alfresco.caller.CallServiceRest;

public class DownloadDocument {

	public static void main(String[] args) {

		final String HOST = "10.10.14.62";
		final String PORT = "8080";
		final String USER = "admin";
		final String PASS = "admin";
		final String UUID_DOC = "2d0319af-5b08-4ed4-a576-a5397b4d0ae2";

		RptaBean rpta = CallServiceRest.ServiceDownload(HOST, PORT, USER, PASS, UUID_DOC);

		System.out.println("CODIGO: " + rpta.getCode());
		System.out.println("MENSAJE: " + rpta.getMessage());

		if (rpta.getCode() == "00000") {
			System.out.println("PESO: " + rpta.getSize());
			System.out.println("NOMBRE: " + rpta.getFileName());
			System.out.println("MIMETYPE: " + rpta.getMimeType());
			try {
				FileOutputStream fileOuputStream = new FileOutputStream(rpta.getFileName());
				fileOuputStream.write(rpta.getContent());
				fileOuputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			System.out.println("EXCEPTION: " + rpta.getException());
	}

}
