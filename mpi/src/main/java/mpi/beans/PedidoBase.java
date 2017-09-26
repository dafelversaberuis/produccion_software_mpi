package mpi.beans;

import java.io.Serializable;
import java.util.Date;

public class PedidoBase implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6428126412065580703L;

	private Integer						tIdGreenCard;
	private String						centroOperacion;
	private String						tipoDocumento;
	private Integer						numeroDocumento;
	private Date							fechaPedido;
	private String						nitClienteFacturar;
	private String						razonSocial;
	private String						sucursal;
	private String						codigoItem;
	private String						descripcionItem;
	private String						numeroOrdenCompra;
	private String						ubicacion;
	private String						tCodigoItems;
	private String						tDescripcionItems;

	private Integer						tCantidad;

	public PedidoBase() {

	}

	public String getCentroOperacion() {
		return centroOperacion;
	}

	public void setCentroOperacion(String centroOperacion) {
		this.centroOperacion = centroOperacion;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public Integer getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(Integer numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public Date getFechaPedido() {
		return fechaPedido;
	}

	public void setFechaPedido(Date fechaPedido) {
		this.fechaPedido = fechaPedido;
	}

	public String getNitClienteFacturar() {
		return nitClienteFacturar;
	}

	public void setNitClienteFacturar(String nitClienteFacturar) {
		this.nitClienteFacturar = nitClienteFacturar;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public String getCodigoItem() {
		return codigoItem;
	}

	public void setCodigoItem(String codigoItem) {
		this.codigoItem = codigoItem;
	}

	public String getDescripcionItem() {
		return descripcionItem;
	}

	public void setDescripcionItem(String descripcionItem) {
		this.descripcionItem = descripcionItem;
	}

	public String getNumeroOrdenCompra() {
		return numeroOrdenCompra;
	}

	public void setNumeroOrdenCompra(String numeroOrdenCompra) {
		this.numeroOrdenCompra = numeroOrdenCompra;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public Integer gettIdGreenCard() {
		return tIdGreenCard;
	}

	public void settIdGreenCard(Integer tIdGreenCard) {
		this.tIdGreenCard = tIdGreenCard;
	}

	public Integer gettCantidad() {
		return tCantidad;
	}

	public void settCantidad(Integer tCantidad) {
		this.tCantidad = tCantidad;
	}

	public String gettCodigoItems() {
		return tCodigoItems;
	}

	public void settCodigoItems(String tCodigoItems) {
		this.tCodigoItems = tCodigoItems;
	}

	public String gettDescripcionItems() {
		return tDescripcionItems;
	}

	public void settDescripcionItems(String tDescripcionItems) {
		this.tDescripcionItems = tDescripcionItems;
	}

}
