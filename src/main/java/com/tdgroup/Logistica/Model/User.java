package com.tdgroup.Logistica.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name = "users")
public class User {


	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean attivo;

    @Column(nullable = false)
    private String tipoUtente;
    
    
    
    
    
    
    
    
    

	public User(Long id, String username, String password, boolean attivo, String tipoUtente) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.attivo = attivo;
		this.tipoUtente = tipoUtente;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAttivo() {
		return attivo;
	}

	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}

	public String getTipoUtente() {
		return tipoUtente;
	}
	
	/*
	
	
	   public void setTipoUtente(String tipoUtente) {
	        if ("admin".equalsIgnoreCase(tipoUtente) || "utente".equalsIgnoreCase(tipoUtente)) {
	            this.tipoUtente = tipoUtente.toLowerCase(); // Consistenza nel salvataggio
	        } else {
	            throw new IllegalArgumentException("Il tipo di utente deve essere 'admin' o 'utente'.");
	        }
	    }
	
    
    */
    
}
