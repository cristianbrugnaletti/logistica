package com.tdgroup.Logistica.DTORequest;

import java.time.LocalDateTime;
import java.util.List;

public class PreFatturazioneRequest {

	
	
	private Double importo;
	private Double penale;
	private String cliente;
	private String fornitore;
	private LocalDateTime scadenzaPrefatturazione;
	private List<Long> idViaggio;
	private Boolean fatturato;


	public Double getImporto() {
		return importo;
	}
	public void setImporto(Double importo) {
		this.importo = importo;
	}
	public Double getPenale() {
		return penale;
	}
	public void setPenale(Double penale) {
		this.penale = penale;
	}

	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getFornitore() {
		return fornitore;
	}
	public void setFornitore(String fornitore) {
		this.fornitore = fornitore;
	}
	public LocalDateTime getScadenzaPrefatturazione() {
		return scadenzaPrefatturazione;
	}
	public void setScadenzaPrefatturazione(LocalDateTime scadenzaPrefatturazione) {
		this.scadenzaPrefatturazione = scadenzaPrefatturazione;
	}
	public List<Long> getIdViaggio() {
		return idViaggio;
	}
	public void setIdViaggio(List<Long> idViaggio) {
		this.idViaggio = idViaggio;
	}
	public Boolean getFatturato() {
		return fatturato;
	}
	public void setFatturato(Boolean fatturato) {
		this.fatturato = fatturato;
	}

	




}
