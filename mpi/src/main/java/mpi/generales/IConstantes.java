package mpi.generales;

import org.apache.log4j.Logger;

public interface IConstantes {

	// manejo de logs
	public static final Logger	log																			= Logger.getLogger("mpi");

	// validaciones anotaciones beans

	public static Integer				CONSEGUTIVO_SIGUIENTE_NUMERO_ENTREGA_V2	= 577;
	public static Integer				CONSEGUTIVO_DESDE_NUEVO_REPORTE					= 577;
	public static Integer				DIGITOS_NUMERO_ENTREGA									= 6;

	public static String				NOMBRE_SOFTWARE													= "Software - MPI despacho";
	public static String				VALIDACION_MAXIMA_LONGITUD							= "Máximo {max} caracteres";
	public static String				VALIDACION_MINIMA_LONGITUD							= "Mínimo {min} caracteres";
	public static String				VALIDACION_MAXIMO_ENTERO								= "Máximo {value}";
	public static String				VALIDACION_MINIMO_ENTERO								= "Mínimo {value}";
	public static String				VALIDACION_VACIO												= "No se permite sólo vacío";

	public static String				VALIDACION_ACTIVO_INACTIVO							= "Se debe especificar si está activo o inactivo";
	public static String				VALIDACION_SI_NO												= "Se debe especificar SI ó NO";
	public static String				VALIDACION_MAXIMO_DECIMAL								= "Máximo un número de {integer} digitos enteros y {fraction} decimales";
	public static String				VALIDACION_EMAIL_INCORRECTO							= "El formato del correo electrónico es incorrecto";
	public static String				VALIDACION_MONEDA												= "Sólo pesos colombianos ó dólares";
	public static Integer				NUMERO_DIGITOS_CLAVE_ALEATORIA					= 6;

	public static String				AFIRMACION															= "S";
	public static String				NEGACION																= "N";
	public static String				ACTIVO																	= "A";
	public static String				INACTIVO																= "I";
	public static String				PESO_COLOMBIANO													= "COP";
	public static String				DOLAR																		= "US";
	public static String				IDIOMA_ESPANOL													= "ES";
	public static String				TRANSACCION_APROBADA										= "A";
	public static String				TRANSACCION_PENDIENTE										= "P";
	public static String				ADMINISTRACION_TOTAL										= "T";
	public static String				ADMINISTRACION_PARCIAL									= "P";
	public static String				TIPO_MEDIDOR														= "M";
	public static String				NO_APLICA																= "N";

	public static Integer				MAXIMOS_REMITENTES_CORREO								= 100;

	public static final String	SMTP_HOST_NAME													= "smtp.gmail.com";
	public static final int			SMTP_HOST_PORT													= 465;
	public static final String	SMTP_AUTH_USER													= "quimerapps@gmail.com";
	public static final String	SMTP_AUTH_PWD														= "mariacamila12";

	public static String				ID_USUARIO_SESION												= "dafelver";

	public static final String	PAQUETE_MODULO_REPORTES									= "/reportes/";
	public static final String	PAQUETE_IMAGENES												= "/imagenes/";
	public static final String	LOGO1																		= "mpi.png";
	public static final String	LOGO2																		= "calidad.png";
	public static final String	LOGO5																		= "calidad2.png";

	public static final String	LOGO3																		= "logo_mpi_medidor.png";
	public static final String	LOGO4																		= "logo_incontec_medidor.png";

	// "/arbolitos/adopcionCliente/administrarCuenta.xhtml?faces-redirect=true";
	public static String				PAGINA_NO_LOGUEO												= "/index.xhtml?faces-redirect=true";
	public static String				PAGINA_HOME															= "/home.xhtml?faces-redirect=true";

	public static final String	NOMBRE_REPORTE													= "TICKET";
	public static final String	REPORTE																	= "imprimirTicket.jasper";

	public static final String	CREO_VENTA															= "CREO VENTA";
	public static final String	ACTUALIZO_VENTA													= "ACTUALIZÓ VENTA";
	public static final String	ACTUALIZO_BASCULA_I											= "ACTUALIZÓ BÁSCULA PESO INICIAL";
	public static final String	ACTUALIZO_BASCULA_F											= "ACTUALIZÓ BÁSCULA PESO FINAL";
	public static final String	ACTUALIZO_BAHIA													= "ACTUALIZÓ BAHÍA";
	public static final String	GENERO_REMISION													= "GENERÓ REMISIÓN";
	public static final String	ANULO_REMISION													= "ANULÓ REMISIÓN";
	public static final String	GENERO_TICKET														= "GUARDÓ OBSERVACIONES-TICKET";
	public static final String	REMISION_EXITOSA												= "Remisión realizada con éxito";

}
