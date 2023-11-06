package com.tdgroup.Logistica.DTOResponse;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FatturazioneDTO {
	private String numeroFattura;
    private LocalDateTime dataEmissione;
    private Double importo;
    private Double penale;
    private Double totale;
    private String cliente;
    private String fornitore;
    private List<Long> idViaggio;
    
    
    
	public String getNumeroFattura() {
		return numeroFattura;
	}
	public void setNumeroFattura(String numeroFattura) {
		this.numeroFattura = numeroFattura;
	}
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
	public Double getTotale() {
		return totale;
	}
	public void setTotale(Double totale) {
		this.totale = totale;
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
