package mpi.modulos.despachos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

import mpi.Conexion;
import mpi.beans.Auditoria;
import mpi.beans.Bahia;
import mpi.beans.Bascula;
import mpi.beans.Cliente;
import mpi.beans.Conductor;
import mpi.beans.Despacho;
import mpi.beans.Medidor;
import mpi.beans.PedidoBase;
import mpi.beans.TiempoEstandar;
import mpi.beans.Ubicacion;
import mpi.generales.ConsultarFuncionesAPI;
import mpi.generales.IConstantes;
import mpi.modulos.IConsultasDAO;
import mpi.modulos.personal.AdministrarSesionCliente;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import soap.ConexionWS;

@ManagedBean
@ViewScoped
public class AdministrarDespacho extends ConsultarFuncionesAPI implements Serializable {

	/**
	 * 
	 */
	private static final long					serialVersionUID	= -3283694171346100837L;

	// inyecta el bean de sesion
	@ManagedProperty(value = "#{administrarSesionCliente}")
	private AdministrarSesionCliente	administrarSesionCliente;

	private Integer										copiaBasculaFinal;
	private Despacho									despachoConsulta;
	private Despacho									despacho;
	private Despacho									despachoTransaccion;
	private Despacho									causaOportuna;
	private Despacho									causaInterna;

	private PieChartModel							graficoCausasNoOportunas;
	private PieChartModel							graficoCausasInternas;

	private List<Despacho>						despachos;
	private List<PedidoBase>					pedidosBaseCrear;
	private List<Conductor>						conductoresSiesa;

	private List<Auditoria>						auditorias;

	private List<TiempoEstandar>			tiemposTabla			= null;

	private List<SelectItem>					itemsUbicaciones;
	private List<SelectItem>					itemsVehiculos;
	private List<SelectItem>					itemsBahiasActivas;
	private List<SelectItem>					itemsBasculasActivas;
	private List<SelectItem>					itemsBasculasFinalesActivas;

	// el del medidor el método

	public String getEstiloBasculaInicial() {
		String estilo = "";
		if (this.despachoTransaccion.getPesoFinal() != null || (this.despachoTransaccion.getBasculaFinal() != null && this.despachoTransaccion.getBasculaFinal().getId() != null)) {
			estilo = "INACTIVO";
		}
		return estilo;
	}

	public String getEstiloBasculaFinal2() {
		String estilo = "";
		if ((this.despachoTransaccion.getLote() == null || (this.despachoTransaccion.getTipoDespacho().equals("B") && (this.despachoTransaccion.getBasculaInicial() == null || this.despachoTransaccion.getBasculaInicial().getId() == null)))) {
			estilo = "INACTIVO";
		}
		return estilo;
	}

	public String getEstiloBasculaFinal() {
		String estilo = "";
		if (this.despachoTransaccion.getNumeroRemision() != null || (this.despachoTransaccion.getTipoDespacho().equals("B") && (this.despachoTransaccion.getBasculaInicial() == null || this.despachoTransaccion.getBasculaInicial().getId() == null || this.despachoTransaccion.getLote() == null))) {
			estilo = "INACTIVO";
		}
		return estilo;
	}

