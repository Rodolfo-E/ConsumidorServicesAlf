package pe.gob.sernanp.alfresco.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pe.gob.sernanp.alfresco.bean.*;

/**
 * Clase que contiene metodos utilitarios
 * 
 * @author Domain Consulting
 * @version 1.0
 */
public class Util {

	/**
	 * Metodo que obtener el mensaje de error o exito desde un archivo de
	 * propiedades.
	 * 
	 * @param keyMessage Codigo del mensaje de error o exito.
	 * @return Devuelve el mensaje de error o exito segun el codigo enviado.
	 */
	public static String getMessage(String keyMessage) {
		String message = null;
		try {
			ResourceBundle rb = ResourceBundle.getBundle("Errors");
			message = rb.getString(keyMessage);
		} catch (MissingResourceException e) {
			message = "No se encontro mensaje para el codigo de error.";
		}
		return message;
	}

	/**
	 * Metodo que obtener el mimetype de Alfresco de acuerdo a la extension enviada
	 * 
	 * @param ext Extension del archivo.
	 * @return Devuelve el mimetype que entiende Alfresco.
	 */
	public static String getMimeType(String ext) {
		String value = null;
		try {
			ResourceBundle rb = ResourceBundle.getBundle("TipoArchivo");
			value = rb.getString(ext);
		} catch (MissingResourceException e) {
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println(e.toString());
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
			value = "application/octet-stream";
		}
		return value;
	}

	/**
	 * Metodo que obtiene la extension del archivo a traves de su nombre.
	 * 
	 * @param fileName Nombre del archivo.
	 * @return Devuelve la extension del archivo.
	 */
	public static String getFileExtension(String fileName) {
		if (fileName.contains(".")) {
			String arr[] = fileName.split("\\.");
			return arr[arr.length - 1].toLowerCase();
		} else {
			return null;
		}
	}

	/**
	 * Metodo que mapea el codigo y mensaje en un objeto del tipo RptaBean
	 * 
	 * @param code    Codigo de la respuesta.
	 * @param message Mensaje de la respuesta.
	 * @return Devuelve objeto del tipo RptaBean.
	 */
	public static RptaBean setErrorRpta(String code, String message) {
		Status st = new Status();
		st.setCode(400);
		RptaBean error = new RptaBean();
		if (Util.isBlank(message)) {
			error.setCode(code);
			error.setMessage(getMessage(code));
		} else {
			error.setCode(code);
			error.setMessage(getMessage(code));
			error.setException(message);
		}
		error.setStatus(st);
		return error;
	}

	/**
	 * Metodo que valida si la cadena enviada es vacia o nula.
	 * 
	 * @param ptext Cadena a procesar.
	 * @return Devuelve true si la cadena enviada es nula o vacia. Devuelve false en
	 *         caso contrario.
	 */
	public static boolean isBlank(String ptext) {
		return ptext == null || ptext.trim().length() == 0;
	}

	/**
	 * Metodo que valida si el Hashmap enviado es vacio o nulo.
	 * 
	 * @param map Hashmap a procesar.
	 * @return Devuelve true si el Hashmap enviado es vacio o nulo. Devuelve false
	 *         en caso contrario.
	 */
	public static boolean isBlankMap(HashMap<String, String> map) {
		return map == null || map.size() == 0;
	}

	/**
	 * Metodo que valida si la cadena enviada contiene caracteres invalidados para
	 * el nombre del documento en Alfresco.
	 * 
	 * @param toExamine Cadena a examinar.
	 * @return Devuelve true si la cadena contiene caracteres invalidos. Devuelve
	 *         false en caso contrario.
	 */
	public static boolean containsIllegals(String toExamine) {
		boolean resp = true;
		if (!Util.isBlank(toExamine)) {
			Pattern pattern = Pattern.compile("[~#@*+%{}<>\\[\\]|\"\\^]");
			Matcher matcher = pattern.matcher(toExamine);
			resp = matcher.find();
		}
		return resp;
	}

