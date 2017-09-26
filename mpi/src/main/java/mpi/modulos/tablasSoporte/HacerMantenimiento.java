package mpi.modulos.tablasSoporte;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import mpi.Conexion;
import mpi.beans.Bahia;
import mpi.beans.Bascula;
import mpi.beans.TiempoEstandar;
import mpi.generales.ConsultarFuncionesAPI;
import mpi.generales.IConstantes;
import mpi.modulos.IConsultasDAO;

@ManagedBean
@ViewScoped
public class HacerMantenimiento extends ConsultarFuncionesAPI implements Serializable {

	/**
	 * 
	 */
	private static final long			serialVersionUID	= 5700817004191801685L;

	private TiempoEstandar				tiempo;
	private TiempoEstandar				tiempoTransaccion;
	private Bahia									bahia;
	private Bahia									bahiaTransaccion;
	private Bascula								bascula;
	private Bascula								basculaTransaccion;
	private List<TiempoEstandar>	tiempos;
	private List<Bahia>						bahias;
	private List<Bascula>					basculas;

	// privados

	/**
	 * Valida si cumple las condiciones de bascula para ser guardado o editado
	 * 
	 * @param aTransaccion
	 * @return ok
	 */
	private boolean isOkBascula(String aTransaccion) {
		boolean ok = true;

		if (aTransaccion.equals("C")) {
			if (this.isVacio(this.bascula.getNombre())) {
				ok = false;
				this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
			} else {
				this.bascula.setNombre(this.bascula.getNombre().trim());
			}

		} else {
			if (this.isVacio(this.basculaTransaccion.getNombre())) {
				ok = false;
				this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
			} else {
				this.basculaTransaccion.setNombre(this.basculaTransaccion.getNombre().trim());
			}

		}

		return ok;

	}

	/**
	 * Valida si cumple las condiciones de bahia para ser guardado o editado
	 * 
	 * 
	 * @return ok
	 */
	private boolean isOkTiempo() {
		boolean ok = true; 
		boolean ok2 = true;

		if (this.tiempo.getSegundaBahia() != null && this.tiempo.getSegundaBahia().getId() != null) {
			if (this.tiempo.getSegundaBahia().getId().intValue() == this.tiempo.getPrimeraBahia().getId().intValue()) {
				ok = false;
				this.mostrarMensajeGlobalPersonalizado("La segunda bahía debe ser diferente a la primera", "advertencia");

			} else {

				if (this.tiempos != null && this.tiempos.size() > 0) {
					if (this.tiempos.stream().anyMatch(p -> p.getPrimeraBahia().getId().intValue() == this.tiempo.getSegundaBahia().getId().intValue() && p.getTipoDespacho().equals(this.tiempo.getTipoDespacho()))) {
						ok2 = false;
					}

					if (this.tiempos.stream().anyMatch(p -> p.getSegundaBahia() != null && p.getSegundaBahia().getId() != null && p.getSegundaBahia().getId().intValue() == this.tiempo.getSegundaBahia().getId().intValue() && p.getTipoDespacho().equals(this.tiempo.getTipoDespacho()))) {
						ok2 = false;
					}
				}

			}

		}

		if (this.tiempos != null && this.tiempos.size() > 0) {
			if (this.tiempos.stream().anyMatch(p -> p.getPrimeraBahia().getId().intValue() == this.tiempo.getPrimeraBahia().getId().intValue() && p.getTipoDespacho().equals(this.tiempo.getTipoDespacho()))) {
				ok2 = false;
			}

			if (this.tiempos.stream().anyMatch(p -> p.getSegundaBahia() != null && p.getSegundaBahia().getId() != null && p.getSegundaBahia().getId().intValue() == this.tiempo.getPrimeraBahia().getId().intValue() && p.getTipoDespacho().equals(this.tiempo.getTipoDespacho()))) {
				ok2 = false;
			}
		}

		if (!ok2) {
			ok = false;
			this.mostrarMensajeGlobalPersonalizado("La(s) bahías a asignar en esta transacción no pueden estar en otro registro ni como primera ni segunda bahía, si ésta posee el mismo tipo de despacho. Revise", "advertencia");
		}

		return ok;

	}

	private boolean isOkBahia(String aTransaccion) {
		boolean ok = true;

		if (aTransaccion.equals("C")) {
			if (this.isVacio(this.bahia.getNombre())) {
				ok = false;
				this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
			} else {
				this.bahia.setNombre(this.bahia.getNombre().trim());
			}

		} else {
			if (this.isVacio(this.bahiaTransaccion.getNombre())) {
				ok = false;
				this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
			} else {
				this.bahiaTransaccion.setNombre(this.bahiaTransaccion.getNombre().trim());
			}

		}

		return ok;

	}

