package pe.gob.sernanp.alfresco.caller;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpException;

import pe.gob.sernanp.alfresco.bean.*;
import pe.gob.sernanp.alfresco.service.Services;
import pe.gob.sernanp.alfresco.util.Util;

/**
 * Clase que expone los metodos para el consumo de los servicios del Gestor
 * Documental
 * 
 * @author Domain Consulting
 * @version 1.0
 */
public class CallServiceRest {

	/**
	 * Metodo que permite la carga/subida de un documento al Gestor Documental
	 * Alfresco
	 * 
	 * @param host       IP o dominio del Gestor Documental Alfresco.
	 * @param port       Puerto del Gestor Documental Alfresco.
	 * @param user       Usuario de conexión al Gestor Documental Alfresco.
	 * @param pass       Clave del usuario del Gestor Documental Alfresco.
	 * @param file       Array de bytes del documento.
	 * @param fileName   Nombre que tendra el documento.
	 * @param nodeType   Tipo Documental que tendra el documento a cargar.
	 * @param attributes Hashmap de la metadata que tendra el documento a cargar.
	 * @param path       Ruta del repositorio donde se cargara el documneto.
	 * @return Devuelve objeto del tipo RptaBean con Código, Mensaje y en caso de
	 *         exito: UUID del documento.
	 */
	public static RptaBean ServiceUpload(String host, String port, String user, String pass, byte[] file,
			String fileName, String nodeType, HashMap<String, String> attributes, String path) {

		RptaBean rpta = new RptaBean();
		HashMap<String, String> credenciales = Util.validateCredenciales(host, port, user, pass);
		if (!(credenciales.size() == 0 || file.length == 0 || Util.isBlank(fileName) || Util.isBlank(path))) {
			if (Util.containsIllegals(fileName)) {
				rpta = Util.setErrorRpta("E2002", null);
				return rpta;
			} else if (Util.containsIllegalsFolderName(path)) {
				rpta = Util.setErrorRpta("E2003", null);
				return rpta;
			}
			if (nodeType == null || nodeType.trim() == "") {
				nodeType = "cm:content";
				if (attributes != null) {
					attributes = null;
				}
			}
			String ext = Util.getFileExtension(fileName);
			String mime = null;
			if (ext != null) {
				mime = Util.getMimeType(ext);
			}
			rpta = Services.uploadDocument(credenciales, file, fileName, mime, nodeType, attributes, path);
		} else {
			rpta = Util.setErrorRpta("E1005", null);
			return rpta;
		}
		return rpta;
	}

	/**
	 * Metodo que permite la creacion de carpetas o subcarpeta en el Gestor
	 * Documental Alfresco
	 * 
	 * @param host        IP o dominio del Gestor Documental Alfresco.
	 * @param port        Puerto del Gestor Documental Alfresco.
	 * @param user        Usuario de conexión al Gestor Documental Alfresco.
	 * @param pass        Clave del usuario del Gestor Documental Alfresco.
	 * @param rutaCarpeta Ruta que incluye las carpetas y subcarpetas que se
	 *                    crearan.
	 * @return Devuelve objeto del tipo RptaBean con Código, Mensaje y en caso de
	 *         exito: UUID de la carpeta.
	 */
	public static RptaBean ServiceCreateFolder(String host, String port, String user, String pass, String rutaCarpeta, String type, HashMap<String, String> attribs) {

		RptaBean rpta = new RptaBean();
		HashMap<String, String> credenciales = Util.validateCredenciales(host, port, user, pass);
		if (!(credenciales.size() == 0 || Util.isBlank(rutaCarpeta))) {
			rutaCarpeta = Util.formatPathFolder(rutaCarpeta);
			if (Util.containsIllegalsFolderName(rutaCarpeta)) {
				rpta = Util.setErrorRpta("E2003", null);
				return rpta;
			}
			if (type == null || type.trim() == "") {
				type = "cm:folder";
				if (type != null) {
					type = null;
				}
			}
			rpta = Services.createFolder(credenciales, rutaCarpeta, type, attribs);
		} else {
			rpta = Util.setErrorRpta("E1005", null);
			return rpta;
		}
		return rpta;
	}

	/**
	 * Metodo que permite la descarga de un documento del Gestor Documental Alfresco
	 * 
	 * @param host IP o dominio del Gestor Documental Alfresco.
	 * @param port Puerto del Gestor Documental Alfresco.
	 * @param user Usuario de conexión al Gestor Documental Alfresco.
	 * @param pass Clave del usuario del Gestor Documental Alfresco.
	 * @param uuid Identificador unico del documento que se desea descargar.
	 * @return Devuelve objeto del tipo RptaBean con Código, Mensaje y en caso de
	 *         exito: Nombre del archivo (fileName), bytes del documento (content),
	 *         tipo de documento (mimetype)`.
	 */
	public static RptaBean ServiceDownload(String host, String port, String user, String pass, String uuid) {

		RptaBean rpta = new RptaBean();
		HashMap<String, String> credenciales = Util.validateCredenciales(host, port, user, pass);
		if (!(credenciales.size() == 0 || Util.isBlank(uuid))) {
			if (Util.containsIllegals(uuid)) {
				rpta = Util.setErrorRpta("E3005", null);
				return rpta;
			}
			rpta = Services.downloadDocument(credenciales, uuid);
		} else {
			rpta = Util.setErrorRpta("E1005", null);
			return rpta;
		}

		return rpta;
	}

