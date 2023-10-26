package com.tdgroup.Logistica.DTORequest;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FatturazioneRequest {



	private LocalDateTime dataEmissione;
	private Double importo;
	private Double penale;  
	private String cliente;
	private String fornitore;
	private List<Long> idViaggio;


	public LocalDateTime getDataEmissione() {
		return dataEmissione;
	}
	public void setDataEmissione(LocalDateTime dataEmissione) {
		this.dataEmissione = dataEmissione;
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
	public List<Long> getIdViaggio() {
		return idViaggio;
	}
	public void setIdViaggio(List<Long> idViaggio) {
		this.idViaggio = idViaggio;
	}




}
