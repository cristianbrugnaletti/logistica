package com.tdgroup.Logistica.Model;


import java.time.LocalDateTime;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;




@Entity
public class Fatturazione {
	
		
	
	  	@Id
	  	@Column(name = "NUMERO_FATTURA", nullable = false, unique = true)
	    private String numeroFattura;

	    @Column(name = "DATA_EMISSIONE")
	    private LocalDateTime dataEmissione;

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

	   
	    @ManyToOne
	    @JoinColumn(name = "ID_VIAGGIO")
	    private Viaggio viaggio;

	    
	

	 

	
	 
	    
	

		public Fatturazione() {
			super();
		}

		public Fatturazione(String numeroFattura, LocalDateTime dataEmissione, Double importo, Double penale,
				Double totale, Long cliente, String fornitore, Long idViaggio, Viaggio viaggio) {
			super();
			this.numeroFattura = numeroFattura;
			this.dataEmissione = dataEmissione;
			this.importo = importo;
			this.penale = penale;
			this.totale = totale;
			this.cliente = cliente;
			this.fornitore = fornitore;
			this.viaggio = viaggio;
		}

		public String getNumeroFattura() {
			return numeroFattura;
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


		public Viaggio getViaggio() {
			return viaggio;
		}

		public void setViaggio(Viaggio viaggio) {
			this.viaggio = viaggio;
		}

		public void setNumeroFattura(String numeroFattura) {
			this.numeroFattura = numeroFattura;
		}
	    
	    
	    
	    
	    
	    
	    
}
