package mpi.modulos.personal;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import mpi.beans.Personal;
import mpi.generales.ConsultarFuncionesAPI;
import mpi.generales.IConstantes;
import mpi.modulos.IConsultasDAO;

@ManagedBean
@SessionScoped
public class AdministrarSesionCliente extends ConsultarFuncionesAPI implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -742151151515626517L;   
	private Personal					administradorSesion;
	private Personal					administrador;

	public void init() {

	}

	// privados

	/**
	 * Obtiene validación de rol no admitido
	 * 
	 * @return pagina
	 */
	public String getNoRolAdmitido(String aInterfaz) {
		String pagina = null;
		int sw = 0;

		if (this.administradorSesion != null && this.administradorSesion.getTipoAdministracion().trim().equals("P") && aInterfaz.equals("B1") && !this.administradorSesion.istBascula()) {
			sw = 1;
		} else if (this.administradorSesion != null && this.administradorSesion.getTipoAdministracion().trim().equals("P") && aInterfaz.equals("B2") && !this.administradorSesion.istBascula2()) {
			sw = 1;
		} else if (this.administradorSesion != null && this.administradorSesion.getTipoAdministracion().trim().equals("P") && aInterfaz.equals("BH") && !this.administradorSesion.istBahia()) {
			sw = 1;
		} else if (this.administradorSesion != null && this.administradorSesion.getTipoAdministracion().trim().equals("P") && aInterfaz.equals("V") && !this.administradorSesion.istVenta()) {
			sw = 1;
		} else if (this.administradorSesion != null && this.administradorSesion.getTipoAdministracion().trim().equals("P") && aInterfaz.equals("PBH") && !this.administradorSesion.istParametrosBahias()) {
			sw = 1;
		} else if (this.administradorSesion != null && this.administradorSesion.getTipoAdministracion().trim().equals("P") && aInterfaz.equals("PB") && !this.administradorSesion.istParametrosBasculas()) {
			sw = 1;
		} else if (this.administradorSesion != null && this.administradorSesion.getTipoAdministracion().trim().equals("P") && aInterfaz.equals("C") && !this.administradorSesion.istClientes()) {
			sw = 1;
		} else if (this.administradorSesion != null && this.administradorSesion.getTipoAdministracion().trim().equals("P") && aInterfaz.equals("CO") && !this.administradorSesion.istConductores()) {
			sw = 1;
		} else if (this.administradorSesion != null && this.administradorSesion.getTipoAdministracion().trim().equals("P") && aInterfaz.equals("PT") && !this.administradorSesion.istParametrosTiempos()) {
			sw = 1;
		} else if (this.administradorSesion != null && this.administradorSesion.getTipoAdministracion().trim().equals("P") && aInterfaz.equals("IT") && !this.administradorSesion.istInformeTrazabilidad()) {
			sw = 1;
		} else if (this.administradorSesion != null && this.administradorSesion.getTipoAdministracion().trim().equals("P") && aInterfaz.equals("IC") && !this.administradorSesion.istInformeCausasRetraso()) {
			sw = 1;
		} else if (this.administradorSesion != null && this.administradorSesion.getTipoAdministracion().trim().equals("P") && aInterfaz.equals("ITO") && !this.administradorSesion.istInformeTiempos()) {
			sw = 1;
		} else if (this.administradorSesion != null && this.administradorSesion.getTipoAdministracion().trim().equals("P") && aInterfaz.equals("IM") && !this.administradorSesion.istInformeMonitoreo()) {
			sw = 1;
		}

		else if (this.administradorSesion != null && this.administradorSesion.getTipoAdministracion().trim().equals("P") && aInterfaz.equals("T")) {
			sw = 1;
		} else if (this.administradorSesion == null || this.administradorSesion.getId() == null) {
			sw = 2;
		}
		if (sw == 1) {
			pagina = IConstantes.PAGINA_HOME;

			this.mostrarMensajeGlobal("noPoseePrivilegiosSobreInterfaz", "advertencia");
			// Guarda el mensaje antes de redireccionar y lo muestra
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);

		} else if (sw == 2) {
			pagina = IConstantes.PAGINA_NO_LOGUEO;

			this.mostrarMensajeGlobal("noPoseePrivilegiosSobreInterfaz", "advertencia");
			// Guarda el mensaje antes de redireccionar y lo muestra
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);

		}

		return pagina;
	}

	/**
	 * Determina si un administrador esta en sesión y lo deja o no acceder
	 * 
	 * @return pagina
	 */
	public String getNologueoAdministrador() {
		String pagina = null;
		if (!(this.administradorSesion != null && this.administradorSesion.getId() != null)) {

			pagina = IConstantes.PAGINA_NO_LOGUEO;

			this.mostrarMensajeGlobal("noPoseePrivilegios", "advertencia");
			// Guarda el mensaje antes de redireccionar y lo muestra
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);

		}

		return pagina;
	}

	// publicos

	/**
	 * Cierra la sesión del administrador
	 */
	public String getCerrarSesion() {
		String pagina = IConstantes.PAGINA_NO_LOGUEO;
		this.administradorSesion = null;

		// this.vistaLogueado = 0;

		this.mostrarMensajeGlobal("cierreSesionCorrecto", "exito");

		// Guarda el mensaje antes de redireccionar y lo muestra
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getFlash().setKeepMessages(true);

		return pagina;

	}

	/**
	 * Permite el acceso del administrador
	 */
	public String accederAdministrador() {
		String pagina = null;
		List<Personal> administradores = null;
		try {

			if (this.administrador != null && this.administrador.getCorreoElectronico() != null && !this.administrador.getCorreoElectronico().trim().equals("") && this.administrador.getClave() != null && !this.administrador.getClave().trim().equals("")) {
				this.administrador.setEstadoVigencia(IConstantes.ACTIVO);
				administradores = IConsultasDAO.getAdministradores(this.administrador);
				if (administradores != null && administradores.size() > 0 && administradores.get(0) != null && administradores.get(0).getId() != null) {

					this.administradorSesion = administradores.get(0);
					this.mostrarMensajeGlobal("ingresoCorrecto", "exito");
					this.administrador = null;

					pagina = IConstantes.PAGINA_HOME;

					// Guarda el mensaje antes de redireccionar y lo muestra
					FacesContext context = FacesContext.getCurrentInstance();
					context.getExternalContext().getFlash().setKeepMessages(true);

				} else {
					this.mostrarMensajeGlobal("noCoincideCredenciales", "advertencia");
				}

			} else {

				this.mostrarMensajeGlobal("noCoincideCredenciales", "advertencia");

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

		return pagina;
	}

	/**
	 * Obtiene el administrador del sistema
	 * 
	 * @return administrador
	 */
	public Personal getAdministrador() {
		if (this.administrador == null) {
			this.administrador = new Personal();
		}
		return administrador;
	}

	/**
	 * Establece el administrador
	 * 
	 * @param administrador
	 */
	public void setAdministrador(Personal administrador) {
		this.administrador = administrador;
	}

	/**
	 * Obtiene los datos de administrador de sesión
	 * 
	 * @return administradorSesion
	 */
	public Personal getAdministradorSesion() {
		return administradorSesion;
	}

	/**
	 * Establece los datos de administrador de sesión
	 * 
	 * @param administradorSesion
	 */
	public void setAdministradorSesion(Personal administradorSesion) {
		this.administradorSesion = administradorSesion;
	}

}
