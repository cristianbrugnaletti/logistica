package com.tdgroup.Logistica.DTOResponse;

import java.time.LocalDateTime;

public class FatturazioneDTO {
	private String numeroFattura;
    private LocalDateTime dataEmissione;
    private Double importo;
    private Double penale;
    private Double totale;
    private Long cliente;
    private String fornitore;
    private Long idViaggio;
    
    
    
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