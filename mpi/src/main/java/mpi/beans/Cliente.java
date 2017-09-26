package mpi.beans;

import java.io.Serializable;

import javax.validation.constraints.Size;

import mpi.generales.IConstantes;

public class Cliente implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2921166176010168545L;
	private Integer						id;
	private String						nombre;
	private String						nit;

	private String						tCopiaNit;

	private EstructuraTabla		estructuraTabla;

	public Cliente() {
		this.estructuraTabla = new EstructuraTabla();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("clientes");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);
		this.estructuraTabla.getPersistencia().put("nombre", this.nombre);
		this.estructuraTabla.getPersistencia().put("nit", this.nit);

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Size(max = 250, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Size(max = 25, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public String gettCopiaNit() {
		return tCopiaNit;
	}

	public void settCopiaNit(String tCopiaNit) {
		this.tCopiaNit = tCopiaNit;
	}

}
