package com.tdgroup.Logistica.Service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tdgroup.Logistica.Model.Fatturazione;
import com.tdgroup.Logistica.Model.Viaggio;


public interface ViaggioService {

	 Optional<Viaggio> getViaggioById(Long id);
	 Viaggio aggiungiViaggio(Viaggio viaggio);
	 
	 
}
