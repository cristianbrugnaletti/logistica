package com.tdgroup.Logistica.DTORequest;

import java.time.LocalDateTime;

public class PreFatturazioneRequest {


	private LocalDateTime dataPrefatturazione;
	private Double importo;
	private Double penale;
	private Long cliente;
	private String fornitore;
	private LocalDateTime scadenzaPrefatturazione;
	private Long idViaggio;


	public LocalDateTime getDataPrefatturazione() {
		return dataPrefatturazione;
	}
	public void setDataPrefatturazione(LocalDateTime dataPrefatturazione) {
		this.dataPrefatturazione = dataPrefatturazione;
	}
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

	public Long getCliente() {
		return cliente;
	}
	public void setCliente(Long cliente) {
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
	public Long getIdViaggio() {
		return idViaggio;
	}
	public void setIdViaggio(Long idViaggio) {
		this.idViaggio = idViaggio;
	}




}
