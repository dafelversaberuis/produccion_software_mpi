package mpi.beans;

import java.io.Serializable;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import mpi.generales.IConstantes;

public class Personal implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6403513485321497563L;
	private Integer						id;
	private String						nombres;

	private String						apellidos;
	private String						correoElectronico;
	private String						estadoVigencia;
	private String						clave;

	private String						tipoAdministracion;

	private String						tTipoClave;

	private EstructuraTabla		estructuraTabla;

	private boolean						tVenta;
	private boolean						tBascula;
	private boolean						tBascula2;
	private boolean						tBahia;
	private boolean						tParametrosBasculas;
	private boolean						tParametrosBahias;
	private boolean						tConductores;
	private boolean						tClientes;

	private boolean						tParametrosTiempos;
	private boolean						tInformeTrazabilidad;
	private boolean						tInformeCausasRetraso;
	private boolean						tInformeTiempos;
	private boolean						tInformeMonitoreo;

	public Personal() {
		this.estructuraTabla = new EstructuraTabla();

	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("personal");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);

		this.estructuraTabla.getPersistencia().put("nombres", this.nombres);
		this.estructuraTabla.getPersistencia().put("apellidos", this.apellidos);
		this.estructuraTabla.getPersistencia().put("correo_electronico", this.correoElectronico);
		this.estructuraTabla.getPersistencia().put("estado_vigencia", this.estadoVigencia);
		this.estructuraTabla.getPersistencia().put("clave", this.clave);
		this.estructuraTabla.getPersistencia().put("tipo_administracion", this.tipoAdministracion);
		this.estructuraTabla.getPersistencia().put("venta", this.tVenta);
		this.estructuraTabla.getPersistencia().put("bahia", this.tBahia);
		this.estructuraTabla.getPersistencia().put("bascula", this.tBascula);

		this.estructuraTabla.getPersistencia().put("bascula_final", this.tBascula2);
		this.estructuraTabla.getPersistencia().put("clientes", this.tClientes);
		this.estructuraTabla.getPersistencia().put("conductores", this.tConductores);
		this.estructuraTabla.getPersistencia().put("parametros_bahias", this.tParametrosBahias);
		this.estructuraTabla.getPersistencia().put("parametros_basculas", this.tParametrosBasculas);

		this.estructuraTabla.getPersistencia().put("parametros_tiempos", this.tParametrosTiempos);
		this.estructuraTabla.getPersistencia().put("informe_trazabilidad", this.tInformeTrazabilidad);
		this.estructuraTabla.getPersistencia().put("informe_causas_retraso", this.tInformeCausasRetraso);
		this.estructuraTabla.getPersistencia().put("informe_tiempos", this.tInformeTiempos);
		this.estructuraTabla.getPersistencia().put("informe_monitoreo", this.tInformeMonitoreo);

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Size(max = 250, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	@Size(max = 250, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = IConstantes.VALIDACION_EMAIL_INCORRECTO)
	@Size(max = 250, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	@Pattern(regexp = "[AI]", message = IConstantes.VALIDACION_ACTIVO_INACTIVO)
	public String getEstadoVigencia() {
		return estadoVigencia;
	}

	public void setEstadoVigencia(String estadoVigencia) {
		this.estadoVigencia = estadoVigencia;
	}

	@Size(max = 250, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public String gettTipoClave() {
		return tTipoClave;
	}

	public void settTipoClave(String tTipoClave) {
		this.tTipoClave = tTipoClave;
	}

	public String getTipoAdministracion() {
		return tipoAdministracion;
	}

	public void setTipoAdministracion(String tipoAdministracion) {
		this.tipoAdministracion = tipoAdministracion;
	}

	public boolean istVenta() {
		return tVenta;
	}

	public void settVenta(boolean tVenta) {
		this.tVenta = tVenta;
	}

	public boolean istBascula() {
		return tBascula;
	}

	public void settBascula(boolean tBascula) {
		this.tBascula = tBascula;
	}

	public boolean istBahia() {
		return tBahia;
	}

	public void settBahia(boolean tBahia) {
		this.tBahia = tBahia;
	}

	public boolean istBascula2() {
		return tBascula2;
	}

	public void settBascula2(boolean tBascula2) {
		this.tBascula2 = tBascula2;
	}

	public boolean istParametrosBasculas() {
		return tParametrosBasculas;
	}

	public void settParametrosBasculas(boolean tParametrosBasculas) {
		this.tParametrosBasculas = tParametrosBasculas;
	}

	public boolean istParametrosBahias() {
		return tParametrosBahias;
	}

	public void settParametrosBahias(boolean tParametrosBahias) {
		this.tParametrosBahias = tParametrosBahias;
	}

	public boolean istConductores() {
		return tConductores;
	}

	public void settConductores(boolean tConductores) {
		this.tConductores = tConductores;
	}

	public boolean istClientes() {
		return tClientes;
	}

	public void settClientes(boolean tClientes) {
		this.tClientes = tClientes;
	}

	public boolean istParametrosTiempos() {
		return tParametrosTiempos;
	}

	public void settParametrosTiempos(boolean tParametrosTiempos) {
		this.tParametrosTiempos = tParametrosTiempos;
	}

	public boolean istInformeTrazabilidad() {
		return tInformeTrazabilidad;
	}

	public void settInformeTrazabilidad(boolean tInformeTrazabilidad) {
		this.tInformeTrazabilidad = tInformeTrazabilidad;
	}

	public boolean istInformeCausasRetraso() {
		return tInformeCausasRetraso;
	}

	public void settInformeCausasRetraso(boolean tInformeCausasRetraso) {
		this.tInformeCausasRetraso = tInformeCausasRetraso;
	}

	public boolean istInformeTiempos() {
		return tInformeTiempos;
	}

	public void settInformeTiempos(boolean tInformeTiempos) {
		this.tInformeTiempos = tInformeTiempos;
	}

	public boolean istInformeMonitoreo() {
		return tInformeMonitoreo;
	}

	public void settInformeMonitoreo(boolean tInformeMonitoreo) {
		this.tInformeMonitoreo = tInformeMonitoreo;
	}

}
