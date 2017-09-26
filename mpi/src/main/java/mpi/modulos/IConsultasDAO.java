package mpi.modulos;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mpi.Conexion;
import mpi.Conexion2;
import mpi.Conexion3;
import mpi.beans.Auditoria;
import mpi.beans.Bahia;
import mpi.beans.Bascula;
import mpi.beans.Cliente;
import mpi.beans.Conductor;
import mpi.beans.Despacho;
import mpi.beans.Medidor;
import mpi.beans.PedidoBase;
import mpi.beans.Personal;
import mpi.beans.TiempoEstandar;
import mpi.beans.Ubicacion;
import mpi.generales.IConstantes;

public interface IConsultasDAO {

	/**
	 * Efectuar actualización de notas
	 * 
	 * @param aNumeroRemision
	 * @param aCentroOperacion
	 * @param aNotas
	 * @return
	 * @throws Exception
	 */
	public static boolean isActualizarNotasRemision(String[] remisiones, String aCentroOperacion, String aNotas) throws Exception {
		boolean ok = false;
		Conexion3 conexion = new Conexion3();

		try {

			for (String re : remisiones) {

				conexion.ejecutarSP(Integer.parseInt(re), aCentroOperacion, aNotas);

			}
			ok = true;
		} catch (Exception e) {
			throw new Exception(e);

		} finally {
			conexion.cerrarConexion();

		}

		return ok;

	}

	/**
	 * Obtiene los items y su cantidad
	 * 
	 * @param aPedidoBase
	 * @return
	 * @throws Exception
	 */
	public static List<PedidoBase> getPedidoBaseItems(PedidoBase aPedidoBase) throws Exception {

		List<PedidoBase> pedidos = new ArrayList<PedidoBase>();
		List<Object> parametros = new ArrayList<Object>();
		PedidoBase pedido = null;
		Conexion3 conexion = new Conexion3();
		ResultSet rs = null;

		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT COUNT(*) as Cantidad, CAST(Referencia as char(50)) as Referencia , DescItem");
			sql.append("  FROM GT_PedBase a ");

			sql.append("  WHERE NumeroDocumento = ?");
			sql.append("  AND NumOrdenCompra = ?");
			sql.append("  AND NitClienteFacturar = ?");
			sql.append("  AND FechaPedido = ?");
			sql.append("  AND CentroOperacion = ?");
			sql.append("  AND Sucursal = ?");

			sql.append("  group by Referencia, DescItem");
			sql.append("  order by Referencia, DescItem");

			parametros.add(aPedidoBase.getNumeroDocumento());
			parametros.add(aPedidoBase.getNumeroOrdenCompra());
			parametros.add(aPedidoBase.getNitClienteFacturar());
			parametros.add(aPedidoBase.getFechaPedido());
			parametros.add(aPedidoBase.getCentroOperacion());
			parametros.add(aPedidoBase.getSucursal());

			rs = conexion.consultarBD(sql.toString(), parametros);

			while (rs.next()) {

				pedido = new PedidoBase();
				pedido.settCantidad((Integer) rs.getObject("Cantidad"));
				pedido.setCodigoItem((String) rs.getObject("Referencia"));
				pedido.setDescripcionItem((String) rs.getObject("DescItem"));

				pedidos.add(pedido);

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}

		return pedidos;

	}

	/**
	 * Obtiene la remisión que acabó de hacer
	 * 
	 * @param aPedidoBase
	 * @return despacho
	 * @throws Exception
	 */
	public static Despacho getRemision(Despacho aDespacho) throws Exception {

		List<Object> parametros = new ArrayList<Object>();
		Despacho despacho = null;
		Conexion3 conexion = new Conexion3();
		ResultSet rs = null;
		int i = 0;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT DISTINCT CO_REMISION, TIPO_DOCTO_REMISION, NUMERO_REMISION, REFERENCIA, DESCRIPCION");
			sql.append("  FROM GT_RemisionPedido a ");
			sql.append("  WHERE NUM_PEDIDO = ?");
			sql.append("  AND CO_PEDIDO = ?");
			sql.append("  AND FECHA = ?");
			sql.append("  order by NUMERO_REMISION");

			parametros.add(aDespacho.getPedido());
			parametros.add(aDespacho.getCentroOperacion());
			parametros.add(aDespacho.getFechaHoraPresionoRemision());

			rs = conexion.consultarBD(sql.toString(), parametros);
			despacho = new Despacho();
			while (rs.next()) {
				i++;
				if (i == 1) {

					despacho.setCentroOperacionRemision((String) rs.getObject("CO_REMISION"));
					despacho.setTipoDocumentoRemision((String) rs.getObject("TIPO_DOCTO_REMISION"));
					despacho.setNumeroRemision((String) rs.getObject("TIPO_DOCTO_REMISION") + " " + (Integer) rs.getObject("NUMERO_REMISION"));
					despacho.setReferencia((String) rs.getObject("REFERENCIA"));
					despacho.setDescripcion((String) rs.getObject("DESCRIPCION"));
					despacho.settRemisionesInvolucradas("" + (Integer) rs.getObject("NUMERO_REMISION"));

				} else {
					despacho.setTipoDocumentoRemision((String) rs.getObject("TIPO_DOCTO_REMISION"));
					despacho.setNumeroRemision(despacho.getNumeroRemision() + "; " + (String) rs.getObject("TIPO_DOCTO_REMISION") + " " + (Integer) rs.getObject("NUMERO_REMISION"));
					despacho.setReferencia(despacho.getReferencia() + "; " + (String) rs.getObject("REFERENCIA"));
					despacho.setDescripcion(despacho.getDescripcion() + "; " + (String) rs.getObject("DESCRIPCION"));

					despacho.settRemisionesInvolucradas(despacho.gettRemisionesInvolucradas() + "-" + (Integer) rs.getObject("NUMERO_REMISION"));
				}

			}

			if (i == 0) {
				despacho = null;
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}

		return despacho;

	}

