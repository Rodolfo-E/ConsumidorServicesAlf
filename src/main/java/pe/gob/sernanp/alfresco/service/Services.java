package pe.gob.sernanp.alfresco.service;

import java.util.Map;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.*;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NoHttpResponseException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import pe.gob.sernanp.alfresco.bean.*;
import pe.gob.sernanp.alfresco.util.Util;

/**
 * Clase que se conecta directamente con los servicios del Gestor Documental
 * 
 * @author Domain Consulting
 * @version 1.0
 */
public class Services {

	private static String HOST;
	private static String PORT;
	private static String USER;
	private static String PASSWORD;
	private static String RUTABASE;
	static HttpMethodRetryHandler myretryhandler;
	private static final String WORKSPACE = "workspace://SpacesStore/";

	/**
	 * Metodo que setear los parametros de conexion a Alfresco
	 * 
	 * @param credenciales Hashmap con los datos de conexion.
	 */
	public static void setConexion(HashMap<String, String> credenciales) {
		HOST = credenciales.get("HOST");
		PORT = credenciales.get("PORT");
		USER = credenciales.get("USER");
		PASSWORD = credenciales.get("PASSWORD");
		RUTABASE = credenciales.get("RUTABASE");
	}

	/**
	 * Metodo que permite configurar el componente Http
	 * 
	 * @param credenciales Hashmap con los datos de conexion.
	 * @param rutaCarpeta  Ruta que incluye las carpetas y subcarpetas que se
	 *                     crearan.
	 * @return Devuelve objeto del tipo RptaBean con Código, Mensaje y en caso de
	 *         exito: UUID de la carpeta.
	 */
	static HttpClient setHttp() {
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
		myretryhandler = new HttpMethodRetryHandler() {
			public boolean retryMethod(final HttpMethod method, final IOException exception, int executionCount) {
				if (executionCount >= 1) {
					return false;
				}
				if (exception instanceof NoHttpResponseException) {
					return true;
				}
				if (!method.isRequestSent()) {
					return true;
				}
				return false;
			}
		};
		return client;
	}

	/**
	 * Metodo que permite la creacion de carpetas o subcarpeta en el Gestor
	 * Documental Alfresco
	 * 
	 * @param credenciales Hashmap con los datos de conexion.
	 * @param rutaCarpeta  Ruta que incluye las carpetas y subcarpetas que se
	 *                     crearan.
	 * @return Devuelve objeto del tipo RptaBean con Código, Mensaje y en caso de
	 *         exito: UUID de la carpeta.
	 */
	public static RptaBean createFolder(HashMap<String, String> credenciales, String rutaCarpeta, String type,
			HashMap<String, String> attribs) {
		setConexion(credenciales);
		int size = 1;
		boolean withtype = false, withattribs = false;
		if (!(attribs == null)) {
			withattribs = true;
			size = attribs.size() + size;
		}
		if (!(type == null)) {
			withtype = true;
			size = size + 1;
		}
		Part[] datos = new Part[size];
		datos[0] = new StringPart("ruta", rutaCarpeta, "utf-8");
		if (withtype) {
			datos[1] = new StringPart("nodetype", type, "utf-8");
		}
		int i = 2;
		if (withattribs) {
			for (Map.Entry<String, String> entry : attribs.entrySet()) {
				datos[i] = new StringPart(entry.getKey(), entry.getValue(), "utf-8");
				i++;
			}
		}
		return conectarScript(datos, "newfolder");
	}