	/**
	 * Metodo que permite la eliminacion de un documento del Gestor Documental
	 * Alfresco
	 * 
	 * @param host IP o dominio del Gestor Documental Alfresco.
	 * @param port Puerto del Gestor Documental Alfresco.
	 * @param user Usuario de conexión al Gestor Documental Alfresco.
	 * @param pass Clave del usuario del Gestor Documental Alfresco.
	 * @param uuid Identificador unico del documento que se desea eliminar.
	 * @return Devuelve objeto del tipo RptaBean con Código y Mensaje de la
	 *         operacion.
	 */
	public static RptaBean ServiceDelete(String host, String port, String user, String pass, String uuid) {

		RptaBean rpta = new RptaBean();
		HashMap<String, String> credenciales = Util.validateCredenciales(host, port, user, pass);
		if (!(credenciales.size() == 0 || Util.isBlank(uuid))) {
			if (Util.containsIllegals(uuid)) {
				rpta = Util.setErrorRpta("E3005", null);
				return rpta;
			}
			rpta = Services.deleteDocument(credenciales, uuid);
		} else {
			rpta = Util.setErrorRpta("E1005", null);
			return rpta;
		}

		return rpta;
	}

	/**
	 * Metodo que permite la actualizacion de un documento del Gestor Documental
	 * Alfresco
	 * 
	 * @param host       IP o dominio del Gestor Documental Alfresco.
	 * @param port       Puerto del Gestor Documental Alfresco.
	 * @param user       Usuario de conexión al Gestor Documental Alfresco.
	 * @param pass       Clave del usuario del Gestor Documental Alfresco.
	 * @param uuid       Identificador unico del documento que se desea actualizar.
	 * @param file       Array de bytes del documento.
	 * @param fileName   Nombre con el cual se actualizara el documento.
	 * @param attributes Hashmap de la metadata que tendra el documento a
	 *                   actualizar.
	 * @return Devuelve objeto del tipo RptaBean con Código y Mensaje de la
	 *         operacion.
	 */
	public static RptaBean ServiceUpdate(String host, String port, String user, String pass, String uuid,
			String fileName, byte[] file, HashMap<String, String> attributes) {

		RptaBean rpta = new RptaBean();
		try {
			HashMap<String, String> credenciales = Util.validateCredenciales(host, port, user, pass);
			if (!(credenciales.size() == 0 || Util.isBlank(uuid) || file.length == 0)) {
				if (Util.containsIllegals(fileName)) {
					rpta = Util.setErrorRpta("E2002", null);
				} else if (Util.containsIllegals(uuid)) {
					rpta = Util.setErrorRpta("E3005", null);
				} else {
					rpta = Services.updateDocument(credenciales, uuid, fileName, file, attributes);
				}
			} else {
				rpta = Util.setErrorRpta("E1005", null);
			}
		} catch (Exception ex) {
			rpta = Util.setErrorRpta("E9999", ex.getMessage());
		} finally {
			// Cerrar sesion
		}

		return rpta;
	}

	/**
	 * Metodo que permite la eliminacion de una carpeta del Gestor Documental
	 * Alfresco
	 * 
	 * @param host        IP o dominio del Gestor Documental Alfresco.
	 * @param port        Puerto del Gestor Documental Alfresco.
	 * @param user        Usuario de conexión al Gestor Documental Alfresco.
	 * @param pass        Clave del usuario del Gestor Documental Alfresco.
	 * @param folderPath  Ruta completa de la carpeta que se desea eliminar.
	 * @param uuid        Identificador unico del documento que se desea eliminar.
	 * @param forceDelete Flag que indica si se eliminara la carpeta aún cuando
	 *                    tenga contenido dentro.
	 * @return Devuelve objeto del tipo RptaBean con Código y Mensaje de la
	 *         operacion.
	 */
	public static RptaBean ServiceDeleteFolder(String host, String port, String user, String pass, String folderPath,
			String uuid, boolean forceDelete) {

		String force = "false";
		if (forceDelete)
			force = "true";
		RptaBean rpta = new RptaBean();
		HashMap<String, String> credenciales = Util.validateCredenciales(host, port, user, pass);
		if (credenciales.size() == 0) {
			rpta = Util.setErrorRpta("E1005", null);
		} else if (Util.isBlank(folderPath)) {
			if (Util.isBlank(uuid)) {
				rpta = Util.setErrorRpta("E1005", null);
			} else if (Util.containsIllegals(uuid)) {
				rpta = Util.setErrorRpta("E3005", null);
			} else {
				uuid = "workspace://SpacesStore/" + uuid;
				rpta = Services.deleteFolder(credenciales, "", uuid, force);
			}
		} else if (Util.containsIllegalsFolderName(folderPath)) {
			rpta = Util.setErrorRpta("E2003", null);
		} else {
			folderPath = Util.formatPathFolder(folderPath);
			rpta = Services.deleteFolder(credenciales, folderPath, "", force);
		}

		return rpta;
	}

