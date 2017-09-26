package mpi.beans;

import java.io.Serializable;

import javax.validation.constraints.Size;

import mpi.generales.IConstantes;

public class Conductor implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2921166176010168545L;
	private Integer						id;
	private String						nombre;
	private String						documento;
	private String						placa;
	private String						remolque;

	private String						tCopiaDocumento;

	private String						tCodigoTransportador;
	private String						tSucursalTransportador;
	private String						tIdentificacionConductor;
	

	private EstructuraTabla		estructuraTabla;

	public Conductor() {
		this.estructuraTabla = new EstructuraTabla();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("conductores");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);
		this.estructuraTabla.getPersistencia().put("nombre", this.nombre);
		this.estructuraTabla.getPersistencia().put("documento", this.documento);
		this.estructuraTabla.getPersistencia().put("placa", this.placa);
		this.estructuraTabla.getPersistencia().put("remolque", this.remolque);

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Size(max = 50, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Size(max = 25, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	@Size(max = 10, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	@Size(max = 30, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getRemolque() {
		return remolque;
	}

	public void setRemolque(String remolque) {
		this.remolque = remolque;
	}

	public String gettCopiaDocumento() {
		return tCopiaDocumento;
	}

	public void settCopiaDocumento(String tCopiaDocumento) {
		this.tCopiaDocumento = tCopiaDocumento;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public String gettCodigoTransportador() {
		return tCodigoTransportador;
	}

	public void settCodigoTransportador(String tCodigoTransportador) {
		this.tCodigoTransportador = tCodigoTransportador;
	}

	public String gettSucursalTransportador() {
		return tSucursalTransportador;
	}

	public void settSucursalTransportador(String tSucursalTransportador) {
		this.tSucursalTransportador = tSucursalTransportador;
	}

	public String gettIdentificacionConductor() {
		return tIdentificacionConductor;
	}

	public void settIdentificacionConductor(String tIdentificacionConductor) {
		this.tIdentificacionConductor = tIdentificacionConductor;
	}

}
