package com.tdgroup.Logistica.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
	

    public List<Fatturazione> findFatturazioneByFiltri(String numeroFattura,LocalDateTime dataEmissione,Double totale,Double penale,String cliente,String fornitore);
	 
}
