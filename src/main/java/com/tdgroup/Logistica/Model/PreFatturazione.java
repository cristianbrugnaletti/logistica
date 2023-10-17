package com.tdgroup.Logistica.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class PreFatturazione {
	 	@Id
	 	@Column(name = "NUMERO_PREFATTURAZIONE", nullable = false, unique = true)
	    private String numeroPrefatturazione;

	    @Column(name = "DATA_PREFATTURAZIONE")
	    private LocalDateTime dataPrefatturazione;

	    @Column(name = "IMPORTO")
	    private Double importo;

	    @Column(name = "PENALE")
	    private Double penale;

	    @Column(name = "TOTALE")
	    private Double totale;

	    @Column(name = "CLIENTE")
	    private Long cliente;

	    @Column(name = "FORNITORE")
	    private String fornitore;

	    @Column(name = "SCADENZA_PREFATTURAZIONE")
	    private LocalDateTime scadenzaPrefatturazione;


	    @ManyToOne
	    @JoinColumn(name = "ID_VIAGGIO")
	    private Viaggio viaggio;

	    
	    
	    
	    
	    
	    
	    
	    

		public PreFatturazione() {
			super();
		}


		public PreFatturazione(String numeroPrefatturazione, LocalDateTime dataPrefatturazione, Double importo,
				Double penale, Double totale, Long cliente, String fornitore, LocalDateTime scadenzaPrefatturazione,
				Viaggio viaggio) {
			super();
			this.numeroPrefatturazione = numeroPrefatturazione;
			this.dataPrefatturazione = dataPrefatturazione;
			this.importo = importo;
			this.penale = penale;
			this.totale = totale;
			this.cliente = cliente;
			this.fornitore = fornitore;
			this.scadenzaPrefatturazione = scadenzaPrefatturazione;
			this.viaggio = viaggio;
		}


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


		public Viaggio getViaggio() {
			return viaggio;
		}


		public void setViaggio(Viaggio viaggio) {
			this.viaggio = viaggio;
		}
	    
	    
	    
	    
	    
	    
	    
	    
	    
}