	/**
	 * Obtiene los pedidos base
	 * 
	 * @param aPedidoBase
	 * @return
	 * @throws Exception
	 */
	public static List<PedidoBase> getPedidoBase(PedidoBase aPedidoBase) throws Exception {

		List<PedidoBase> pedidos = new ArrayList<PedidoBase>();
		List<Object> parametros = new ArrayList<Object>();
		PedidoBase pedido = null;
		Conexion3 conexion = new Conexion3();
		ResultSet rs = null;
		int i = 0;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT distinct NumeroDocumento, CentroOperacion, FechaPedido, NitClienteFacturar, RazonSocial, Ubicacion, NumOrdenCompra,Sucursal");
			sql.append("  FROM GT_PedBase a ");
			sql.append("  WHERE NumeroDocumento = ?");
			sql.append("  group by NumeroDocumento, CentroOperacion, FechaPedido, NitClienteFacturar, RazonSocial,Ubicacion, NumOrdenCompra ,Sucursal");
			sql.append("  order by FechaPedido DESC, RazonSocial, CentroOperacion, NitClienteFacturar, NumOrdenCompra");

			parametros.add(aPedidoBase.getNumeroDocumento());

			rs = conexion.consultarBD(sql.toString(), parametros);

			while (rs.next()) {
				i++;
				pedido = new PedidoBase();
				pedido.settIdGreenCard(i);
				pedido.setCentroOperacion((String) rs.getObject("CentroOperacion"));
				pedido.setFechaPedido((Date) rs.getObject("FechaPedido"));
				pedido.setNitClienteFacturar((String) rs.getObject("NitClienteFacturar"));
				pedido.setNumeroDocumento((Integer) rs.getObject("NumeroDocumento"));
				pedido.setNumeroOrdenCompra((String) rs.getObject("NumOrdenCompra"));
				pedido.setRazonSocial((String) rs.getObject("RazonSocial"));
				pedido.setSucursal((String) rs.getObject("Sucursal"));

				pedido.setUbicacion((String) rs.getObject("Ubicacion"));

				// pedido.setCodigoItem(rs.getObject("CodItem"));
				// pedido.setDescripcionItem(rs.getObject("DescItem"));
				// pedido.setTipoDocumento(rs.getObject("TipoDocumento"));

				pedidos.add(pedido);

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}

		return pedidos;

	}

	/**
	 * Obtiene un listado de ubicaciones de siesa
	 * 
	 * @param aUbicacion
	 * @return ubicaciones
	 * @throws Exception
	 */
	public static List<Ubicacion> getUbicacionesSiesa(Ubicacion aUbicacion) throws Exception {

		List<Ubicacion> ubicaciones = new ArrayList<Ubicacion>();
		List<Object> parametros = new ArrayList<Object>();
		Ubicacion ubicacion = null;
		Conexion3 conexion = new Conexion3();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT distinct IdUbicacion");
			sql.append("  FROM GT_Ubicaciones a ");
			sql.append("  WHERE 1=1");

			sql.append("  ORDER BY a.IdUbicacion ");

			rs = conexion.consultarBD(sql.toString(), parametros);

			while (rs.next()) {

				ubicacion = new Ubicacion();

				ubicacion.setIdUbicacion((String) rs.getObject("IdUbicacion"));
				if (ubicacion.getIdUbicacion() != null) {
					ubicacion.setIdUbicacion(ubicacion.getIdUbicacion().trim());
				}

				ubicaciones.add(ubicacion);

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}

		return ubicaciones;

	}

