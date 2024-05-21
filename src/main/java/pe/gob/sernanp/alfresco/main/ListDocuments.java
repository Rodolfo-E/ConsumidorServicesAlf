package pe.gob.sernanp.alfresco.main;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.plaf.SliderUI;

import pe.gob.sernanp.alfresco.bean.BeanFile;
import pe.gob.sernanp.alfresco.bean.RptaBean;
import pe.gob.sernanp.alfresco.caller.CallServiceRest;

public class ListDocuments {

	public static void main(String[] args) {
		// datos de conexión
		String HOST = "10.10.14.62";
		String PORT = "8080";
		String USER = "admin";
		String PASS = "admin";
		String PATH = "Company Home/SGD/000002";
		String RUTA_DESCARGA = "C:/Users/Domain5/Desktop/";
		
		RptaBean rpta = CallServiceRest.ServiceListDocuments(HOST, PORT, USER, PASS, PATH);
		
		if (rpta.getCode() == "00000") {
			System.out.println("> " + rpta.getPath() + " [" + rpta.getUuid() + "]");
			//Lista de documentos
			for (BeanFile beanFile : rpta.getNodes()) {
				System.out.println("\t- " + beanFile.getType() + ": " + beanFile.getArchivo());
				System.out.println("\t\t- uuid: " + beanFile.getUuid());
				System.out.println("\t\t- path: " + beanFile.getPath());
				if (beanFile.getType().equals("folder")) {
					int i = 1;
					printNodes(beanFile, i);
				}
			}
		} else {
			System.out.println("EXCEPTION: " + rpta.getException());
		}
	}
	
	static void printNodes(BeanFile bf, int i) {
		String tTabs = "\t";
		String cTabs = "\t\t";
		int j = i;
		while (j > 0) {
			tTabs = tTabs + "\t";
			cTabs = cTabs + "\t";
			j--;
		}
		System.out.println(tTabs + "\t- nodes:");
		for (BeanFile beanFile : bf.getNodes()) {
			System.out.println(tTabs + "\t- " + beanFile.getType() + ": " + beanFile.getArchivo());
			System.out.println(cTabs + "\t- uuid: " + beanFile.getUuid());
			System.out.println(cTabs + "\t- path: " + beanFile.getPath());
			if (beanFile.getType().equals("folder")) {
				i++;
				printNodes(beanFile, i);
			}
		}
	}
	
}