	/**
	 * Metodo que genera la query de búsqueda.
	 * 
	 * @param path       Ruta interna para buscar.
	 * @param parametro1 Palabra a buscar
	 * @param parametro2 Frase a buscar.
	 * @param parametro3 Palabra a ignorar.
	 * @param parametro4 Query libre.
	 * @param nodeType   Tipo documental.
	 * @param attributes Metadatos para buscar.
	 * @return Devuelve la query completa para realizar la busqueda.
	 */
	public static String generarQuery(String path, String parametro1, String parametro2, String parametro3,
			String parametro4, String nodeType, HashMap<String, String> attributes) {

		String texto = "";
		String key = "";
		if (Util.isBlank(path)) {
			path = "";
		}
		String luceneQuery = "PATH:\"" + path + "//*\"";
		if (!isBlank(parametro1)) {
			texto += " AND TEXT:" + parametro1 + "";
		}
		if (!isBlank(parametro2)) {
			texto += " AND TEXT:\"" + parametro2 + "\"";
		}
		if (!isBlank(parametro3)) {
			texto += " AND NOT TEXT:" + parametro3;
		}
		if (!isBlank(parametro4)) {
			texto += " AND " + parametro4;
		}
		if (!(Util.isBlank(nodeType))) {
			texto += " AND TYPE:\"" + nodeType + "\"";
		}
		if (!Util.isBlankMap(attributes)) {
			for (Map.Entry<String, String> entry : attributes.entrySet()) {
				key = entry.getKey();
				key = key.replace(":", "\\:");
				key = key.replace("{", "\\{");
				key = key.replace("}", "\\}");
				texto += " AND @" + key + ":\"" + entry.getValue() + "\"";
			}
		}
		luceneQuery += texto;
		return luceneQuery;
	}

	/**
	 * Metodo que valida si la cadena enviada contiene caracteres invalidados para
	 * el nombre de la carpeta en Alfresco.
	 * 
	 * @param folderName Cadena a examinar.
	 * @return Devuelve true si la cadena contiene caracteres invalidos. Devuelve
	 *         false en caso contrario.
	 */
	public static boolean containsIllegalsFolderName(String folderName) {
		boolean bandera = false;
		String[] caracteres = { "*", "\"", "<", ">", "\\", "//", "?", ":", "|", "." };
		for (int i = 0; i < caracteres.length - 1; i++) {
			int pos = folderName.indexOf(caracteres[i]);
			if (pos != -1) {
				bandera = true;
			}
		}
		if (folderName.substring(folderName.length() - 1).equals(caracteres[caracteres.length - 1])) {
			bandera = true;
		}
		return bandera;
	}

	/**
	 * Metodo que valida y completa la cadena que representa una ruta en Alfresco.
	 * 
	 * @param pathFolder Cadena a examinar.
	 * @return Devuelve la cadena validada y completa.
	 */
	public static String formatPathFolder(String pathFolder) {
		if (pathFolder.substring(0, 1).equals("/")) {
			pathFolder = pathFolder.substring(1);
		}
		if (pathFolder.substring(pathFolder.length() - 1).equals("/")) {
			pathFolder = pathFolder.substring(0, pathFolder.length() - 1);
		}
		return pathFolder;
	}

	/**
	 * Metodo que mapea la respuesta de un webscript de login al objeto
	 * RptaLoginBean.
	 * 
	 * @param error     Error resultante.
	 * @param status    Estado de la respuesta.
	 * @param message   Mensaje de la respuesta.
	 * @param data      Informacion devuelto por el webscript.
	 * @param exception Excepcion en caso de que ocurra un error.
	 * @param callStack Stack del error.
	 * @param code      Codigo resultante.
	 * @return Devuelve los datos enviados en el objeto RptaLoginBean.
	 */
	public static Object setErrorRptaLoginBean(Object error, int status, String message, String data, String exception,
			List<String> callStack, String code) {
		((RptaLoginBean) error).setStatus(status);
		((RptaLoginBean) error).setMessage(message);
		((RptaLoginBean) error).setData(data);
		((RptaLoginBean) error).setException(exception);
		((RptaLoginBean) error).setCallstack(callStack);
		((RptaLoginBean) error).setCode(code);
		return error;
	}

	/**
	 * Metodo que mapea los datos de conexion en un Hashmap.
	 * 
	 * @param host Host de Alfresco.
	 * @param port Puerto de Alfresco.
	 * @param user Usuario de Alfresco.
	 * @param pass Clave de Alfresco.
	 * @return Devuelve los datos enviados en el objeto RptaLoginBean.
	 */
	public static HashMap<String, String> validateCredenciales(String host, String port, String user, String pass) {
		HashMap<String, String> credenciales = new HashMap<String, String>();
		if (isBlank(host) || isBlank(user) || isBlank(pass)) {
			credenciales.clear();
		} else {
//            pass = EncryptUtil.dencrypt(pass);
			credenciales.clear();
			credenciales.put("HOST", host);
			credenciales.put("PORT", port);
			credenciales.put("USER", user);
			credenciales.put("PASSWORD", pass);
		}
		return credenciales;
	}

