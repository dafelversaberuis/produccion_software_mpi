package soap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import mpi.generales.IConstantes;

public class ConexionWS {

	// private static final String NAMESPACE = "http://generictransfer.com/";
	// private static String URL =
	// "http://192.168.5.4/GTIntegration/ServiciosWeb/wsGenerarPlano.asmx";

	private static final String				NAMESPACE1	= "http://movil.uis.edu.co/";
	private static String							URL2				= "https://www.uis.edu.co/movil-web-serv-bu/WebServicesBeneficios";

	private SoapObject								request			= null;
	private SoapSerializationEnvelope	envelope		= null;

	// Metodo que queremos ejecutar en el servicio web
	private static final String				Metodo			= "ImportarDatosXML";
	// Namespace definido en el servicio web
	private static final String				namespace		= "http://generictransfer.com/";
	// namespace + metodo
	private static final String				accionSoap	= "http://generictransfer.com/ImportarDatosXML";
	// Fichero de definicion del servcio web
	private static final String				url					= "http://192.168.5.4/GTIntegrationReal/ServiciosWeb/wsGenerarPlano.asmx";

	@SuppressWarnings("unchecked")
	public List<String> generarRemision(String aCentroOperacion, Date aFechaPedido, String aNumeroPedido, String aPlaca, String aCodigoTransportador, String aSucursalTransportador, String aCodigoConductor, String aNombreConductor, String aIdentificacionConductor, String aNumeroRemolque, String aNumeroDespacho, String aObra, String aDocuemntoNulo) throws Exception {
		List<String> resultados = new ArrayList<String>();
		String resultado = "";
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyyMMdd");

		try {

			String path = "C:\\inetpub\\wwwroot\\GTIntegrationReal\\Planos\\";
			// Modelo el request
			SoapObject request = new SoapObject(namespace, Metodo);
			// request.addProperty("Param", "valor"); // Paso parametros al WS
			request.addProperty("idDocumento", Integer.parseInt("43142"));
			request.addProperty("strNombreDocumento", "Remision");
			request.addProperty("idCompania", Integer.parseInt("2"));
			request.addProperty("strCompania", "1");
			request.addProperty("strUsuario", "gt");
			request.addProperty("strClave", "gt");

			// Despacho No.: "+aNumeroDespacho+"

			if (aDocuemntoNulo != null && aDocuemntoNulo.equals("C")) {
				aCodigoConductor = "";   
			}

			if (aDocuemntoNulo != null && aDocuemntoNulo.equals("I")) {
				aIdentificacionConductor = "";
			}

			request.addProperty("strFuenteDatos", "<MyDataSet><Remision><centro_operacion>" + aCentroOperacion + "</centro_operacion><fecha_remision>" + formatoFecha.format(aFechaPedido) + "</fecha_remision><numero_pedido>" + aNumeroPedido + "</numero_pedido><CodVehiculo>" + aPlaca + "</CodVehiculo><CodTransportador>" + aCodigoTransportador + "</CodTransportador><SucursalTransportador>" + aSucursalTransportador + "</SucursalTransportador><CodConductor>" + aCodigoConductor + "</CodConductor><NombreConductor>" + aNombreConductor + "</NombreConductor><IdentificacionConductor>" + aIdentificacionConductor + "</IdentificacionConductor><NumeroGuia>" + aNumeroRemolque + "</NumeroGuia><Notas>" + aObra + "</Notas></Remision></MyDataSet>");
			request.addProperty("Path", path);

			// Modelo el Sobre
			SoapSerializationEnvelope sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			sobre.dotNet = true;
			sobre.setOutputSoapObject(request);

			// Modelo el transporte
			HttpTransportSE transporte = new HttpTransportSE(url);

			// Llamada
			transporte.call(accionSoap, sobre);

			// Resultado
			java.util.Vector<SoapObject> rs = (java.util.Vector<SoapObject>) sobre.getResponse();
			// SoapPrimitive cs = (SoapPrimitive) envelope.getResponse();
			if (rs != null && rs.size() > 0) {
				for (int i = 0; i < rs.size(); i++) {
					String valor = "" + rs.elementAt(i);
					if (!valor.contains("Importacion exitosa")) {
						String[] valores = valor.split("</f_detalle>");

						if (valores != null && valores.length > 0) {
							for (String v : valores) {
								if (v.contains("<f_detalle>")) {

									int inicio = v.indexOf("<f_detalle>");
									resultado = v.substring(inicio + 11);
									resultados.add("REVISAR EN SIESA > " + resultado);
								}
							}
						}

						if (!(resultados != null && resultados.size() > 0)) {
							resultados.add("REVISAR EN SIESA > " + valor);
						}

					} else {
						// resultado = valor;
						resultados.add(IConstantes.REMISION_EXITOSA);
						break;

					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return resultados;

	}

	@SuppressWarnings("unchecked")
	public ArrayList<GenericoWS> invocarDepartamentosYMunicipios(Integer codigo, String metodo) throws Exception {
		ArrayList<GenericoWS> codigos = null;
		request = new SoapObject(NAMESPACE1, metodo);
		request.addProperty("codigo", codigo);

		envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		envelope.setOutputSoapObject(request);

		HttpTransportSE transporte = new HttpTransportSE(URL2);

		transporte.call(NAMESPACE1 + metodo, envelope);

		SoapObject response = (SoapObject) envelope.bodyIn;

		int count = response.getPropertyCount();

		if (count == 1) {
			codigos = new ArrayList<GenericoWS>();
			GenericoWS cod = new GenericoWS();
			SoapObject cs = (SoapObject) envelope.getResponse();

			cod.setCodigo(Integer.parseInt(cs.getPropertyAsString("codigo")));
			cod.setNombre(cs.getPropertyAsString("nombre"));
			codigos.add(cod);

		} else {
			if (count >= 2) {

				java.util.Vector<SoapObject> rs = (java.util.Vector<SoapObject>) envelope.getResponse();

				if (rs != null && !rs.toString().equals("anyType{}")) {
					codigos = new ArrayList<GenericoWS>();
					for (SoapObject cs : rs) {
						GenericoWS cod = new GenericoWS();
						cod.setCodigo(Integer.parseInt(cs.getPropertyAsString("codigo")));
						cod.setNombre(cs.getPropertyAsString("nombre"));
						codigos.add(cod);
					}
				}
			}
		}

		return codigos;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {

			// new ConexionWS().invocarDepartamentosYMunicipios(1,
			// "consultarDepartamentos");

			// List<String> resultados = new ConexionWS().generarRemision();
			// if (resultados != null && resultados.size() > 0) {
			// for (String r : resultados) {
			// System.out.println(r);
			// }
			//
			// }

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
