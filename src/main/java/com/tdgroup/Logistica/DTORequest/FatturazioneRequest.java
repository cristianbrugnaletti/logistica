package com.tdgroup.Logistica.DTORequest;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FatturazioneRequest {



	private LocalDateTime dataEmissione;



	public LocalDateTime getDataEmissione() {
		return dataEmissione;
	}
	public void setDataEmissione(LocalDateTime dataEmissione) {
		this.dataEmissione = dataEmissione;
	}





}