	/**
	 * Metodo que convierte una cadena a un HashMap que contiene los metadatos.
	 * 
	 * @param toHash Cadena a convertir.
	 * @return Devuelve el HashMap de metadatos.
	 */
	static HashMap<String, String> convertStringToHash(String toHash) {
		if (toHash.substring(0, 3).equals("-;-")) {
			toHash = toHash.substring(3);
		}
		String[] properties = toHash.split("-;-");
		HashMap<String, String> m = new HashMap<String, String>();
		for (String propertie : properties) {
			if (propertie.substring(0, 3).equals("-,-")) {
				propertie = propertie.substring(3);
			}
			if (propertie.lastIndexOf("-,-") + 3 == propertie.length()) {
				propertie += "null";
			}
			String[] valores = propertie.split("-,-");
			if (valores.length == 2) {
				if (valores[0].indexOf("modified") >= 0 || valores[0].indexOf("created") >= 0) {
					valores[1] = valores[1].substring(0, valores[1].lastIndexOf(":") + 3);
				}
				if (valores[1] == null || valores[1].equals("null")) {
					valores[1] = "";
				}
				if (valores.length > 1) {
					m.put(valores[0], valores[1]);
				} else {
					m.put(valores[0], "");
				}
			}
		}
		return m;
	}

	/**
	 * Metodo que da formato a la respuesta de la búsqueda.
	 * 
	 * @param rpta Objeto resultante del webscript.
	 * @return Objeto con el formato de busqueda.
	 */
	public static RptaBean formatRptaBusqueda(RptaBean rpta) {
		String propiedades = rpta.getPropiedades();
		String[] propsNode = propiedades.split("-;;-");
		List<BeanFile> list = rpta.getNodes();
		for (BeanFile beanFile : list) {
			for (String props : propsNode) {
				if (props.indexOf(beanFile.getUuid()) >= 0) {
					beanFile.setProperties(convertStringToHash(props));
				}
			}
		}
		rpta.setNodes(list);
		rpta.setPropiedades("");
		return rpta;
	}

	/**
	 * Metodo que da formato a la respuesta de listar los Datalist.
	 * 
	 * @param rpta Objeto resultante del webscript.
	 * @return Objeto con el formato de datalist.
	 */
	public static RptaBean formatRptaDatalist(RptaBean rpta) {
		String propiedades = rpta.getPropiedades();
		String[] propsNode = propiedades.split("-;;-");
		List<BeanFile> list = rpta.getNodes();
		for (String props : propsNode) {
			for (BeanFile beanFile : list) {
				if (Util.isBlankMap(beanFile.getProperties())) {
					beanFile.setProperties(convertStringToHash(props));
					break;
				}
			}
		}
		rpta.setNodes(list);
		rpta.setPropiedades("");
		return rpta;
	}

	/**
	 * Metodo que identifica el tipo de error a partir del mensaje de respuesta del
	 * webscript.
	 * 
	 * @param message Mensaje de respuesta.
	 * @return Devuelve el codigo del error conocido.
	 */
	public static String formatError(String message) {
		if (message.startsWith("E") && message.length() == 5)
			return message;
		if (message.contains("AccessDeniedException"))
			return "E1004";
		if (message.contains("java.lang.IllegalArgumentException")
				&& message.contains("has not been defined in the data dictionary"))
			return "E2001";
		if (message.contains("org.alfresco.service.cmr.model.FileExistsException"))
			return "E2004";
		if (message.contains("Mandatory property not set"))
			return "E2005";
		if (message.contains("Failed to execute search"))
			return "E4003";
		return "E9999";
	}

	public static byte[] getBytes(String rutaFile) {
		FileInputStream fileInputStream = null;
		File f = new File(rutaFile);
		byte[] bFile = new byte[(int) f.length()];
		try {
			fileInputStream = new FileInputStream(f);
			fileInputStream.read(bFile);
			fileInputStream.close();
		} catch (Exception e) {
			return null;
		}
		return bFile;
	}

}
