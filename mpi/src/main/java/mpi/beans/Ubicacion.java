package mpi.beans;

import java.io.Serializable;

public class Ubicacion implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7604155012152980207L;
	private String						idUbicacion;

	public Ubicacion() {

	}

	public String getIdUbicacion() {
		return idUbicacion;
	}

	public void setIdUbicacion(String idUbicacion) {
		this.idUbicacion = idUbicacion;
	}

}