	/**
	 * Metodo que permite la carga/subida de un documento al Gestor Documental
	 * Alfresco
	 * 
	 * @param credenciales   Hashmap con los datos de conexion.
	 * @param fileobj        Array de bytes del documento.
	 * @param filename       Nombre que tendra el documento.
	 * @param filetype       Mimetype del documento.
	 * @param type           Tipo Documental que tendra el documento a cargar.
	 * @param attribs        Hashmap de la metadata que tendra el documento a
	 *                       cargar.
	 * @param rutaRepository Ruta del repositorio donde se cargara el documneto.
	 * @return Devuelve objeto del tipo RptaBean con Código, Mensaje y en caso de
	 *         exito: UUID del documento.
	 */
	public static RptaBean uploadDocument(HashMap<String, String> credenciales, byte[] fileobj, String filename,
			String filetype, String type, HashMap<String, String> attribs, String rutaRepository) {

		RptaBean data = new RptaBean();
		setConexion(credenciales);
		String uploaddirectory = "";
		int size = 3;
		boolean withtype = false, withattribs = false;
		if (!(attribs == null)) {
			withattribs = true;
			size = attribs.size() + size;
		}
		if (!(type == null)) {
			withtype = true;
			size = size + 1;
		}
		if (Util.isBlank(rutaRepository)) {
			uploaddirectory = RUTABASE;
			rutaRepository = "";
		} else {
			uploaddirectory = Util.isBlank(RUTABASE) ? rutaRepository : RUTABASE + "/" + rutaRepository;
			uploaddirectory = uploaddirectory.replace("//", "/");
		}
		data = createFolder(credenciales, uploaddirectory, null, null);
		if (data.getStatus().getCode() == 500) {
			return data;
		}
		Part[] datos = new Part[size];
		datos[0] = new FilePart("filedata", new ByteArrayPartSource(filename, fileobj), filetype, "utf-8");
		datos[1] = new StringPart("filename", filename, "utf-8");
		datos[2] = new StringPart("uploaddirectory", uploaddirectory, "utf-8");
		if (withtype) {
			datos[3] = new StringPart("nodetype", type, "utf-8");
		}
		int i = 4;
		if (withattribs) {
			for (Map.Entry<String, String> entry : attribs.entrySet()) {
				datos[i] = new StringPart(entry.getKey(), entry.getValue(), "utf-8");
				i++;
			}
		}
		return conectarScript(datos, "upload");
	}

	/**
	 * Metodo que permite la eliminacion de un documento del Gestor Documental
	 * Alfresco
	 * 
	 * @param credenciales Hashmap con los datos de conexion.
	 * @param uuid         Identificador unico del documento que se desea eliminar.
	 * @return Devuelve objeto del tipo RptaBean con Código y Mensaje de la
	 *         operacion.
	 */
	public static RptaBean deleteDocument(HashMap<String, String> credenciales, String uuid) {
		setConexion(credenciales);
		String referencia = WORKSPACE + uuid;
		Part[] parts = { new StringPart("referencia", referencia, "utf-8") };
		return conectarScript(parts, "deleteDocument");
	}

	/**
	 * Metodo que permite la descarga de un documento del Gestor Documental Alfresco
	 * 
	 * @param credenciales Hashmap con los datos de conexion.
	 * @param uuid         Identificador unico del documento que se desea descargar.
	 * @return Devuelve objeto del tipo RptaBean con Código, Mensaje y en caso de
	 *         exito: Nombre del archivo (fileName), bytes del documento (content),
	 *         tipo de documento (mimetype)`.
	 */
	public static RptaBean downloadDocument(HashMap<String, String> credenciales, String uuid) {
		setConexion(credenciales);
		RptaBean resultado = new RptaBean();
		String referencia = null;
		referencia = WORKSPACE + uuid;
		// RptaLoginBean login = login();
		Part[] parts = { new StringPart("referencia", referencia, "utf-8") };

		resultado = conectarScript(parts, "getProperties");

		if (resultado.getStatus().getCode() == 200) {
			try {
				resultado.setContent(getContent(uuid));
			} catch (IOException ex) {
				resultado = Util.setErrorRpta("E1000", ex.getMessage());
			}
		}
		return resultado;
	}

