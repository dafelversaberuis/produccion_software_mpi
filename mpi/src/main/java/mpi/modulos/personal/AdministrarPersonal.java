package mpi.modulos.personal;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import mpi.Conexion;
import mpi.beans.Cliente;
import mpi.beans.Conductor;
import mpi.beans.Personal;
import mpi.generales.ConsultarFuncionesAPI;
import mpi.generales.IConstantes;
import mpi.generales.IEmail;
import mpi.modulos.IConsultasDAO;

@ManagedBean
@ViewScoped
public class AdministrarPersonal extends ConsultarFuncionesAPI implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2221958861238708985L;

	private Conductor					conductorConsulta;
	private Conductor					conductor;
	private Conductor					conductorTransaccion;

	private Cliente						clienteConsulta;
	private Cliente						cliente;
	private Cliente						clienteTransaccion;

	private Personal					personal;
	private Personal					personalTransaccion;
	private List<Personal>		administradores;
	private List<Cliente>			clientes;
	private List<Conductor>		conductores;

	// privados

	/**
	 * Obtiene una clave aleatoria numérica de n dígitos
	 * 
	 * @return clave
	 */
	public String getClaveAleatoria() {
		String clave = "";
		int n = 0;
		try {
			for (int i = 1; i <= IConstantes.NUMERO_DIGITOS_CLAVE_ALEATORIA.intValue(); i++) {
				n = (int) (10.0 * Math.random()) + 0;
				clave = clave + String.valueOf(n);

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return clave;

	}

	// privados

	/**
	 * Valida si el conductor es adecuado
	 * 
	 * @param aTransaccion
	 * @return ok
	 */
	private boolean isOkConductor(String aTransaccion) {
		boolean ok = true;
		Conductor conductor = null;
		List<Conductor> conductores = null;
		try {

			if (aTransaccion.equals("C")) {

				if (this.isVacio(this.conductor.getNombre())) {
					ok = false;

				} else {
					this.conductor.setNombre(this.conductor.getNombre().trim());
				}

				if (this.isVacio(this.conductor.getDocumento())) {
					ok = false;

				} else {
					this.conductor.setDocumento(this.conductor.getDocumento().trim());
				}

				if (this.isVacio(this.conductor.getPlaca())) {
					ok = false;

				} else {
					this.conductor.setPlaca(this.conductor.getPlaca().trim());
				}

				if (this.isVacio(this.conductor.getRemolque())) {
					ok = false;

				} else {
					this.conductor.setRemolque(this.conductor.getRemolque().trim());
				}

				if (!ok) {
					this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
				}

				conductor = new Conductor();
				conductor.setDocumento(this.conductor.getDocumento());
				conductores = IConsultasDAO.getConductores(conductor);

				if (conductores != null && conductores.size() > 0) {
					ok = false;
					this.mostrarMensajeGlobal("conductorExistente", "advertencia");

				}

			} else {

				if (this.isVacio(this.conductorTransaccion.getNombre())) {
					ok = false;

				} else {
					this.conductorTransaccion.setNombre(this.conductorTransaccion.getNombre().trim());
				}

				if (this.isVacio(this.conductorTransaccion.getDocumento())) {
					ok = false;

				} else {
					this.conductorTransaccion.setDocumento(this.conductorTransaccion.getDocumento().trim());
				}

				if (this.isVacio(this.conductorTransaccion.getPlaca())) {
					ok = false;

				} else {
					this.conductorTransaccion.setPlaca(this.conductorTransaccion.getPlaca().trim());
				}

				if (this.isVacio(this.conductorTransaccion.getRemolque())) {
					ok = false;

				} else {
					this.conductorTransaccion.setRemolque(this.conductorTransaccion.getRemolque().trim());
				}

				if (!ok) {
					this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
				}

				// analiza nit
				if (!this.isVacio(this.conductorTransaccion.getDocumento())) {

					if (!this.conductorTransaccion.gettCopiaDocumento().trim().toUpperCase().equals(this.conductorTransaccion.getDocumento().trim().toUpperCase())) {
						conductor = new Conductor();
						conductor.setDocumento(this.conductorTransaccion.getDocumento());
						conductores = IConsultasDAO.getConductores(conductor);

						if (conductores != null && conductores.size() > 0) {
							ok = false;
							this.mostrarMensajeGlobal("conductorExistente", "advertencia");

						}
					}

				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

		return ok;

	}

	/**
	 * Valida si cumple las condiciones de cliente
	 * 
	 * @param aTransaccion
	 * @return ok
	 */
	private boolean isOkCliente(String aTransaccion) {
		boolean ok = true;
		try {
			boolean clienteExistente = false;
			String parteA = null;
			String parteB = null;

			if (aTransaccion.equals("C")) {
				if (this.isVacio(this.cliente.getNombre())) {
					ok = false;

				} else {
					this.cliente.setNombre(this.cliente.getNombre().trim());
				}

				if (this.isVacio(this.cliente.getNit())) {
					ok = false;

				} else {
					this.cliente.setNit(this.cliente.getNit().trim());
				}

				if (!ok) {
					this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
				}

				// analiza nit
				if (!this.isVacio(this.cliente.getNit())) {
					String[] partes = this.cliente.getNit().split("-");

					if (partes != null && partes.length == 1) {
						parteA = this.cliente.getNit().toUpperCase().trim();
						this.cliente.setNit(parteA);

					} else {
						parteA = partes[0].toUpperCase().trim();
						parteB = partes[0].toUpperCase().trim() + "-" + partes[1].toUpperCase().trim();

						this.cliente.setNit(parteB);
					}

					clienteExistente = IConsultasDAO.getClienteExistente(parteA);
					if (clienteExistente) {
						ok = false;
						this.mostrarMensajeGlobal("clienteExistente", "advertencia");

					}

				}

			} else {
				if (this.isVacio(this.clienteTransaccion.getNombre())) {
					ok = false;

				} else {
					this.clienteTransaccion.setNombre(this.clienteTransaccion.getNombre().trim());
				}

				if (this.isVacio(this.clienteTransaccion.getNit())) {
					ok = false;

				} else {
					this.clienteTransaccion.setNit(this.clienteTransaccion.getNit().trim());
				}

				if (!ok) {
					this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
				}

				// analiza nit
				if (!this.isVacio(this.clienteTransaccion.getNit())) {
					String[] partes = this.clienteTransaccion.getNit().split("-");

					if (partes != null && partes.length == 1) {
						parteA = this.clienteTransaccion.getNit().toUpperCase().trim();
						this.clienteTransaccion.setNit(parteA);

					} else {
						parteA = partes[0].toUpperCase().trim();
						parteB = partes[0].toUpperCase().trim() + "-" + partes[1].toUpperCase().trim();

						this.clienteTransaccion.setNit(parteB);
					}

					if (!this.clienteTransaccion.gettCopiaNit().trim().toUpperCase().equals(parteA)) {
						clienteExistente = IConsultasDAO.getClienteExistente(parteA);
						if (clienteExistente) {
							ok = false;
							this.mostrarMensajeGlobal("clienteExistente", "advertencia");

						}
					}

				}

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

		return ok;

	}

	/**
	 * Valida un administrador
	 * 
	 * @param aTransaccion
	 * @return ok
	 */
	private boolean isValidoAdministrador(String aTransaccion) {
		boolean ok = true;

		if (aTransaccion.equals("C")) {
			if (this.administradores != null && this.administradores.size() > 0 && this.administradores.stream().anyMatch(i -> i.getCorreoElectronico().trim().toLowerCase().equals(this.personal.getCorreoElectronico().trim().toLowerCase()))) {
				ok = false;
				this.mostrarMensajeGlobal("correoExistenteAdministrador", "advertencia");
			}
			if (this.isVacio(this.personal.getNombres())) {
				ok = false;
				this.mostrarMensajeGlobal("nombresVacios", "advertencia");
			}

			if (this.isVacio(this.personal.getApellidos())) {
				ok = false;
				this.mostrarMensajeGlobal("apellidosVacios", "advertencia");
			}

			if (this.isVacio(this.personal.getCorreoElectronico())) {
				ok = false;
				this.mostrarMensajeGlobal("correoVacio", "advertencia");
			}
			if (this.personal.getTipoAdministracion().equals(IConstantes.ADMINISTRACION_PARCIAL)) {
				if (!this.personal.istVenta() && !this.personal.istBascula() && !this.personal.istBascula2() && !this.personal.istBahia() && !this.personal.istParametrosBasculas() && !this.personal.istParametrosBahias() && !this.personal.istParametrosTiempos() && !this.personal.istInformeTrazabilidad() && !this.personal.istInformeCausasRetraso() && !this.personal.istInformeTiempos() && !this.personal.istInformeMonitoreo() && !this.personal.istClientes() && !this.personal.istConductores()) {
					ok = false;
					this.mostrarMensajeGlobal("minimoUnaInterfaz", "advertencia");
				}
			}

		} else {

			if (this.administradores != null && this.administradores.size() > 0 && this.administradores.stream().anyMatch(i -> i.getId() != this.personalTransaccion.getId() && i.getCorreoElectronico().trim().toLowerCase().equals(this.personalTransaccion.getCorreoElectronico().trim().toLowerCase()))) {
				ok = false;
				this.mostrarMensajeGlobal("correoExistenteAdministrador", "advertencia");
			}
			if (this.isVacio(this.personalTransaccion.getNombres())) {
				ok = false;
				this.mostrarMensajeGlobal("nombresVacios", "advertencia");
			}

			if (this.isVacio(this.personalTransaccion.getApellidos())) {
				ok = false;
				this.mostrarMensajeGlobal("apellidosVacios", "advertencia");
			}

			if (this.isVacio(this.getPersonalTransaccion().getCorreoElectronico())) {
				ok = false;
				this.mostrarMensajeGlobal("correoVacio", "advertencia");
			}
			if (this.personalTransaccion.getTipoAdministracion().equals(IConstantes.ADMINISTRACION_PARCIAL)) {
				if (!this.personalTransaccion.istVenta() && !this.personalTransaccion.istBascula() && !this.personalTransaccion.istBascula2() && !this.personalTransaccion.istBahia() && !this.personalTransaccion.istParametrosBasculas() && !this.personalTransaccion.istParametrosBahias() && !this.personalTransaccion.istParametrosTiempos() && !this.personalTransaccion.istInformeTrazabilidad() && !this.personalTransaccion.istInformeCausasRetraso() && !this.personalTransaccion.istInformeTiempos() && !this.personalTransaccion.istInformeMonitoreo() && !this.personalTransaccion.istClientes() && !this.personalTransaccion.istConductores()) {
					ok = false;
					this.mostrarMensajeGlobal("minimoUnaInterfaz", "advertencia");
				}
			}

		}

		return ok;
	}

	// publicos

	/**
	 * Consulta los conductores por distintos criterios
	 */
	public void consultarConductores() {
		Conexion conexion = new Conexion();
		try {

			this.conductores = IConsultasDAO.getConductores(this.conductorConsulta);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Consulta los clientes
	 */
	public void consultarClientes() {
		Conexion conexion = new Conexion();
		try {

			this.clientes = IConsultasDAO.getClientes(this.clienteConsulta);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Asigna un conductor por distintos criterios
	 * 
	 * @param aConductor
	 * @param aVista
	 */
	public void asignarConductor(Conductor aConductor, String aVista) {

		try {

			this.conductorTransaccion = aConductor;

			if (aVista != null && aVista.equals("MODAL_EDITAR")) {

				this.conductorTransaccion.settCopiaDocumento(aConductor.getDocumento().trim().toUpperCase());

				this.abrirModal("panelEdicion");

			} else {

				this.abrirModal("panelEliminacion");

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Este método asigna un objeto del listado de c para relizarle operaciones
	 * distintas
	 * 
	 * @param aCliente
	 * @param aVista
	 */
	public void asignarCliente(Cliente aCliente, String aVista) {

		try {

			this.clienteTransaccion = aCliente;

			if (aVista != null && aVista.equals("MODAL_EDITAR")) {

				String[] partes = this.clienteTransaccion.getNit().split("-");

				if (partes != null && partes.length == 1) {

					this.clienteTransaccion.settCopiaNit(this.clienteTransaccion.getNit().toUpperCase().trim());

				} else {
					this.clienteTransaccion.settCopiaNit(partes[0].toUpperCase().trim());

				}

				this.abrirModal("panelEdicion");

			} else {

				this.abrirModal("panelEliminacion");

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Cancela y resetea criterios de consulta de conductores
	 */
	public void cancelarConsultaConductores() {
		this.conductorConsulta = null;
		this.getConductorConsulta();

		this.conductores = null;
	}

	/**
	 * Cancela y resetea criterios de consulta y resultados antes cargados
	 */
	public void cancelarConsulta() {
		this.clienteConsulta = null;
		this.getClienteConsulta();

		this.clientes = null;
	}

	/**
	 * Cancela la edición de un conductor en transacción
	 * 
	 * @param aVista
	 */
	public void cancelarConductorTransaccion(String aVista) {
		try {

			this.conductorTransaccion = null;
			this.getConductorTransaccion();
			this.conductores = null;
			this.consultarConductores();

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
	 * Cancela la edición de un cliente en transacción
	 * 
	 * @param aVista
	 */
	public void cancelarClienteTransaccion(String aVista) {
		try {

			this.clienteTransaccion = null;
			this.getClienteTransaccion();
			this.clientes = null;
			this.consultarClientes();

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
	 * Elimina un conductor
	 */
	public void eliminarConductor() {

		Conexion conexion = new Conexion();
		try {

			conexion.setAutoCommitBD(false);
			this.conductorTransaccion.getCamposBD();
			conexion.eliminarBD(this.conductorTransaccion.getEstructuraTabla().getTabla(), this.conductorTransaccion.getEstructuraTabla().getLlavePrimaria());
			conexion.commitBD();
			this.mostrarMensajeGlobal("eliminacionExitosa", "exito");

			this.conductores = null;
			this.consultarConductores();

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");

		} finally {
			conexion.cerrarConexion();
		}

		// reseteo de variables
		this.conductorTransaccion = null;
		this.getConductorTransaccion();
		this.conductores = null;
		this.consultarConductores();

	}

	/**
	 * Elimina un cliente
	 */
	public void eliminarCliente() {

		Conexion conexion = new Conexion();
		try {

			conexion.setAutoCommitBD(false);
			this.clienteTransaccion.getCamposBD();
			conexion.eliminarBD(this.clienteTransaccion.getEstructuraTabla().getTabla(), this.clienteTransaccion.getEstructuraTabla().getLlavePrimaria());
			conexion.commitBD();
			this.mostrarMensajeGlobal("eliminacionExitosa", "exito");

			this.clientes = null;
			this.consultarClientes();

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");

		} finally {
			conexion.cerrarConexion();
		}

		// reseteo de variables
		this.clienteTransaccion = null;
		this.getClienteTransaccion();
		this.clientes = null;
		this.consultarClientes();

	}

	/**
	 * Edita un conductor
	 */
	public void editarConductor() {
		Conexion conexion = new Conexion();

		try {
			if (isOkConductor("E")) {

				conexion.setAutoCommitBD(false);
				this.conductorTransaccion.getCamposBD();
				conexion.actualizarBD(this.conductorTransaccion.getEstructuraTabla().getTabla(), this.conductorTransaccion.getEstructuraTabla().getPersistencia(), this.conductorTransaccion.getEstructuraTabla().getLlavePrimaria(), null);
				conexion.commitBD();

				this.mostrarMensajeGlobal("edicionExitosa", "exito");
				this.cerrarModal("panelEdicion");

				// reseteo de variables
				this.conductores = null;
				this.consultarConductores();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Edita un registro de cliente
	 */
	public void editarCliente() {
		Conexion conexion = new Conexion();

		try {
			if (isOkCliente("E")) {

				conexion.setAutoCommitBD(false);
				this.clienteTransaccion.getCamposBD();
				conexion.actualizarBD(this.clienteTransaccion.getEstructuraTabla().getTabla(), this.clienteTransaccion.getEstructuraTabla().getPersistencia(), this.clienteTransaccion.getEstructuraTabla().getLlavePrimaria(), null);
				conexion.commitBD();

				this.mostrarMensajeGlobal("edicionExitosa", "exito");
				this.cerrarModal("panelEdicion");

				// reseteo de variables
				this.clientes = null;
				this.consultarClientes();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Crea un registro de conductor
	 */
	public void crearConductor() {
		Conexion conexion = new Conexion();

		try {
			if (isOkConductor("C")) {
				conexion.setAutoCommitBD(false);

				this.conductor.getCamposBD();
				conexion.insertarBD(this.conductor.getEstructuraTabla().getTabla(), this.conductor.getEstructuraTabla().getPersistencia());
				conexion.commitBD();

				this.mostrarMensajeGlobal("creacionExitosa", "exito");

				// reseteo de variables

				this.conductorConsulta = null;
				this.getConductorConsulta();
				this.conductorConsulta.setDocumento(this.conductor.getDocumento());
				this.conductorConsulta.setNombre(this.conductor.getNombre());
				this.conductorConsulta.setPlaca(this.conductor.getPlaca());

				this.conductor = null;
				this.getConductor();
				this.conductores = null;
				this.consultarConductores();

			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Crea un registro de cliente
	 */
	public void crearCliente() {
		Conexion conexion = new Conexion();

		try {
			if (isOkCliente("C")) {
				conexion.setAutoCommitBD(false);

				this.cliente.getCamposBD();
				conexion.insertarBD(this.cliente.getEstructuraTabla().getTabla(), this.cliente.getEstructuraTabla().getPersistencia());
				conexion.commitBD();

				this.mostrarMensajeGlobal("creacionExitosa", "exito");

				// reseteo de variables

				this.clienteConsulta = null;
				this.getClienteConsulta();
				this.clienteConsulta.setNit(this.cliente.getNit());
				this.clienteConsulta.setNombre(this.cliente.getNombre());

				this.cliente = null;
				this.getCliente();
				this.clientes = null;
				this.consultarClientes();

			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Cancela un conductor en creación
	 */
	public void cancelarConductor() {

		try {
			this.conductor = null;
			this.getConductor();

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Cancela la creación de un cliente
	 */
	public void cancelarCliente() {

		try {
			this.cliente = null;
			this.getCliente();

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Crea un nuevo administrador del software
	 */
	public void crearAdministrador() {
		Conexion conexion = new Conexion();

		try {
			if (isValidoAdministrador("C")) {
				conexion.setAutoCommitBD(false);

				this.personal.setEstadoVigencia(IConstantes.ACTIVO);
				this.personal.setNombres(this.getSinEspacios(this.personal.getNombres()));
				this.personal.setApellidos(this.getSinEspacios(this.personal.getApellidos()));
				this.personal.setCorreoElectronico(this.personal.getCorreoElectronico().trim());
				this.personal.setClave(this.getClaveAleatoria());

				this.personal.getCamposBD();

				conexion.insertarBD(this.personal.getEstructuraTabla().getTabla(), this.personal.getEstructuraTabla().getPersistencia());
				conexion.commitBD();

				this.mostrarMensajeGlobal("creacionExitosa", "exito");
				this.mostrarMensajeGlobalPersonalizado(this.getMensaje("claveAleatoria", this.getPersonal().getClave()), "exito");

				IEmail.enviarCorreo(this.getMensaje("mensajeClaveAleatoria", this.personal.getNombres() + " " + this.personal.getApellidos(), this.personal.getClave()), this.getMensaje("asuntoClaveAleatoria"), this.personal.getCorreoElectronico());
				this.mostrarMensajeGlobalPersonalizado(this.getMensaje("claveCorreoExitoso", this.getPersonal().getCorreoElectronico()), "exito");

				// reseteo de variables
				this.personal = null;
				this.getPersonal();
				this.administradores = null;
				this.getAdministradores();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Genera una nueva clave aleatoria para el administrador
	 */
	public void generarClaveAdministrador() {

		Conexion conexion = new Conexion();

		try {
			boolean ok = true;
			if (this.personalTransaccion != null && this.personalTransaccion.gettTipoClave() != null && this.personalTransaccion.gettTipoClave().equals("A")) {

				this.personalTransaccion.setClave(this.getClaveAleatoria());

			} else {

				if (this.isVacio(this.personalTransaccion.getClave())) {
					ok = false;
				}

			}
			if (ok) {
				conexion.setAutoCommitBD(false);

				this.personalTransaccion.getCamposBD();

				conexion.actualizarBD(this.personalTransaccion.getEstructuraTabla().getTabla(), this.personalTransaccion.getEstructuraTabla().getPersistencia(), this.personalTransaccion.getEstructuraTabla().getLlavePrimaria(), null);
				conexion.commitBD();

				this.mostrarMensajeGlobalPersonalizado(this.getMensaje("claveAleatoria", this.personalTransaccion.getClave()), "exito");

				IEmail.enviarCorreo(this.getMensaje("mensajeClaveAleatoria", this.personalTransaccion.getNombres() + " " + this.personalTransaccion.getApellidos(), this.personalTransaccion.getClave()), this.getMensaje("asuntoClaveAleatoria"), this.personalTransaccion.getCorreoElectronico());
				this.mostrarMensajeGlobalPersonalizado(this.getMensaje("claveCorreoExitoso", this.personalTransaccion.getCorreoElectronico()), "exito");

				this.cerrarModal("panelClaveAdministrador");

				// reseteo de variables
				this.personalTransaccion = null;
				this.getPersonalTransaccion();
				this.administradores = null;
				this.getAdministradores();

			} else {

				this.mostrarMensajeGlobal("claveEnBlanco", "error");
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Edita un registro de administrador de software
	 */
	public void editarAdministrador() {
		Conexion conexion = new Conexion();

		try {
			if (isValidoAdministrador("E")) {
				conexion.setAutoCommitBD(false);

				this.personalTransaccion.setNombres(this.getSinEspacios(this.personalTransaccion.getNombres()));
				this.personalTransaccion.setApellidos(this.getSinEspacios(this.personalTransaccion.getApellidos()));

				this.personalTransaccion.getCamposBD();
				conexion.actualizarBD(this.personalTransaccion.getEstructuraTabla().getTabla(), this.personalTransaccion.getEstructuraTabla().getPersistencia(), this.personalTransaccion.getEstructuraTabla().getLlavePrimaria(), null);
				conexion.commitBD();
				this.mostrarMensajeGlobal("edicionExitosa", "exito");
				this.mostrarMensajeGlobal("algunosCambios", "advertencia");
				this.cerrarModal("panelEdicionAdministrador");

				// reseteo de variables
				this.personalTransaccion = null;
				this.getPersonalTransaccion();
				this.administradores = null;
				this.getAdministradores();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Elimina un registro de adminiistador
	 */
	public void eliminarAdministrador() {

		Conexion conexion = new Conexion();
		try {

			conexion.setAutoCommitBD(false);
			this.personalTransaccion.getCamposBD();
			conexion.eliminarBD(this.personalTransaccion.getEstructuraTabla().getTabla(), this.personalTransaccion.getEstructuraTabla().getLlavePrimaria());
			conexion.commitBD();
			this.mostrarMensajeGlobal("eliminacionExitosa", "exito");

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
			this.mostrarMensajeGlobal("eliminacionFallida", "error");

		} finally {
			conexion.cerrarConexion();
		}

		// reseteo de variables
		this.personalTransaccion = null;
		this.administradores = null;
		this.getAdministradores();

	}

	/**
	 * Este método borra el formulario de creación de un administrador
	 */
	public void cancelarAdministrador() {

		try {
			this.personal = new Personal();

			this.administradores = null;
			this.getAdministradores();
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Este método borra el formulario de edición de un administrador en
	 * transacción
	 */
	public void cancelarAdministradorTransaccion(String aVista) {
		try {

			this.personalTransaccion = new Personal();
			this.administradores = null;
			this.getAdministradores();

			if (aVista != null && aVista.equals("MODAL_EDITAR_ADMINISTRADOR")) {
				this.cerrarModal("panelEdicionAdministrador");

			} else if (aVista != null && aVista.equals("MODAL_CLAVE_ADMINISTRADOR")) {
				this.cerrarModal("panelClaveAdministrador");

			} else if (aVista != null && aVista.equals("MODAL_ELIMINAR_ADMINISTRADOR")) {
				this.cerrarModal("panelEliminacionAdministrador");

			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	/**
	 * Asigna un administrador para realizar una acción
	 * 
	 * @param aAgrupador
	 * @param aVista
	 */
	public void asignarAdministrador(Personal aPersonal, String aVista) {

		try {

			this.personalTransaccion = aPersonal;

			if (aVista != null && aVista.equals("MODAL_EDITAR_ADMINISTRADOR")) {
				this.abrirModal("panelEdicionAdministrador");

			} else if (aVista != null && aVista.equals("MODAL_CLAVE_ADMINISTRADOR")) {
				this.abrirModal("panelClaveAdministrador");

			} else {

				this.abrirModal("panelEliminacionAdministrador");

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Obtiene un listado de administradores
	 * 
	 * @return administradores
	 */
	public List<Personal> getAdministradores() {
		try {
			if (this.administradores == null) {

				this.administradores = IConsultasDAO.getAdministradores(null);

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return administradores;
	}

	// get/sets

	public Personal getPersonal() {
		try {
			if (this.personal == null) {
				this.personal = new Personal();

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return personal;
	}

	public void setPersonal(Personal personal) {
		this.personal = personal;
	}

	public Personal getPersonalTransaccion() {
		return personalTransaccion;
	}

	public void setPersonalTransaccion(Personal personalTransaccion) {
		this.personalTransaccion = personalTransaccion;
	}

	public void setAdministradores(List<Personal> administradores) {
		this.administradores = administradores;
	}

	public Cliente getCliente() {
		try {
			if (this.cliente == null) {
				this.cliente = new Cliente();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cliente getClienteTransaccion() {
		try {
			if (this.clienteTransaccion == null) {
				this.clienteTransaccion = new Cliente();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return clienteTransaccion;
	}

	public void setClienteTransaccion(Cliente clienteTransaccion) {
		this.clienteTransaccion = clienteTransaccion;
	}

	public Cliente getClienteConsulta() {
		try {
			if (this.clienteConsulta == null) {
				this.clienteConsulta = new Cliente();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return clienteConsulta;
	}

	public void setClienteConsulta(Cliente clienteConsulta) {
		this.clienteConsulta = clienteConsulta;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public Conductor getConductorConsulta() {
		try {
			if (this.conductorConsulta == null) {
				this.conductorConsulta = new Conductor();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return conductorConsulta;
	}

	public void setConductorConsulta(Conductor conductorConsulta) {
		this.conductorConsulta = conductorConsulta;
	}

	public Conductor getConductor() {
		try {
			if (this.conductor == null) {
				this.conductor = new Conductor();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return conductor;
	}

	public void setConductor(Conductor conductor) {
		this.conductor = conductor;
	}

	public Conductor getConductorTransaccion() {
		try {
			if (this.conductorTransaccion == null) {
				this.conductorTransaccion = new Conductor();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return conductorTransaccion;
	}

	public void setConductorTransaccion(Conductor conductorTransaccion) {
		this.conductorTransaccion = conductorTransaccion;
	}

	public List<Conductor> getConductores() {
		return conductores;
	}

	public void setConductores(List<Conductor> conductores) {
		this.conductores = conductores;
	}

}