	/**
	 * Obtiene el último dato del medidor
	 * 
	 * @param aId
	 * @return medidor
	 * @throws Exception
	 */
	public static Medidor getMedidor(Integer aId) throws Exception {
		Medidor medidor = null;
		Conexion2 conexion = new Conexion2();
		ResultSet rs = null;
		Float valor = null;
		try {
			if (aId != null && (aId.intValue() == 6 || aId.intValue() == 5)) {
				StringBuilder sql = new StringBuilder();
				sql.append("  SELECT TOP 1 *");
				sql.append("  FROM Datos_de_Planta");
				sql.append("  WHERE 1=1 ");

				sql.append("  ORDER BY datos_de_planta_ndx DESC");

				rs = conexion.consultarBD(sql.toString(), null);
				rs.setFetchSize(1);

				if (rs.next()) {
					medidor = new Medidor();
					medidor.setId((Integer) rs.getObject("datos_de_planta_ndx"));
					medidor.setFlujo3((Integer) rs.getObject("Valor_Despachado_B3"));
					medidor.setFlujo4((Integer) rs.getObject("Valor_Despachado_B4"));

					medidor.setAno3((Integer) rs.getObject("Año_Inicio_Bahía_3"));
					medidor.setMes3((Integer) rs.getObject("Mes_Inicio_Bahía_3"));
					medidor.setDia3((Integer) rs.getObject("Dia_Inicio_Bahía_3"));

					medidor.setAno4((Integer) rs.getObject("Año_Inicio_Bahía_4"));
					medidor.setMes4((Integer) rs.getObject("Mes_Inicio_Bahía4"));
					medidor.setDia4((Integer) rs.getObject("Dia_Inicio_Bahía_4"));

					medidor.setHoraInicio3((Integer) rs.getObject("Hora_Inicio_Bahía_3"));
					medidor.setMinutoInicio3((Integer) rs.getObject("Minuto_Inicio_Bahía_3"));
					medidor.setSegundoInicio3((Integer) rs.getObject("Segundo_Inicio_Bahía_3"));

					medidor.setHoraInicio4((Integer) rs.getObject("Hora_Inicio_Bahía_4"));
					medidor.setMinutoInicio4((Integer) rs.getObject("Minuto_Inicio_Bahía_4"));
					medidor.setSegundoInicio4((Integer) rs.getObject("Segundo_Inicio_Bahía_4"));

					medidor.setHoraFin3((Integer) rs.getObject("Hora_fin_Bahía_3"));
					medidor.setMinutoFin3((Integer) rs.getObject("Minuto_Fin_Bahía_3"));
					medidor.setSegundoFin3((Integer) rs.getObject("Segundo_Fin_Bahía_3"));

					medidor.setHoraFin4((Integer) rs.getObject("Hora_fin_Bahía_4"));
					medidor.setMinutoFin4((Integer) rs.getObject("Minuto_Fin_Bahía_4"));
					medidor.setSegundoFin4((Integer) rs.getObject("Segundo_Fin_Bahía_4"));

					valor = (Float) rs.getObject("Temp_Bahía_3");
					if (valor != null) {
						medidor.setTemperaturaMedidor3(new BigDecimal(valor));
					}

					valor = (Float) rs.getObject("Temp_Bahia_4");
					if (valor != null) {
						medidor.setTemperaturaMedidor4(new BigDecimal(valor));
					}

				}

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return medidor;

	}

	/**
	 * Obtiene la auditoría de un despacho
	 * 
	 * @param aId
	 * @return auditorias
	 * @throws Exception
	 */
	public static List<Auditoria> getAuditoria(Integer aIdDespacho) throws Exception {

		List<Auditoria> auditorias = new ArrayList<Auditoria>();
		List<Object> parametros = new ArrayList<Object>();
		Auditoria auditoria = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT a.*, p.nombres, p.apellidos");
			sql.append("  FROM auditoria a ");
			sql.append("  INNER JOIN personal p ON a.id_administrador = p.id");
			sql.append("  WHERE a.id_despacho = ?");
			parametros.add(aIdDespacho);

			sql.append("  ORDER BY fecha DESC, hora DESC, minuto DESC, segundo DESC ");

			rs = conexion.consultarBD(sql.toString(), parametros);

			while (rs.next()) {

				auditoria = new Auditoria();

				auditoria.setId((Integer) rs.getObject("id"));
				auditoria.setConcepto((String) rs.getObject("concepto"));
				auditoria.setFecha((Date) rs.getObject("fecha"));
				auditoria.setHora((Integer) rs.getObject("hora"));
				auditoria.setMinuto((Integer) rs.getObject("minuto"));
				auditoria.setSegundo((Integer) rs.getObject("segundo"));

				// DESPACHOS
				auditoria.getDespacho().setId((Integer) rs.getObject("id_despacho"));

				// PERSONAL

				auditoria.getPersonal().setNombres((String) rs.getObject("nombres"));
				auditoria.getPersonal().setApellidos((String) rs.getObject("apellidos"));

				auditorias.add(auditoria);

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}

		return auditorias;

	}

	/**
	 * Obtiene el siguiente número de entrega en long
	 * 
	 * @return numero
	 * @throws Exception
	 */
	public static Integer getSiguienteEntrega() throws Exception {
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		Integer numero = 0;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT CAST(last_value AS INTEGER) FROM despachos_id_seq;");

			rs = conexion.consultarBD(sql.toString(), null);
			if (rs.next()) {

				numero = ((Integer) rs.getObject(1)).intValue() + 1;
				// - IConstantes.CONSEGUTIVO_SIGUIENTE_NUMERO_ENTREGA_V2.intValue();

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return numero;

	}

	/**
	 * Obtiene los despachos de acuerdo a los criterios especificados
	 * 
	 * @param aDespacho
	 * @return despachos
	 * @throws Exception
	 */
	public static List<Despacho> getDespachos(Despacho aDespacho, Integer aCantidadMaxima) throws Exception {
		List<Despacho> despachos = new ArrayList<Despacho>();
		List<Object> prametros = new ArrayList<Object>();
		Despacho despacho = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT p.*, c.nombre nombre_cliente, a.nombre nombre_conductor, b.nombre nombre_bahia, c.nit, a.documento, b.indicativo_vigencia, p1.nombres nombres1, p1.apellidos apellidos1, p2.nombres nombres2, p2.apellidos apellidos2, p3.nombres nombres3, p3.apellidos apellidos3, p4.nombres nombres4, p4.apellidos apellidos4 ");
			sql.append("  FROM despachos p");
			sql.append("  INNER JOIN clientes c ON c.id = p.id_cliente");
			sql.append("  INNER JOIN conductores a ON a.id = p.id_conductor");

			sql.append("  LEFT JOIN personal p1 ON p.id_personal_crear_venta = p1.id");
			sql.append("  LEFT JOIN personal p2 ON p.id_personal_bascula_inicial = p2.id");
			sql.append("  LEFT JOIN personal p3 ON p.id_personal_bascula_final = p3.id");
			sql.append("  LEFT JOIN personal p4 ON p.id_personal_bahia = p4.id");

			sql.append("  LEFT JOIN bahias b ON b.id = p.id_bahia");

			sql.append("  WHERE 1=1 ");

			if (aDespacho != null && aDespacho.getId() != null) {
				sql.append("  AND p.id =   ? ");
				prametros.add(aDespacho.getId());
			}

			if (aDespacho != null && aDespacho.getSoftwareBase() != null && aDespacho.getSoftwareBase().trim().equals("S")) {
				sql.append("  AND p.software_base =   ? ");
				prametros.add(aDespacho.getSoftwareBase().trim());
			} else {
				if (aDespacho != null && aDespacho.getSoftwareBase() != null && aDespacho.getSoftwareBase().trim().equals("G")) {
					sql.append("  AND (p.software_base = 'G' OR p.software_base IS NULL) ");
				}

			}

			if (aDespacho != null && aDespacho.getNumeroInterno() != null && !aDespacho.getNumeroInterno().trim().equals("")) {
				sql.append("  AND UPPER(p.numero_interno) =   ? ");
				prametros.add(aDespacho.getNumeroInterno().trim().toUpperCase());
			}

			if (aDespacho != null && aDespacho.getCliente() != null && aDespacho.getCliente().getNit() != null && !aDespacho.getCliente().getNit().trim().equals("")) {
				sql.append("  AND UPPER(c.nit) LIKE  ? ");
				prametros.add("%" + aDespacho.getCliente().getNit().trim().toUpperCase() + "%");

			}

			if (aDespacho != null && aDespacho.getCliente() != null && aDespacho.getCliente().getNombre() != null && !aDespacho.getCliente().getNombre().trim().equals("")) {
				sql.append("  AND UPPER(c.nombre) LIKE  ? ");
				prametros.add("%" + aDespacho.getCliente().getNombre().trim().toUpperCase() + "%");

			}

			if (aDespacho != null && aDespacho.getConductor() != null && aDespacho.getConductor().getDocumento() != null && !aDespacho.getConductor().getDocumento().trim().equals("")) {
				sql.append("  AND UPPER(a.documento) = ? ");
				prametros.add(aDespacho.getConductor().getDocumento().trim().toUpperCase());

			}

			if (aDespacho != null && aDespacho.getPlaca() != null && !aDespacho.getPlaca().trim().equals("")) {
				sql.append("  AND UPPER(p.placa) =   ? ");
				prametros.add(aDespacho.getPlaca().trim().toUpperCase());
			}

			if (aDespacho != null && aDespacho.getNumeroRemision() != null && !aDespacho.getNumeroRemision().trim().equals("")) {
				sql.append("  AND p.numero_remision LIKE  ? ");
				prametros.add("%" + aDespacho.getNumeroRemision() + "%");
			}

			if (aDespacho != null && aDespacho.getBahia() != null && aDespacho.getBahia().getId() != null) {
				sql.append("  AND p.id_bahia =   ? ");
				prametros.add(aDespacho.getBahia().getId());
			}

			if (aDespacho != null && aDespacho.getTipoDespacho() != null && !aDespacho.getTipoDespacho().trim().equals("")) {
				sql.append("  AND p.tipo_despacho =   ? ");
				prametros.add(aDespacho.getTipoDespacho());
			}

			if (aDespacho != null && aDespacho.getNumeroEntrega() != null) {
				sql.append("  AND p.numero_entrega =   ? ");
				prametros.add(aDespacho.getNumeroEntrega());
			}

			if (aDespacho != null && aDespacho.gettFechaDesde() != null) {
				sql.append("  AND p.fecha >=   ? ");
				prametros.add(aDespacho.gettFechaDesde());
			}

			if (aDespacho != null && aDespacho.gettFechaHasta() != null) {
				sql.append("  AND p.fecha <=   ? ");
				prametros.add(aDespacho.gettFechaHasta());
			}

			if (aDespacho != null && aDespacho.getTicketSalida() != null && aDespacho.getTicketSalida() != null && !aDespacho.getTicketSalida().trim().equals("")) {
				sql.append("  AND UPPER(p.ticket_salida) = ? ");
				prametros.add(aDespacho.getTicketSalida().trim().toUpperCase());

			}

			if (aDespacho != null && aDespacho.gettComprobarAbierto() != null && aDespacho.gettComprobarAbierto().equals(IConstantes.AFIRMACION)) {
				sql.append("  AND p.medidor IS NULL AND p.diferencia_peso IS NULL AND p.tipo_despacho <> 'N'");
			}

			sql.append("  ORDER BY p.fecha DESC, p.hora DESC, p.minuto DESC, p.segundo DESC, p.numero_interno");

			rs = conexion.consultarBD(sql.toString(), prametros);

			if (aCantidadMaxima != null) {
				rs.setFetchSize(aCantidadMaxima);
			}

			while (rs.next()) {

				despacho = new Despacho();

				despacho.setId((Integer) rs.getObject("id"));
				despacho.setNumeroInterno((String) rs.getObject("numero_interno"));
				despacho.setFecha((Date) rs.getObject("fecha"));
				despacho.setHora((Integer) rs.getObject("hora"));
				despacho.setMinuto((Integer) rs.getObject("minuto"));
				despacho.setSegundo((Integer) rs.getObject("segundo"));
				despacho.setOrdenCompra((String) rs.getObject("orden_compra"));
				despacho.setPedido((String) rs.getObject("pedido"));
				despacho.setObra((String) rs.getObject("obra"));
				despacho.setPlaca((String) rs.getObject("placa"));
				despacho.setRemolque((String) rs.getObject("remolque"));
				despacho.setTransporte((String) rs.getObject("transporte"));
				despacho.setFlete((String) rs.getObject("flete"));
				despacho.setCodigoProducto((String) rs.getObject("codigo_producto"));
				despacho.setProducto((String) rs.getObject("producto"));
				despacho.setCantidadDespacho((Integer) rs.getObject("cantidad_despacho"));
				despacho.setPresentacion((String) rs.getObject("presentacion"));
				despacho.setUbicacion((String) rs.getObject("ubicacion"));
				despacho.setTipoDespacho((String) rs.getObject("tipo_despacho"));
				despacho.setTarjetaEmergencia((String) rs.getObject("tarjeta_emergencia"));
				despacho.setContraMuestra((String) rs.getObject("contra_muestra"));
				despacho.setHojaSeguridad((String) rs.getObject("hoja_seguridad"));
				despacho.setReporteCalidad((String) rs.getObject("reporte_calidad"));

				despacho.setTicketSalida((String) rs.getObject("ticket_salida"));

				despacho.setSoftwareBase((String) rs.getObject("software_base"));

				despacho.setTicketMedidor((String) rs.getObject("ticket_medidor"));
				despacho.setNumeroSellos((String) rs.getObject("numero_sellos"));
				despacho.setNumeroStickers((String) rs.getObject("numero_stickers"));
				despacho.setCantidadDespachada((Integer) rs.getObject("cantidad_despachada"));
				despacho.setLote((String) rs.getObject("lote"));
				despacho.setDespachadoDe((String) rs.getObject("despachado_de"));

				despacho.setMedidor((Integer) rs.getObject("medidor"));
				despacho.setNumeroEntrega((Integer) rs.getObject("numero_entrega"));
				despacho.settNumeroEntregaCadena((String) rs.getObject("numero_entrega_v2"));
				despacho.setObservaciones((String) rs.getObject("observaciones"));

				// pesos
				despacho.setPesoInicial((Integer) rs.getObject("peso_inicial"));
				despacho.setPesoFinal((Integer) rs.getObject("peso_final"));
				despacho.setDiferenciaPeso((Integer) rs.getObject("diferencia_peso"));

				// basculas
				despacho.getBasculaInicial().setId((Integer) rs.getObject("bascula_inicial"));
				despacho.getBasculaFinal().setId((Integer) rs.getObject("bascula_final"));

				// clientes
				despacho.getCliente().setId((Integer) rs.getObject("id_cliente"));
				despacho.getCliente().setNombre((String) rs.getObject("nombre_cliente"));
				despacho.getCliente().setNit((String) rs.getObject("nit"));

				// conductores
				despacho.getConductor().setId((Integer) rs.getObject("id_conductor"));
				despacho.getConductor().setNombre((String) rs.getObject("nombre_conductor"));
				despacho.getConductor().setDocumento((String) rs.getObject("documento"));

				// bahias
				despacho.getBahia().setId((Integer) rs.getObject("id_bahia"));
				despacho.getBahia().setNombre((String) rs.getObject("nombre_bahia"));
				despacho.getBahia().setIndicativoVigencia((String) rs.getObject("indicativo_vigencia"));

				// nuevos datos

				despacho.setFechaOriginalSiesa((Date) rs.getObject("fecha_original_siesa"));
				despacho.setCentroOperacion((String) rs.getObject("centro_operacion"));
				despacho.setCodigoTransportador((String) rs.getObject("codigo_transportador"));
				despacho.setSucursalTransportador((String) rs.getObject("sucursal_transportador"));

				// solo cuando se hace la remisión
				despacho.setCentroOperacionRemision((String) rs.getObject("co_remision"));
				despacho.setTipoDocumentoRemision((String) rs.getObject("tipo_docto_remision"));
				despacho.setNumeroRemision((String) rs.getObject("numero_remision"));
				despacho.setReferencia((String) rs.getObject("referencia"));
				despacho.setDescripcion((String) rs.getObject("descripcion"));

				// nuevos2
				despacho.setIdentificacionConductor((String) rs.getObject("identificacion_conductor"));
				despacho.setFechaSolicitudCliente((Date) rs.getObject("fecha_solicitud_cliente"));
				despacho.setFechaPuestoObra((Date) rs.getObject("fecha_puesto_obra"));
				despacho.setCumple((String) rs.getObject("cumple"));
				despacho.setProduccion((String) rs.getObject("produccion"));
				despacho.setCartera((String) rs.getObject("cartera"));
				despacho.setDireccionTecnica((String) rs.getObject("direccion_tecnica"));
				despacho.setVentas((String) rs.getObject("ventas"));
				despacho.setMantenimiento((String) rs.getObject("mantenimiento"));
				despacho.setClienteCausas((String) rs.getObject("cliente"));
				despacho.setCompras((String) rs.getObject("compras"));
				despacho.setGestionIntegral((String) rs.getObject("gestion_integral"));
				despacho.setObservacionesRetraso((String) rs.getObject("observaciones_retraso"));
				despacho.setTransporteCausas((String) rs.getObject("transporte_causas"));

				// para los tiempos
				despacho.getPersonalCreoVenta().setId((Integer) rs.getObject("id_personal_crear_venta"));
				despacho.getPersonalCreoVenta().setNombres((String) rs.getObject("nombres1"));
				despacho.getPersonalCreoVenta().setApellidos((String) rs.getObject("apellidos1"));

				despacho.getPersonalBasculaInicial().setId((Integer) rs.getObject("id_personal_bascula_inicial"));
				despacho.getPersonalBasculaInicial().setNombres((String) rs.getObject("nombres2"));
				despacho.getPersonalBasculaInicial().setApellidos((String) rs.getObject("apellidos2"));

				despacho.getPersonalBasculaFinal().setId((Integer) rs.getObject("id_personal_bascula_final"));
				despacho.getPersonalBasculaFinal().setNombres((String) rs.getObject("nombres3"));
				despacho.getPersonalBasculaFinal().setApellidos((String) rs.getObject("apellidos3"));

				despacho.getPersonalBahia().setId((Integer) rs.getObject("id_personal_bahia"));
				despacho.getPersonalBahia().setNombres((String) rs.getObject("nombres4"));
				despacho.getPersonalBahia().setApellidos((String) rs.getObject("apellidos4"));

				despacho.setFechaHoraCreoVenta((Date) rs.getObject("fecha_hora_venta"));
				despacho.setFechaHoraBasculaInicial((Date) rs.getObject("fecha_hora_bascula_inicial"));
				despacho.setFechaHoraBasculaFinal((Date) rs.getObject("fecha_hora_bascula_final"));
				despacho.setFechaHoraBahia((Date) rs.getObject("fecha_hora_bahia"));
				despacho.setFechaHoraPresionoRemision((Date) rs.getObject("fecha_hora_presiono_remision"));

				despacho.setObservacionesInternas((String) rs.getObject("observaciones_internas"));

				// nuevas campos medidor
				despacho.setFechaMedidor((Date) rs.getObject("fecha_medidor"));
				despacho.setHoraInicioMedidor((Date) rs.getObject("hora_inicio_medidor"));
				despacho.setHoraFinMedidor((Date) rs.getObject("hora_fin_medidor"));
				despacho.setTemperaturaMedidor((BigDecimal) rs.getObject("temperatura_medidor"));
				despacho.setObservacionesMedidor((String) rs.getObject("observaciones_medidor"));

				// nueva al remisionar
				despacho.setRemisionesAnuladas((String) rs.getObject("remisiones_anuladas"));
				despacho.setDestare((Integer) rs.getObject("destare"));
				
				
				//nuevo al guardar
				despacho.setDocumentoNulo((String) rs.getObject("documento_nulo"));

				despachos.add(despacho);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return despachos;

	}

	/**
	 * Obtiene un cliente a partir
	 * 
	 * @param aParteA
	 * @return cliente
	 * @throws Exception
	 */
	public static Cliente getCliente(String aParteA) throws Exception {
		Cliente cliente = null;
		List<Object> prametros = new ArrayList<Object>();
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT *");
			sql.append("  FROM clientes p");
			sql.append("  WHERE 1=1 ");

			sql.append("  AND ( UPPER(p.nit) = ?");
			sql.append("  		  OR UPPER(p.nit) LIKE ? ");
			sql.append("  		) ");

			prametros.add(aParteA.toUpperCase().trim());
			prametros.add(aParteA.toUpperCase().trim() + "-%");

			rs = conexion.consultarBD(sql.toString(), prametros);

			if (rs.next()) {
				cliente = new Cliente();
				cliente.setId((Integer) rs.getObject("id"));
				cliente.setNombre((String) rs.getObject("nombre"));
				cliente.setNit((String) rs.getObject("nit"));

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return cliente;

	}

	/**
	 * Obtiene el cliente por el nombre exacto
	 * 
	 * @param aNombre
	 * @return cliente
	 * @throws Exception
	 */
	public static Cliente getClientePorNombre(String aNombre) throws Exception {
		Cliente cliente = null;
		List<Object> prametros = new ArrayList<Object>();
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT *");
			sql.append("  FROM clientes p");
			sql.append("  WHERE 1=1 ");

			sql.append("  AND UPPER(p.nombre) = ?");

			prametros.add(aNombre.toUpperCase().trim());

			rs = conexion.consultarBD(sql.toString(), prametros);

			if (rs.next()) {
				cliente = new Cliente();
				cliente.setId((Integer) rs.getObject("id"));
				cliente.setNombre((String) rs.getObject("nombre"));
				cliente.setNit((String) rs.getObject("nit"));

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return cliente;

	}

	/**
	 * Valida si un cliente existe o no de acuerdo a su nit
	 * 
	 * @param aParteA
	 * @param aParteB
	 * @return existente
	 * @throws Exception
	 */
	public static boolean getClienteExistente(String aParteA) throws Exception {
		boolean existente = false;
		List<Object> prametros = new ArrayList<Object>();
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT *");
			sql.append("  FROM clientes p");
			sql.append("  WHERE 1=1 ");

			sql.append("  AND ( UPPER(p.nit) = ?");
			sql.append("  		  OR UPPER(p.nit) LIKE ? ");
			sql.append("  		) ");

			prametros.add(aParteA.toUpperCase().trim());
			prametros.add(aParteA.toUpperCase().trim() + "-%");

			rs = conexion.consultarBD(sql.toString(), prametros);

			if (rs.next()) {
				existente = true;
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return existente;

	}

	/**
	 * Obtiene los conductores asociados a la consukta según parámetros
	 * 
	 * @param aConductor
	 * @return conductores
	 * @throws Exception
	 */
	public static List<Conductor> getConductoresSiesa(Conductor aConductor) throws Exception {
		List<Conductor> conductores = new ArrayList<Conductor>();
		List<Object> prametros = new ArrayList<Object>();
		Conductor conductor = null;
		Conexion3 conexion = new Conexion3();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT *");
			sql.append("  FROM GT_Vehiculos_1 p");
			sql.append("  WHERE 1=1 ");
			sql.append("  AND (p.IdentificacionConductor = ?  OR p.CodConductor = ?)");
			prametros.add(aConductor.getDocumento().trim());
			prametros.add(aConductor.getDocumento().trim());

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {

				conductor = new Conductor();

				// estos dos no los posee el radicado de vehiculos en siesa
				// conductor.setId((Integer) rs.getObject("id"));
				// conductor.setRemolque((String) rs.getObject("remolque"));

				conductor.setNombre((String) rs.getObject("NombreConductor"));
				conductor.setDocumento((String) rs.getObject("CodConductor"));
				conductor.setPlaca((String) rs.getObject("CodVehiculo"));

				conductor.settCodigoTransportador((String) rs.getObject("CodTransportador"));
				conductor.settIdentificacionConductor((String) rs.getObject("IdentificacionConductor"));
				conductor.settSucursalTransportador((String) rs.getObject("SucursalTransportador"));
				
			
				conductores.add(conductor);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return conductores;

	}

	/**
	 * Obtiene los conductores asociados a la consukta según parámetros
	 * 
	 * @param aConductor
	 * @return conductores
	 * @throws Exception
	 */
	public static List<Conductor> getConductores(Conductor aConductor) throws Exception {
		List<Conductor> conductores = new ArrayList<Conductor>();
		List<Object> prametros = new ArrayList<Object>();
		Conductor conductor = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT *");
			sql.append("  FROM conductores p");
			sql.append("  WHERE 1=1 ");

			if (aConductor != null && aConductor.getId() != null) {
				sql.append("  AND p.id =   ? ");
				prametros.add(aConductor.getId());
			}

			if (aConductor != null && aConductor.getDocumento() != null && !aConductor.getDocumento().trim().equals("")) {
				sql.append("  AND UPPER(p.documento) =  ? ");
				prametros.add(aConductor.getDocumento().trim().toUpperCase());
			}

			if (aConductor != null && aConductor.getPlaca() != null && !aConductor.getPlaca().trim().equals("")) {
				sql.append("  AND UPPER(p.placa) =  ? ");
				prametros.add(aConductor.getPlaca().trim().toUpperCase());
			}

			if (aConductor != null && aConductor.getNombre() != null && !aConductor.getNombre().trim().equals("")) {
				sql.append("  AND UPPER(p.nombre) LIKE  ? ");
				prametros.add("%" + aConductor.getNombre().trim().toUpperCase() + "%");
			}

			sql.append("  ORDER BY p.nombre");

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {

				conductor = new Conductor();
				conductor.setId((Integer) rs.getObject("id"));
				conductor.setNombre((String) rs.getObject("nombre"));
				conductor.setDocumento((String) rs.getObject("documento"));
				conductor.setPlaca((String) rs.getObject("placa"));
				conductor.setRemolque((String) rs.getObject("remolque"));

				conductores.add(conductor);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return conductores;

	}

	/**
	 * Obtiene un registro de los clientes limitado a 100
	 * 
	 * @param aCliente
	 * @return clientes
	 * @throws Exception
	 */
	public static List<Cliente> getClientesLimitados(Cliente aCliente) throws Exception {
		List<Cliente> clientes = new ArrayList<Cliente>();
		List<Object> prametros = new ArrayList<Object>();
		Cliente cliente = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT *");
			sql.append("  FROM clientes p");
			sql.append("  WHERE 1=1 ");

			if (aCliente != null && aCliente.getNit() != null && !aCliente.getNit().trim().equals("")) {
				sql.append("  AND UPPER(p.nit) LIKE  ? ");
				prametros.add("%" + aCliente.getNit().trim().toUpperCase() + "%");
			}

			if (aCliente != null && aCliente.getNombre() != null && !aCliente.getNombre().trim().equals("")) {
				sql.append("  AND UPPER(p.nombre) LIKE  ? ");
				prametros.add("%" + aCliente.getNombre().trim().toUpperCase() + "%");
			}

			sql.append("  ORDER BY p.nombre");

			rs = conexion.consultarBD(sql.toString(), prametros);
			rs.setFetchSize(100);

			while (rs.next()) {

				cliente = new Cliente();
				cliente.setId((Integer) rs.getObject("id"));
				cliente.setNombre((String) rs.getObject("nombre"));
				cliente.setNit((String) rs.getObject("nit"));

				clientes.add(cliente);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return clientes;

	}

	/**
	 * Obtiene un registro de los clientes
	 * 
	 * @param aCliente
	 * @return clientes
	 * @throws Exception
	 */
	public static List<Cliente> getClientes(Cliente aCliente) throws Exception {
		List<Cliente> clientes = new ArrayList<Cliente>();
		List<Object> prametros = new ArrayList<Object>();
		Cliente cliente = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT *");
			sql.append("  FROM clientes p");
			sql.append("  WHERE 1=1 ");

			if (aCliente != null && aCliente.getNit() != null && !aCliente.getNit().trim().equals("")) {
				sql.append("  AND UPPER(p.nit) LIKE  ? ");
				prametros.add("%" + aCliente.getNit().trim().toUpperCase() + "%");
			}

			if (aCliente != null && aCliente.getNombre() != null && !aCliente.getNombre().trim().equals("")) {
				sql.append("  AND UPPER(p.nombre) LIKE  ? ");
				prametros.add("%" + aCliente.getNombre().trim().toUpperCase() + "%");
			}

			sql.append("  ORDER BY p.nombre");

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {

				cliente = new Cliente();
				cliente.setId((Integer) rs.getObject("id"));
				cliente.setNombre((String) rs.getObject("nombre"));
				cliente.setNit((String) rs.getObject("nit"));

				clientes.add(cliente);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return clientes;

	}

	/**
	 * Establece un consulta de basculas por distintos criterios
	 * 
	 * @param aBascula
	 * @return
	 * @throws Exception
	 */
	public static List<Bascula> getBasculas(Bascula aBascula) throws Exception {

		List<Bascula> basculas = new ArrayList<Bascula>();
		List<Object> parametros = new ArrayList<Object>();
		Bascula bascula = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT a.*");
			sql.append("  FROM basculas a ");
			sql.append("  WHERE 1=1");

			if (aBascula != null && aBascula.getId() != null) {
				sql.append("  AND a.id = ?");
				parametros.add(aBascula.getId());
			}

			if (aBascula != null && aBascula.getIndicativoVigencia() != null && !aBascula.getIndicativoVigencia().trim().equals("")) {
				sql.append("  AND a.indicativo_vigencia = ?");
				parametros.add(aBascula.getIndicativoVigencia().trim());
			}

			if (aBascula != null && aBascula.getPermiteTicket() != null && !aBascula.getPermiteTicket().trim().equals("")) {
				sql.append("  AND a.permite_ticket = ?");
				parametros.add(aBascula.getPermiteTicket().trim());
			}

			sql.append("  ORDER BY a.id ");

			rs = conexion.consultarBD(sql.toString(), parametros);

			while (rs.next()) {

				bascula = new Bascula();

				bascula.setId((Integer) rs.getObject("id"));
				bascula.setNombre((String) rs.getObject("nombre"));
				bascula.setIndicativoVigencia((String) rs.getObject("indicativo_vigencia"));
				bascula.setPermiteTicket((String) rs.getObject("permite_ticket"));

				basculas.add(bascula);

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}

		return basculas;

	}

	public static List<TiempoEstandar> getTiempos(TiempoEstandar aTiempo) throws Exception {

		List<TiempoEstandar> tiempos = new ArrayList<TiempoEstandar>();
		List<Object> parametros = new ArrayList<Object>();
		TiempoEstandar tiempo = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT a.*");
			sql.append("  FROM tiempos a ");
			sql.append("  WHERE 1=1");

			if (aTiempo != null && aTiempo.getId() != null) {
				sql.append("  AND a.id = ?");
				parametros.add(aTiempo.getId());
			}

			sql.append("  ORDER BY a.id ");

			rs = conexion.consultarBD(sql.toString(), parametros);

			while (rs.next()) {

				tiempo = new TiempoEstandar();
				tiempo.setId((Integer) rs.getObject("id"));
				tiempo.setTiempoEstandar((Long) rs.getObject("tiempo_estandar"));
				tiempo.setTipoDespacho((String) rs.getObject("tipo_despacho"));

				tiempo.getPrimeraBahia().setId((Integer) rs.getObject("primera_bahia"));
				tiempo.getSegundaBahia().setId((Integer) rs.getObject("segunda_bahia"));

				tiempo.setPrimeraBahia(getBahiasConexion(conexion, tiempo.getPrimeraBahia()).get(0));

				if (tiempo.getSegundaBahia() != null && tiempo.getSegundaBahia().getId() != null) {
					tiempo.setSegundaBahia(getBahiasConexion(conexion, tiempo.getSegundaBahia()).get(0));
				}

				tiempos.add(tiempo);

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}

		return tiempos;

	}

	/**
	 * Obtiene un listado de bahias de acuerdo a los criterios especificados
	 * 
	 * @param aBahia
	 * @return bahias
	 * @throws Exception
	 */
	public static List<Bahia> getBahias(Bahia aBahia) throws Exception {

		List<Bahia> bahias = new ArrayList<Bahia>();
		List<Object> parametros = new ArrayList<Object>();
		Bahia bahia = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT a.*");
			sql.append("  FROM bahias a ");
			sql.append("  WHERE 1=1");

			if (aBahia != null && aBahia.getId() != null) {
				sql.append("  AND a.id = ?");
				parametros.add(aBahia.getId());
			}

			if (aBahia != null && aBahia.getIndicativoVigencia() != null && !aBahia.getIndicativoVigencia().trim().equals("")) {
				sql.append("  AND a.indicativo_vigencia = ?");
				parametros.add(aBahia.getIndicativoVigencia().trim());
			}

			sql.append("  ORDER BY a.id ");

			rs = conexion.consultarBD(sql.toString(), parametros);

			while (rs.next()) {

				bahia = new Bahia();

				bahia.setId((Integer) rs.getObject("id"));
				bahia.setNombre((String) rs.getObject("nombre"));
				bahia.setIndicativoVigencia((String) rs.getObject("indicativo_vigencia"));

				bahias.add(bahia);

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}

		return bahias;

	}

	public static List<Bahia> getBahiasConexion(Conexion conexion, Bahia aBahia) throws Exception {

		List<Bahia> bahias = new ArrayList<Bahia>();
		List<Object> parametros = new ArrayList<Object>();
		Bahia bahia = null;

		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT a.*");
			sql.append("  FROM bahias a ");
			sql.append("  WHERE 1=1");

			if (aBahia != null && aBahia.getId() != null) {
				sql.append("  AND a.id = ?");
				parametros.add(aBahia.getId());
			}

			if (aBahia != null && aBahia.getIndicativoVigencia() != null && !aBahia.getIndicativoVigencia().trim().equals("")) {
				sql.append("  AND a.indicativo_vigencia = ?");
				parametros.add(aBahia.getIndicativoVigencia().trim());
			}

			sql.append("  ORDER BY a.id ");

			rs = conexion.consultarBD(sql.toString(), parametros);

			while (rs.next()) {

				bahia = new Bahia();

				bahia.setId((Integer) rs.getObject("id"));
				bahia.setNombre((String) rs.getObject("nombre"));
				bahia.setIndicativoVigencia((String) rs.getObject("indicativo_vigencia"));

				bahias.add(bahia);

			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}

		}

		return bahias;

	}

	/**
	 * Obtiene un listado de administradores del software
	 * 
	 * @param aPersonal
	 * @return administradores
	 * @throws Exception
	 */
	public static List<Personal> getAdministradores(Personal aPersonal) throws Exception {
		List<Personal> administradores = new ArrayList<Personal>();
		List<Object> prametros = new ArrayList<Object>();
		Personal administrador = null;
		Conexion conexion = new Conexion();
		ResultSet rs = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("  SELECT *");
			sql.append("  FROM personal p");
			sql.append("  WHERE 1=1 ");

			if (aPersonal != null && aPersonal.getCorreoElectronico() != null && !aPersonal.getCorreoElectronico().trim().equals("")) {
				sql.append("  AND UPPER(p.correo_electronico) = ?");
				prametros.add(aPersonal.getCorreoElectronico().trim().toUpperCase());
			}

			if (aPersonal != null && aPersonal.getClave() != null && !aPersonal.getClave().trim().equals("")) {
				sql.append("  AND p.clave = ?");
				prametros.add(aPersonal.getClave().trim());
			}

			if (aPersonal != null && aPersonal.getEstadoVigencia() != null && !aPersonal.getEstadoVigencia().trim().equals("")) {
				sql.append("  AND p.estado_vigencia = ?");
				prametros.add(aPersonal.getEstadoVigencia().trim());
			}

			sql.append("  ORDER BY nombres, apellidos");

			rs = conexion.consultarBD(sql.toString(), prametros);

			while (rs.next()) {
				administrador = new Personal();
				administrador.setId((Integer) rs.getObject("id"));
				administrador.setNombres((String) rs.getObject("nombres"));
				administrador.setApellidos((String) rs.getObject("apellidos"));
				administrador.setCorreoElectronico((String) rs.getObject("correo_electronico"));
				administrador.setClave((String) rs.getObject("clave"));
				administrador.setEstadoVigencia((String) rs.getObject("estado_vigencia"));
				administrador.setTipoAdministracion((String) rs.getObject("tipo_administracion"));
				administrador.settBahia((boolean) rs.getObject("bahia"));
				administrador.settBascula((boolean) rs.getObject("bascula"));
				administrador.settVenta((boolean) rs.getObject("venta"));

				administrador.settBascula2((boolean) rs.getObject("bascula_final"));
				administrador.settClientes((boolean) rs.getObject("clientes"));
				administrador.settConductores((boolean) rs.getObject("conductores"));
				administrador.settParametrosBahias((boolean) rs.getObject("parametros_bahias"));
				administrador.settParametrosBasculas((boolean) rs.getObject("parametros_basculas"));

				administrador.settParametrosTiempos((boolean) rs.getObject("parametros_tiempos"));
				administrador.settInformeTrazabilidad((boolean) rs.getObject("informe_trazabilidad"));
				administrador.settInformeCausasRetraso((boolean) rs.getObject("informe_causas_retraso"));
				administrador.settInformeTiempos((boolean) rs.getObject("informe_tiempos"));
				administrador.settInformeMonitoreo((boolean) rs.getObject("informe_monitoreo"));

				administradores.add(administrador);
			}

		} catch (Exception e) {
			throw new Exception(e);

		} finally {

			if (rs != null) {
				rs.close();
			}
			conexion.cerrarConexion();

		}
		return administradores;

	}

}
