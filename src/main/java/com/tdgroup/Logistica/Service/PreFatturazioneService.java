package com.tdgroup.Logistica.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import com.tdgroup.Logistica.Model.PreFatturazione;

public interface PreFatturazioneService {

	PreFatturazione aggiungiPreFatturazione(PreFatturazione preFatturazione);
	void eliminaPreFatturazione(PreFatturazione preFatturazione);
	Optional<PreFatturazione> findPreFatturazione(String numeroPrefatturazione);
	PreFatturazione modificaPreFatturazione(PreFatturazione preFatturazione, LocalDateTime nuovaScadenzaPrefatturazione);

	List<PreFatturazione> findAll();
	List<PreFatturazione> findPrefatturazioniByFiltri(String numeroPrefatturazione, LocalDateTime dataPrefatturazione, Double totale, LocalDateTime scadenzaPrefatturazione, Boolean fatturato, String cliente, String fornitore, Double penale, Double importo);
	List<PreFatturazione> findPrefatturazioneByFiltri(String numeroPrefatturazione, LocalDateTime dataPrefatturazione, Double totale, LocalDateTime scadenzaPrefatturazione,  String cliente, String fornitore, Double penale, Double importo);
}
