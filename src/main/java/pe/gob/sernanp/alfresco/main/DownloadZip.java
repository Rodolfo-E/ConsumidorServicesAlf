package pe.gob.sernanp.alfresco.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import pe.gob.sernanp.alfresco.bean.RptaBean;
import pe.gob.sernanp.alfresco.caller.CallServiceRest;

public class DownloadZip {

	public static void main(String[] args) {

		String HOST = "10.10.14.62";
		String PORT = "8080";
		String USER = "admin";
		String PASS = "admin";
		String UUID = "5adfa9b6-afbf-45d0-b56d-87aa76ddaf13";
		String NAME = "downloaded";

		RptaBean rpta = CallServiceRest.ServiceDownloadZip(HOST, PORT, USER, PASS, UUID, NAME);

		try {
			File file = new File("descargado.zip");
			FileOutputStream fileOuputStream = new FileOutputStream(file);
			fileOuputStream.write(rpta.getContent());
			fileOuputStream.close();
			System.out.println("ZIP descargado en: " + file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
