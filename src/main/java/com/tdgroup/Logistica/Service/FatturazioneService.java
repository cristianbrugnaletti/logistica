package com.tdgroup.Logistica.Service;

import java.util.List;
import java.util.Optional;



import com.tdgroup.Logistica.Model.Fatturazione;
import com.tdgroup.Logistica.Model.Viaggio;

public interface FatturazioneService {

	Fatturazione aggiungiFatturazione(Fatturazione fatturazione);
	void eliminaFatturazione(Fatturazione fatturazione);
	Optional<Fatturazione> findFatturazione(String numeroFatturazione);
	List<Fatturazione>findAll();
	Optional<Fatturazione>findFatturaByNumeroFattura(String numeroFattura);
	
	
	 
}