	// publicos

	/**
	 * Este método asigna un objeto del listado de basculas para relizarle
	 * operaciones distintas
	 * 
	 * @param aBascula
	 * @param aVista
	 */
	public void asignarBascula(Bascula aBascula, String aVista) {

		try {

			this.basculaTransaccion = aBascula;

			if (aVista != null && aVista.equals("MODAL_EDITAR")) {
				this.abrirModal("panelEdicion");

			} else {

				this.abrirModal("panelEliminacion");

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	public void asignarTiempo(TiempoEstandar aTiempo, String aVista) {

		try {

			this.tiempoTransaccion = aTiempo;

			if (aVista != null && aVista.equals("MODAL_EDITAR")) {
				this.abrirModal("panelEdicion");

			} else {

				this.abrirModal("panelEliminacion");

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Este método asigna un objeto del listado de bahias para relizarle
	 * operaciones distintas
	 * 
	 * @param aBahia
	 * @param aVista
	 */
	public void asignarBahia(Bahia aBahia, String aVista) {

		try {

			this.bahiaTransaccion = aBahia;

			if (aVista != null && aVista.equals("MODAL_EDITAR")) {
				this.abrirModal("panelEdicion");

			} else {

				this.abrirModal("panelEliminacion");

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Cancela la edición de una báscula en transacción
	 * 
	 * @param aVista
	 */
	public void cancelarBasculaTransaccion(String aVista) {
		try {

			this.basculaTransaccion = null;
			this.getBasculaTransaccion();
			this.basculas = null;
			this.getBasculas();

			if (aVista != null && aVista.equals("MODAL_EDITAR")) {
				this.cerrarModal("panelEdicion");

			} else if (aVista != null && aVista.equals("MODAL_ELIMINAR")) {
				this.cerrarModal("panelEliminacion");

			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	public void cancelarTiempoTransaccion(String aVista) {
		try {

			this.tiempoTransaccion = null;
			this.getTiempoTransaccion();
			this.tiempos = null;
			this.getTiempos();

			if (aVista != null && aVista.equals("MODAL_EDITAR")) {
				this.cerrarModal("panelEdicion");

			} else if (aVista != null && aVista.equals("MODAL_ELIMINAR")) {
				this.cerrarModal("panelEliminacion");

			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	/**
	 * Cancela la edición o eliminación de una bahia y la vuelve su estado
	 * original
	 * 
	 * @param aVista
	 */
	public void cancelarBahiaTransaccion(String aVista) {
		try {

			this.bahiaTransaccion = null;
			this.getBahiaTransaccion();
			this.bahias = null;
			this.getBahias();

			if (aVista != null && aVista.equals("MODAL_EDITAR")) {
				this.cerrarModal("panelEdicion");

			} else if (aVista != null && aVista.equals("MODAL_ELIMINAR")) {
				this.cerrarModal("panelEliminacion");

			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	/**
	 * Elimina un registro de bascula
	 */
	public void eliminarBascula() {

		Conexion conexion = new Conexion();
		try {

			conexion.setAutoCommitBD(false);
			this.basculaTransaccion.getCamposBD();
			conexion.eliminarBD(this.basculaTransaccion.getEstructuraTabla().getTabla(), this.basculaTransaccion.getEstructuraTabla().getLlavePrimaria());
			conexion.commitBD();
			this.mostrarMensajeGlobal("eliminacionExitosa", "exito");

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");

		} finally {
			conexion.cerrarConexion();
		}

		// reseteo de variables
		this.basculaTransaccion = null;
		this.getBasculaTransaccion();
		this.basculas = null;
		this.getBasculas();

	}

	/**
	 * Elimina un registro de bahia
	 */
	public void eliminarBahia() {

		Conexion conexion = new Conexion();
		try {

			conexion.setAutoCommitBD(false);
			this.bahiaTransaccion.getCamposBD();
			conexion.eliminarBD(this.bahiaTransaccion.getEstructuraTabla().getTabla(), this.bahiaTransaccion.getEstructuraTabla().getLlavePrimaria());
			conexion.commitBD();
			this.mostrarMensajeGlobal("eliminacionExitosa", "exito");

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");

		} finally {
			conexion.cerrarConexion();
		}

		// reseteo de variables
		this.bahiaTransaccion = null;
		this.getBahiaTransaccion();
		this.bahias = null;
		this.getBahias();

	}

	/**
	 * 
	 */
	public void eliminarTiempo() {

		Conexion conexion = new Conexion();
		try {

			conexion.setAutoCommitBD(false);
			this.tiempoTransaccion.getCamposBD();
			conexion.eliminarBD(this.tiempoTransaccion.getEstructuraTabla().getTabla(), this.tiempoTransaccion.getEstructuraTabla().getLlavePrimaria());
			conexion.commitBD();
			this.mostrarMensajeGlobal("eliminacionExitosa", "exito");

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");

		} finally {
			conexion.cerrarConexion();
		}

		// reseteo de variables
		this.tiempoTransaccion = null;
		this.getTiempoTransaccion();
		this.tiempos = null;
		this.getTiempos();

	}

	/**
	 * Edita un registro de bascula
	 */
	public void editarBascula() {
		Conexion conexion = new Conexion();

		try {
			if (isOkBascula("E")) {

				conexion.setAutoCommitBD(false);
				this.basculaTransaccion.getCamposBD();
				conexion.actualizarBD(this.basculaTransaccion.getEstructuraTabla().getTabla(), this.basculaTransaccion.getEstructuraTabla().getPersistencia(), this.basculaTransaccion.getEstructuraTabla().getLlavePrimaria(), null);
				conexion.commitBD();

				this.mostrarMensajeGlobal("edicionExitosa", "exito");
				this.cerrarModal("panelEdicion");

				// reseteo de variables
				this.basculas = null;
				this.getBasculas();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Edita un registro de bahia
	 */
	public void editarBahia() {
		Conexion conexion = new Conexion();

		try {
			if (isOkBahia("E")) {

				conexion.setAutoCommitBD(false);
				this.bahiaTransaccion.getCamposBD();
				conexion.actualizarBD(this.bahiaTransaccion.getEstructuraTabla().getTabla(), this.bahiaTransaccion.getEstructuraTabla().getPersistencia(), this.bahiaTransaccion.getEstructuraTabla().getLlavePrimaria(), null);
				conexion.commitBD();

				this.mostrarMensajeGlobal("edicionExitosa", "exito");
				this.cerrarModal("panelEdicion");

				// reseteo de variables
				this.bahias = null;
				this.getBahias();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	public void editarTiempo() {
		Conexion conexion = new Conexion();

		try {

			conexion.setAutoCommitBD(false);
			this.tiempoTransaccion.getCamposBD();
			conexion.actualizarBD(this.tiempoTransaccion.getEstructuraTabla().getTabla(), this.tiempoTransaccion.getEstructuraTabla().getPersistencia(), this.tiempoTransaccion.getEstructuraTabla().getLlavePrimaria(), null);
			conexion.commitBD();

			this.mostrarMensajeGlobal("edicionExitosa", "exito");
			this.cerrarModal("panelEdicion");

			// reseteo de variables
			this.tiempos = null;
			this.getTiempos();

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Crea un registro de báscula
	 */
	public void crearBascula() {
		Conexion conexion = new Conexion();

		try {
			if (isOkBascula("C")) {
				conexion.setAutoCommitBD(false);

				this.bascula.setIndicativoVigencia(IConstantes.ACTIVO);

				this.bascula.getCamposBD();
				conexion.insertarBD(this.bascula.getEstructuraTabla().getTabla(), this.bascula.getEstructuraTabla().getPersistencia());
				conexion.commitBD();

				this.mostrarMensajeGlobal("creacionExitosa", "exito");

				// reseteo de variables
				this.bascula = null;
				this.getBascula();
				this.basculas = null;
				this.getBasculas();

			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	public void crearTiempo() {
		Conexion conexion = new Conexion();

		try {
			if (isOkTiempo()) {
				conexion.setAutoCommitBD(false);

				this.tiempo.getCamposBD();
				conexion.insertarBD(this.tiempo.getEstructuraTabla().getTabla(), this.tiempo.getEstructuraTabla().getPersistencia());
				conexion.commitBD();

				this.mostrarMensajeGlobal("creacionExitosa", "exito");

				// reseteo de variables
				this.tiempo = null;
				this.getTiempo();
				this.tiempos = null;
				this.getTiempos();

			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Crea un nuevo registro de bahia
	 */
	public void crearBahia() {
		Conexion conexion = new Conexion();

		try {
			if (isOkBahia("C")) {
				conexion.setAutoCommitBD(false);

				this.bahia.setIndicativoVigencia(IConstantes.ACTIVO);

				this.bahia.getCamposBD();
				conexion.insertarBD(this.bahia.getEstructuraTabla().getTabla(), this.bahia.getEstructuraTabla().getPersistencia());
				conexion.commitBD();

				this.mostrarMensajeGlobal("creacionExitosa", "exito");

				// reseteo de variables
				this.bahia = null;
				this.getBahia();
				this.bahias = null;
				this.getBahias();

			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Cancela la creación de una báscula
	 */
	public void cancelarBascula() {

		try {
			this.bascula = null;
			this.getBascula();
			this.basculas = null;
			this.getBasculas();

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Este método borra el formulario de creación de una bahia
	 */
	public void cancelarBahia() {

		try {
			this.bahia = null;
			this.getBahia();
			this.bahias = null;
			this.getBahias();

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	public void cancelarTiempo() {

		try {
			this.tiempo = null;
			this.getTiempo();
			this.tiempos = null;
			this.getTiempos();

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	// listados y etructuras

	// gets/sets

	/**
	 * Este método lo usamos para obtener el objeto que crea la bahia oral
	 * 
	 * @return bahia
	 */
	public Bahia getBahia() {
		try {
			if (this.bahia == null) {
				this.bahia = new Bahia();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return bahia;
	}

	/**
	 * Metodo para asignar la bahia
	 * 
	 * @param bahia
	 */
	public void setBahia(Bahia bahia) {
		this.bahia = bahia;
	}

	/**
	 * Obtiene la bahia para realizarle operaciones
	 * 
	 * @return bahiaTransaccion
	 */
	public Bahia getBahiaTransaccion() {
		try {

			if (this.bahiaTransaccion == null) {
				this.bahiaTransaccion = new Bahia();
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return bahiaTransaccion;
	}

	/**
	 * Establece una bahia para realizarle operaciones
	 * 
	 * @param bahiaTransaccion
	 */
	public void setBahiaTransaccion(Bahia bahiaTransaccion) {
		this.bahiaTransaccion = bahiaTransaccion;
	}

	/**
	 * Obtiene un listado de higienes orales
	 * 
	 * @return bahias
	 */
	public List<Bahia> getBahias() {
		try {
			this.bahias = IConsultasDAO.getBahias(null);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return bahias;
	}

	/**
	 * Establece un listado de basculas
	 * 
	 * @param bahias
	 */
	public void setBahias(List<Bahia> bahias) {
		this.bahias = bahias;
	}

	/**
	 * Obtiene una bascula poara crear
	 * 
	 * @return bascula
	 */
	public Bascula getBascula() {
		try {
			if (this.bascula == null) {
				this.bascula = new Bascula();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return bascula;
	}

	/**
	 * Establece una bascula a crear
	 * 
	 * @param bascula
	 */
	public void setBascula(Bascula bascula) {
		this.bascula = bascula;
	}

	/**
	 * Obtiene un tiempo estáqndar
	 * 
	 * @return
	 */
	public TiempoEstandar getTiempo() {
		try {
			if (this.tiempo == null) {
				this.tiempo = new TiempoEstandar();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return tiempo;
	}

	public void setTiempo(TiempoEstandar tiempo) {
		this.tiempo = tiempo;
	}

	public TiempoEstandar getTiempoTransaccion() {
		try {
			if (this.tiempoTransaccion == null) {
				this.tiempoTransaccion = new TiempoEstandar();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return tiempoTransaccion;
	}

	public void setTiempoTransaccion(TiempoEstandar tiempoTransaccion) {
		this.tiempoTransaccion = tiempoTransaccion;
	}

	/**
	 * Obtiene una bascula para realixar transacciones
	 * 
	 * @return basculaTransaccion
	 */
	public Bascula getBasculaTransaccion() {
		try {

			if (this.basculaTransaccion == null) {
				this.basculaTransaccion = new Bascula();
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return basculaTransaccion;
	}

	/**
	 * Establece una bascula para realziar transacciones
	 * 
	 * @param basculaTransaccion
	 */
	public void setBasculaTransaccion(Bascula basculaTransaccion) {
		this.basculaTransaccion = basculaTransaccion;
	}

	/**
	 * Obtiene un listado de basculas
	 * 
	 * @return basculas
	 */
	public List<Bascula> getBasculas() {
		try {
			this.basculas = IConsultasDAO.getBasculas(null);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return basculas;
	}

	public void setBasculas(List<Bascula> basculas) {
		this.basculas = basculas;
	}

	public List<TiempoEstandar> getTiempos() {
		try {
			this.tiempos = IConsultasDAO.getTiempos(null);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return tiempos;
	}

	public void setTiempos(List<TiempoEstandar> tiempos) {
		this.tiempos = tiempos;
	}

}
