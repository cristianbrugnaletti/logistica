package com.tdgroup.Logistica.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Viaggio {
	  	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private long ID;
	  
	  
	  
	  

	public Viaggio() {
		super();
	}

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}
	  
	  
	  
	  
	  
}