	/**
	 * Metodo que permite la busqueda de un documento del Gestor Documental Alfresco
	 * 
	 * @param host        IP o dominio del Gestor Documental Alfresco.
	 * @param port        Puerto del Gestor Documental Alfresco.
	 * @param user        Usuario de conexión al Gestor Documental Alfresco.
	 * @param pass        Clave del usuario del Gestor Documental Alfresco.
	 * @param path        Ruta especifica donde se realizara la busqueda.
	 * @param words       Palabra o palabras por la que se buscara.
	 * @param phrase      Frase a buscar.
	 * @param toIgnore    Palabra a ignorar.
	 * @param customQuery Query personalizada para buscar.
	 * @param nodeType    Tipo documental a buscar.
	 * @param attributes  Hashmap de la metadata que tendrá el documento a cargar.
	 * @return Devuelve objeto del tipo RptaBean con Código, Mensaje y en caso de
	 *         exito: Lista de BeanFile (nodes) que contiene por cada documento
	 *         encontrado: nombre (archivo), uuid (uuid) y propiedades (properties)
	 */
	public static RptaBean ServiceSearch(String host, String port, String user, String pass, String path, String words,
			String phrase, String toIgnore, String customQuery, String nodeType, HashMap<String, String> attributes) {

		RptaBean rpta = new RptaBean();
		HashMap<String, String> credenciales = Util.validateCredenciales(host, port, user, pass);
		if (Util.isBlankMap(credenciales) || (Util.isBlank(words) && Util.isBlank(phrase) && Util.isBlank(toIgnore)
				&& Util.isBlank(customQuery) && Util.isBlank(nodeType) && Util.isBlankMap(attributes))) {
			rpta = Util.setErrorRpta("E1005", null);
		} else {
			rpta = Services.searchDocument(credenciales, path, words, phrase, toIgnore, customQuery, nodeType,
					attributes);
			if (rpta.getCode().equals("00000"))
				rpta = Util.formatRptaBusqueda(rpta);
		}
		return rpta;
	}
	
	/**
	 * Metodo que permite la busqueda los doccumentos de una carpeta del Gestor Documental Alfresco
	 * 
	 * @param host        IP o dominio del Gestor Documental Alfresco.
	 * @param port        Puerto del Gestor Documental Alfresco.
	 * @param user        Usuario de conexión al Gestor Documental Alfresco.
	 * @param pass        Clave del usuario del Gestor Documental Alfresco.
	 * @param path        Ruta especifica donde se realizara la busqueda.
	 * @param words       Palabra o palabras por la que se buscara.
	 * @param phrase      Frase a buscar.
	 * @param toIgnore    Palabra a ignorar.
	 * @param customQuery Query personalizada para buscar.
	 * @param nodeType    Tipo documental a buscar.
	 * @param attributes  Hashmap de la metadata que tendrá el documento a cargar.
	 * @return Devuelve objeto del tipo RptaBean con Código, Mensaje y en caso de
	 *         exito: Lista de BeanFile (nodes) que contiene por cada documento
	 *         encontrado: nombre (archivo), uuid (uuid) y propiedades (properties)
	 */
	public static RptaBean ServiceListDocuments(String host, String port, String user, String pass, String path) {

		RptaBean rpta = new RptaBean();
		HashMap<String, String> credenciales = Util.validateCredenciales(host, port, user, pass);
		if (Util.isBlankMap(credenciales) || (Util.isBlank(path))) {
			rpta = Util.setErrorRpta("E1005", null);
		} else {
			rpta = Services.listDocuments(credenciales, path);
		}
		return rpta;
	}
	
	/**
	 * Metodo que permite la busqueda los doccumentos de una carpeta del Gestor Documental Alfresco
	 * 
	 * @param host        IP o dominio del Gestor Documental Alfresco.
	 * @param port        Puerto del Gestor Documental Alfresco.
	 * @param user        Usuario de conexión al Gestor Documental Alfresco.
	 * @param pass        Clave del usuario del Gestor Documental Alfresco.
	 * @param uuid        Ruta especifica donde se realizara la busqueda.
	 * @param filename    Palabra o palabras por la que se buscara.
	 * @return Devuelve objeto del tipo RptaBean con Código, Mensaje y en caso de
	 *         exito: Lista de BeanFile (nodes) que contiene por cada documento
	 *         encontrado: nombre (archivo), uuid (uuid) y propiedades (properties)
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public static RptaBean ServiceDownloadZip(String host, String port, String user, String pass, String uuid, String filename) {
		
		RptaBean rpta = new RptaBean();
		HashMap<String, String> credenciales = Util.validateCredenciales(host, port, user, pass);
		if (Util.isBlankMap(credenciales) || (Util.isBlank(uuid))) {
			rpta = Util.setErrorRpta("E1005", null);
		} else {
			rpta = Services.downloadZip(credenciales, uuid, filename);
		}
		return rpta;
	}
	
}