	/**
	 * Metodo que permite la actualizacion de un documento del Gestor Documental
	 * Alfresco
	 * 
	 * @param credenciales Hashmap con los datos de conexion.
	 * @param uuid         Identificador unico del documento que se desea
	 *                     actualizar.
	 * @param fileobj      Array de bytes del documento.
	 * @param fileName     Nombre con el cual se actualizara el documento.
	 * @param attribs      Hashmap de la metadata que tendra el documento a
	 *                     actualizar.
	 * @return Devuelve objeto del tipo RptaBean con Código y Mensaje de la
	 *         operacion.
	 */
	public static RptaBean updateDocument(HashMap<String, String> credenciales, String uuid, String fileName,
			byte[] fileobj, HashMap<String, String> attribs) {
		setConexion(credenciales);
		String referencia = WORKSPACE + uuid;
		int size = 3;
		boolean withattribs = false;
		if (!(attribs == null)) {
			withattribs = true;
			size = attribs.size() + size;
		}
		Part[] datos = new Part[size];
		datos[0] = new StringPart("referencia", referencia, "utf-8");
		datos[1] = new FilePart("filedata", new ByteArrayPartSource(fileName, fileobj), "cm:content", null);
		datos[2] = new StringPart("filename", fileName, "utf-8");
		int i = 3;
		if (withattribs) {
			for (Map.Entry<String, String> entry : attribs.entrySet()) {
				datos[i] = new StringPart(entry.getKey(), entry.getValue(), "utf-8");
				i++;
			}
		}
		return conectarScript(datos, "update");
	}

