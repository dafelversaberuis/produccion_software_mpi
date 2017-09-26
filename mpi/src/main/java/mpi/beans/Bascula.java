package mpi.beans;

import java.io.Serializable;

import javax.validation.constraints.Size;

import mpi.generales.IConstantes;

public class Bascula implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5045614709694816743L;
	private Integer						id;
	private String						nombre;
	private String						indicativoVigencia;
	private String						permiteTicket;

	private EstructuraTabla		estructuraTabla;

	public Bascula() {
		this.estructuraTabla = new EstructuraTabla();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("basculas");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);
		this.estructuraTabla.getPersistencia().put("nombre", this.nombre);
		this.estructuraTabla.getPersistencia().put("indicativo_vigencia", this.indicativoVigencia);
		this.estructuraTabla.getPersistencia().put("permite_ticket", this.permiteTicket);

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

	public String getIndicativoVigencia() {
		return indicativoVigencia;
	}

	public void setIndicativoVigencia(String indicativoVigencia) {
		this.indicativoVigencia = indicativoVigencia;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public String getPermiteTicket() {
		return permiteTicket;
	}

	public void setPermiteTicket(String permiteTicket) {
		this.permiteTicket = permiteTicket;
	}

}
