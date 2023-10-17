package com.tdgroup.Logistica.DTORequest;

import java.time.LocalDateTime;

public class FatturazioneRequest {



	private LocalDateTime dataEmissione;
	private Double importo;
	private Double penale;  
	private Long cliente;
	private String fornitore;
	private Long idViaggio;


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
	public Long getIdViaggio() {
		return idViaggio;
	}
	public void setIdViaggio(Long idViaggio) {
		this.idViaggio = idViaggio;
	}



}
