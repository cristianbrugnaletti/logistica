package com.tdgroup.Logistica.Service;

import java.util.List;
import java.util.Optional;


import com.tdgroup.Logistica.Model.PreFatturazione;

public interface PreFatturazioneService {

	PreFatturazione aggiungiPreFatturazione(PreFatturazione preFatturazione);
	void eliminaPreFatturazione(PreFatturazione preFatturazione);
	Optional<PreFatturazione> findPreFatturazione(String numeroPrefatturazione);
	PreFatturazione modificaPreFatturazione(PreFatturazione preFatturazione);
	List<PreFatturazione> findAll();
}
