package com.tdgroup.Logistica.DTOResponse;

import java.time.LocalDateTime;

public class PreFatturazioneDTO {
	private String numeroPrefatturazione;
    private LocalDateTime dataPrefatturazione;
    private Double importo;
    private Double penale;
    private Double totale;
    private Long cliente;
    private String fornitore;
    private LocalDateTime scadenzaPrefatturazione;
	public String getNumeroPrefatturazione() {
		return numeroPrefatturazione;
	}
	public void setNumeroPrefatturazione(String numeroPrefatturazione) {
		this.numeroPrefatturazione = numeroPrefatturazione;
	}
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
	public LocalDateTime getScadenzaPrefatturazione() {
		return scadenzaPrefatturazione;
	}
	public void setScadenzaPrefatturazione(LocalDateTime scadenzaPrefatturazione) {
		this.scadenzaPrefatturazione = scadenzaPrefatturazione;
	}
    
    
    
    
}
