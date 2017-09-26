package mpi.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import mpi.generales.IConstantes;

public class Despacho implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4249239851960523408L;

	private Integer						id;
	private String						numeroInterno;

	private Date							fecha;
	private Date							fechaOriginalSiesa;

	private Cliente						cliente;
	private String						ordenCompra;
	private String						pedido;
	private String						obra;
	private Conductor					conductor;
	private String						placa;
	private String						remolque;
	private String						transporte;
	private String						flete;
	private String						codigoProducto;
	private String						producto;
	private Integer						cantidadDespacho;
	private String						presentacion;
	private String						ubicacion;
	private String						tipoDespacho;
	private Bahia							bahia;
	private String						tarjetaEmergencia;
	private String						contraMuestra;
	private String						hojaSeguridad;
	private String						reporteCalidad;

	private Date							tFechaDesde;
	private Date							tFechaHasta;

	private Integer						hora;
	private Integer						minuto;
	private Integer						segundo;

	private Integer						pesoInicial;
	private Integer						pesoFinal;
	private Integer						diferenciaPeso;
	private String						ticketSalida;

	private Bascula						basculaInicial;
	private Bascula						basculaFinal;

	private String						ticketMedidor;
	private String						despachadoDe;
	private String						lote;
	private Integer						cantidadDespachada;
	private String						numeroStickers;
	private String						numeroSellos;

	private Integer						tBasculaTransaccion;
	private String						tTicketSalida;
	private Integer						tNumeroEntrega;

	private String						tComprobarAbierto;

	private Integer						medidor;
	private Integer						numeroEntrega;
	private String						observaciones;

	private String						softwareBase;

	private boolean						tTarjetaEmergencia;
	private boolean						tContraMuestra;
	private boolean						tHoja;
	private boolean						tReporteCalidad;
	private boolean						tCumple;

	private String						tNumeroEntregaCadena;											// no es
																																			// temporal

	private String						centroOperacion;
	private String						codigoTransportador;
	private String						sucursalTransportador;
	private String						identificacionConductor;
	private String						centroOperacionRemision;
	private String						tipoDocumentoRemision;
	private String						numeroRemision;
	private String						referencia;
	private String						descripcion;

	private Date							fechaSolicitudCliente;
	private Date							fechaPuestoObra;
	private String						cumple;
	private String						produccion;
	private String						cartera;
	private String						direccionTecnica;
	private String						ventas;
	private String						mantenimiento;
	private String						clienteCausas;
	private String						compras;
	private String						gestionIntegral;
	private String						observacionesRetraso;
	private String						transporteCausas;

	private String						observacionesInternas;

	private EstructuraTabla		estructuraTabla;

	private Integer						numeroProduccion;
	private Integer						numeroDireccion;
	private Integer						numeroMantenimiento;
	private Integer						numeroCompras;
	private Integer						numeroTransporte;
	private Integer						numeroCartera;
	private Integer						numeroVentas;
	private Integer						numeroClientes;
	private Integer						numeroGestion;
	private Integer						numeroTotalDespachos;
	private Integer						numeroTotalCausas;

	private BigDecimal				porcentajeProduccion;
	private BigDecimal				porcentajeDireccion;
	private BigDecimal				porcentajeMantenimiento;
	private BigDecimal				porcentajeCompras;
	private BigDecimal				porcentajeTransporte;
	private BigDecimal				porcentajeCartera;
	private BigDecimal				porcentajeVentas;
	private BigDecimal				porcentajeClientes;
	private BigDecimal				porcentajeGestion;
	private BigDecimal				porcentajeTotalDespachos;
	private BigDecimal				porcentajeTotalCausas;

	private Personal					personalCreoVenta;
	private Personal					personalBasculaInicial;
	private Personal					personalBasculaFinal;
	private Personal					personalBahia;

	private Date							fechaHoraCreoVenta;
	private Date							fechaHoraBasculaInicial;
	private Date							fechaHoraBasculaFinal;
	private Date							fechaHoraBahia;
	private Date							fechaHoraPresionoRemision;

	private Long							tTiempoEstandar;
	private Long							tTiempoGastado;

	private Date							fechaMedidor;
	private Date							horaInicioMedidor;
	private Date							horaFinMedidor;
	private BigDecimal				temperaturaMedidor;
	private String						observacionesMedidor;

	private String						tRemisionesInvolucradas;

	private String						remisionesAnuladas;
	
	private String documentoNulo;

	private Integer						destare;

	public Despacho() {
		this.estructuraTabla = new EstructuraTabla();

		this.cliente = new Cliente();
		this.conductor = new Conductor();
		this.bahia = new Bahia();

		this.basculaInicial = new Bascula();
		this.basculaFinal = new Bascula();

		this.personalBahia = new Personal();
		this.personalBasculaFinal = new Personal();
		this.personalBasculaInicial = new Personal();
		this.personalCreoVenta = new Personal();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("despachos");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);

		this.estructuraTabla.getPersistencia().put("numero_interno", this.numeroInterno);

		this.estructuraTabla.getPersistencia().put("fecha", this.fecha);
		this.estructuraTabla.getPersistencia().put("hora", this.hora);
		this.estructuraTabla.getPersistencia().put("minuto", this.minuto);
		this.estructuraTabla.getPersistencia().put("segundo", this.segundo);

		if (this.cliente != null && this.cliente.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_cliente", this.cliente.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_cliente", null);
		}

		this.estructuraTabla.getPersistencia().put("orden_compra", this.ordenCompra);
		this.estructuraTabla.getPersistencia().put("pedido", this.pedido);
		this.estructuraTabla.getPersistencia().put("obra", this.obra);

		if (this.conductor != null && this.conductor.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_conductor", this.conductor.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_conductor", null);
		}

		this.estructuraTabla.getPersistencia().put("placa", this.placa);
		this.estructuraTabla.getPersistencia().put("remolque", this.remolque);
		this.estructuraTabla.getPersistencia().put("transporte", this.transporte);
		this.estructuraTabla.getPersistencia().put("flete", this.flete);
		this.estructuraTabla.getPersistencia().put("codigo_producto", this.codigoProducto);
		this.estructuraTabla.getPersistencia().put("producto", this.producto);
		this.estructuraTabla.getPersistencia().put("cantidad_despacho", this.cantidadDespacho);
		this.estructuraTabla.getPersistencia().put("presentacion", this.presentacion);
		this.estructuraTabla.getPersistencia().put("ubicacion", this.ubicacion);
		this.estructuraTabla.getPersistencia().put("tipo_despacho", this.tipoDespacho);

		if (this.bahia != null && this.bahia.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_bahia", this.bahia.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_bahia", null);
		}

		this.estructuraTabla.getPersistencia().put("tarjeta_emergencia", this.tarjetaEmergencia);
		this.estructuraTabla.getPersistencia().put("contra_muestra", this.contraMuestra);
		this.estructuraTabla.getPersistencia().put("hoja_seguridad", this.hojaSeguridad);
		this.estructuraTabla.getPersistencia().put("reporte_calidad", this.reporteCalidad);

		this.estructuraTabla.getPersistencia().put("peso_inicial", this.pesoInicial);
		this.estructuraTabla.getPersistencia().put("peso_final", this.pesoFinal);
		this.estructuraTabla.getPersistencia().put("diferencia_peso", this.diferenciaPeso);

		if (this.basculaInicial != null && this.basculaInicial.getId() != null) {
			this.estructuraTabla.getPersistencia().put("bascula_inicial", this.basculaInicial.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("bascula_inicial", null);
		}

		if (this.basculaFinal != null && this.basculaFinal.getId() != null) {
			this.estructuraTabla.getPersistencia().put("bascula_final", this.basculaFinal.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("bascula_final", null);
		}

		this.estructuraTabla.getPersistencia().put("ticket_salida", this.ticketSalida);

		this.estructuraTabla.getPersistencia().put("ticket_medidor", this.ticketMedidor);
		this.estructuraTabla.getPersistencia().put("numero_sellos", this.numeroSellos);
		this.estructuraTabla.getPersistencia().put("numero_stickers", this.numeroStickers);
		this.estructuraTabla.getPersistencia().put("cantidad_despachada", this.cantidadDespachada);
		this.estructuraTabla.getPersistencia().put("lote", this.lote);
		this.estructuraTabla.getPersistencia().put("despachado_de", this.despachadoDe);

		this.estructuraTabla.getPersistencia().put("medidor", this.medidor);
		this.estructuraTabla.getPersistencia().put("numero_entrega", this.numeroEntrega);
		this.estructuraTabla.getPersistencia().put("observaciones", this.observaciones);

		this.estructuraTabla.getPersistencia().put("software_base", this.softwareBase);

		this.estructuraTabla.getPersistencia().put("numero_entrega_v2", this.tNumeroEntregaCadena);

		this.estructuraTabla.getPersistencia().put("fecha_original_siesa", this.fechaOriginalSiesa);
		this.estructuraTabla.getPersistencia().put("centro_operacion", this.centroOperacion);
		this.estructuraTabla.getPersistencia().put("codigo_transportador", this.codigoTransportador);

		this.estructuraTabla.getPersistencia().put("sucursal_transportador", this.sucursalTransportador);
		this.estructuraTabla.getPersistencia().put("identificacion_conductor", this.identificacionConductor);

		this.estructuraTabla.getPersistencia().put("co_remision", this.centroOperacionRemision);
		this.estructuraTabla.getPersistencia().put("tipo_docto_remision", this.tipoDocumentoRemision);
		this.estructuraTabla.getPersistencia().put("numero_remision", this.numeroRemision);
		this.estructuraTabla.getPersistencia().put("referencia", this.referencia);
		this.estructuraTabla.getPersistencia().put("descripcion", this.descripcion);

		this.estructuraTabla.getPersistencia().put("fecha_solicitud_cliente", this.fechaSolicitudCliente);
		this.estructuraTabla.getPersistencia().put("fecha_puesto_obra", this.fechaPuestoObra);
		this.estructuraTabla.getPersistencia().put("cumple", this.cumple);
		this.estructuraTabla.getPersistencia().put("produccion", this.produccion);
		this.estructuraTabla.getPersistencia().put("cartera", this.cartera);
		this.estructuraTabla.getPersistencia().put("direccion_tecnica", this.direccionTecnica);
		this.estructuraTabla.getPersistencia().put("ventas", this.ventas);
		this.estructuraTabla.getPersistencia().put("mantenimiento", this.mantenimiento);
		this.estructuraTabla.getPersistencia().put("cliente", this.clienteCausas);
		this.estructuraTabla.getPersistencia().put("compras", this.compras);
		this.estructuraTabla.getPersistencia().put("gestion_integral", this.gestionIntegral);
		this.estructuraTabla.getPersistencia().put("observaciones_retraso", this.observacionesRetraso);
		this.estructuraTabla.getPersistencia().put("transporte_causas", this.transporteCausas);

		this.estructuraTabla.getPersistencia().put("id_personal_crear_venta", this.personalCreoVenta.getId());
		this.estructuraTabla.getPersistencia().put("id_personal_bascula_inicial", this.personalBasculaInicial.getId());
		this.estructuraTabla.getPersistencia().put("id_personal_bascula_final", this.personalBasculaFinal.getId());
		this.estructuraTabla.getPersistencia().put("id_personal_bahia", this.personalBahia.getId());
		this.estructuraTabla.getPersistencia().put("fecha_hora_venta", this.fechaHoraCreoVenta);
		this.estructuraTabla.getPersistencia().put("fecha_hora_bascula_inicial", this.fechaHoraBasculaInicial);
		this.estructuraTabla.getPersistencia().put("fecha_hora_bascula_final", this.fechaHoraBasculaFinal);
		this.estructuraTabla.getPersistencia().put("fecha_hora_bahia", this.fechaHoraBahia);
		this.estructuraTabla.getPersistencia().put("fecha_hora_presiono_remision", this.fechaHoraPresionoRemision);

		this.estructuraTabla.getPersistencia().put("observaciones_internas", this.observacionesInternas);

		this.estructuraTabla.getPersistencia().put("fecha_medidor", this.fechaMedidor);
		this.estructuraTabla.getPersistencia().put("hora_inicio_medidor", this.horaInicioMedidor);
		this.estructuraTabla.getPersistencia().put("hora_fin_medidor", this.horaFinMedidor);
		this.estructuraTabla.getPersistencia().put("temperatura_medidor", this.temperaturaMedidor);

		this.estructuraTabla.getPersistencia().put("observaciones_medidor", this.observacionesMedidor);

		this.estructuraTabla.getPersistencia().put("remisiones_anuladas", this.remisionesAnuladas);

		this.estructuraTabla.getPersistencia().put("destare", this.destare);
		
		
		this.estructuraTabla.getPersistencia().put("documento_nulo", this.documentoNulo);

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Size(max = 50, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getNumeroInterno() {
		return numeroInterno;
	}

	public void setNumeroInterno(String numeroInterno) {
		this.numeroInterno = numeroInterno;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Size(max = 100, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getOrdenCompra() {
		return ordenCompra;
	}

	public void setOrdenCompra(String ordenCompra) {
		this.ordenCompra = ordenCompra;
	}

	@Size(max = 100, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getPedido() {
		return pedido;
	}

	public void setPedido(String pedido) {
		this.pedido = pedido;
	}

	@Size(max = 250, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getObra() {
		return obra;
	}

	public void setObra(String obra) {
		this.obra = obra;
	}

	public Conductor getConductor() {
		return conductor;
	}

	public void setConductor(Conductor conductor) {
		this.conductor = conductor;
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

	@Size(max = 250, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getTransporte() {
		return transporte;
	}

	public void setTransporte(String transporte) {
		this.transporte = transporte;
	}

	@Size(max = 100, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getFlete() {
		return flete;
	}

	public void setFlete(String flete) {
		this.flete = flete;
	}

	public String getCodigoProducto() {
		return codigoProducto;
	}

	public void setCodigoProducto(String codigoProducto) {
		this.codigoProducto = codigoProducto;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	@Min(value = 1, message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public Integer getCantidadDespacho() {
		return cantidadDespacho;
	}

	public void setCantidadDespacho(Integer cantidadDespacho) {
		this.cantidadDespacho = cantidadDespacho;
	}

	@Size(max = 100, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getPresentacion() {
		return presentacion;
	}

	public void setPresentacion(String presentacion) {
		this.presentacion = presentacion;
	}

	@Size(max = 100, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getTipoDespacho() {
		return tipoDespacho;
	}

	public void setTipoDespacho(String tipoDespacho) {
		this.tipoDespacho = tipoDespacho;
	}

	public Bahia getBahia() {
		return bahia;
	}

	public void setBahia(Bahia bahia) {
		this.bahia = bahia;
	}

	public String getTarjetaEmergencia() {
		return tarjetaEmergencia;
	}

	public void setTarjetaEmergencia(String tarjetaEmergencia) {
		this.tarjetaEmergencia = tarjetaEmergencia;
	}

	public String getContraMuestra() {
		return contraMuestra;
	}

	public void setContraMuestra(String contraMuestra) {
		this.contraMuestra = contraMuestra;
	}

	public String getHojaSeguridad() {
		return hojaSeguridad;
	}

	public void setHojaSeguridad(String hojaSeguridad) {
		this.hojaSeguridad = hojaSeguridad;
	}

	public String getReporteCalidad() {
		return reporteCalidad;
	}

	public void setReporteCalidad(String reporteCalidad) {
		this.reporteCalidad = reporteCalidad;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public Date gettFechaDesde() {
		return tFechaDesde;
	}

	public void settFechaDesde(Date tFechaDesde) {
		this.tFechaDesde = tFechaDesde;
	}

	public Date gettFechaHasta() {
		return tFechaHasta;
	}

	public void settFechaHasta(Date tFechaHasta) {
		this.tFechaHasta = tFechaHasta;

	}

	public Integer getHora() {
		return hora;
	}

	public Integer getMinuto() {
		return minuto;
	}

	public void setMinuto(Integer minuto) {
		this.minuto = minuto;
	}

	public Integer getSegundo() {
		return segundo;
	}

	public void setSegundo(Integer segundo) {
		this.segundo = segundo;
	}

	public void setHora(Integer hora) {
		this.hora = hora;
	}

	@Min(value = 1, message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public Integer getPesoInicial() {
		return pesoInicial;
	}

	public void setPesoInicial(Integer pesoInicial) {
		this.pesoInicial = pesoInicial;
	}

	@Min(value = 1, message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public Integer getPesoFinal() {
		return pesoFinal;
	}

	public void setPesoFinal(Integer pesoFinal) {
		this.pesoFinal = pesoFinal;
	}

	public Integer getDiferenciaPeso() {
		return diferenciaPeso;
	}

	public void setDiferenciaPeso(Integer diferenciaPeso) {
		this.diferenciaPeso = diferenciaPeso;
	}

	public Bascula getBasculaInicial() {
		return basculaInicial;
	}

	public void setBasculaInicial(Bascula basculaInicial) {
		this.basculaInicial = basculaInicial;
	}

	public Bascula getBasculaFinal() {
		return basculaFinal;
	}

	public void setBasculaFinal(Bascula basculaFinal) {
		this.basculaFinal = basculaFinal;
	}

	public Integer gettBasculaTransaccion() {
		return tBasculaTransaccion;
	}

	public void settBasculaTransaccion(Integer tBasculaTransaccion) {
		this.tBasculaTransaccion = tBasculaTransaccion;
	}

	@Size(max = 50, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getTicketSalida() {
		return ticketSalida;
	}

	public void setTicketSalida(String ticketSalida) {
		this.ticketSalida = ticketSalida;
	}

	@Size(max = 100, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getTicketMedidor() {
		return ticketMedidor;
	}

	public void setTicketMedidor(String ticketMedidor) {
		this.ticketMedidor = ticketMedidor;
	}

	@Size(max = 100, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getDespachadoDe() {
		return despachadoDe;
	}

	public void setDespachadoDe(String despachadoDe) {
		this.despachadoDe = despachadoDe;
	}

	@Size(max = 100, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	@Min(value = 1, message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public Integer getCantidadDespachada() {
		return cantidadDespachada;
	}

	public void setCantidadDespachada(Integer cantidadDespachada) {
		this.cantidadDespachada = cantidadDespachada;
	}

	@Size(max = 100, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getNumeroStickers() {
		return numeroStickers;
	}

	public void setNumeroStickers(String numeroStickers) {
		this.numeroStickers = numeroStickers;
	}

	@Size(max = 100, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getNumeroSellos() {
		return numeroSellos;
	}

	public void setNumeroSellos(String numeroSellos) {
		this.numeroSellos = numeroSellos;
	}

	public String gettTicketSalida() {
		return tTicketSalida;
	}

	public void settTicketSalida(String tTicketSalida) {
		this.tTicketSalida = tTicketSalida;
	}

	public Integer getMedidor() {
		return medidor;
	}

	public void setMedidor(Integer medidor) {
		this.medidor = medidor;
	}

	@Min(value = 1, message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public Integer getNumeroEntrega() {
		return numeroEntrega;
	}

	public void setNumeroEntrega(Integer numeroEntrega) {
		this.numeroEntrega = numeroEntrega;
	}

	@Size(max = 250, message = IConstantes.VALIDACION_MAXIMA_LONGITUD)
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String gettComprobarAbierto() {
		return tComprobarAbierto;
	}

	public void settComprobarAbierto(String tComprobarAbierto) {
		this.tComprobarAbierto = tComprobarAbierto;
	}

	public Integer gettNumeroEntrega() {
		return tNumeroEntrega;
	}

	public void settNumeroEntrega(Integer tNumeroEntrega) {
		this.tNumeroEntrega = tNumeroEntrega;
	}

	public String getSoftwareBase() {
		return softwareBase;
	}

	public void setSoftwareBase(String softwareBase) {
		this.softwareBase = softwareBase;
	}

	public boolean istTarjetaEmergencia() {
		return tTarjetaEmergencia;
	}

	public void settTarjetaEmergencia(boolean tTarjetaEmergencia) {
		this.tTarjetaEmergencia = tTarjetaEmergencia;
	}

	public boolean istContraMuestra() {
		return tContraMuestra;
	}

	public void settContraMuestra(boolean tContraMuestra) {
		this.tContraMuestra = tContraMuestra;
	}

	public boolean istHoja() {
		return tHoja;
	}

	public void settHoja(boolean tHoja) {
		this.tHoja = tHoja;
	}

	public boolean istReporteCalidad() {
		return tReporteCalidad;
	}

	public void settReporteCalidad(boolean tReporteCalidad) {
		this.tReporteCalidad = tReporteCalidad;
	}

	public String gettNumeroEntregaCadena() {
		return tNumeroEntregaCadena;
	}

	public void settNumeroEntregaCadena(String tNumeroEntregaCadena) {
		this.tNumeroEntregaCadena = tNumeroEntregaCadena;
	}

	public Date getFechaOriginalSiesa() {
		return fechaOriginalSiesa;
	}

	public void setFechaOriginalSiesa(Date fechaOriginalSiesa) {
		this.fechaOriginalSiesa = fechaOriginalSiesa;
	}

	public String getCentroOperacion() {
		return centroOperacion;
	}

	public void setCentroOperacion(String centroOperacion) {
		this.centroOperacion = centroOperacion;
	}

	public String getCodigoTransportador() {
		return codigoTransportador;
	}

	public void setCodigoTransportador(String codigoTransportador) {
		this.codigoTransportador = codigoTransportador;
	}

	public String getSucursalTransportador() {
		return sucursalTransportador;
	}

	public void setSucursalTransportador(String sucursalTransportador) {
		this.sucursalTransportador = sucursalTransportador;
	}

	public String getCentroOperacionRemision() {
		return centroOperacionRemision;
	}

	public void setCentroOperacionRemision(String centroOperacionRemision) {
		this.centroOperacionRemision = centroOperacionRemision;
	}

	public String getTipoDocumentoRemision() {
		return tipoDocumentoRemision;
	}

	public void setTipoDocumentoRemision(String tipoDocumentoRemision) {
		this.tipoDocumentoRemision = tipoDocumentoRemision;
	}

	public String getNumeroRemision() {
		return numeroRemision;
	}

	public void setNumeroRemision(String numeroRemision) {
		this.numeroRemision = numeroRemision;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFechaSolicitudCliente() {
		return fechaSolicitudCliente;
	}

	public void setFechaSolicitudCliente(Date fechaSolicitudCliente) {
		this.fechaSolicitudCliente = fechaSolicitudCliente;
	}

	public Date getFechaPuestoObra() {
		return fechaPuestoObra;
	}

	public void setFechaPuestoObra(Date fechaPuestoObra) {
		this.fechaPuestoObra = fechaPuestoObra;
	}

	public String getCumple() {
		return cumple;
	}

	public void setCumple(String cumple) {
		this.cumple = cumple;
	}

	public String getProduccion() {
		return produccion;
	}

	public void setProduccion(String produccion) {
		this.produccion = produccion;
	}

	public String getCartera() {
		return cartera;
	}

	public void setCartera(String cartera) {
		this.cartera = cartera;
	}

	public String getDireccionTecnica() {
		return direccionTecnica;
	}

	public void setDireccionTecnica(String direccionTecnica) {
		this.direccionTecnica = direccionTecnica;
	}

	public String getVentas() {
		return ventas;
	}

	public void setVentas(String ventas) {
		this.ventas = ventas;
	}

	public String getMantenimiento() {
		return mantenimiento;
	}

	public void setMantenimiento(String mantenimiento) {
		this.mantenimiento = mantenimiento;
	}

	public String getClienteCausas() {
		return clienteCausas;
	}

	public void setClienteCausas(String clienteCausas) {
		this.clienteCausas = clienteCausas;
	}

	public String getCompras() {
		return compras;
	}

	public void setCompras(String compras) {
		this.compras = compras;
	}

	public String getGestionIntegral() {
		return gestionIntegral;
	}

	public void setGestionIntegral(String gestionIntegral) {
		this.gestionIntegral = gestionIntegral;
	}

	public String getObservacionesRetraso() {
		return observacionesRetraso;
	}

	public void setObservacionesRetraso(String observacionesRetraso) {
		this.observacionesRetraso = observacionesRetraso;
	}

	public String getTransporteCausas() {
		return transporteCausas;
	}

	public void setTransporteCausas(String transporteCausas) {
		this.transporteCausas = transporteCausas;
	}

	public String getIdentificacionConductor() {
		return identificacionConductor;
	}

	public void setIdentificacionConductor(String identificacionConductor) {
		this.identificacionConductor = identificacionConductor;
	}

	public boolean istCumple() {
		return tCumple;
	}

	public void settCumple(boolean tCumple) {
		this.tCumple = tCumple;
	}

	public Integer getNumeroProduccion() {
		return numeroProduccion;
	}

	public void setNumeroProduccion(Integer numeroProduccion) {
		this.numeroProduccion = numeroProduccion;
	}

	public Integer getNumeroDireccion() {
		return numeroDireccion;
	}

	public void setNumeroDireccion(Integer numeroDireccion) {
		this.numeroDireccion = numeroDireccion;
	}

	public Integer getNumeroMantenimiento() {
		return numeroMantenimiento;
	}

	public void setNumeroMantenimiento(Integer numeroMantenimiento) {
		this.numeroMantenimiento = numeroMantenimiento;
	}

	public Integer getNumeroCompras() {
		return numeroCompras;
	}

	public void setNumeroCompras(Integer numeroCompras) {
		this.numeroCompras = numeroCompras;
	}

	public Integer getNumeroTransporte() {
		return numeroTransporte;
	}

	public void setNumeroTransporte(Integer numeroTransporte) {
		this.numeroTransporte = numeroTransporte;
	}

	public Integer getNumeroCartera() {
		return numeroCartera;
	}

	public void setNumeroCartera(Integer numeroCartera) {
		this.numeroCartera = numeroCartera;
	}

	public Integer getNumeroVentas() {
		return numeroVentas;
	}

	public void setNumeroVentas(Integer numeroVentas) {
		this.numeroVentas = numeroVentas;
	}

	public Integer getNumeroClientes() {
		return numeroClientes;
	}

	public void setNumeroClientes(Integer numeroClientes) {
		this.numeroClientes = numeroClientes;
	}

	public Integer getNumeroGestion() {
		return numeroGestion;
	}

	public void setNumeroGestion(Integer numeroGestion) {
		this.numeroGestion = numeroGestion;
	}

	public Integer getNumeroTotalDespachos() {
		return numeroTotalDespachos;
	}

	public void setNumeroTotalDespachos(Integer numeroTotalDespachos) {
		this.numeroTotalDespachos = numeroTotalDespachos;
	}

	public Integer getNumeroTotalCausas() {
		return numeroTotalCausas;
	}

	public void setNumeroTotalCausas(Integer numeroTotalCausas) {
		this.numeroTotalCausas = numeroTotalCausas;
	}

	public BigDecimal getPorcentajeProduccion() {
		return porcentajeProduccion;
	}

	public void setPorcentajeProduccion(BigDecimal porcentajeProduccion) {
		this.porcentajeProduccion = porcentajeProduccion;
	}

	public BigDecimal getPorcentajeDireccion() {
		return porcentajeDireccion;
	}

	public void setPorcentajeDireccion(BigDecimal porcentajeDireccion) {
		this.porcentajeDireccion = porcentajeDireccion;
	}

	public BigDecimal getPorcentajeMantenimiento() {
		return porcentajeMantenimiento;
	}

	public void setPorcentajeMantenimiento(BigDecimal porcentajeMantenimiento) {
		this.porcentajeMantenimiento = porcentajeMantenimiento;
	}

	public BigDecimal getPorcentajeCompras() {
		return porcentajeCompras;
	}

	public void setPorcentajeCompras(BigDecimal porcentajeCompras) {
		this.porcentajeCompras = porcentajeCompras;
	}

	public BigDecimal getPorcentajeTransporte() {
		return porcentajeTransporte;
	}

	public void setPorcentajeTransporte(BigDecimal porcentajeTransporte) {
		this.porcentajeTransporte = porcentajeTransporte;
	}

	public BigDecimal getPorcentajeCartera() {
		return porcentajeCartera;
	}

	public void setPorcentajeCartera(BigDecimal porcentajeCartera) {
		this.porcentajeCartera = porcentajeCartera;
	}

	public BigDecimal getPorcentajeVentas() {
		return porcentajeVentas;
	}

	public void setPorcentajeVentas(BigDecimal porcentajeVentas) {
		this.porcentajeVentas = porcentajeVentas;
	}

	public BigDecimal getPorcentajeClientes() {
		return porcentajeClientes;
	}

	public void setPorcentajeClientes(BigDecimal porcentajeClientes) {
		this.porcentajeClientes = porcentajeClientes;
	}

	public BigDecimal getPorcentajeGestion() {
		return porcentajeGestion;
	}

	public void setPorcentajeGestion(BigDecimal porcentajeGestion) {
		this.porcentajeGestion = porcentajeGestion;
	}

	public BigDecimal getPorcentajeTotalDespachos() {
		return porcentajeTotalDespachos;
	}

	public void setPorcentajeTotalDespachos(BigDecimal porcentajeTotalDespachos) {
		this.porcentajeTotalDespachos = porcentajeTotalDespachos;
	}

	public BigDecimal getPorcentajeTotalCausas() {
		return porcentajeTotalCausas;
	}

	public void setPorcentajeTotalCausas(BigDecimal porcentajeTotalCausas) {
		this.porcentajeTotalCausas = porcentajeTotalCausas;
	}

	public Personal getPersonalCreoVenta() {
		return personalCreoVenta;
	}

	public void setPersonalCreoVenta(Personal personalCreoVenta) {
		this.personalCreoVenta = personalCreoVenta;
	}

	public Personal getPersonalBasculaInicial() {
		return personalBasculaInicial;
	}

	public void setPersonalBasculaInicial(Personal personalBasculaInicial) {
		this.personalBasculaInicial = personalBasculaInicial;
	}

	public Personal getPersonalBasculaFinal() {
		return personalBasculaFinal;
	}

	public void setPersonalBasculaFinal(Personal personalBasculaFinal) {
		this.personalBasculaFinal = personalBasculaFinal;
	}

	public Personal getPersonalBahia() {
		return personalBahia;
	}

	public void setPersonalBahia(Personal personalBahia) {
		this.personalBahia = personalBahia;
	}

	public Date getFechaHoraCreoVenta() {
		return fechaHoraCreoVenta;
	}

	public void setFechaHoraCreoVenta(Date fechaHoraCreoVenta) {
		this.fechaHoraCreoVenta = fechaHoraCreoVenta;
	}

	public Date getFechaHoraBasculaInicial() {
		return fechaHoraBasculaInicial;
	}

	public void setFechaHoraBasculaInicial(Date fechaHoraBasculaInicial) {
		this.fechaHoraBasculaInicial = fechaHoraBasculaInicial;
	}

	public Date getFechaHoraBasculaFinal() {
		return fechaHoraBasculaFinal;
	}

	public void setFechaHoraBasculaFinal(Date fechaHoraBasculaFinal) {
		this.fechaHoraBasculaFinal = fechaHoraBasculaFinal;
	}

	public Date getFechaHoraBahia() {
		return fechaHoraBahia;
	}

	public void setFechaHoraBahia(Date fechaHoraBahia) {
		this.fechaHoraBahia = fechaHoraBahia;
	}

	public Date getFechaHoraPresionoRemision() {
		return fechaHoraPresionoRemision;
	}

	public void setFechaHoraPresionoRemision(Date fechaHoraPresionoRemision) {
		this.fechaHoraPresionoRemision = fechaHoraPresionoRemision;
	}

	public Long gettTiempoEstandar() {
		return tTiempoEstandar;
	}

	public void settTiempoEstandar(Long tTiempoEstandar) {
		this.tTiempoEstandar = tTiempoEstandar;
	}

	public Long gettTiempoGastado() {
		return tTiempoGastado;
	}

	public void settTiempoGastado(Long tTiempoGastado) {
		this.tTiempoGastado = tTiempoGastado;
	}

	public String getObservacionesInternas() {
		return observacionesInternas;
	}

	public void setObservacionesInternas(String observacionesInternas) {
		this.observacionesInternas = observacionesInternas;
	}

	public Date getFechaMedidor() {
		return fechaMedidor;
	}

	public void setFechaMedidor(Date fechaMedidor) {
		this.fechaMedidor = fechaMedidor;
	}

	public Date getHoraInicioMedidor() {
		return horaInicioMedidor;
	}

	public void setHoraInicioMedidor(Date horaInicioMedidor) {
		this.horaInicioMedidor = horaInicioMedidor;
	}

	public Date getHoraFinMedidor() {
		return horaFinMedidor;
	}

	public void setHoraFinMedidor(Date horaFinMedidor) {
		this.horaFinMedidor = horaFinMedidor;
	}

	public BigDecimal getTemperaturaMedidor() {
		return temperaturaMedidor;
	}

	public void setTemperaturaMedidor(BigDecimal temperaturaMedidor) {
		this.temperaturaMedidor = temperaturaMedidor;
	}

	public String getObservacionesMedidor() {
		return observacionesMedidor;
	}

	public void setObservacionesMedidor(String observacionesMedidor) {
		this.observacionesMedidor = observacionesMedidor;
	}

	public String gettRemisionesInvolucradas() {
		return tRemisionesInvolucradas;
	}

	public void settRemisionesInvolucradas(String tRemisionesInvolucradas) {
		this.tRemisionesInvolucradas = tRemisionesInvolucradas;
	}

	public String getRemisionesAnuladas() {
		return remisionesAnuladas;
	}

	public void setRemisionesAnuladas(String remisionesAnuladas) {
		this.remisionesAnuladas = remisionesAnuladas;
	}

	@Min(value = 0, message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public Integer getDestare() {
		return destare;
	}

	public void setDestare(Integer destare) {
		this.destare = destare;
	}

	public String getDocumentoNulo() {
		return documentoNulo;
	}

	public void setDocumentoNulo(String documentoNulo) {
		this.documentoNulo = documentoNulo;
	}
	
	

}
