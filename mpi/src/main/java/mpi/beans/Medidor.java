package mpi.beans;

import java.io.Serializable;
import java.math.BigDecimal;

public class Medidor implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2668646777318632193L;
	private Integer						id;
	private Integer						flujo4;
	private Integer						flujo3;

	private Integer						dia3;
	private Integer						mes3;
	private Integer						ano3;

	private Integer						horaInicio3;
	private Integer						minutoInicio3;
	private Integer						segundoInicio3;
	private Integer						horaFin3;
	private Integer						minutoFin3;
	private Integer						segundoFin3;

	private Integer						dia4;
	private Integer						mes4;
	private Integer						ano4;
	private Integer						horaInicio4;
	private Integer						minutoInicio4;
	private Integer						segundoInicio4;
	private Integer						horaFin4;
	private Integer						minutoFin4;
	private Integer						segundoFin4;

	private BigDecimal				temperaturaMedidor4;
	private BigDecimal				temperaturaMedidor3;

	private EstructuraTabla		estructuraTabla;

	public Medidor() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFlujo4() {
		return flujo4;
	}

	public void setFlujo4(Integer flujo4) {
		this.flujo4 = flujo4;
	}

	public Integer getFlujo3() {
		return flujo3;
	}

	public void setFlujo3(Integer flujo3) {
		this.flujo3 = flujo3;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

	public BigDecimal getTemperaturaMedidor4() {
		return temperaturaMedidor4;
	}

	public void setTemperaturaMedidor4(BigDecimal temperaturaMedidor4) {
		this.temperaturaMedidor4 = temperaturaMedidor4;
	}

	public BigDecimal getTemperaturaMedidor3() {
		return temperaturaMedidor3;
	}

	public void setTemperaturaMedidor3(BigDecimal temperaturaMedidor3) {
		this.temperaturaMedidor3 = temperaturaMedidor3;
	}

	public Integer getDia3() {
		return dia3;
	}

	public void setDia3(Integer dia3) {
		this.dia3 = dia3;
	}

	public Integer getMes3() {
		return mes3;
	}

	public void setMes3(Integer mes3) {
		this.mes3 = mes3;
	}

	public Integer getAno3() {
		return ano3;
	}

	public void setAno3(Integer ano3) {
		this.ano3 = ano3;
	}

	public Integer getHoraInicio3() {
		return horaInicio3;
	}

	public void setHoraInicio3(Integer horaInicio3) {
		this.horaInicio3 = horaInicio3;
	}

	public Integer getMinutoInicio3() {
		return minutoInicio3;
	}

	public void setMinutoInicio3(Integer minutoInicio3) {
		this.minutoInicio3 = minutoInicio3;
	}

	public Integer getSegundoInicio3() {
		return segundoInicio3;
	}

	public void setSegundoInicio3(Integer segundoInicio3) {
		this.segundoInicio3 = segundoInicio3;
	}

	public Integer getHoraFin3() {
		return horaFin3;
	}

	public void setHoraFin3(Integer horaFin3) {
		this.horaFin3 = horaFin3;
	}

	public Integer getMinutoFin3() {
		return minutoFin3;
	}

	public void setMinutoFin3(Integer minutoFin3) {
		this.minutoFin3 = minutoFin3;
	}

	public Integer getSegundoFin3() {
		return segundoFin3;
	}

	public void setSegundoFin3(Integer segundoFin3) {
		this.segundoFin3 = segundoFin3;
	}

	public Integer getDia4() {
		return dia4;
	}

	public void setDia4(Integer dia4) {
		this.dia4 = dia4;
	}

	public Integer getMes4() {
		return mes4;
	}

	public void setMes4(Integer mes4) {
		this.mes4 = mes4;
	}

	public Integer getAno4() {
		return ano4;
	}

	public void setAno4(Integer ano4) {
		this.ano4 = ano4;
	}

	public Integer getHoraInicio4() {
		return horaInicio4;
	}

	public void setHoraInicio4(Integer horaInicio4) {
		this.horaInicio4 = horaInicio4;
	}

	public Integer getMinutoInicio4() {
		return minutoInicio4;
	}

	public void setMinutoInicio4(Integer minutoInicio4) {
		this.minutoInicio4 = minutoInicio4;
	}

	public Integer getSegundoInicio4() {
		return segundoInicio4;
	}

	public void setSegundoInicio4(Integer segundoInicio4) {
		this.segundoInicio4 = segundoInicio4;
	}

	public Integer getHoraFin4() {
		return horaFin4;
	}

	public void setHoraFin4(Integer horaFin4) {
		this.horaFin4 = horaFin4;
	}

	public Integer getMinutoFin4() {
		return minutoFin4;
	}

	public void setMinutoFin4(Integer minutoFin4) {
		this.minutoFin4 = minutoFin4;
	}

	public Integer getSegundoFin4() {
		return segundoFin4;
	}

	public void setSegundoFin4(Integer segundoFin4) {
		this.segundoFin4 = segundoFin4;
	}

}