	/**
	 * Metodo que permite obtener los bytes de un documento a traves de su uuid.
	 * 
	 * @param uuid Hashmap con los datos de conexion.
	 * @return Devuelve el array de bytes del documento consultado.
	 */
	static byte[] getContent(String uuid) throws HttpException, IOException {
		HttpClient client = setHttp();
		byte[] bFile = null;
		RptaLoginBean login = new RptaLoginBean();
		login = login();
		if (login.getStatus() == HttpStatus.SC_OK) {
			String apiurl = "http://" + HOST + ":" + PORT
					+ "/alfresco/service/api/node/content;cm:content/workspace/SpacesStore/" + uuid + "?alf_ticket="
					+ login.getData();
			GetMethod get = new GetMethod(apiurl);
			get.setDoAuthentication(true);
			get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, myretryhandler);
			get.setRequestHeader("Content-Type", "application/json");
			client.executeMethod(get);
			bFile = get.getResponseBody();
		}
		return bFile;
	}

	/**
	 * Metodo que permite la busqueda de un documento del Gestor Documental Alfresco
	 * 
	 * @param credenciales   Hashmap con los datos de conexion.
	 * @param rutaSubCarpeta Ruta especifica donde se realizara la busqueda.
	 * @param words          Palabra o palabras por la que se buscara.
	 * @param phrase         Frase a buscar.
	 * @param toIgnore       Palabra a ignorar.
	 * @param customQuery    Query personalizada para buscar.
	 * @param nodeType       Tipo documental a buscar.
	 * @param attributes     Hashmap de la metadata que tendrá el documento a
	 *                       cargar.
	 * @return Devuelve objeto del tipo RptaBean con Código, Mensaje y en caso de
	 *         exito: Lista de BeanFile (nodes) que contiene por cada documento
	 *         encontrado: nombre (archivo), uuid (uuid) y propiedades (properties)
	 */
	public static RptaBean searchDocument(HashMap<String, String> credenciales, String rutaSubCarpeta, String words,
			String phrase, String toIgnore, String customQuery, String nodeType, HashMap<String, String> attributes) {

		setConexion(credenciales);
		String query = Util.generarQuery(rutaSubCarpeta, words, phrase, toIgnore, customQuery, nodeType, attributes);
		Part[] parts = { new StringPart("query", query, "utf-8") };
		return conectarScript(parts, "searchContent");
	}

	/**
	 * Metodo que iniciar sesion en Alfresco y generar un ticket de autenticacion
	 * 
	 * @return Devuelve objeto del tipo RptaLoginBean con Código y Mensaje de la
	 *         operacion. En caso de exito, el ticket de autenticacion.
	 */
	public static RptaLoginBean login() {

		RptaLoginBean response = new RptaLoginBean();

		if (Util.isBlank(HOST) || Util.isBlank(PORT)) {
			response = (RptaLoginBean) Util.setErrorRptaLoginBean(response, 400, Util.getMessage("E1001"), null, null,
					null, "E1001");
			return response;
		}
		if (Util.isBlank(USER) || Util.isBlank(PASSWORD)) {
			response = (RptaLoginBean) Util.setErrorRptaLoginBean(response, 400, Util.getMessage("E1003"), null, null,
					null, "E1003");
			return response;
		}

		try {
			HttpClient client = new HttpClient();
			PostMethod mPost = new PostMethod("http://" + HOST + ":" + PORT + "/alfresco/s/api/login");
			String jsonDataObject = "{\"username\":\"" + USER + "\",\"password\":\"" + PASSWORD + "\"}";
			mPost.setDoAuthentication(true);
			mPost.setRequestHeader("Content-Type", "application/json");
			mPost.setRequestEntity(new StringRequestEntity(jsonDataObject, "application/json", "UTF-8"));
			client.executeMethod(mPost);
			response.setStatus(client.executeMethod(mPost));
			if (response.getStatus() != HttpStatus.SC_OK) {
				response = (RptaLoginBean) Util.setErrorRptaLoginBean(response, 400, Util.getMessage("E1002"), null,
						null, null, "E1002");
				return response;
			} else {
				response.setCode("00000");
				response.setMessage(Util.getMessage("00000"));
			}
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = mPost.getResponseBodyAsStream().read(buffer)) != -1) {
				result.write(buffer, 0, length);
			}

			response.setData(result.toString("UTF-8"));
			response.setData(response.getData().substring(response.getData().indexOf("ticket") + 9,
					response.getData().length() - 6));
		} catch (IOException e) {
			e.printStackTrace();
			response = (RptaLoginBean) Util.setErrorRptaLoginBean(response, 400, Util.getMessage("E1000"), null, null,
					null, "E1000");
			return response;
		}

		return response;
	}

	/**
	 * Metodo que permite la eliminacion de una carpeta del Gestor Documental
	 * Alfresco
	 * 
	 * @param credenciales Hashmap con los datos de conexion.
	 * @param rutaCarpeta  Ruta completa de la carpeta que se desea eliminar.
	 * @param uuid         Identificador unico del documento que se desea eliminar.
	 * @param forceDelete  Flag que indica si se eliminara la carpeta aún cuando
	 *                     tenga contenido dentro.
	 * @return Devuelve objeto del tipo RptaBean con Código y Mensaje de la
	 *         operacion.
	 */
	public static RptaBean deleteFolder(HashMap<String, String> credenciales, String rutaCarpeta, String uuid,
			String forceDelete) {
		setConexion(credenciales);
		Part[] parts = { new StringPart("ruta", rutaCarpeta, "utf-8"), new StringPart("referencia", uuid, "utf-8"),
				new StringPart("force", forceDelete, "utf-8") };
		return conectarScript(parts, "deletefolder");
	}

	/**
	 * Metodo que permite conectar a un webscript del Gestor Documental Alfresco
	 * 
	 * @param parts     Datos de entrada al webscript.
	 * @param webscript Identificador del webscript.
	 * @return Devuelve objeto del tipo RptaBean con Código y Mensaje de la
	 *         operacion.
	 */
	public static RptaBean conectarScript(Part[] parts, String webscript) {
		RptaLoginBean login = new RptaLoginBean();
		HttpClient client = setHttp();
		PostMethod mPost = null;
		RptaBean resultado = new RptaBean();
		String cadena = "";
		try {

			login = login();
			if (login.getStatus() == HttpStatus.SC_OK) {
				if (PORT.equals("")) {
					mPost = new PostMethod(
							"http://" + HOST + "/alfresco/service/" + webscript + "?alf_ticket=" + login.getData());
				} else {
					mPost = new PostMethod("http://" + HOST + ":" + PORT + "/alfresco/service/" + webscript
							+ "?alf_ticket=" + login.getData());
				}
				mPost.setRequestEntity(new MultipartRequestEntity(parts, mPost.getParams()));

				// mPost.setContentEncoding("UTF-8");
				mPost.getParams().setContentCharset("UTF-8");

				mPost.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, myretryhandler);
				client.executeMethod(mPost);
				ByteArrayOutputStream result = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int length;
				while ((length = mPost.getResponseBodyAsStream().read(buffer)) != -1) {
					result.write(buffer, 0, length);
				}
				cadena = result.toString("UTF-8");
				result.close();
				
				try {
					resultado = new Gson().fromJson(cadena, RptaBean.class);
					if (resultado.getStatus().getCode() == 500) {
						resultado.setCode(Util.formatError(resultado.getMessage()));
						resultado.setException(resultado.getMessage());
						resultado.setMessage(Util.getMessage(resultado.getCode()));
					}
				} catch (Exception e) {
					System.out.println("ERROR: " + cadena);
					e.printStackTrace();
				}
			} else {
				resultado = Util.setErrorRpta(login.getCode(), null);
			}
		} catch (HttpException e) {
			resultado = Util.setErrorRpta("E1000", e.getMessage());
		} catch (IOException e) {
			resultado = Util.setErrorRpta("E1000", e.getMessage());
		}
		return resultado;
	}

	/**
	 * Metodo que permite los documentos de una carpeta del Gestor Documental
	 * Alfresco
	 * 
	 * @param credenciales Hashmap con los datos de conexion.
	 * @param rutaCarpeta  Ruta completa de la carpeta que se desea eliminar.
	 * @return Devuelve objeto del tipo RptaBean con Código y Mensaje de la
	 *         operacion.
	 */
	public static RptaBean listDocuments(HashMap<String, String> credenciales, String rutaCarpeta) {
		setConexion(credenciales);
		Part[] parts = { new StringPart("ruta", rutaCarpeta, "utf-8") };
		return conectarScript(parts, "listDocuments");
	}

	/**
	 * Metodo que permite comprimir una carpeta y descargarla del Gestor Documental
	 * Alfresco
	 * 
	 * @param credenciales Hashmap con los datos de conexion.
	 * @param uuid         Identificador unico del documento que se desea eliminar.
	 * @return Devuelve objeto del tipo RptaBean con Código y Mensaje de la
	 *         operacion.
	 */
	public static RptaBean downloadZip(HashMap<String, String> credenciales, String uuid, String filename) {
		setConexion(credenciales);
		RptaBean rpta = new RptaBean();
		HttpClient client = setHttp();
		RptaLoginBean login = new RptaLoginBean();
		login = login();
		if (login.getStatus() == HttpStatus.SC_OK) {
			String apiurl = "http://" + HOST + ":" + PORT
					+ "/alfresco/service/downloadZip?nodes=" + uuid + "&filename=" + filename
					+ "&alf_ticket=" + login.getData();
			GetMethod get = new GetMethod(apiurl);
			get.setDoAuthentication(true);
			get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, myretryhandler);
			get.setRequestHeader("Content-Type", "application/json");
			try {
				client.executeMethod(get);
				rpta.setContent(get.getResponseBody());
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return rpta;
	}

//	public static byte[] downloadZip(HashMap<String, String> credenciales, String uuid, String fileName) {
//		HttpClient client = setHttp();
//		byte[] bFile = null;
//		RptaLoginBean login = new RptaLoginBean();
//		String referencia = uuid;
//		login = login();
//		if (login.getStatus() == HttpStatus.SC_OK) {
//			String apiurl = "http://"
//					+ HOST
//					+ ":"
//					+ PORT
//					+ "/alfresco/service/downloadZip?alf_ticket=" + login.getData();
//			PostMethod mPost = new PostMethod(apiurl);
//			mPost.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
//					myretryhandler);
//			// mPost.setContentEncoding("UTF-8");
//			mPost.getParams().setContentCharset("UTF-8");
//		
//			Part[] datos = new Part[2];
//			datos[0] = new StringPart("node", referencia, "utf-8");
//			datos[1] = new StringPart("filename", fileName, "utf-8");
//			
//			mPost.setRequestEntity(new MultipartRequestEntity(datos, mPost
//					.getParams()));
//			
//			try {
//				client.executeMethod(mPost);
//				bFile = mPost.getResponseBody();
//			} catch (HttpException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			
//		}
//		return bFile;
//	}

}