	/**
	 * Obtiene el valor de la clave aleatoria
	 */
	public void obtenerValor() {

		try {
			String fecha = "";
			String horaInicio = "";
			String horaFin = "";

			SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");

			this.despachoTransaccion.setMedidor(null);
			if (this.despachoTransaccion.getBahia() != null && this.despachoTransaccion.getBahia().getId() != null) {

				Medidor medidor = IConsultasDAO.getMedidor(this.despachoTransaccion.getBahia().getId());
				if (medidor != null) {

					if (this.despachoTransaccion.getBahia().getId().intValue() == 6) {
						if (medidor.getFlujo4() != null) {
							this.despachoTransaccion.setMedidor(medidor.getFlujo4());
						} else {
							this.despachoTransaccion.setMedidor(null);
						}
						if (medidor.getTemperaturaMedidor4() != null) {
							this.despachoTransaccion.setTemperaturaMedidor(this.getValorRedondeado(medidor.getTemperaturaMedidor4(), 2));
						} else {
							this.despachoTransaccion.setTemperaturaMedidor(null);
						}

						if (medidor.getAno4() != null && medidor.getMes4() != null && medidor.getDia4() != null) {
							fecha = "" + medidor.getAno4();
							if (medidor.getMes4().intValue() < 10) {
								fecha += "-0" + medidor.getMes4();
							} else {
								fecha += "-" + medidor.getMes4();
							}
							if (medidor.getDia4().intValue() < 10) {
								fecha += "-0" + medidor.getDia4();
							} else {
								fecha += "-" + medidor.getDia4();
							}
							try {
								this.despachoTransaccion.setFechaMedidor(formatoFecha.parse(fecha));
							} catch (Exception ee) {
								this.despachoTransaccion.setFechaMedidor(null);
							}

						} else {

							this.despachoTransaccion.setFechaMedidor(null);
						}

						if (medidor.getHoraInicio4() != null && medidor.getMinutoInicio4() != null && medidor.getSegundoInicio4() != null) {

							horaInicio = "";
							if (medidor.getHoraInicio4().intValue() < 10) {
								horaInicio += "0" + medidor.getHoraInicio4();
							} else {
								horaInicio += "" + medidor.getHoraInicio4();
							}
							if (medidor.getMinutoInicio4().intValue() < 10) {
								horaInicio += ":0" + medidor.getMinutoInicio4();
							} else {
								horaInicio += ":" + medidor.getMinutoInicio4();
							}

							if (medidor.getSegundoInicio4().intValue() < 10) {
								horaInicio += ":0" + medidor.getSegundoInicio4();
							} else {
								horaInicio += ":" + medidor.getSegundoInicio4();
							}

							try {
								this.despachoTransaccion.setHoraInicioMedidor(formatoHora.parse(horaInicio));
							} catch (Exception ee) {
								this.despachoTransaccion.setHoraInicioMedidor(null);
							}

						} else {

							this.despachoTransaccion.setHoraInicioMedidor(null);
						}

						if (medidor.getHoraFin4() != null && medidor.getMinutoFin4() != null && medidor.getSegundoFin4() != null) {

							horaFin = "";
							if (medidor.getHoraFin4().intValue() < 10) {
								horaFin += "0" + medidor.getHoraFin4();
							} else {
								horaFin += "" + medidor.getHoraFin4();
							}
							if (medidor.getMinutoFin4().intValue() < 10) {
								horaFin += ":0" + medidor.getMinutoFin4();
							} else {
								horaFin += ":" + medidor.getMinutoFin4();
							}

							if (medidor.getSegundoFin4().intValue() < 10) {
								horaFin += ":0" + medidor.getSegundoFin4();
							} else {
								horaFin += ":" + medidor.getSegundoFin4();
							}

							try {
								this.despachoTransaccion.setHoraFinMedidor(formatoHora.parse(horaFin));
							} catch (Exception ee) {
								this.despachoTransaccion.setHoraFinMedidor(null);
							}

						} else {

							this.despachoTransaccion.setHoraFinMedidor(null);
						}

					} else {
						if (medidor.getFlujo3() != null) {
							this.despachoTransaccion.setMedidor(medidor.getFlujo3());
						} else {
							this.despachoTransaccion.setMedidor(null);
						}
						if (medidor.getTemperaturaMedidor3() != null) {
							this.despachoTransaccion.setTemperaturaMedidor(this.getValorRedondeado(medidor.getTemperaturaMedidor3(), 2));
						} else {
							this.despachoTransaccion.setTemperaturaMedidor(null);
						}

						if (medidor.getAno3() != null && medidor.getMes3() != null && medidor.getDia3() != null) {
							fecha = "" + medidor.getAno3();
							if (medidor.getMes3().intValue() < 10) {
								fecha += "-0" + medidor.getMes3();
							} else {
								fecha += "-" + medidor.getMes3();
							}
							if (medidor.getDia3().intValue() < 10) {
								fecha += "-0" + medidor.getDia3();
							} else {
								fecha += "-" + medidor.getDia3();
							}
							try {
								this.despachoTransaccion.setFechaMedidor(formatoFecha.parse(fecha));
							} catch (Exception ee) {
								this.despachoTransaccion.setFechaMedidor(null);
							}

						} else {

							this.despachoTransaccion.setFechaMedidor(null);
						}

						if (medidor.getHoraInicio3() != null && medidor.getMinutoInicio3() != null && medidor.getSegundoInicio3() != null) {

							horaInicio = "";
							if (medidor.getHoraInicio3().intValue() < 10) {
								horaInicio += "0" + medidor.getHoraInicio3();
							} else {
								horaInicio += "" + medidor.getHoraInicio3();
							}
							if (medidor.getMinutoInicio3().intValue() < 10) {
								horaInicio += ":0" + medidor.getMinutoInicio3();
							} else {
								horaInicio += ":" + medidor.getMinutoInicio3();
							}

							if (medidor.getSegundoInicio3().intValue() < 10) {
								horaInicio += ":0" + medidor.getSegundoInicio3();
							} else {
								horaInicio += ":" + medidor.getSegundoInicio3();
							}

							try {
								this.despachoTransaccion.setHoraInicioMedidor(formatoHora.parse(horaInicio));
							} catch (Exception ee) {
								this.despachoTransaccion.setHoraInicioMedidor(null);
							}

						} else {

							this.despachoTransaccion.setHoraInicioMedidor(null);
						}

						if (medidor.getHoraFin3() != null && medidor.getMinutoFin3() != null && medidor.getSegundoFin3() != null) {

							horaFin = "";
							if (medidor.getHoraFin3().intValue() < 10) {
								horaFin += "0" + medidor.getHoraFin3();
							} else {
								horaFin += "" + medidor.getHoraFin3();
							}
							if (medidor.getMinutoFin3().intValue() < 10) {
								horaFin += ":0" + medidor.getMinutoFin3();
							} else {
								horaFin += ":" + medidor.getMinutoFin3();
							}

							if (medidor.getSegundoFin3().intValue() < 10) {
								horaFin += ":0" + medidor.getSegundoFin3();
							} else {
								horaFin += ":" + medidor.getSegundoFin3();
							}

							try {
								this.despachoTransaccion.setHoraFinMedidor(formatoHora.parse(horaFin));
							} catch (Exception ee) {
								this.despachoTransaccion.setHoraFinMedidor(null);
							}

						} else {

							this.despachoTransaccion.setHoraFinMedidor(null);
						}

					}

				}
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	// privados

	/**
	 * Obtiene el siguiente despacho
	 * 
	 * @return numero
	 */
	private String getSiguienteDespacho(Integer aNumero) {
		String numero = "";
		try {

			numero = "" + (aNumero.intValue() - IConstantes.CONSEGUTIVO_SIGUIENTE_NUMERO_ENTREGA_V2.intValue());

			String[] partes = numero.split("");
			while (partes.length < IConstantes.DIGITOS_NUMERO_ENTREGA.intValue()) {
				numero = "0" + numero;
				partes = numero.split("");
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return numero;

	}

	/**
	 * Valida si la bahía posee todo correcto
	 * 
	 * @return ok
	 */
	private boolean isOkBahia() {
		boolean ok = true;

		try {
			if (this.despachoTransaccion.getTipoDespacho().equals(IConstantes.TIPO_MEDIDOR)) {
				if (this.isVacio(this.despachoTransaccion.getTicketMedidor())) {
					ok = false;

				} else {

					this.despachoTransaccion.setTicketMedidor(this.despachoTransaccion.getTicketMedidor().trim());

				}
			}

			if (this.isVacio(this.despachoTransaccion.getDespachadoDe())) {
				ok = false;

			} else {
				this.despachoTransaccion.setDespachadoDe(this.despachoTransaccion.getDespachadoDe().trim());

			}

			if (this.isVacio(this.despachoTransaccion.getLote())) {
				ok = false;

			} else {
				this.despachoTransaccion.setLote(this.despachoTransaccion.getLote().trim());

			}

			if (!this.isVacio(this.despachoTransaccion.getNumeroSellos())) {
				this.despachoTransaccion.setNumeroSellos(this.despachoTransaccion.getNumeroSellos().trim());
			}

			if (!this.isVacio(this.despachoTransaccion.getNumeroStickers())) {
				this.despachoTransaccion.setNumeroStickers(this.despachoTransaccion.getNumeroStickers().trim());

			}

			if (this.despachoTransaccion.getTipoDespacho().equals(IConstantes.TIPO_MEDIDOR)) {

				if (this.despachoTransaccion.getMedidor() == null || this.despachoTransaccion.getMedidor().intValue() < 0) {
					ok = false;
					this.mostrarMensajeGlobal("medidorvacioCero", "advertencia");
				}

			}

			if (!ok) {
				this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

		return ok;

	}

	/**
	 * Validaciones de báscula final
	 * 
	 * @return ok
	 */
	private boolean isOkBasculaFinal() {
		boolean ok = true;
		List<Despacho> despachosT = null;
		Despacho despachoC = null;
		try {

			if (this.despachoTransaccion.getDiferenciaPeso() < 0) {
				ok = false;
				this.mostrarMensajeGlobal("pesoFinalIncorrecto", "advertencia");

			}

			if (this.isVacio(this.despachoTransaccion.getTicketSalida())) {
				ok = false;
				this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");

			} else {
				this.despachoTransaccion.setTicketSalida(this.despachoTransaccion.getTicketSalida().trim());
			}

			if (this.despachoTransaccion.getObservaciones() != null) {
				this.despachoTransaccion.setObservaciones(this.despachoTransaccion.getObservaciones().trim());
			}

			if (ok) {

				if (this.despachoTransaccion.gettTicketSalida() == null || (this.despachoTransaccion.gettTicketSalida() != null && !this.despachoTransaccion.gettTicketSalida().trim().toUpperCase().equals(this.despachoTransaccion.getTicketSalida().trim().toUpperCase()))) {

					despachoC = new Despacho();
					despachoC.setTicketSalida(this.despachoTransaccion.getTicketSalida().trim().toUpperCase());
					despachosT = IConsultasDAO.getDespachos(despachoC, 1);
					if (despachosT != null && despachosT.size() > 0) {
						ok = false;
						this.mostrarMensajeGlobal("ticketSalidaExiste", "advertencia");
					}

				}

				// if (this.despachoTransaccion.gettNumeroEntrega() == null ||
				// (this.despachoTransaccion.getNumeroEntrega() != null &&
				// this.despachoTransaccion.gettNumeroEntrega() != null &&
				// this.despachoTransaccion.gettNumeroEntrega().intValue() !=
				// this.despachoTransaccion.getNumeroEntrega().intValue())) {
				//
				// despachoC = new Despacho();
				// despachoC.setNumeroEntrega(this.despachoTransaccion.getNumeroEntrega());
				// despachosT = IConsultasDAO.getDespachos(despachoC, 1);
				// if (despachosT != null && despachosT.size() > 0) {
				// ok = false;
				// this.mostrarMensajeGlobal("numeroEntregaExistente", "advertencia");
				// }
				//
				// }

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

		return ok;

	}

	/**
	 * Valida si es correcto el despacho por venta
	 * 
	 * @param aTransaccion
	 * @return ok
	 */
	private boolean isOkDespachoVenta(String aTransaccion) {
		boolean ok = true;
		SimpleDateFormat formatoJava = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formatoHora = new SimpleDateFormat("HH");
		SimpleDateFormat formatoMinuto = new SimpleDateFormat("mm");
		SimpleDateFormat formatoSegundo = new SimpleDateFormat("ss");

		try {

			if (aTransaccion.equals("C")) {

				if (this.isVacio(this.despacho.getNumeroInterno())) {
					ok = false;

				} else {
					this.despacho.setNumeroInterno(this.despacho.getNumeroInterno().trim());
				}

				if (this.isVacio(this.despacho.getCodigoProducto())) {
					ok = false;

				} else {
					this.despacho.setCodigoProducto(this.despacho.getCodigoProducto().trim());
				}

				// if (this.isVacio(this.despacho.getContraMuestra())) {
				// ok = false;
				//
				// } else {
				// this.despacho.setContraMuestra(this.despacho.getContraMuestra().trim());
				// }

				if (this.isVacio(this.despacho.getFlete())) {
					ok = false;

				} else {
					this.despacho.setFlete(this.despacho.getFlete().trim());
				}

				// if (this.isVacio(this.despacho.getHojaSeguridad())) {
				// ok = false;
				//
				// } else {
				// this.despacho.setHojaSeguridad(this.despacho.getHojaSeguridad().trim());
				// }

				if (this.isVacio(this.despacho.getObra())) {
					ok = false;

				} else {
					this.despacho.setObra(this.despacho.getObra().trim());
				}

				if (this.isVacio(this.despacho.getOrdenCompra())) {
					ok = false;

				} else {
					this.despacho.setOrdenCompra(this.despacho.getOrdenCompra().trim());
				}

				if (this.isVacio(this.despacho.getPedido())) {
					ok = false;

				} else {
					this.despacho.setPedido(this.despacho.getPedido().trim());
				}

				if (this.isVacio(this.despacho.getPlaca())) {
					ok = false;

				} else {
					this.despacho.setPlaca(this.despacho.getPlaca().trim());
				}

				if (this.isVacio(this.despacho.getPresentacion())) {
					ok = false;

				} else {
					this.despacho.setPresentacion(this.despacho.getPresentacion().trim());
				}

				if (this.isVacio(this.despacho.getProducto())) {
					ok = false;

				} else {
					this.despacho.setProducto(this.despacho.getProducto().trim());
				}

				if (this.isVacio(this.despacho.getRemolque())) {
					ok = false;

				} else {
					this.despacho.setRemolque(this.despacho.getRemolque().trim());
				}

				// if (this.isVacio(this.despacho.getReporteCalidad())) {
				// ok = false;
				//
				// } else {
				// this.despacho.setReporteCalidad(this.despacho.getReporteCalidad().trim());
				// }

				// if (this.isVacio(this.despacho.getTarjetaEmergencia())) {
				// ok = false;
				//
				// } else {
				// this.despacho.setTarjetaEmergencia(this.despacho.getTarjetaEmergencia().trim());
				// }

				if (this.isVacio(this.despacho.getTipoDespacho())) {
					ok = false;

				} else {
					this.despacho.setTipoDespacho(this.despacho.getTipoDespacho().trim());
				}

				if (this.isVacio(this.despacho.getTransporte())) {
					ok = false;

				} else {
					this.despacho.setTransporte(this.despacho.getTransporte().trim());
				}

				if (this.isVacio(this.despacho.getUbicacion())) {
					ok = false;

				} else {
					this.despacho.setUbicacion(this.despacho.getUbicacion().trim());
				}
				this.despacho.setFecha(formatoJava.parse(formatoJava.format(this.despacho.getFecha())));
				if (this.despacho.getFechaOriginalSiesa() != null) {
					this.despacho.setFechaOriginalSiesa(formatoJava.parse(formatoJava.format(this.despacho.getFechaOriginalSiesa())));
				} else {
					this.despacho.setFechaOriginalSiesa(null);
				}
				this.despacho.setHora(Integer.parseInt(formatoHora.format(this.despacho.getFecha())));
				this.despacho.setMinuto(Integer.parseInt(formatoMinuto.format(this.despacho.getFecha())));
				this.despacho.setSegundo(Integer.parseInt(formatoSegundo.format(this.despacho.getFecha())));

				if (isDespachoAbierto()) {
					ok = false;
				}

				if (this.despacho.istTarjetaEmergencia()) {
					this.despacho.setTarjetaEmergencia(IConstantes.AFIRMACION);
				} else {
					this.despacho.setTarjetaEmergencia(IConstantes.NEGACION);
				}

				if (this.despacho.istContraMuestra()) {
					this.despacho.setContraMuestra(IConstantes.AFIRMACION);
				} else {
					this.despacho.setContraMuestra(IConstantes.NEGACION);
				}

				if (this.despacho.istHoja()) {
					this.despacho.setHojaSeguridad(IConstantes.AFIRMACION);
				} else {
					this.despacho.setHojaSeguridad(IConstantes.NEGACION);
				}

				if (this.despacho.istReporteCalidad()) {
					this.despacho.setReporteCalidad(IConstantes.AFIRMACION);
				} else {
					this.despacho.setReporteCalidad(IConstantes.NEGACION);
				}

				if (this.despacho.istCumple()) {
					this.despacho.setCumple(IConstantes.AFIRMACION);
				} else {
					this.despacho.setCumple(IConstantes.NEGACION);
				}

				if (isMostrarObservacionesCausa(this.despacho)) {
					if (!(this.despacho.getObservacionesRetraso() != null && !this.despacho.getObservacionesRetraso().trim().equals(""))) {
						ok = false;
						this.mostrarMensajeGlobalPersonalizado("Debido a que escribio en alguna causa de restrasodebe escribir alguna observación de restraso", "advertencia");
					}

				} else {
					this.despacho.setObservacionesRetraso(null);
				}

			} else {

				if (this.isVacio(this.despachoTransaccion.getNumeroInterno())) {
					ok = false;

				} else {
					this.despachoTransaccion.setNumeroInterno(this.despachoTransaccion.getNumeroInterno().trim());
				}

				if (this.isVacio(this.despachoTransaccion.getCodigoProducto())) {
					ok = false;

				} else {
					this.despachoTransaccion.setCodigoProducto(this.despachoTransaccion.getCodigoProducto().trim());
				}

				// if (this.isVacio(this.despachoTransaccion.getContraMuestra())) {
				// ok = false;
				//
				// } else {
				// this.despachoTransaccion.setContraMuestra(this.despachoTransaccion.getContraMuestra().trim());
				// }

				if (this.isVacio(this.despachoTransaccion.getFlete())) {
					ok = false;

				} else {
					this.despachoTransaccion.setFlete(this.despachoTransaccion.getFlete().trim());
				}

				// if (this.isVacio(this.despachoTransaccion.getHojaSeguridad())) {
				// ok = false;
				//
				// } else {
				// this.despachoTransaccion.setHojaSeguridad(this.despachoTransaccion.getHojaSeguridad().trim());
				// }

				if (this.isVacio(this.despachoTransaccion.getObra())) {
					ok = false;

				} else {
					this.despachoTransaccion.setObra(this.despachoTransaccion.getObra().trim());
				}

				if (this.isVacio(this.despachoTransaccion.getOrdenCompra())) {
					ok = false;

				} else {
					this.despachoTransaccion.setOrdenCompra(this.despachoTransaccion.getOrdenCompra().trim());
				}

				if (this.isVacio(this.despachoTransaccion.getPedido())) {
					ok = false;

				} else {
					this.despachoTransaccion.setPedido(this.despachoTransaccion.getPedido().trim());
				}

				if (this.isVacio(this.despachoTransaccion.getPlaca())) {
					ok = false;

				} else {
					this.despachoTransaccion.setPlaca(this.despachoTransaccion.getPlaca().trim());
				}

				if (this.isVacio(this.despachoTransaccion.getPresentacion())) {
					ok = false;

				} else {
					this.despachoTransaccion.setPresentacion(this.despachoTransaccion.getPresentacion().trim());
				}

				if (this.isVacio(this.despachoTransaccion.getProducto())) {
					ok = false;

				} else {
					this.despachoTransaccion.setProducto(this.despachoTransaccion.getProducto().trim());
				}

				if (this.isVacio(this.despachoTransaccion.getRemolque())) {
					ok = false;

				} else {
					this.despachoTransaccion.setRemolque(this.despachoTransaccion.getRemolque().trim());
				}

				// if (this.isVacio(this.despachoTransaccion.getReporteCalidad())) {
				// ok = false;
				//
				// } else {
				// this.despachoTransaccion.setReporteCalidad(this.despachoTransaccion.getReporteCalidad().trim());
				// }

				// if (this.isVacio(this.despachoTransaccion.getTarjetaEmergencia())) {
				// ok = false;
				//
				// } else {
				// this.despachoTransaccion.setTarjetaEmergencia(this.despachoTransaccion.getTarjetaEmergencia().trim());
				// }

				if (this.isVacio(this.despachoTransaccion.getTipoDespacho())) {
					ok = false;

				} else {
					this.despachoTransaccion.setTipoDespacho(this.despachoTransaccion.getTipoDespacho().trim());
				}

				if (this.isVacio(this.despachoTransaccion.getTransporte())) {
					ok = false;

				} else {
					this.despachoTransaccion.setTransporte(this.despachoTransaccion.getTransporte().trim());
				}

				if (this.isVacio(this.despachoTransaccion.getUbicacion())) {
					ok = false;

				} else {
					this.despachoTransaccion.setUbicacion(this.despachoTransaccion.getUbicacion().trim());
				}

				if (this.despachoTransaccion.istTarjetaEmergencia()) {
					this.despachoTransaccion.setTarjetaEmergencia(IConstantes.AFIRMACION);
				} else {
					this.despachoTransaccion.setTarjetaEmergencia(IConstantes.NEGACION);
				}

				if (this.despachoTransaccion.istContraMuestra()) {
					this.despachoTransaccion.setContraMuestra(IConstantes.AFIRMACION);
				} else {
					this.despachoTransaccion.setContraMuestra(IConstantes.NEGACION);
				}

				if (this.despachoTransaccion.istHoja()) {
					this.despachoTransaccion.setHojaSeguridad(IConstantes.AFIRMACION);
				} else {
					this.despachoTransaccion.setHojaSeguridad(IConstantes.NEGACION);
				}

				if (this.despachoTransaccion.istReporteCalidad()) {
					this.despachoTransaccion.setReporteCalidad(IConstantes.AFIRMACION);
				} else {
					this.despachoTransaccion.setReporteCalidad(IConstantes.NEGACION);
				}

				if (this.despachoTransaccion.istCumple()) {
					this.despachoTransaccion.setCumple(IConstantes.AFIRMACION);
				} else {
					this.despachoTransaccion.setCumple(IConstantes.NEGACION);
				}

				if (isMostrarObservacionesCausa(this.despachoTransaccion)) {
					if (!(this.despachoTransaccion.getObservacionesRetraso() != null && !this.despachoTransaccion.getObservacionesRetraso().trim().equals(""))) {
						ok = false;
						this.mostrarMensajeGlobalPersonalizado("Debido a que escribio en alguna causa de restrasodebe escribir alguna observación de restraso", "advertencia");
					}

				} else {
					this.despacho.setObservacionesRetraso(null);
				}

			}

			if (!ok) {
				this.mostrarMensajeGlobal("campoEstaVacio", "advertencia");
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

		return ok;

	}

	// publicos

	/**
	 * Obtiene el pedido digitado desde la bd de siesa
	 */
	public void consultarPedidoSiesa() {
		try {

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Método que me selecciona el nombre del cliente, lo busca y llena el nit y
	 * otros
	 * 
	 * @param event
	 */
	public void onItemSelectTransaccion(SelectEvent event) {

		try {
			if (event != null && event.getObject() != null && !event.getObject().toString().trim().equals("") && this.despachoTransaccion != null) {
				Cliente cliente = IConsultasDAO.getClientePorNombre(event.getObject().toString().trim());
				if (cliente != null && cliente.getNit() != null) {
					this.despachoTransaccion.getCliente().setId(cliente.getId());
					this.despachoTransaccion.getCliente().setNit(cliente.getNit());
					this.despachoTransaccion.getCliente().setNombre(cliente.getNombre());
				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Método que me selecciona el nombre del cliente, lo busca y llena el nit y
	 * otros
	 * 
	 * @param event
	 */
	public void onItemSelect(SelectEvent event) {

		try {
			if (event != null && event.getObject() != null && !event.getObject().toString().trim().equals("") && this.despacho != null) {
				Cliente cliente = IConsultasDAO.getClientePorNombre(event.getObject().toString().trim());
				if (cliente != null && cliente.getNit() != null) {
					this.despacho.getCliente().setId(cliente.getId());
					this.despacho.getCliente().setNit(cliente.getNit());
					this.despacho.getCliente().setNombre(cliente.getNombre());
				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Obtiene un método de autocompletar para el nombre del cliente
	 * 
	 * @param aTexto
	 * @return clientes
	 */
	public List<String> usarAutocompletarEditar(String aTexto) {
		final List<String> clientes = new ArrayList<String>();
		try {

			if (aTexto != null && !aTexto.equals("")) {
				Cliente cliente = new Cliente();
				cliente.setNombre(aTexto.trim().toUpperCase());
				List<Cliente> listadoClientes = IConsultasDAO.getClientesLimitados(cliente);

				if (listadoClientes != null && listadoClientes.size() > 0) {

					listadoClientes.forEach(p -> clientes.add(p.getNombre()));
				}

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return clientes;
	}

	/**
	 * Asigna conductor de siesa
	 * 
	 * @param aConductor
	 * @param aModal
	 */
	public void asignarConductorSiesaDespacho(Conductor aConductor, String aModal) {
		aConductor.setNombre(aConductor.getNombre().trim());

		if (aConductor.getDocumento() == null && aConductor.gettIdentificacionConductor() != null) {
			aConductor.setDocumento(aConductor.gettIdentificacionConductor().trim());
			this.despacho.setDocumentoNulo("C"); // DE CODIGO DOCUMENTO

		}

		if (aConductor.gettIdentificacionConductor() == null) {
			this.despacho.setDocumentoNulo("I"); // DE IDENTIFICACION DE
		}

		aConductor.setDocumento(aConductor.getDocumento().trim());
		this.despacho.setConductor(aConductor);
		this.despacho.setPlaca(aConductor.getPlaca().trim());
		this.despacho.setRemolque(aConductor.getRemolque());
		this.despacho.setIdentificacionConductor(aConductor.gettIdentificacionConductor().trim());
		this.despacho.setCodigoTransportador(aConductor.gettCodigoTransportador().trim());
		this.despacho.setSucursalTransportador(aConductor.gettSucursalTransportador().trim());

		if (aModal != null && aModal.equals(IConstantes.AFIRMACION)) {
			this.cerrarModal("panelConductorSiesa");

		}

		this.mostrarMensajeGlobalPersonalizado("Conductor de SIESA asignado a Green Card, complete los datos faltantes", "exito");
	}

	/**
	 * Asigna un pedido a la base
	 * 
	 * @param aPedidoBase
	 */
	public void asignarPedidoDespacho(PedidoBase aPedidoBase, String aModal) {
		try {
			this.despacho.getCliente().setNit(aPedidoBase.getNitClienteFacturar());
			this.despacho.getCliente().setNombre(aPedidoBase.getRazonSocial());
			this.despacho.setCodigoProducto(aPedidoBase.getCodigoItem());
			this.despacho.setProducto(aPedidoBase.getDescripcionItem());
			if (aPedidoBase.getUbicacion() != null) {
				this.despacho.setUbicacion(aPedidoBase.getUbicacion().trim());
			}
			this.despacho.setOrdenCompra(aPedidoBase.getNumeroOrdenCompra().trim());

			// por ahora SI cambiamos la fecha del despacho
			//// por
			// peticion*******************this.despacho.setFecha(aPedidoBase.getFechaPedido());
			this.despacho.setFechaOriginalSiesa(aPedidoBase.getFechaPedido());

			String parteA = null;
			Cliente cliente = null;

			// analiza nit
			if (!this.isVacio(this.despacho.getCliente().getNit())) {
				String[] partes = this.despacho.getCliente().getNit().split("-");

				if (partes != null && partes.length == 1) {
					parteA = this.despacho.getCliente().getNit().toUpperCase().trim();

				} else {
					parteA = partes[0].toUpperCase().trim();

				}

				cliente = IConsultasDAO.getCliente(parteA);
				if (cliente != null && cliente.getId() != null) {
					this.despacho.setId(cliente.getId());

				} else {
					this.despacho.getCliente().setId(null);
					this.mostrarMensajeGlobal("clienteNuevo", "advertencia");
				}

			}

			if (aModal != null && aModal.equals(IConstantes.AFIRMACION)) {
				this.cerrarModal("panelPedidoSiesa");

			}
			this.despacho.setCentroOperacion(aPedidoBase.getCentroOperacion());

			this.mostrarMensajeGlobalPersonalizado("Pedido de SIESA asignado a Green Card, complete los datos faltantes", "exito");

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	/**
	 * busca un pedido en siesa
	 */
	public void buscarConductorSiesa() {

		try {
			this.conductoresSiesa = null;
			if (this.despacho != null && !this.despacho.getConductor().getDocumento().trim().equals("")) {

				this.conductoresSiesa = IConsultasDAO.getConductoresSiesa(this.despacho.getConductor());

				if (this.conductoresSiesa != null && this.conductoresSiesa.size() > 0) {

					if (this.conductoresSiesa.size() > 1) {

						this.abrirModal("panelConductorSiesa");

					} else {

						asignarConductorSiesaDespacho(this.conductoresSiesa.get(0), null);

					}

				}

			}
		} catch (Exception e) {

		}

	}

	/**
	 * busca un pedido en siesa
	 */
	public void buscarPedidoSiesa() {

		List<PedidoBase> pedidosItems = null;
		int i = 0;
		Integer numero = 0;
		String codigosItems = "";
		String descripcionItems = "";
		try {
			this.pedidosBaseCrear = null;
			if (this.despacho != null && !this.despacho.getPedido().trim().equals("")) {
				PedidoBase pedidoBase = new PedidoBase();
				pedidoBase.setNumeroDocumento(Integer.parseInt(this.despacho.getPedido().trim().toUpperCase()));

				this.pedidosBaseCrear = IConsultasDAO.getPedidoBase(pedidoBase);

				if (this.pedidosBaseCrear != null && this.pedidosBaseCrear.size() > 0) {

					for (PedidoBase pb : this.pedidosBaseCrear) {
						pb.setCodigoItem("");
						pb.setDescripcionItem("");
						codigosItems = "";
						descripcionItems = "";

						pedidosItems = IConsultasDAO.getPedidoBaseItems(pb);
						if (pedidosItems != null && pedidosItems.size() > 0) {
							if (pedidosItems.size() > 1) {
								i = 0;
								for (PedidoBase p : pedidosItems) {
									i++;

									if (p.gettCantidad().intValue() > 1) {

										codigosItems += "" + i + "- " + p.getCodigoItem() + " (" + p.gettCantidad() + " items)\n";
										descripcionItems += "" + i + "- " + p.getDescripcionItem() + " (" + p.gettCantidad() + " items)\n";

									} else {

										codigosItems += "" + i + "- " + p.getCodigoItem() + "\n";
										descripcionItems += "" + i + "- " + p.getDescripcionItem() + "\n";

									}

								}
							} else {
								i = 0;
								for (PedidoBase p : pedidosItems) {
									i++;
									if (p.gettCantidad().intValue() > 1) {

										codigosItems += "" + i + "- " + p.getCodigoItem() + " (" + p.gettCantidad() + " items)\n";
										descripcionItems += "" + i + "- " + p.getDescripcionItem() + " (" + p.gettCantidad() + " items)\n";

									} else {

										codigosItems += p.getCodigoItem() + "\n";
										descripcionItems += p.getDescripcionItem() + "\n";

									}
								}

							}
						}
						pb.setCodigoItem(codigosItems);
						pb.setDescripcionItem(descripcionItems);

					}

					if (this.pedidosBaseCrear.size() > 1) {

						this.abrirModal("panelPedidoSiesa");

					} else {

						asignarPedidoDespacho(this.pedidosBaseCrear.get(0), null);

					}

				} else {

					this.mostrarMensajeGlobalPersonalizado("En SIESA, no existe el número de pedido digitado", "advertencia");
					this.despacho.setCodigoProducto(null);
					this.despacho.setProducto(null);
					this.despacho.setCentroOperacion(null);
					this.despacho.setPedido(null);
					this.despacho.setUbicacion(null);
					this.despacho.setFecha(new Date());
					this.despacho.setOrdenCompra(null);
					this.despacho.setCliente(new Cliente());
					this.despacho.setFechaOriginalSiesa(null);
					numero = IConsultasDAO.getSiguienteEntrega();
					this.despacho.setNumeroEntrega(numero);
					this.despacho.settNumeroEntregaCadena(this.getSiguienteDespacho(numero));

				}

			} else {

				this.despacho.setCodigoProducto(null);
				this.despacho.setProducto(null);
				this.despacho.setCentroOperacion(null);
				this.despacho.setPedido(null);
				this.despacho.setUbicacion(null);
				this.despacho.setFecha(new Date());
				this.despacho.setOrdenCompra(null);
				this.despacho.setCliente(new Cliente());
				this.despacho.setFechaOriginalSiesa(null);
				numero = IConsultasDAO.getSiguienteEntrega();
				this.despacho.setNumeroEntrega(numero);
				this.despacho.settNumeroEntregaCadena(this.getSiguienteDespacho(numero));

			}
		} catch (Exception e) {
			this.mostrarMensajeGlobalPersonalizado("El número de pedido para Siesa debe ser numérico", "advertencia");
			this.despacho.setCodigoProducto(null);
			this.despacho.setProducto(null);
			this.despacho.setCentroOperacion(null);
			this.despacho.setPedido(null);
			this.despacho.setUbicacion(null);
			this.despacho.setFecha(new Date());
			this.despacho.setOrdenCompra(null);
			this.despacho.setCliente(new Cliente());
			this.despacho.setFechaOriginalSiesa(null);
			try {
				numero = IConsultasDAO.getSiguienteEntrega();
			} catch (Exception e1) {

			}
			this.despacho.setNumeroEntrega(numero);
			this.despacho.settNumeroEntregaCadena(this.getSiguienteDespacho(numero));

		}

	}

	/**
	 * Obtiene un método de autocompletar para el nombre del cliente
	 * 
	 * @param aTexto
	 * @return clientes
	 */
	public List<String> usarAutocompletar(String aTexto) {
		final List<String> clientes = new ArrayList<String>();
		try {

			if (aTexto != null && !aTexto.equals("")) {

				Cliente cliente = new Cliente();
				cliente.setNombre(aTexto.trim().toUpperCase());
				List<Cliente> listadoClientes = IConsultasDAO.getClientesLimitados(cliente);

				if (listadoClientes != null && listadoClientes.size() > 0) {

					listadoClientes.forEach(p -> clientes.add(p.getNombre()));

				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return clientes;
	}

	/**
	 * Obtiene toda la trazabilidad
	 */
	public void consultarTrazabilidad() {
		try {

			this.auditorias = IConsultasDAO.getAuditoria(this.despachoTransaccion.getId());

		} catch (Exception e) {
			IConstantes.log.error(e, e);

		}
	}

	/**
	 * Guarda la auditoria de lo que se hace
	 */
	public void guardarAuditoria(Conexion conexion, Despacho aDespacho, String aConcepto) {
		try {
			SimpleDateFormat formatoHora = new SimpleDateFormat("HH");
			SimpleDateFormat formatoMinuto = new SimpleDateFormat("mm");
			SimpleDateFormat formatoSegundo = new SimpleDateFormat("ss");

			Date fechaHoy = new Date();

			Auditoria auditoria = new Auditoria();
			auditoria.setConcepto(aConcepto);
			auditoria.setDespacho(aDespacho);
			auditoria.setPersonal(this.administrarSesionCliente.getAdministradorSesion());
			auditoria.setFecha(fechaHoy);
			auditoria.setHora(Integer.parseInt(formatoHora.format(fechaHoy)));
			auditoria.setMinuto(Integer.parseInt(formatoMinuto.format(fechaHoy)));
			auditoria.setSegundo(Integer.parseInt(formatoSegundo.format(fechaHoy)));
			auditoria.getCamposBD();
			conexion.insertarBD(auditoria.getEstructuraTabla().getTabla(), auditoria.getEstructuraTabla().getPersistencia());

		} catch (Exception e) {
			IConstantes.log.error(e, e);

		}
	}

	/**
	 * Verifica que un despacho no esté abrierto
	 * 
	 * @return abierto
	 */
	public boolean isDespachoAbierto() {
		boolean abierto = false;

		try {
			if (this.despacho.getNumeroInterno() != null && !this.despacho.getNumeroInterno().trim().equals("")) {

				Despacho despacho = new Despacho();
				despacho.setNumeroInterno(this.despacho.getNumeroInterno().trim());
				despacho.settComprobarAbierto(IConstantes.AFIRMACION);

				List<Despacho> despachos = IConsultasDAO.getDespachos(despacho, 1);
				if (despachos != null && despachos.size() > 0) {
					abierto = true;
					this.mostrarMensajeGlobal("despachoAbierto", "advertencia");
				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);

		}

		return abierto;

	}

	/**
	 * Consulta un cliente rapido para editarlo
	 */
	public void consultarConductorFrecuenteTransaccion() {

		try {
			Conductor conductor = new Conductor();

			// analiza documento
			if (!this.isVacio(this.despachoTransaccion.getConductor().getDocumento())) {

				conductor.setDocumento(this.despachoTransaccion.getConductor().getDocumento());
				List<Conductor> conductores = IConsultasDAO.getConductores(conductor);
				if (conductores != null && conductores.size() > 0) {
					this.despachoTransaccion.setConductor(conductores.get(0));
					this.despachoTransaccion.setPlaca(this.despachoTransaccion.getConductor().getPlaca());
					this.despachoTransaccion.setRemolque(this.despachoTransaccion.getConductor().getRemolque());
				} else {
					this.despachoTransaccion.getConductor().setNombre(null);
					this.despachoTransaccion.getConductor().setId(null);
					this.despachoTransaccion.getConductor().setPlaca(null);
					this.despachoTransaccion.getConductor().setRemolque(null);
					this.despachoTransaccion.setPlaca(null);
					this.despachoTransaccion.setRemolque(null);

					this.mostrarMensajeGlobal("conductorNuevo", "advertencia");
				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);

		}
	}

	/**
	 * Consultar conductor frecuente
	 */
	public void consultarConductorFrecuente() {

		try {
			Conductor conductor = new Conductor();

			// analiza documento
			if (!this.isVacio(this.despacho.getConductor().getDocumento())) {

				conductor.setDocumento(this.despacho.getConductor().getDocumento());

				if (this.despacho != null && this.despacho.getSoftwareBase() != null && this.despacho.getSoftwareBase().equals("S")) {
					// SI ES DE SIESA ES EXIGIBLE SOLO CONDUCTORES DE SIESA

					buscarConductorSiesa();

					if (!(this.conductoresSiesa != null && this.conductoresSiesa.size() > 0)) {

						this.despacho.getConductor().setNombre(null);
						this.despacho.getConductor().setId(null);
						this.despacho.getConductor().setPlaca(null);
						this.despacho.getConductor().setDocumento(null);
						this.despacho.setSucursalTransportador(null);
						this.despacho.setCodigoTransportador(null);
						this.despacho.setIdentificacionConductor(null);
						this.despacho.getConductor().setRemolque(null);
						this.despacho.setPlaca(null);
						this.despacho.setRemolque(null);

						this.mostrarMensajeGlobalPersonalizado("El documento ingresado no existe en el maestro de conductores de SIESA, como la base del pedido es SIESA, debe existir", "advertencia");

					}

				} else {

					buscarConductorSiesa();

					if (!(this.conductoresSiesa != null && this.conductoresSiesa.size() > 0)) {
						// si no existe en siesa intenta con los locales
						List<Conductor> conductores = IConsultasDAO.getConductores(conductor);

						if (conductores != null && conductores.size() > 0) {
							this.despacho.setConductor(conductores.get(0));
							this.despacho.setPlaca(this.despacho.getConductor().getPlaca());
							this.despacho.setRemolque(this.despacho.getConductor().getRemolque());
							this.despacho.setSucursalTransportador(null);
							this.despacho.setCodigoTransportador(null);
							this.despacho.setIdentificacionConductor(null);
						} else {
							this.despacho.getConductor().setNombre(null);
							this.despacho.getConductor().setId(null);
							this.despacho.getConductor().setPlaca(null);
							this.despacho.getConductor().setRemolque(null);
							this.despacho.setPlaca(null);
							this.despacho.setRemolque(null);
							this.despacho.setSucursalTransportador(null);
							this.despacho.setCodigoTransportador(null);
							this.despacho.setIdentificacionConductor(null);

							this.mostrarMensajeGlobal("conductorNuevo", "advertencia");
						}

					}

				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);

		}
	}

	/**
	 * Consulta un cliente rapido para editarlo
	 */
	public void consultarClienteFrecuenteTransaccion() {

		try {
			String parteA = null;
			Cliente cliente = null;

			// analiza nit
			if (!this.isVacio(this.despachoTransaccion.getCliente().getNit())) {
				String[] partes = this.despachoTransaccion.getCliente().getNit().split("-");

				if (partes != null && partes.length == 1) {
					parteA = this.despachoTransaccion.getCliente().getNit().toUpperCase().trim();

				} else {
					parteA = partes[0].toUpperCase().trim();

				}

				cliente = IConsultasDAO.getCliente(parteA);
				if (cliente != null && cliente.getId() != null) {
					this.despachoTransaccion.setCliente(cliente);

				} else {
					this.despachoTransaccion.getCliente().setId(null);
					this.despachoTransaccion.getCliente().setNombre(null);
					this.mostrarMensajeGlobal("clienteNuevo", "advertencia");
				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);

		}
	}

	/**
	 * Consulta un cliente rapidamente para saber si se guarda o no
	 */
	public void consultarClienteFrecuente() {

		try {
			String parteA = null;
			Cliente cliente = null;

			// analiza nit
			if (!this.isVacio(this.despacho.getCliente().getNit())) {
				String[] partes = this.despacho.getCliente().getNit().split("-");

				if (partes != null && partes.length == 1) {
					parteA = this.despacho.getCliente().getNit().toUpperCase().trim();

				} else {
					parteA = partes[0].toUpperCase().trim();

				}

				cliente = IConsultasDAO.getCliente(parteA);
				if (cliente != null && cliente.getId() != null) {
					this.despacho.setCliente(cliente);

				} else {
					this.despacho.getCliente().setId(null);
					this.despacho.getCliente().setNombre(null);
					this.mostrarMensajeGlobal("clienteNuevo", "advertencia");
				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);

		}
	}

	/**
	 * Obtiene una bascula completa
	 * 
	 * @param aBascula
	 * @return bascula
	 */
	public Bascula getBasculaCompleta(Integer aBascula) {
		Bascula bascula = null;
		try {
			if (aBascula != null) {
				Bascula basculaC = new Bascula();
				basculaC.setId(aBascula);
				List<Bascula> basculasC = IConsultasDAO.getBasculas(basculaC);
				if (basculasC != null && basculasC.size() > 0) {
					bascula = basculasC.get(0);
				}

			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

		return bascula;

	}

	/**
	 * Calcula la diferencia de pesos. El peso neto
	 */
	public void calcularDiferencia() {
		if (this.despachoTransaccion != null && this.despachoTransaccion.getPesoFinal() != null && this.despachoTransaccion.getPesoInicial() != null) {

			this.despachoTransaccion.setDiferenciaPeso(this.despachoTransaccion.getPesoFinal().intValue() - this.despachoTransaccion.getPesoInicial().intValue());

			if (this.despachoTransaccion.getDestare() != null && !this.despachoTransaccion.getPresentacion().equals("" + this.getMensaje("aGranel"))) {
				this.despachoTransaccion.setDiferenciaPeso(this.despachoTransaccion.getDiferenciaPeso().intValue() - this.despachoTransaccion.getDestare().intValue());
				// if (this.despachoTransaccion.getDiferenciaPeso() < 0) {
				// this.despachoTransaccion.setDiferenciaPeso(this.despachoTransaccion.getPesoFinal().intValue()
				// - this.despachoTransaccion.getPesoInicial().intValue());
				// this.despachoTransaccion.setDestare(null);
				// this.mostrarMensajeGlobalPersonalizado("El peso neto no puede ser
				// negativo. Revise Destare y/o peso final", "advertencia");
				// }
			}

			if (this.despachoTransaccion.getDiferenciaPeso() < 0) {
				this.mostrarMensajeGlobalPersonalizado("El peso neto no puede ser negativo. Revise Destare y/o peso final", "advertencia");
			}

		} else {

			this.despachoTransaccion.setDiferenciaPeso(null);
		}
	}

	/**
	 * Verifica que un despacho esté o no abierto
	 */
	public void consultarDespachoAbierto() {

		isDespachoAbierto();

	}

	/**
	 * Consulta el despacho para que se muestre en bahía
	 */
	public void consultarDespachoBahia() {
		Integer copiaBasculaTransaccion = null;
		Conexion conexion = new Conexion();

		try {
			String copiaDespacho = this.despachoTransaccion.getNumeroInterno();

			Despacho despacho = new Despacho();
			despacho.setNumeroInterno(this.despachoTransaccion.getNumeroInterno());
			List<Despacho> despachos = IConsultasDAO.getDespachos(despacho, 1);
			if (despachos != null && despachos.size() > 0) {

				this.despachoTransaccion = despachos.get(0);

				if (!(this.despachoTransaccion.getTicketMedidor() != null && !this.despachoTransaccion.getTicketMedidor().trim().equals(""))) {
					this.despachoTransaccion.setTicketMedidor("" + this.despachoTransaccion.getId());
				}

				if (!this.despachoTransaccion.getTipoDespacho().equals(IConstantes.NO_APLICA)) {

					if (!(this.despachoTransaccion.getBasculaInicial() != null && this.despachoTransaccion.getBasculaInicial().getId() != null)) {

						if (!this.despachoTransaccion.getTipoDespacho().equals(IConstantes.TIPO_MEDIDOR)) {
							this.mostrarMensajeGlobal("pesoInicialNoRegistrado", "advertencia");
						}

					}

				} else {

					this.despachoTransaccion = null;
					this.getDespachoTransaccion();
					this.despachoTransaccion.setNumeroInterno(copiaDespacho);
					this.despachoTransaccion.settBasculaTransaccion(copiaBasculaTransaccion);
					this.mostrarMensajeGlobal("tarjetaNoAplica", "advertencia");
				}

			} else {

				this.despachoTransaccion = null;
				this.getDespachoTransaccion();
				this.despachoTransaccion.setNumeroInterno(copiaDespacho);
				this.despachoTransaccion.settBasculaTransaccion(copiaBasculaTransaccion);

				this.mostrarMensajeGlobal("noExisteDesapacho", "advertencia");
			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Consulta un despacho por el número de referencia
	 */
	public void consultarDespachoPesoFinal() {
		Integer copiaBasculaTransaccion = null;
		Conexion conexion = new Conexion();

		try {
			String copiaDespacho = this.despachoTransaccion.getNumeroInterno();
			if (this.despachoTransaccion.getBasculaFinal() != null && this.despachoTransaccion.getBasculaFinal().getId() != null) {
				copiaBasculaTransaccion = this.despachoTransaccion.getBasculaFinal().getId();
			}

			Despacho despacho = new Despacho();
			despacho.setNumeroInterno(this.despachoTransaccion.getNumeroInterno());
			List<Despacho> despachos = IConsultasDAO.getDespachos(despacho, 1);
			if (despachos != null && despachos.size() > 0) {

				this.despachoTransaccion = despachos.get(0);

				if (!this.despachoTransaccion.getTipoDespacho().equals(IConstantes.NO_APLICA)) {

					this.despachoTransaccion.settNumeroEntrega(this.despachoTransaccion.getNumeroEntrega());
					this.despachoTransaccion.settTicketSalida(this.despachoTransaccion.getTicketSalida());

					if (!(this.despachoTransaccion.getBasculaInicial() != null && this.despachoTransaccion.getBasculaInicial().getId() != null)) {
						this.mostrarMensajeGlobal("pesoInicialNoRegistrado", "advertencia");

					}

					// no ha pasado bahía
					if (!(this.despachoTransaccion.getLote() != null && !this.despachoTransaccion.getLote().trim().equals(""))) {
						this.mostrarMensajeGlobal("noPasadoBahia", "advertencia");

					}

				} else {

					this.despachoTransaccion = null;
					this.getDespachoTransaccion();
					this.despachoTransaccion.setNumeroInterno(copiaDespacho);
					this.despachoTransaccion.settBasculaTransaccion(copiaBasculaTransaccion);

					this.mostrarMensajeGlobal("tarjetaNoAplica", "advertencia");

				}

			} else {

				this.despachoTransaccion = null;
				this.getDespachoTransaccion();
				this.despachoTransaccion.setNumeroInterno(copiaDespacho);
				this.despachoTransaccion.settBasculaTransaccion(copiaBasculaTransaccion);

				this.mostrarMensajeGlobal("noExisteDesapacho", "advertencia");
			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Consulta un despacho por el número de referencia
	 */
	public void consultarDespachoPesoInicial() {
		Integer copiaBasculaTransaccion = null;
		Conexion conexion = new Conexion();

		try {
			String copiaDespacho = this.despachoTransaccion.getNumeroInterno();
			if (this.despachoTransaccion.getBasculaInicial() != null && this.despachoTransaccion.getBasculaInicial().getId() != null) {
				copiaBasculaTransaccion = this.despachoTransaccion.getBasculaInicial().getId();
			}

			Despacho despacho = new Despacho();
			despacho.setNumeroInterno(this.despachoTransaccion.getNumeroInterno());
			List<Despacho> despachos = IConsultasDAO.getDespachos(despacho, 1);
			if (despachos != null && despachos.size() > 0) {

				this.despachoTransaccion = despachos.get(0);

				if (!this.despachoTransaccion.getTipoDespacho().equals(IConstantes.NO_APLICA)) {

					if (this.despachoTransaccion.getBasculaFinal() != null && this.despachoTransaccion.getBasculaFinal().getId() != null) {
						this.mostrarMensajeGlobal("pesoFinalRegistrado", "advertencia");

					}

				} else {

					this.despachoTransaccion = null;
					this.getDespachoTransaccion();
					this.despachoTransaccion.setNumeroInterno(copiaDespacho);
					this.despachoTransaccion.settBasculaTransaccion(copiaBasculaTransaccion);

					this.mostrarMensajeGlobal("tarjetaNoAplica", "advertencia");

				}

			} else {

				this.despachoTransaccion = null;
				this.getDespachoTransaccion();
				this.despachoTransaccion.setNumeroInterno(copiaDespacho);
				this.despachoTransaccion.settBasculaTransaccion(copiaBasculaTransaccion);

				this.mostrarMensajeGlobal("noExisteDesapacho", "advertencia");
			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		} finally {
			conexion.cerrarConexion();
		}

	}

	public String getTituloTiempos(TiempoEstandar aTiempoEstandar) {
		String titulo = "";
		try {
			String tipo = "";
			if (aTiempoEstandar != null && aTiempoEstandar.getTipoDespacho() != null && aTiempoEstandar.getTipoDespacho().equals("B")) {
				tipo = "POR BASCULA";
			}
			if (aTiempoEstandar != null && aTiempoEstandar.getTipoDespacho() != null && aTiempoEstandar.getTipoDespacho().equals("M")) {
				tipo = "POR MEDIDOR";
			}

			titulo = "TIEMPO DESPACHOS " + tipo + " DESDE " + aTiempoEstandar.getPrimeraBahia().getNombre().toUpperCase();
			if (aTiempoEstandar != null && aTiempoEstandar.getSegundaBahia() != null && aTiempoEstandar.getSegundaBahia().getId() != null) {
				titulo = titulo + " Y " + aTiempoEstandar.getSegundaBahia().getNombre().toUpperCase();
			}

			aTiempoEstandar.settLabel(titulo);

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}
		return titulo;

	}

	public void consultarInformeTiempos() {
		Conexion conexion = new Conexion();

		try {
			List<TiempoEstandar> tiempos = IConsultasDAO.getTiempos(null);
			this.tiemposTabla = null;
			List<Despacho> despachosT = this.despachos = IConsultasDAO.getDespachos(this.despachoConsulta, null);
			this.despachos = null;
			if (despachosT != null && despachosT.size() > 0 && tiempos != null && tiempos.size() > 0) {
				this.despachos = new ArrayList<Despacho>();
				for (Despacho d : despachosT) {
					if (d.getTipoDespacho() != null && !d.getTipoDespacho().equals("N")) {

						if (!d.getTipoDespacho().equals("M") && d.getDiferenciaPeso() != null) {

							for (TiempoEstandar p : tiempos) {
								if ((p.getPrimeraBahia().getId().intValue() == d.getBahia().getId().intValue() && p.getTipoDespacho().equals(d.getTipoDespacho())) || (p.getSegundaBahia() != null && p.getSegundaBahia().getId() != null && p.getSegundaBahia().getId().intValue() == d.getBahia().getId().intValue() && p.getTipoDespacho() != null && p.getTipoDespacho().equals(d.getTipoDespacho()))) {

									if (d.getFechaHoraCreoVenta() != null && d.getFechaHoraBasculaFinal() != null && d.getFechaHoraBasculaInicial() != null && d.getFechaHoraBahia() != null) {

										d.settTiempoEstandar(p.getTiempoEstandar());
										d.settTiempoGastado(this.getMinutosDosFechas(d.getFechaHoraCreoVenta(), d.getFechaHoraBasculaFinal()));
										this.despachos.add(d);
										break;

									}
								}
							}

						}
						if (d.getTipoDespacho().equals("M") && d.getTicketMedidor() != null && !d.getTicketMedidor().trim().equals("")) {

							for (TiempoEstandar p : tiempos) {
								if ((p.getPrimeraBahia().getId().intValue() == d.getBahia().getId().intValue() && p.getTipoDespacho().equals(d.getTipoDespacho())) || (p.getSegundaBahia() != null && p.getSegundaBahia().getId() != null && p.getSegundaBahia().getId().intValue() == d.getBahia().getId().intValue() && p.getTipoDespacho() != null && p.getTipoDespacho().equals(d.getTipoDespacho()))) {
									if (d.getFechaHoraCreoVenta() != null && d.getFechaHoraBahia() != null) {

										d.settTiempoEstandar(p.getTiempoEstandar());
										d.settTiempoGastado(this.getMinutosDosFechas(d.getFechaHoraCreoVenta(), d.getFechaHoraBahia()));
										this.despachos.add(d);
										break;
									}
								}

							}

						}

					}

				}

				if (this.despachos != null && this.despachos.size() > 0) {
					this.tiemposTabla = tiempos;

					for (TiempoEstandar t : this.tiemposTabla) {
						t.settDespachos(new ArrayList<Despacho>());
						t.setGraficoTiempo(new BarChartModel());
						ChartSeries periodos = new ChartSeries();
						ChartSeries metaReferencia = new ChartSeries();

						periodos.setLabel("TIEMPO OPERACION");
						metaReferencia.setLabel("ESTANDAR");

						for (Despacho d : this.despachos) {
							if (d.getTipoDespacho() != null && d.getTipoDespacho().equals(t.getTipoDespacho()) && d.getBahia() != null && d.getBahia().getId() != null && ((t.getPrimeraBahia() != null && t.getPrimeraBahia().getId() != null && t.getPrimeraBahia().getId().intValue() == d.getBahia().getId().intValue()) || (t.getSegundaBahia() != null && t.getSegundaBahia().getId() != null && t.getSegundaBahia().getId().intValue() == d.getBahia().getId().intValue()))) {

								// if (d.gettNumeroEntregaCadena() != null) {
								// if(d.gettTiempoGastado()==null){
								// d.settTiempoGastado(new Long(0));
								// }
								// if(d.gettTiempoEstandar()==null){
								// d.settTiempoEstandar(new Long(0));
								// }
								//
								// periodos.set(d.gettNumeroEntregaCadena(),
								// d.gettTiempoGastado());
								// metaReferencia.set(d.gettNumeroEntregaCadena(),
								// d.gettTiempoEstandar());
								// } else {
								// periodos.set(d.getNumeroEntrega(), d.gettTiempoGastado());
								// metaReferencia.set(d.getNumeroEntrega(),
								// d.gettTiempoEstandar());
								//
								// }

								t.gettDespachos().add(d);
							}
						}

						for (Despacho d : t.gettDespachos()) {
							periodos.set(d.gettNumeroEntregaCadena(), d.gettTiempoGastado());
							metaReferencia.set(d.gettNumeroEntregaCadena(), d.gettTiempoEstandar());
						}

						t.getGraficoTiempo().addSeries(periodos);
						t.getGraficoTiempo().addSeries(metaReferencia);

						t.getGraficoTiempo().setTitle("GRAFICA-" + getTituloTiempos(t));
						t.getGraficoTiempo().setAnimate(true);
						t.getGraficoTiempo().setLegendPosition("nw");
						Axis ejeX = t.getGraficoTiempo().getAxis(AxisType.X);
						ejeX.setTickAngle(-30);
						ejeX.setLabel("DESPACHO");

						Axis ejeY = t.getGraficoTiempo().getAxis(AxisType.Y);
						ejeY.setLabel("TIEMPO (MINUTOS)");

					}

				}

			} else {

				this.mostrarMensajeGlobalPersonalizado("No existe resultados para criterios indicados. Recuerde que solo s evalidan registros cerrados(procesos completos) y que se haya ingresado los tiempos estándar para compararlos", "advertencia");

			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		} finally {
			conexion.cerrarConexion();
		}

	}

	public void consultarInformeCausasRetraso() {
		Conexion conexion = new Conexion();
		try {
			this.graficoCausasNoOportunas = null;
			this.despachos = IConsultasDAO.getDespachos(this.despachoConsulta, null);

			this.causaOportuna = new Despacho();
			this.causaOportuna.setNumeroProduccion(0);
			this.causaOportuna.setNumeroDireccion(0);
			this.causaOportuna.setNumeroMantenimiento(0);
			this.causaOportuna.setNumeroCompras(0);
			this.causaOportuna.setNumeroTransporte(0);
			this.causaOportuna.setNumeroCartera(0);
			this.causaOportuna.setNumeroVentas(0);
			this.causaOportuna.setNumeroClientes(0);
			this.causaOportuna.setNumeroGestion(0);
			this.causaOportuna.setNumeroTotalCausas(0);
			this.causaOportuna.setNumeroTotalDespachos(0);

			this.causaInterna = new Despacho();
			this.causaInterna.setNumeroProduccion(0);
			this.causaInterna.setNumeroDireccion(0);
			this.causaInterna.setNumeroMantenimiento(0);
			this.causaInterna.setNumeroCompras(0);
			this.causaInterna.setNumeroTransporte(0);
			this.causaInterna.setNumeroCartera(0);
			this.causaInterna.setNumeroVentas(0);
			this.causaInterna.setNumeroClientes(0);
			this.causaInterna.setNumeroGestion(0);
			this.causaInterna.setNumeroTotalCausas(0);
			this.causaInterna.setNumeroTotalDespachos(0);

			if (this.despachos != null && this.despachos.size() > 0) {

				for (Despacho d : this.despachos) {

					if (d.getCumple() != null && d.getCumple().equals("N")) {

						this.causaOportuna.setNumeroTotalDespachos(this.causaOportuna.getNumeroTotalDespachos().intValue() + 1);

						if (d.getProduccion() != null && !d.getProduccion().trim().equals("")) {
							this.causaOportuna.setNumeroProduccion(this.causaOportuna.getNumeroProduccion().intValue() + 1);
						}

						if (d.getDireccionTecnica() != null && !d.getDireccionTecnica().trim().equals("")) {
							this.causaOportuna.setNumeroDireccion(this.causaOportuna.getNumeroDireccion().intValue() + 1);
						}

						if (d.getMantenimiento() != null && !d.getMantenimiento().trim().equals("")) {
							this.causaOportuna.setNumeroMantenimiento(this.causaOportuna.getNumeroMantenimiento().intValue() + 1);
						}

						if (d.getCompras() != null && !d.getCompras().trim().equals("")) {
							this.causaOportuna.setNumeroCompras(this.causaOportuna.getNumeroCompras().intValue() + 1);
						}

						if (d.getTransporteCausas() != null && !d.getTransporteCausas().trim().equals("")) {
							this.causaOportuna.setNumeroTransporte(this.causaOportuna.getNumeroTransporte().intValue() + 1);
						}

						if (d.getCartera() != null && !d.getCartera().trim().equals("")) {
							this.causaOportuna.setNumeroCartera(this.causaOportuna.getNumeroCartera().intValue() + 1);
						}

						if (d.getVentas() != null && !d.getVentas().trim().equals("")) {
							this.causaOportuna.setNumeroVentas(this.causaOportuna.getNumeroVentas().intValue() + 1);
						}

						if (d.getClienteCausas() != null && !d.getClienteCausas().trim().equals("")) {
							this.causaOportuna.setNumeroClientes(this.causaOportuna.getNumeroClientes().intValue() + 1);
						}

						if (d.getGestionIntegral() != null && !d.getGestionIntegral().trim().equals("")) {
							this.causaOportuna.setNumeroGestion(this.causaOportuna.getNumeroGestion().intValue() + 1);
						}
					}

					if (d.getCumple() != null && d.getCumple().equals("S")) {
						this.causaInterna.setNumeroTotalDespachos(this.causaInterna.getNumeroTotalDespachos().intValue() + 1);

						if (d.getProduccion() != null && !d.getProduccion().trim().equals("")) {
							this.causaInterna.setNumeroProduccion(this.causaInterna.getNumeroProduccion().intValue() + 1);
						}

						if (d.getDireccionTecnica() != null && !d.getDireccionTecnica().trim().equals("")) {
							this.causaInterna.setNumeroDireccion(this.causaInterna.getNumeroDireccion().intValue() + 1);
						}

						if (d.getMantenimiento() != null && !d.getMantenimiento().trim().equals("")) {
							this.causaInterna.setNumeroMantenimiento(this.causaInterna.getNumeroMantenimiento().intValue() + 1);
						}

						if (d.getCompras() != null && !d.getCompras().trim().equals("")) {
							this.causaInterna.setNumeroCompras(this.causaInterna.getNumeroCompras().intValue() + 1);
						}

						if (d.getTransporteCausas() != null && !d.getTransporteCausas().trim().equals("")) {
							this.causaInterna.setNumeroTransporte(this.causaInterna.getNumeroTransporte().intValue() + 1);
						}

						if (d.getCartera() != null && !d.getCartera().trim().equals("")) {
							this.causaInterna.setNumeroCartera(this.causaInterna.getNumeroCartera().intValue() + 1);
						}

						if (d.getVentas() != null && !d.getVentas().trim().equals("")) {
							this.causaInterna.setNumeroVentas(this.causaInterna.getNumeroVentas().intValue() + 1);
						}

						if (d.getClienteCausas() != null && !d.getClienteCausas().trim().equals("")) {
							this.causaInterna.setNumeroClientes(this.causaInterna.getNumeroClientes().intValue() + 1);
						}

						if (d.getGestionIntegral() != null && !d.getGestionIntegral().trim().equals("")) {
							this.causaInterna.setNumeroGestion(this.causaInterna.getNumeroGestion().intValue() + 1);
						}
					}

				}

				this.causaOportuna.setNumeroTotalCausas(this.causaOportuna.getNumeroProduccion().intValue() + this.causaOportuna.getNumeroDireccion().intValue() + this.causaOportuna.getNumeroMantenimiento().intValue() + this.causaOportuna.getNumeroCompras().intValue() + this.causaOportuna.getNumeroTransporte() + this.causaOportuna.getNumeroCartera().intValue() + this.causaOportuna.getNumeroVentas().intValue() + this.causaOportuna.getNumeroClientes() + this.causaOportuna.getNumeroGestion());
				this.causaInterna.setNumeroTotalCausas(this.causaInterna.getNumeroProduccion().intValue() + this.causaInterna.getNumeroDireccion().intValue() + this.causaInterna.getNumeroMantenimiento().intValue() + this.causaInterna.getNumeroCompras().intValue() + this.causaInterna.getNumeroTransporte() + this.causaInterna.getNumeroCartera().intValue() + this.causaInterna.getNumeroVentas().intValue() + this.causaInterna.getNumeroClientes() + this.causaInterna.getNumeroGestion());

				if (this.causaOportuna.getNumeroTotalCausas().intValue() > 0) {

					this.causaOportuna.setPorcentajeProduccion(this.getValorRedondeado(new BigDecimal((this.causaOportuna.getNumeroProduccion().intValue() * 100.0) / (this.causaOportuna.getNumeroTotalCausas() * 1.0)), 2));
					this.causaOportuna.setPorcentajeDireccion(this.getValorRedondeado(new BigDecimal((this.causaOportuna.getNumeroDireccion().intValue() * 100.0) / (this.causaOportuna.getNumeroTotalCausas() * 1.0)), 2));
					this.causaOportuna.setPorcentajeMantenimiento(this.getValorRedondeado(new BigDecimal((this.causaOportuna.getNumeroMantenimiento().intValue() * 100.0) / (this.causaOportuna.getNumeroTotalCausas() * 1.0)), 2));
					this.causaOportuna.setPorcentajeCompras(this.getValorRedondeado(new BigDecimal((this.causaOportuna.getNumeroCompras().intValue() * 100.0) / (this.causaOportuna.getNumeroTotalCausas() * 1.0)), 2));
					this.causaOportuna.setPorcentajeTransporte(this.getValorRedondeado(new BigDecimal((this.causaOportuna.getNumeroTransporte().intValue() * 100.0) / (this.causaOportuna.getNumeroTotalCausas() * 1.0)), 2));
					this.causaOportuna.setPorcentajeCartera(this.getValorRedondeado(new BigDecimal((this.causaOportuna.getNumeroCartera().intValue() * 100.0) / (this.causaOportuna.getNumeroTotalCausas() * 1.0)), 2));
					this.causaOportuna.setPorcentajeVentas(this.getValorRedondeado(new BigDecimal((this.causaOportuna.getNumeroVentas().intValue() * 100.0) / (this.causaOportuna.getNumeroTotalCausas() * 1.0)), 2));
					this.causaOportuna.setPorcentajeClientes(this.getValorRedondeado(new BigDecimal((this.causaOportuna.getNumeroClientes().intValue() * 100.0) / (this.causaOportuna.getNumeroTotalCausas() * 1.0)), 2));
					this.causaOportuna.setPorcentajeGestion(this.getValorRedondeado(new BigDecimal((this.causaOportuna.getNumeroGestion().intValue() * 100.0) / (this.causaOportuna.getNumeroTotalCausas() * 1.0)), 2));

				} else {

					this.causaOportuna.setPorcentajeProduccion(this.getValorRedondeado(new BigDecimal(0), 2));
					this.causaOportuna.setPorcentajeDireccion(this.getValorRedondeado(new BigDecimal(0), 2));
					this.causaOportuna.setPorcentajeMantenimiento(this.getValorRedondeado(new BigDecimal(0), 2));
					this.causaOportuna.setPorcentajeCompras(this.getValorRedondeado(new BigDecimal(0), 2));
					this.causaOportuna.setPorcentajeTransporte(this.getValorRedondeado(new BigDecimal(0), 2));
					this.causaOportuna.setPorcentajeCartera(this.getValorRedondeado(new BigDecimal(0), 2));
					this.causaOportuna.setPorcentajeVentas(this.getValorRedondeado(new BigDecimal(0), 2));
					this.causaOportuna.setPorcentajeClientes(this.getValorRedondeado(new BigDecimal(0), 2));
					this.causaOportuna.setPorcentajeGestion(this.getValorRedondeado(new BigDecimal(0), 2));

				}
				if (this.causaInterna.getNumeroTotalCausas().intValue() > 0) {

					this.causaInterna.setPorcentajeProduccion(this.getValorRedondeado(new BigDecimal((this.causaInterna.getNumeroProduccion().intValue() * 100.0) / (this.causaInterna.getNumeroTotalCausas() * 1.0)), 2));
					this.causaInterna.setPorcentajeDireccion(this.getValorRedondeado(new BigDecimal((this.causaInterna.getNumeroDireccion().intValue() * 100.0) / (this.causaInterna.getNumeroTotalCausas() * 1.0)), 2));
					this.causaInterna.setPorcentajeMantenimiento(this.getValorRedondeado(new BigDecimal((this.causaInterna.getNumeroMantenimiento().intValue() * 100.0) / (this.causaInterna.getNumeroTotalCausas() * 1.0)), 2));
					this.causaInterna.setPorcentajeCompras(this.getValorRedondeado(new BigDecimal((this.causaInterna.getNumeroCompras().intValue() * 100.0) / (this.causaInterna.getNumeroTotalCausas() * 1.0)), 2));
					this.causaInterna.setPorcentajeTransporte(this.getValorRedondeado(new BigDecimal((this.causaInterna.getNumeroTransporte().intValue() * 100.0) / (this.causaInterna.getNumeroTotalCausas() * 1.0)), 2));
					this.causaInterna.setPorcentajeCartera(this.getValorRedondeado(new BigDecimal((this.causaInterna.getNumeroCartera().intValue() * 100.0) / (this.causaInterna.getNumeroTotalCausas() * 1.0)), 2));
					this.causaInterna.setPorcentajeVentas(this.getValorRedondeado(new BigDecimal((this.causaInterna.getNumeroVentas().intValue() * 100.0) / (this.causaInterna.getNumeroTotalCausas() * 1.0)), 2));
					this.causaInterna.setPorcentajeClientes(this.getValorRedondeado(new BigDecimal((this.causaInterna.getNumeroClientes().intValue() * 100.0) / (this.causaInterna.getNumeroTotalCausas() * 1.0)), 2));
					this.causaInterna.setPorcentajeGestion(this.getValorRedondeado(new BigDecimal((this.causaInterna.getNumeroGestion().intValue() * 100.0) / (this.causaInterna.getNumeroTotalCausas() * 1.0)), 2));

				} else {

					this.causaInterna.setPorcentajeProduccion(this.getValorRedondeado(new BigDecimal(0), 2));
					this.causaInterna.setPorcentajeDireccion(this.getValorRedondeado(new BigDecimal(0), 2));
					this.causaInterna.setPorcentajeMantenimiento(this.getValorRedondeado(new BigDecimal(0), 2));
					this.causaInterna.setPorcentajeCompras(this.getValorRedondeado(new BigDecimal(0), 2));
					this.causaInterna.setPorcentajeTransporte(this.getValorRedondeado(new BigDecimal(0), 2));
					this.causaInterna.setPorcentajeCartera(this.getValorRedondeado(new BigDecimal(0), 2));
					this.causaInterna.setPorcentajeVentas(this.getValorRedondeado(new BigDecimal(0), 2));
					this.causaInterna.setPorcentajeClientes(this.getValorRedondeado(new BigDecimal(0), 2));
					this.causaInterna.setPorcentajeGestion(this.getValorRedondeado(new BigDecimal(0), 2));

				}

				this.causaOportuna.setPorcentajeTotalCausas(new BigDecimal(100));
				this.causaInterna.setPorcentajeTotalCausas(new BigDecimal(100));

				this.graficoCausasNoOportunas = new PieChartModel();
				this.graficoCausasNoOportunas.setTitle("CAUSAS DE ENTREGA NO OPORTUNA");
				if (this.despachoConsulta != null && this.despachoConsulta.gettFechaDesde() != null) {
					this.graficoCausasNoOportunas.setTitle(this.graficoCausasNoOportunas.getTitle() + " DESDE " + this.getFechaColombia(this.despachoConsulta.gettFechaDesde()));
				}
				if (this.despachoConsulta != null && this.despachoConsulta.gettFechaHasta() != null) {
					this.graficoCausasNoOportunas.setTitle(this.graficoCausasNoOportunas.getTitle() + " HASTA " + this.getFechaColombia(this.despachoConsulta.gettFechaHasta()));
				}
				this.graficoCausasNoOportunas.setLegendPosition("w");
				this.graficoCausasNoOportunas.setShowDataLabels(true);
				this.graficoCausasNoOportunas.set("PRODUCCION", this.causaOportuna.getPorcentajeProduccion());
				this.graficoCausasNoOportunas.set("DIRECCION TECNICA", this.causaOportuna.getPorcentajeDireccion());
				this.graficoCausasNoOportunas.set("MANTENIMIENTO", this.causaOportuna.getPorcentajeMantenimiento());
				this.graficoCausasNoOportunas.set("COMPRAS", this.causaOportuna.getPorcentajeCompras());
				this.graficoCausasNoOportunas.set("TRANSPORTE", this.causaOportuna.getPorcentajeTransporte());
				this.graficoCausasNoOportunas.set("CARTERA", this.causaOportuna.getPorcentajeCartera());
				this.graficoCausasNoOportunas.set("VENTAS", this.causaOportuna.getPorcentajeVentas());
				this.graficoCausasNoOportunas.set("CLIENTES", this.causaOportuna.getPorcentajeClientes());
				this.graficoCausasNoOportunas.set("GESTION INTEGRAL", this.causaOportuna.getPorcentajeGestion());

				this.graficoCausasInternas = new PieChartModel();
				this.graficoCausasInternas.setTitle("CAUSAS INTERNAS");
				if (this.despachoConsulta != null && this.despachoConsulta.gettFechaDesde() != null) {
					this.graficoCausasInternas.setTitle(this.graficoCausasInternas.getTitle() + " DESDE " + this.getFechaColombia(this.despachoConsulta.gettFechaDesde()));
				}
				if (this.despachoConsulta != null && this.despachoConsulta.gettFechaHasta() != null) {
					this.graficoCausasInternas.setTitle(this.graficoCausasInternas.getTitle() + " HASTA " + this.getFechaColombia(this.despachoConsulta.gettFechaHasta()));
				}
				this.graficoCausasInternas.setLegendPosition("w");
				this.graficoCausasInternas.setShowDataLabels(true);
				this.graficoCausasInternas.set("PRODUCCION", this.causaInterna.getPorcentajeProduccion());
				this.graficoCausasInternas.set("DIRECCION TECNICA", this.causaInterna.getPorcentajeDireccion());
				this.graficoCausasInternas.set("MANTENIMIENTO", this.causaInterna.getPorcentajeMantenimiento());
				this.graficoCausasInternas.set("COMPRAS", this.causaInterna.getPorcentajeCompras());
				this.graficoCausasInternas.set("TRANSPORTE", this.causaInterna.getPorcentajeTransporte());
				this.graficoCausasInternas.set("CARTERA", this.causaInterna.getPorcentajeCartera());
				this.graficoCausasInternas.set("VENTAS", this.causaInterna.getPorcentajeVentas());
				this.graficoCausasInternas.set("CLIENTES", this.causaInterna.getPorcentajeClientes());
				this.graficoCausasInternas.set("GESTION INTEGRAL", this.causaInterna.getPorcentajeGestion());

			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		} finally {
			conexion.cerrarConexion();
		}

	}

	public void consultarInformeTrazabilidad() {
		Conexion conexion = new Conexion();
		try {

			this.despachoConsulta.setSoftwareBase("S");
			this.despachos = IConsultasDAO.getDespachos(this.despachoConsulta, null);

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Consulta los despachos por distintos criterios
	 */
	public void consultarDespachos() {
		Conexion conexion = new Conexion();
		try {

			this.despachos = IConsultasDAO.getDespachos(this.despachoConsulta, null);

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Obtiene el siguiente consecutivo donde comenzó la v2
	 */
	public Integer getComienzoNumeroEntregaV2() {

		return IConstantes.CONSEGUTIVO_SIGUIENTE_NUMERO_ENTREGA_V2;

	}

	/**
	 * Asigna un despacho
	 * 
	 * @param aDespacho
	 * @param aVista
	 */
	public void asignarDespacho(Despacho aDespacho, String aVista) {

		try {

			if (aDespacho != null && aDespacho.gettNumeroEntregaCadena() != null && !aDespacho.gettNumeroEntregaCadena().trim().equals("")) {
				aDespacho.settNumeroEntregaCadena(aDespacho.gettNumeroEntregaCadena().trim());
			} else {
				aDespacho.settNumeroEntregaCadena(null);
			}

			this.despachoTransaccion = aDespacho;

			if (this.despachoTransaccion.getTarjetaEmergencia() != null && this.despachoTransaccion.getTarjetaEmergencia().equals(IConstantes.AFIRMACION)) {
				this.despachoTransaccion.settTarjetaEmergencia(true);
			} else {
				this.despachoTransaccion.settTarjetaEmergencia(false);
			}

			if (this.despachoTransaccion.getContraMuestra() != null && this.despachoTransaccion.getContraMuestra().equals(IConstantes.AFIRMACION)) {
				this.despachoTransaccion.settContraMuestra(true);
			} else {
				this.despachoTransaccion.settContraMuestra(false);
			}

			if (this.despachoTransaccion.getHojaSeguridad() != null && this.despachoTransaccion.getHojaSeguridad().equals(IConstantes.AFIRMACION)) {
				this.despachoTransaccion.settHoja(true);
			} else {
				this.despachoTransaccion.settHoja(false);
			}

			if (this.despachoTransaccion.getReporteCalidad() != null && this.despachoTransaccion.getReporteCalidad().equals(IConstantes.AFIRMACION)) {
				this.despachoTransaccion.settReporteCalidad(true);
			} else {
				this.despachoTransaccion.settReporteCalidad(false);
			}

			if (this.despachoTransaccion.getCumple() != null && this.despachoTransaccion.getCumple().equals(IConstantes.AFIRMACION)) {
				this.despachoTransaccion.settCumple(true);
			} else {
				this.despachoTransaccion.settCumple(false);
			}

			if (aVista != null && aVista.equals("MODAL_EDITAR")) {

				this.abrirModal("panelEdicion");

			} else if (aVista != null && aVista.equals("MODAL_TRAZABILIDAD")) {
				this.consultarTrazabilidad();
				this.abrirModal("panelTrazabiidad");

			} else if (aVista != null && aVista.equals("MODAL_IMPRIMIR")) {

				this.abrirModal("panelImprimir");

			} else if (aVista != null && aVista.equals("MODAL_VISUALIZACION")) {

				this.abrirModal("panelVisualizacion");

			} else if (aVista != null && aVista.equals("ABRIR_REMISION")) {

				this.abrirModal("panelRemision");

			} else if (aVista != null && aVista.equals("ABRIR_ANULAR")) {

				this.abrirModal("panelAnular");

			} else if (aVista != null && aVista.equals("GENERAR_ANULAR")) {

				anularRemision();
				this.cerrarModal("panelAnular");

			}

			else if (aVista != null && aVista.equals("GENERAR_REMISION")) {
				List<String> resultados = new ConexionWS().generarRemision(this.despachoTransaccion.getCentroOperacion(), this.despachoTransaccion.getFechaHoraPresionoRemision(), this.despachoTransaccion.getPedido(), this.despachoTransaccion.getPlaca(), this.despachoTransaccion.getCodigoTransportador(), this.despachoTransaccion.getSucursalTransportador(), this.despachoTransaccion.getConductor().getDocumento(), this.despachoTransaccion.getConductor().getNombre(), this.despachoTransaccion.getIdentificacionConductor(), this.despachoTransaccion.getRemolque(), this.despachoTransaccion.gettNumeroEntregaCadena(), this.despachoTransaccion.getObra(),this.despachoTransaccion.getDocumentoNulo());
				if (resultados != null && resultados.size() > 0) {    
					for (String r : resultados) {
						if (resultados.size() == 1 && r.equals(IConstantes.REMISION_EXITOSA)) {
							this.mostrarMensajeGlobalPersonalizado(r, "exito");
							actualizarRemision();
							try {
								if (this.despachoTransaccion.gettRemisionesInvolucradas() != null && !this.despachoTransaccion.gettRemisionesInvolucradas().trim().equals("")) {
									String[] remisiones = this.despachoTransaccion.gettRemisionesInvolucradas().split("-");
									if (remisiones != null && remisiones.length > 0) {

										IConsultasDAO.isActualizarNotasRemision(remisiones, this.despachoTransaccion.getCentroOperacion(), "Despacho No.: " + this.despachoTransaccion.gettNumeroEntregaCadena());

										this.cerrarModal("panelRemision");

									}
								}
							} catch (Exception ee) {

							}

							break;

						} else {
							this.mostrarMensajeGlobalPersonalizado(r, "advertencia");
						}
					}

				}
			}

			else {

				this.abrirModal("panelEliminacion");

			}

		} catch (

		Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	/**
	 * Cancela la consulta de despachos
	 */
	public void cancelarConsultaDespachos() {
		this.despachoConsulta = null;
		this.getDespachoConsulta();

		this.despachos = null;
	}

	/**
	 * Cambia la base del software
	 */
	public void cambiarBaseSoftware() {

		cancelarSeleccionPedido("N");
		cancelarSeleccionConductor("N");
	}

	/**
	 * Cancela el pedido de selección de siesa
	 */
	public void cancelarSeleccionPedido(String aModal) {

		try {
			Integer numero = 0;
			this.despacho.setCodigoProducto(null);
			this.despacho.setPedido(null);
			this.despacho.setProducto(null);
			this.despacho.setCentroOperacion(null);
			this.despacho.setUbicacion(null);
			this.despacho.setFecha(new Date());
			this.despacho.setOrdenCompra(null);
			this.despacho.setCliente(new Cliente());
			this.despacho.setFechaOriginalSiesa(null);
			numero = IConsultasDAO.getSiguienteEntrega();
			this.despacho.setNumeroEntrega(numero);
			this.despacho.settNumeroEntregaCadena(this.getSiguienteDespacho(numero));
			if (aModal != null && aModal.equals("S")) {
				this.cerrarModal("panelPedidoSiesa");
			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}
	}

	/**
	 * Cancela el pedido de selección de siesa
	 */
	public void cancelarSeleccionConductor(String aModal) {

		try {
			this.despacho.getConductor().setDocumento(null);
			this.despacho.getConductor().setNombre(null);
			this.despacho.getConductor().setId(null);
			this.despacho.getConductor().setPlaca(null);
			this.despacho.getConductor().setRemolque(null);
			this.despacho.setPlaca(null);
			this.despacho.setRemolque(null);
			this.despacho.setIdentificacionConductor(null);
			this.despacho.setCodigoTransportador(null);
			this.despacho.setSucursalTransportador(null);
			if (aModal != null && aModal.equals("S")) {
				this.cerrarModal("panelConductorSiesa");
			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}
	}

	/**
	 * Cancela la edición de un despacho en transacción
	 * 
	 * @param aVista
	 */
	public void cancelarDespachoTransaccion(String aVista) {
		try {

			this.despachoTransaccion = null;
			this.getDespachoTransaccion();
			this.despachos = null;
			this.consultarDespachos();

			if (aVista != null && aVista.equals("MODAL_EDITAR")) {
				this.cerrarModal("panelEdicion");

			} else if (aVista != null && aVista.equals("MODAL_ELIMINAR")) {
				this.cerrarModal("panelEliminacion");

			} else if (aVista != null && aVista.equals("MODAL_TRAZABILIDAD")) {
				this.cerrarModal("panelTrazabiidad");

			} else if (aVista != null && aVista.equals("MODAL_IMPRIMIR")) {
				this.cerrarModal("panelImprimir");

			} else if (aVista != null && aVista.equals("MODAL_VISUALIZACION")) {
				this.cerrarModal("panelVisualizacion");

			} else if (aVista != null && aVista.equals("MODAL_REMISION")) {
				this.cerrarModal("panelRemision");

			} else if (aVista != null && aVista.equals("MODAL_ANULAR")) {
				this.cerrarModal("panelAnular");

			}

		} catch (Exception e) {

			IConstantes.log.error(e, e);

		}

	}

	/**
	 * Elimina un despacho siempre y cuando no posea infromación asociada
	 */
	public void eliminarDespacho() {

		Conexion conexion = new Conexion();
		try {

			conexion.setAutoCommitBD(false);

			Auditoria auditoria = new Auditoria();

			Map<String, Object> condiciones = new HashMap<String, Object>();
			condiciones.put("id_despacho", this.despachoTransaccion.getId());

			auditoria.getCamposBD();
			conexion.eliminarBD(auditoria.getEstructuraTabla().getTabla(), condiciones);

			this.despachoTransaccion.getCamposBD();
			conexion.eliminarBD(this.despachoTransaccion.getEstructuraTabla().getTabla(), this.despachoTransaccion.getEstructuraTabla().getLlavePrimaria());
			conexion.commitBD();
			this.mostrarMensajeGlobal("eliminacionExitosa", "exito");

			this.despachos = null;
			this.consultarDespachos();

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");

		} finally {
			conexion.cerrarConexion();
		}

		// reseteo de variables
		this.despachoTransaccion = null;
		this.getDespachoTransaccion();
		this.despachos = null;
		this.consultarDespachos();

	}

	/**
	 * Cancela el despacho del peso inicial
	 */
	public void cancelarDespachoBasculaPesoInicial() {

		Integer copiaBascula = this.despachoTransaccion.getBasculaInicial().getId();

		this.despachoTransaccion = null;
		this.getDespachoTransaccion();
		this.despachoTransaccion.getBasculaInicial().setId(copiaBascula);

	}

	/**
	 * Cancela el despacho por bahía
	 */
	public void cancelarDespachoBahia() {

		Integer copiaBascula = this.despachoTransaccion.getBahia().getId();

		this.despachoTransaccion = null;
		this.getDespachoTransaccion();
		this.despachoTransaccion.getBahia().setId(copiaBascula);

	}

	/**
	 * Cancela un despacho de peso final
	 */
	public void cancelarDespachoBasculaPesoFinal() {

		Integer copiaBascula = this.despachoTransaccion.getBasculaFinal().getId();

		this.despachoTransaccion = null;
		this.getDespachoTransaccion();
		this.despachoTransaccion.getBasculaFinal().setId(copiaBascula);

	}

	public void anularRemision() {
		Conexion conexion = new Conexion();

		try {
			Despacho remision = IConsultasDAO.getRemision(this.despachoTransaccion);
			if (this.despachoTransaccion != null && this.despachoTransaccion.getId() != null && remision != null) {

				conexion.setAutoCommitBD(false);

				if (this.despachoTransaccion.getRemisionesAnuladas() != null && !this.despachoTransaccion.getRemisionesAnuladas().trim().equals("")) {
					this.despachoTransaccion.setRemisionesAnuladas(this.despachoTransaccion.getRemisionesAnuladas() + ", " + this.despachoTransaccion.getNumeroRemision() + " (el " + this.getFechaColombia(new Date()) + ")");
				} else {

					this.despachoTransaccion.setRemisionesAnuladas(this.despachoTransaccion.getNumeroRemision() + " (el " + this.getFechaColombia(new Date()) + ")");
				}

				this.despachoTransaccion.setFechaHoraPresionoRemision(null);
				this.despachoTransaccion.setNumeroRemision(null);
				this.despacho.setTipoDocumentoRemision(null);

				this.despachoTransaccion.getCamposBD();
				conexion.actualizarBD(this.despachoTransaccion.getEstructuraTabla().getTabla(), this.despachoTransaccion.getEstructuraTabla().getPersistencia(), this.despachoTransaccion.getEstructuraTabla().getLlavePrimaria(), null);

				guardarAuditoria(conexion, this.despachoTransaccion, IConstantes.ANULO_REMISION);

				conexion.commitBD();

				this.mostrarMensajeGlobalPersonalizado("Se anuló la remisión. Recuerde que ésta sólo se anula en Green Card. Debe hacer el proceso respectivo en Siesa de forma manual", "exito");
				consultarDespachos();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	public void actualizarRemision() {
		Conexion conexion = new Conexion();

		try {
			Despacho remision = IConsultasDAO.getRemision(this.despachoTransaccion);
			if (this.despachoTransaccion != null && this.despachoTransaccion.getId() != null && remision != null) {

				conexion.setAutoCommitBD(false);

				this.despachoTransaccion.setCentroOperacionRemision(remision.getCentroOperacion());
				this.despachoTransaccion.setTipoDocumentoRemision(remision.getTipoDocumentoRemision());
				this.despachoTransaccion.setNumeroRemision(remision.getNumeroRemision());
				this.despachoTransaccion.setReferencia(remision.getReferencia());
				this.despachoTransaccion.setDescripcion(remision.getDescripcion());
				this.despachoTransaccion.settRemisionesInvolucradas(remision.gettRemisionesInvolucradas());
				this.despachoTransaccion.getCamposBD();
				conexion.actualizarBD(this.despachoTransaccion.getEstructuraTabla().getTabla(), this.despachoTransaccion.getEstructuraTabla().getPersistencia(), this.despachoTransaccion.getEstructuraTabla().getLlavePrimaria(), null);

				guardarAuditoria(conexion, this.despachoTransaccion, IConstantes.GENERO_REMISION);

				conexion.commitBD();

				this.mostrarMensajeGlobalPersonalizado("También se actualizó en Green Card los valores de la Remisión", "exito");
				consultarDespachos();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Edita un despacho por bahía
	 */
	public void editarDespachoBahia() {
		Conexion conexion = new Conexion();
		this.copiaBasculaFinal = null;
		try {

			Integer copiaBascula = this.despachoTransaccion.getBahia().getId();

			if (isOkBahia()) {

				if (this.despachoTransaccion != null && this.despachoTransaccion.getId() != null) {

					conexion.setAutoCommitBD(false);

					if (this.despachoTransaccion.getFechaHoraBahia() == null) {
						this.despachoTransaccion.setFechaHoraBahia(new Date());
						this.despachoTransaccion.setPersonalBahia(this.administrarSesionCliente.getAdministradorSesion());
					}

					this.despachoTransaccion.setCantidadDespachada(this.despachoTransaccion.getCantidadDespacho());
					this.despachoTransaccion.getCamposBD();
					conexion.actualizarBD(this.despachoTransaccion.getEstructuraTabla().getTabla(), this.despachoTransaccion.getEstructuraTabla().getPersistencia(), this.despachoTransaccion.getEstructuraTabla().getLlavePrimaria(), null);

					guardarAuditoria(conexion, this.despachoTransaccion, IConstantes.ACTUALIZO_BAHIA);

					conexion.commitBD();

					this.mostrarMensajeGlobal("edicionExitosa", "exito");

					if (this.despachoTransaccion != null && this.despachoTransaccion.getTipoDespacho() != null && this.despachoTransaccion.getTipoDespacho().equals(IConstantes.TIPO_MEDIDOR)) {

						this.copiaBasculaFinal = copiaBascula;

						this.abrirModal("panelImprimir");

					} else {

						// reseteo de variables
						this.despachoTransaccion = null;
						this.getDespachoTransaccion();
						this.despachoTransaccion.getBahia().setId(copiaBascula);

					}

				}

			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Guarda el ticket
	 */
	public void guardarImprimir() {
		Conexion conexion = new Conexion();

		try {

			if (this.despachoTransaccion != null && this.despachoTransaccion.getId() != null) {

				conexion.setAutoCommitBD(false);

				this.despachoTransaccion.getCamposBD();
				conexion.actualizarBD(this.despachoTransaccion.getEstructuraTabla().getTabla(), this.despachoTransaccion.getEstructuraTabla().getPersistencia(), this.despachoTransaccion.getEstructuraTabla().getLlavePrimaria(), null);

				guardarAuditoria(conexion, this.despachoTransaccion, IConstantes.GENERO_TICKET);

				conexion.commitBD();

				imprimirTicket();

			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	public void imprimirInformeTiempos() {
		try {
			Map<String, Object> parametros = new HashMap<String, Object>();
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			parametros.put("pLogo1", this.getPath(IConstantes.PAQUETE_IMAGENES + IConstantes.LOGO1));
			parametros.put("pLogo2", this.getPath(IConstantes.PAQUETE_IMAGENES + IConstantes.LOGO2));

			parametros.put("pFechaInicial", this.despachos.get(this.despachos.size() - 1).getFecha());
			parametros.put("pFechaFinal", this.despachos.get(0).getFecha());
			parametros.put("tiemposTabla", this.tiemposTabla);
			parametros.put("SUBREPORT_DIR", this.getPath(IConstantes.PAQUETE_MODULO_REPORTES) + "/");

			this.generarListado(new JRBeanCollectionDataSource(this.despachos), "imprimirInformeTiempos.jasper", "INFORME_TIEMPOS_" + formato.format(new Date()), null, parametros);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	public void imprimirInformeCausasRetraso() {
		try {
			Map<String, Object> parametros = new HashMap<String, Object>();
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			parametros.put("pLogo1", this.getPath(IConstantes.PAQUETE_IMAGENES + IConstantes.LOGO1));
			parametros.put("pLogo2", this.getPath(IConstantes.PAQUETE_IMAGENES + IConstantes.LOGO2));

			parametros.put("pFechaInicial", this.despachos.get(this.despachos.size() - 1).getFecha());
			parametros.put("pFechaFinal", this.despachos.get(0).getFecha());
			parametros.put("causaOportuna", this.causaOportuna);
			parametros.put("causaInterna", this.causaInterna);

			this.generarListado(new JRBeanCollectionDataSource(this.despachos), "imprimiCausasRetraso.jasper", "INFORME_CAUSAS_RETRASO_" + formato.format(new Date()), null, parametros);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	public void imprimirInformeMonitoreo() {
		try {
			Map<String, Object> parametros = new HashMap<String, Object>();
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			parametros.put("pLogo1", this.getPath(IConstantes.PAQUETE_IMAGENES + IConstantes.LOGO1));
			parametros.put("pLogo2", this.getPath(IConstantes.PAQUETE_IMAGENES + IConstantes.LOGO2));

			parametros.put("pFechaInicial", this.despachos.get(this.despachos.size() - 1).getFecha());
			parametros.put("pFechaFinal", this.despachos.get(0).getFecha());

			this.generarListado(new JRBeanCollectionDataSource(this.despachos), "imprimirInformMonitoreo.jasper", "INFORME_MONITOREO_" + formato.format(new Date()), null, parametros);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	public void imprimirInformeTrazabilidad() {
		try {
			Map<String, Object> parametros = new HashMap<String, Object>();
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			parametros.put("pLogo1", this.getPath(IConstantes.PAQUETE_IMAGENES + IConstantes.LOGO1));
			parametros.put("pLogo2", this.getPath(IConstantes.PAQUETE_IMAGENES + IConstantes.LOGO2));

			parametros.put("pFechaInicial", this.despachos.get(this.despachos.size() - 1).getFecha());
			parametros.put("pFechaFinal", this.despachos.get(0).getFecha());

			this.generarListado(new JRBeanCollectionDataSource(this.despachos), "imprimirInformeTrazabilidad.jasper", "INFORME_TRAZABILIDAD_" + formato.format(new Date()), null, parametros);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	public void imprimirTicketBahia() {
		try {
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("pDespacho", this.despachoTransaccion);
			parametros.put("pLogo1", this.getPath(IConstantes.PAQUETE_IMAGENES + IConstantes.LOGO3));
			parametros.put("pLogo2", this.getPath(IConstantes.PAQUETE_IMAGENES + IConstantes.LOGO4));

			this.generarListado(null, "imprimirTicketMedidorBahia.jasper", IConstantes.NOMBRE_REPORTE + "_BAHIA_" + formato.format(new Date()), null, parametros);

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	/**
	 * Imprime el ticket
	 */
	public void imprimirTicket() {
		try {
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("pDespacho", this.despachoTransaccion);
			parametros.put("pLogo1", this.getPath(IConstantes.PAQUETE_IMAGENES + IConstantes.LOGO1));
			parametros.put("pLogo2", this.getPath(IConstantes.PAQUETE_IMAGENES + IConstantes.LOGO2));
			if (this.despachoTransaccion != null && this.despachoTransaccion.getId() != null && this.despachoTransaccion.getId().intValue() >= IConstantes.CONSEGUTIVO_DESDE_NUEVO_REPORTE) {
				parametros.put("pLogo2", this.getPath(IConstantes.PAQUETE_IMAGENES + IConstantes.LOGO5));
				this.generarListado(null, "imprimirTicketV2.jasper", IConstantes.NOMBRE_REPORTE + "_" + formato.format(new Date()), null, parametros);
			} else {
				this.generarListado(null, IConstantes.REPORTE, IConstantes.NOMBRE_REPORTE + "_" + formato.format(new Date()), null, parametros);
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
	}

	/**
	 * Cierra el ticket para otro despacho
	 */
	public void cerrarTicketBahia() {
		// reseteo de variables

		this.despachoTransaccion = null;
		this.getDespachoTransaccion();

		// que en realidad es bahia la global se uso variable para no repetir

		this.despachoTransaccion.getBahia().setId(this.copiaBasculaFinal);
		this.copiaBasculaFinal = null;

		this.cerrarModal("panelImprimir");
	}

	/**
	 * Cierra el ticket para otro despacho
	 */
	public void cerrarTicket() {
		// reseteo de variables

		this.despachoTransaccion = null;
		this.getDespachoTransaccion();
		this.despachoTransaccion.getBasculaFinal().setId(this.copiaBasculaFinal);
		this.copiaBasculaFinal = null;

		this.cerrarModal("panelImprimir");
	}

	/**
	 * Edita un despacho de peso final
	 */
	public void editarDespachoBasculaPesoFinal() {
		Conexion conexion = new Conexion();

		try {
			this.copiaBasculaFinal = null;
			Integer copiaBascula = this.despachoTransaccion.getBasculaFinal().getId();

			if (isOkBasculaFinal()) {

				if (this.despachoTransaccion != null && this.despachoTransaccion.getId() != null) {

					conexion.setAutoCommitBD(false);

					if (this.despachoTransaccion.getFechaHoraBasculaFinal() == null) {
						this.despachoTransaccion.setFechaHoraBasculaFinal(new Date());
						this.despachoTransaccion.setPersonalBasculaFinal(this.administrarSesionCliente.getAdministradorSesion());
					}

					this.despachoTransaccion.getCamposBD();
					conexion.actualizarBD(this.despachoTransaccion.getEstructuraTabla().getTabla(), this.despachoTransaccion.getEstructuraTabla().getPersistencia(), this.despachoTransaccion.getEstructuraTabla().getLlavePrimaria(), null);

					guardarAuditoria(conexion, this.despachoTransaccion, IConstantes.ACTUALIZO_BASCULA_F);

					conexion.commitBD();

					this.mostrarMensajeGlobal("edicionExitosa", "exito");

					this.copiaBasculaFinal = copiaBascula;

					// abre el modal de impresion

					this.abrirModal("panelImprimir");

					// reseteo de variables
					// this.despachoTransaccion = null;
					// this.getDespachoTransaccion();
					// this.despachoTransaccion.getBasculaFinal().setId(copiaBascula);

				}

			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Edita un despacho de báscula
	 */
	public void editarDespachoBasculaPesoInicial() {
		Conexion conexion = new Conexion();

		try {

			Integer copiaBascula = this.despachoTransaccion.getBasculaInicial().getId();

			if (this.despachoTransaccion != null && this.despachoTransaccion.getId() != null) {

				conexion.setAutoCommitBD(false);
				if (this.despachoTransaccion.getFechaHoraBasculaInicial() == null) {
					this.despachoTransaccion.setFechaHoraBasculaInicial(new Date());
					this.despachoTransaccion.setPersonalBasculaInicial(this.administrarSesionCliente.getAdministradorSesion());
				}

				this.despachoTransaccion.getCamposBD();
				conexion.actualizarBD(this.despachoTransaccion.getEstructuraTabla().getTabla(), this.despachoTransaccion.getEstructuraTabla().getPersistencia(), this.despachoTransaccion.getEstructuraTabla().getLlavePrimaria(), null);

				guardarAuditoria(conexion, this.despachoTransaccion, IConstantes.ACTUALIZO_BASCULA_I);

				conexion.commitBD();

				this.mostrarMensajeGlobal("edicionExitosa", "exito");

				// reseteo de variables
				this.despachoTransaccion = null;
				this.getDespachoTransaccion();
				this.despachoTransaccion.getBasculaInicial().setId(copiaBascula);

			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Edita un despacho
	 */
	public void editarDespacho() {
		Conexion conexion = new Conexion();

		try {
			if (isOkDespachoVenta("E")) {

				conexion.setAutoCommitBD(false);

				// CLIENTE
				if (this.despachoTransaccion.getCliente() != null && this.despachoTransaccion.getCliente().getId() != null) {

					this.despachoTransaccion.getCliente().getCamposBD();
					conexion.actualizarBD(this.despachoTransaccion.getCliente().getEstructuraTabla().getTabla(), this.despachoTransaccion.getCliente().getEstructuraTabla().getPersistencia(), this.despachoTransaccion.getCliente().getEstructuraTabla().getLlavePrimaria(), null);

				} else {

					this.despachoTransaccion.getCliente().getCamposBD();
					conexion.insertarBD(this.despachoTransaccion.getCliente().getEstructuraTabla().getTabla(), this.despachoTransaccion.getCliente().getEstructuraTabla().getPersistencia());

					this.despachoTransaccion.getCliente().setId(conexion.getUltimoSerialTransaccion(this.despachoTransaccion.getCliente().getEstructuraTabla().getTabla()));

				}

				// CONDUCTOR

				if (this.despachoTransaccion.getConductor() != null && this.despachoTransaccion.getConductor().getId() != null) {

					this.despachoTransaccion.getConductor().setPlaca(this.despachoTransaccion.getPlaca());
					this.despachoTransaccion.getConductor().setRemolque(this.despachoTransaccion.getRemolque());

					this.despachoTransaccion.getConductor().getCamposBD();
					conexion.actualizarBD(this.despachoTransaccion.getConductor().getEstructuraTabla().getTabla(), this.despachoTransaccion.getConductor().getEstructuraTabla().getPersistencia(), this.despachoTransaccion.getConductor().getEstructuraTabla().getLlavePrimaria(), null);

				} else {

					this.despachoTransaccion.getConductor().setPlaca(this.despachoTransaccion.getPlaca());
					this.despachoTransaccion.getConductor().setRemolque(this.despachoTransaccion.getRemolque());

					this.despachoTransaccion.getConductor().getCamposBD();
					conexion.insertarBD(this.despachoTransaccion.getConductor().getEstructuraTabla().getTabla(), this.despachoTransaccion.getConductor().getEstructuraTabla().getPersistencia());

					this.despachoTransaccion.getConductor().setId(conexion.getUltimoSerialTransaccion(this.despachoTransaccion.getConductor().getEstructuraTabla().getTabla()));

				}

				this.despachoTransaccion.getCamposBD();
				conexion.actualizarBD(this.despachoTransaccion.getEstructuraTabla().getTabla(), this.despachoTransaccion.getEstructuraTabla().getPersistencia(), this.despachoTransaccion.getEstructuraTabla().getLlavePrimaria(), null);

				guardarAuditoria(conexion, this.despachoTransaccion, IConstantes.ACTUALIZO_VENTA);

				conexion.commitBD();

				this.mostrarMensajeGlobal("edicionExitosa", "exito");
				this.cerrarModal("panelEdicion");

				// reseteo de variables
				this.despachos = null;
				this.consultarDespachos();
			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Muesra observaciones causas o no
	 * 
	 * @param aDespacho
	 * @return
	 */
	public boolean isMostrarObservacionesCausa(Despacho aDespacho) {
		boolean ok = false;
		if (aDespacho != null && aDespacho.getProduccion() != null && !aDespacho.getProduccion().trim().equals("")) {
			ok = true;
		} else {
			aDespacho.setProduccion(null);
		}

		if (aDespacho != null && aDespacho.getCartera() != null && !aDespacho.getCartera().trim().equals("")) {
			ok = true;
		} else {
			aDespacho.setCartera(null);
		}

		if (aDespacho != null && aDespacho.getDireccionTecnica() != null && !aDespacho.getDireccionTecnica().trim().equals("")) {
			ok = true;
		} else {
			aDespacho.setDireccionTecnica(null);
		}

		if (aDespacho != null && aDespacho.getVentas() != null && !aDespacho.getVentas().trim().equals("")) {
			ok = true;
		} else {
			aDespacho.setVentas(null);
		}

		if (aDespacho != null && aDespacho.getMantenimiento() != null && !aDespacho.getMantenimiento().trim().equals("")) {
			ok = true;
		} else {
			aDespacho.setMantenimiento(null);
		}

		if (aDespacho != null && aDespacho.getClienteCausas() != null && !aDespacho.getClienteCausas().trim().equals("")) {
			ok = true;
		} else {
			aDespacho.setClienteCausas(null);
		}

		if (aDespacho != null && aDespacho.getCompras() != null && !aDespacho.getCompras().trim().equals("")) {
			ok = true;
		} else {
			aDespacho.setCompras(null);
		}

		if (aDespacho != null && aDespacho.getGestionIntegral() != null && !aDespacho.getGestionIntegral().trim().equals("")) {
			ok = true;
		} else {
			aDespacho.setGestionIntegral(null);
		}

		if (aDespacho != null && aDespacho.getTransporteCausas() != null && !aDespacho.getTransporteCausas().trim().equals("")) {
			ok = true;
		} else {
			aDespacho.setTransporteCausas(null);
		}

		return ok;

	}

	/**
	 * Crea un despacho para iniciar el proceso
	 */
	public void crearDespacho() {
		Conexion conexion = new Conexion();

		try {
			if (isOkDespachoVenta("C")) {
				conexion.setAutoCommitBD(false);
				this.despacho.setFechaHoraCreoVenta(new Date());
				this.despacho.setPersonalCreoVenta(this.administrarSesionCliente.getAdministradorSesion());

				// Gurda el cliente o lo actualiza según el caso
				if (this.despacho.getCliente() != null && this.despacho.getCliente().getId() != null) {

					this.despacho.getCliente().getCamposBD();
					conexion.actualizarBD(this.despacho.getCliente().getEstructuraTabla().getTabla(), this.despacho.getCliente().getEstructuraTabla().getPersistencia(), this.despacho.getCliente().getEstructuraTabla().getLlavePrimaria(), null);

				} else {

					this.despacho.getCliente().getCamposBD();
					conexion.insertarBD(this.despacho.getCliente().getEstructuraTabla().getTabla(), this.despacho.getCliente().getEstructuraTabla().getPersistencia());

					this.despacho.getCliente().setId(conexion.getUltimoSerialTransaccion(this.despacho.getCliente().getEstructuraTabla().getTabla()));

				}

				// guarda el conductor o lo actualiza según el caso

				if (this.despacho.getConductor() != null && this.despacho.getConductor().getId() != null) {

					this.despacho.getConductor().setPlaca(this.despacho.getPlaca());
					this.despacho.getConductor().setRemolque(this.despacho.getRemolque());

					this.despacho.getConductor().getCamposBD();
					conexion.actualizarBD(this.despacho.getConductor().getEstructuraTabla().getTabla(), this.despacho.getConductor().getEstructuraTabla().getPersistencia(), this.despacho.getConductor().getEstructuraTabla().getLlavePrimaria(), null);

				} else {

					this.despacho.getConductor().setPlaca(this.despacho.getPlaca());
					this.despacho.getConductor().setRemolque(this.despacho.getRemolque());

					this.despacho.getConductor().getCamposBD();
					conexion.insertarBD(this.despacho.getConductor().getEstructuraTabla().getTabla(), this.despacho.getConductor().getEstructuraTabla().getPersistencia());

					this.despacho.getConductor().setId(conexion.getUltimoSerialTransaccion(this.despacho.getConductor().getEstructuraTabla().getTabla()));

				}

				this.despacho.getCamposBD();
				conexion.insertarBD(this.despacho.getEstructuraTabla().getTabla(), this.despacho.getEstructuraTabla().getPersistencia());

				// guarda la auditoría
				this.despacho.setId(conexion.getUltimoSerialTransaccion(this.despacho.getEstructuraTabla().getTabla()));

				if (this.despacho.getId().intValue() != this.despacho.getNumeroEntrega().intValue()) {

					this.despacho.setNumeroEntrega(this.despacho.getId().intValue());
					this.despacho.settNumeroEntregaCadena(this.getSiguienteDespacho(this.despacho.getId().intValue()));

					this.despacho.getCamposBD();
					conexion.actualizarBD(this.despacho.getEstructuraTabla().getTabla(), this.despacho.getEstructuraTabla().getPersistencia(), this.despacho.getEstructuraTabla().getLlavePrimaria(), null);

					this.mostrarMensajeGlobal(this.getMensaje("concurrenciaDspacho", "" + this.despacho.gettNumeroEntregaCadena()), "advertencia");
				}

				guardarAuditoria(conexion, this.despacho, IConstantes.CREO_VENTA);

				conexion.commitBD();

				this.mostrarMensajeGlobal("creacionExitosa", "exito");

				// reseteo de variables

				this.despachoConsulta = null;
				this.getDespachoConsulta();
				this.despachoConsulta.setNumeroInterno(this.despacho.getNumeroInterno());
				this.despachoConsulta.settFechaDesde(this.despacho.getFecha());
				this.despachoConsulta.settFechaHasta(this.despacho.getFecha());

				this.despacho = null;
				this.getDespacho();
				this.despachos = null;
				this.consultarDespachos();

			}

		} catch (Exception e) {
			conexion.rollbackBD();
			this.mostrarMensajeGlobal("transaccionFallida", "error");
		} finally {
			conexion.cerrarConexion();
		}

	}

	/**
	 * Cancela un despacho
	 */
	public void cancelarDespacho() {

		try {
			this.despacho = null;
			this.getDespacho();

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

	}

	// get/sets

	public Despacho getDespachoConsulta() {
		try {
			if (this.despachoConsulta == null) {
				this.despachoConsulta = new Despacho();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return despachoConsulta;
	}

	public void setDespachoConsulta(Despacho despachoConsulta) {
		this.despachoConsulta = despachoConsulta;
	}

	public Despacho getDespacho() {
		try {
			Integer numero = 0;
			if (this.despacho == null) {
				this.despacho = new Despacho();
				this.despacho.setFecha(new Date());

				this.despacho.settTarjetaEmergencia(false);
				this.despacho.settContraMuestra(false);
				this.despacho.settHoja(false);
				this.despacho.settReporteCalidad(false);

				numero = IConsultasDAO.getSiguienteEntrega();
				this.despacho.setNumeroEntrega(numero);
				this.despacho.settNumeroEntregaCadena(this.getSiguienteDespacho(numero));

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return despacho;
	}

	public void setDespacho(Despacho despacho) {
		this.despacho = despacho;
	}

	public Despacho getDespachoTransaccion() {
		try {
			if (this.despachoTransaccion == null) {
				this.despachoTransaccion = new Despacho();
			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return despachoTransaccion;
	}

	public void setDespachoTransaccion(Despacho despachoTransaccion) {
		this.despachoTransaccion = despachoTransaccion;
	}

	public List<Despacho> getDespachos() {
		return despachos;
	}

	public void setDespachos(List<Despacho> despachos) {
		this.despachos = despachos;
	}

	/**
	 * Obtiene la bahia activa a crear
	 * 
	 * @return bahia
	 */
	public List<SelectItem> getItemsBahiasActivas() {
		try {
			if (this.itemsBahiasActivas == null) {
				this.itemsBahiasActivas = new ArrayList<SelectItem>();
				Bahia bahia = new Bahia();
				bahia.setIndicativoVigencia(IConstantes.ACTIVO);
				List<Bahia> bahias = IConsultasDAO.getBahias(bahia);
				this.itemsBahiasActivas.add(new SelectItem("", this.getMensaje("comboVacio")));
				bahias.forEach(p -> this.itemsBahiasActivas.add(new SelectItem(p.getId(), p.getNombre())));

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return itemsBahiasActivas;
	}

	public void setItemsBahiasActivas(List<SelectItem> itemsBahiasActivas) {
		this.itemsBahiasActivas = itemsBahiasActivas;
	}

	public boolean isUbicacionListado(Despacho aDespacho) {
		List<Ubicacion> ubicaciones = null;
		boolean ok = false;

		try {
			if (aDespacho != null && aDespacho.getUbicacion() != null) {
				ubicaciones = IConsultasDAO.getUbicacionesSiesa(null);
				if (ubicaciones.stream().anyMatch(p -> p.getIdUbicacion().equals(aDespacho.getUbicacion()))) {
					ok = true;
				}
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}

		return ok;
	}

	public List<SelectItem> getItemsUbicaciones() {
		try {
			if (this.itemsUbicaciones == null) {
				this.itemsUbicaciones = new ArrayList<SelectItem>();
				List<Ubicacion> ubicaciones = IConsultasDAO.getUbicacionesSiesa(null);
				this.itemsUbicaciones.add(new SelectItem("", this.getMensaje("comboVacio")));
				ubicaciones.forEach(p -> this.itemsUbicaciones.add(new SelectItem(p.getIdUbicacion(), p.getIdUbicacion())));

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return itemsUbicaciones;
	}

	public List<SelectItem> getItemsUbicaciones2() {
		try {
			if (this.itemsUbicaciones == null) {
				this.itemsUbicaciones = new ArrayList<SelectItem>();
				List<Ubicacion> ubicaciones = IConsultasDAO.getUbicacionesSiesa(null);
				this.itemsUbicaciones.add(new SelectItem("", this.getMensaje("comboVacio")));
				ubicaciones.forEach(p -> this.itemsUbicaciones.add(new SelectItem(p.getIdUbicacion(), p.getIdUbicacion())));

				if (this.despachoTransaccion != null && this.despachoTransaccion.getDespachadoDe() != null && !this.despachoTransaccion.getDespachadoDe().trim().equals("")) {
					this.itemsUbicaciones.add(new SelectItem(this.despachoTransaccion.getDespachadoDe(), this.despachoTransaccion.getDespachadoDe().trim()));
				}

			}
		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return itemsUbicaciones;
	}

	public void setItemsUbicaciones(List<SelectItem> itemsUbicaciones) {
		this.itemsUbicaciones = itemsUbicaciones;
	}

	public List<SelectItem> getItemsVehiculos() {
		return itemsVehiculos;
	}

	public void setItemsVehiculos(List<SelectItem> itemsVehiculos) {
		this.itemsVehiculos = itemsVehiculos;
	}

	/**
	 * Obtiene todas las bahías para ser consultadas
	 * 
	 * @return items
	 */
	public List<SelectItem> getItemsBahiasConsulta() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		try {

			List<Bahia> bahias = IConsultasDAO.getBahias(null);
			items.add(new SelectItem("", this.getMensaje("comboVacio")));
			bahias.forEach(p -> items.add(new SelectItem(p.getId(), p.getNombre())));

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return items;
	}

	/**
	 * Obtiene los items de cambio de una bahía para uno en particular
	 * 
	 * @return items
	 */
	public List<SelectItem> getItemsBahiasCambio() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		try {

			items.add(new SelectItem("", this.getMensaje("comboVacio")));

			Bahia bahia = new Bahia();
			bahia.setIndicativoVigencia(IConstantes.ACTIVO);
			List<Bahia> bahias = IConsultasDAO.getBahias(bahia);

			if (bahias != null && bahias.size() > 0) {
				for (Bahia p : bahias) {

					items.add(new SelectItem(p.getId(), p.getNombre()));
				}
			}

			if (this.despachoTransaccion != null && this.despachoTransaccion.getBahia() != null && this.despachoTransaccion.getBahia().getIndicativoVigencia() != null && this.despachoTransaccion.getBahia().getIndicativoVigencia().equals(IConstantes.INACTIVO)) {
				items.add(new SelectItem(this.despachoTransaccion.getBahia().getId(), this.despachoTransaccion.getBahia().getNombre()));
			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return items;
	}

	/**
	 * Obtiene las básculas activas DE EPSO INICIAL en el momento
	 * 
	 * @return itemsBasculasActivas
	 */
	public List<SelectItem> getItemsBasculasActivas() {
		try {

			this.itemsBasculasActivas = new ArrayList<SelectItem>();
			Bascula bascula = new Bascula();
			bascula.setIndicativoVigencia(IConstantes.ACTIVO);
			bascula.setPermiteTicket(IConstantes.NEGACION);
			List<Bascula> basculas = IConsultasDAO.getBasculas(bascula);

			// en caso que exista una bascula inactiva al editar
			if (this.despachoTransaccion != null && this.despachoTransaccion.getId() != null && this.despachoTransaccion.getBasculaInicial() != null && this.despachoTransaccion.getBasculaInicial().getId() != null) {

				Bascula basculaEncontrada = null;
				Bascula basculaC = new Bascula();
				basculaC.setId(this.despachoTransaccion.getBasculaInicial().getId());
				List<Bascula> basculasC = IConsultasDAO.getBasculas(basculaC);
				if (basculasC != null && basculasC.size() > 0) {
					basculaEncontrada = basculasC.get(0);
				}

				if (basculaEncontrada.getIndicativoVigencia() != null && basculaEncontrada.getIndicativoVigencia().equals(IConstantes.INACTIVO)) {

					this.itemsBasculasActivas.add(new SelectItem("", this.getMensaje("comboVacio")));
					this.itemsBasculasActivas.add(new SelectItem(basculaEncontrada.getId(), basculaEncontrada.getNombre()));

					if (basculas != null && basculas.size() > 0) {
						basculas.forEach(p -> this.itemsBasculasActivas.add(new SelectItem(p.getId().intValue(), p.getNombre())));
					}

				} else {

					// si hay una sola para que no tenga que escoger
					if (basculas != null && basculas.size() == 1) {

						basculas.forEach(p -> this.itemsBasculasActivas.add(new SelectItem(p.getId().intValue(), p.getNombre())));

					} else {

						// si hay varias que escoja
						this.itemsBasculasActivas.add(new SelectItem("", this.getMensaje("comboVacio")));

						if (basculas != null && basculas.size() > 0) {
							basculas.forEach(p -> this.itemsBasculasActivas.add(new SelectItem(p.getId().intValue(), p.getNombre())));
						}

					}

				}

			} else {

				// si hay una sola para que no tenga que escoger
				if (basculas != null && basculas.size() == 1) {

					basculas.forEach(p -> this.itemsBasculasActivas.add(new SelectItem(p.getId().intValue(), p.getNombre())));

				} else {

					// si hay varias que escoja
					this.itemsBasculasActivas.add(new SelectItem("", this.getMensaje("comboVacio")));

					if (basculas != null && basculas.size() > 0) {
						basculas.forEach(p -> this.itemsBasculasActivas.add(new SelectItem(p.getId().intValue(), p.getNombre())));
					}

				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return itemsBasculasActivas;
	}

	public void setItemsBasculasActivas(List<SelectItem> itemsBasculasActivas) {
		this.itemsBasculasActivas = itemsBasculasActivas;
	}

	/**
	 * Obtiene los items de básculas con peso final
	 * 
	 * @return itemsBasculasFinalesActivas
	 */
	public List<SelectItem> getItemsBasculasFinalesActivas() {
		try {

			this.itemsBasculasFinalesActivas = new ArrayList<SelectItem>();
			Bascula bascula = new Bascula();
			bascula.setIndicativoVigencia(IConstantes.ACTIVO);
			bascula.setPermiteTicket(IConstantes.AFIRMACION);
			List<Bascula> basculas = IConsultasDAO.getBasculas(bascula);

			// en caso que exista una bascula inactiva al editar
			if (this.despachoTransaccion != null && this.despachoTransaccion.getId() != null && this.despachoTransaccion.getBasculaFinal() != null && this.despachoTransaccion.getBasculaFinal().getId() != null) {

				Bascula basculaEncontrada = null;
				Bascula basculaC = new Bascula();
				basculaC.setId(this.despachoTransaccion.getBasculaFinal().getId());
				List<Bascula> basculasC = IConsultasDAO.getBasculas(basculaC);
				if (basculasC != null && basculasC.size() > 0) {
					basculaEncontrada = basculasC.get(0);
				}

				if (basculaEncontrada.getIndicativoVigencia() != null && basculaEncontrada.getIndicativoVigencia().equals(IConstantes.INACTIVO)) {

					this.itemsBasculasFinalesActivas.add(new SelectItem("", this.getMensaje("comboVacio")));
					this.itemsBasculasFinalesActivas.add(new SelectItem(basculaEncontrada.getId(), basculaEncontrada.getNombre()));

					if (basculas != null && basculas.size() > 0) {
						basculas.forEach(p -> this.itemsBasculasFinalesActivas.add(new SelectItem(p.getId(), p.getNombre())));
					}

				} else {

					// si hay una sola para que no tenga que escoger
					if (basculas != null && basculas.size() == 1) {

						basculas.forEach(p -> this.itemsBasculasFinalesActivas.add(new SelectItem(p.getId(), p.getNombre())));

					} else {

						// si hay varias que escoja
						this.itemsBasculasFinalesActivas.add(new SelectItem("", this.getMensaje("comboVacio")));

						if (basculas != null && basculas.size() > 0) {
							basculas.forEach(p -> this.itemsBasculasFinalesActivas.add(new SelectItem(p.getId(), p.getNombre())));
						}

					}

				}

			} else {

				// si hay una sola para que no tenga que escoger
				if (basculas != null && basculas.size() == 1) {

					basculas.forEach(p -> this.itemsBasculasFinalesActivas.add(new SelectItem(p.getId(), p.getNombre())));

				} else {

					// si hay varias que escoja
					this.itemsBasculasFinalesActivas.add(new SelectItem("", this.getMensaje("comboVacio")));

					if (basculas != null && basculas.size() > 0) {
						basculas.forEach(p -> this.itemsBasculasFinalesActivas.add(new SelectItem(p.getId(), p.getNombre())));
					}

				}

			}

		} catch (Exception e) {
			IConstantes.log.error(e, e);
		}
		return itemsBasculasFinalesActivas;
	}

	public void setItemsBasculasFinalesActivas(List<SelectItem> itemsBasculasFinalesActivas) {
		this.itemsBasculasFinalesActivas = itemsBasculasFinalesActivas;
	}

	public Integer getCopiaBasculaFinal() {
		return copiaBasculaFinal;
	}

	public void setCopiaBasculaFinal(Integer copiaBasculaFinal) {
		this.copiaBasculaFinal = copiaBasculaFinal;
	}

	public AdministrarSesionCliente getAdministrarSesionCliente() {
		return administrarSesionCliente;
	}

	public void setAdministrarSesionCliente(AdministrarSesionCliente administrarSesionCliente) {
		this.administrarSesionCliente = administrarSesionCliente;
	}

	public List<Auditoria> getAuditorias() {
		return auditorias;
	}

	public void setAuditorias(List<Auditoria> auditorias) {
		this.auditorias = auditorias;
	}

	public List<PedidoBase> getPedidosBaseCrear() {
		return pedidosBaseCrear;
	}

	public void setPedidosBaseCrear(List<PedidoBase> pedidosBaseCrear) {
		this.pedidosBaseCrear = pedidosBaseCrear;
	}

	public List<Conductor> getConductoresSiesa() {
		return conductoresSiesa;
	}

	public void setConductoresSiesa(List<Conductor> conductoresSiesa) {
		this.conductoresSiesa = conductoresSiesa;
	}

	public Despacho getCausaOportuna() {
		return causaOportuna;
	}

	public void setCausaOportuna(Despacho causaOportuna) {
		this.causaOportuna = causaOportuna;
	}

	public Despacho getCausaInterna() {
		return causaInterna;
	}

	public void setCausaInterna(Despacho causaInterna) {
		this.causaInterna = causaInterna;
	}

	public PieChartModel getGraficoCausasNoOportunas() {
		return graficoCausasNoOportunas;
	}

	public void setGraficoCausasNoOportunas(PieChartModel graficoCausasNoOportunas) {
		this.graficoCausasNoOportunas = graficoCausasNoOportunas;
	}

	public PieChartModel getGraficoCausasInternas() {
		return graficoCausasInternas;
	}

	public void setGraficoCausasInternas(PieChartModel graficoCausasInternas) {
		this.graficoCausasInternas = graficoCausasInternas;
	}

	public List<TiempoEstandar> getTiemposTabla() {
		return tiemposTabla;
	}

	public void setTiemposTabla(List<TiempoEstandar> tiemposTabla) {
		this.tiemposTabla = tiemposTabla;
	}

}
