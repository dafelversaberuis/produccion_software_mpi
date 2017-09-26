package mpi.beans;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Min;

import org.primefaces.model.chart.BarChartModel;

import mpi.generales.IConstantes;

public class TiempoEstandar implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3006104191851602880L;
	private Integer						id;
	private Bahia							primeraBahia;
	private Bahia							segundaBahia;
	private Long							tiempoEstandar;
	private String						tipoDespacho;

	private EstructuraTabla		estructuraTabla;

	private List<Despacho>		tDespachos;

	private BarChartModel			graficoTiempo;

	private String						tLabel;

	public TiempoEstandar() {
		this.estructuraTabla = new EstructuraTabla();

		this.primeraBahia = new Bahia();
		this.segundaBahia = new Bahia();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("tiempos");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);
		this.estructuraTabla.getPersistencia().put("primera_bahia", this.primeraBahia.getId());

		if (this.segundaBahia != null && this.segundaBahia.getId() != null) {
			this.estructuraTabla.getPersistencia().put("segunda_bahia", this.segundaBahia.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("segunda_bahia", null);
		}

		this.estructuraTabla.getPersistencia().put("tiempo_estandar", this.tiempoEstandar);
		this.estructuraTabla.getPersistencia().put("tipo_despacho", this.tipoDespacho);

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Bahia getPrimeraBahia() {
		return primeraBahia;
	}

	public void setPrimeraBahia(Bahia primeraBahia) {
		this.primeraBahia = primeraBahia;
	}

	public Bahia getSegundaBahia() {
		return segundaBahia;
	}

	public void setSegundaBahia(Bahia segundaBahia) {
		this.segundaBahia = segundaBahia;
	}

	@Min(value = 0, message = IConstantes.VALIDACION_MINIMO_ENTERO)
	public Long getTiempoEstandar() {
		return tiempoEstandar;
	}

	public void setTiempoEstandar(Long tiempoEstandar) {
		this.tiempoEstandar = tiempoEstandar;
	}

	public String getTipoDespacho() {
		return tipoDespacho;
	}

	public void setTipoDespacho(String tipoDespacho) {
		this.tipoDespacho = tipoDespacho;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public List<Despacho> gettDespachos() {
		return tDespachos;
	}

	public void settDespachos(List<Despacho> tDespachos) {
		this.tDespachos = tDespachos;
	}

	public BarChartModel getGraficoTiempo() {
		return graficoTiempo;
	}

	public void setGraficoTiempo(BarChartModel graficoTiempo) {
		this.graficoTiempo = graficoTiempo;
	}

	public String gettLabel() {
		return tLabel;
	}

	public void settLabel(String tLabel) {
		this.tLabel = tLabel;
	}

}
