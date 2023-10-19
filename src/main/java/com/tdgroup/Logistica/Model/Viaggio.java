package com.tdgroup.Logistica.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Viaggio {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private long ID;

	    @ManyToOne 
	    @JoinColumn(name = "fatturazione_numero_fattura")
	    private Fatturazione fatturazione;

	    @ManyToOne 
	    @JoinColumn(name = "pre_fatturazione_numero_pre_fatturazione")
	    private PreFatturazione preFatturazione;
	    
	    

	    public Viaggio() {
	        super();
	    }

	    public long getID() {
	        return ID;
	    }

	    public void setID(long iD) {
	        ID = iD;
	    }

	    public Fatturazione getFatturazione() {
	        return fatturazione;
	    }

	    public void setFatturazione(Fatturazione fatturazione) {
	        this.fatturazione = fatturazione;
	    }

		public PreFatturazione getPreFatturazione() {
			return preFatturazione;
		}

		public void setPreFatturazione(PreFatturazione preFatturazione) {
			this.preFatturazione = preFatturazione;
		}

	
	  
	  
	  
}
