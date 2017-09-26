package mpi.beans;

import java.io.Serializable;
import java.util.Date;

public class Auditoria implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5586702770240676691L;
	private Integer						id;
	private Integer						hora;
	private Integer						minuto;
	private Integer						segundo;
	private Date							fecha;
	private String						concepto;
	private Despacho					despacho;
	private Personal					personal;

	private EstructuraTabla		estructuraTabla;

	public Auditoria() {
		this.estructuraTabla = new EstructuraTabla();
		this.despacho = new Despacho();
		this.personal = new Personal();
	}

	public void getCamposBD() {

		this.estructuraTabla.setTabla("auditoria");
		this.estructuraTabla.getLlavePrimaria().put("id", this.id);
		this.estructuraTabla.getPersistencia().put("hora", this.hora);
		this.estructuraTabla.getPersistencia().put("minuto", this.minuto);
		this.estructuraTabla.getPersistencia().put("segundo", this.segundo);
		this.estructuraTabla.getPersistencia().put("fecha", this.fecha);
		this.estructuraTabla.getPersistencia().put("concepto", this.concepto);

		if (this.despacho != null && this.despacho.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_despacho", this.despacho.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_despacho", null);
		}

		if (this.personal != null && this.personal.getId() != null) {
			this.estructuraTabla.getPersistencia().put("id_administrador", this.personal.getId());
		} else {
			this.estructuraTabla.getPersistencia().put("id_administrador", null);
		}

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Despacho getDespacho() {
		return despacho;
	}

	public void setDespacho(Despacho despacho) {
		this.despacho = despacho;
	}

	public Personal getPersonal() {
		return personal;
	}

	public void setPersonal(Personal personal) {
		this.personal = personal;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getHora() {
		return hora;
	}

	public void setHora(Integer hora) {
		this.hora = hora;
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

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public EstructuraTabla getEstructuraTabla() {
		return estructuraTabla;
	}

	public void setEstructuraTabla(EstructuraTabla estructuraTabla) {
		this.estructuraTabla = estructuraTabla;
	}

}
