package com.tdgroup.Logistica.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tdgroup.Logistica.Model.PreFatturazione;
import com.tdgroup.Logistica.Repository.PreFatturazioneRepository;
import com.tdgroup.Logistica.Service.PreFatturazioneService;

@Service
public class PreFatturazioneServiceImpl implements PreFatturazioneService {

	@Autowired
	PreFatturazioneRepository preFatturazioneRepository;

	@Override
	public PreFatturazione aggiungiPreFatturazione(PreFatturazione preFatturazione) {
		return preFatturazioneRepository.save(preFatturazione);
	}

	@Override
	public void eliminaPreFatturazione(PreFatturazione preFatturazione) {
		preFatturazioneRepository.delete(preFatturazione);
		
	}

	@Override
	public Optional<PreFatturazione> findPreFatturazione(String numeroPrefatturazione) {
		
		return preFatturazioneRepository.findByNumeroPrefatturazione(numeroPrefatturazione);
	}

	@Override
	public PreFatturazione modificaPreFatturazione(PreFatturazione preFatturazione, LocalDateTime nuovaScadenzaPrefatturazione) {
		return preFatturazioneRepository.save(preFatturazione);
	}

	@Override
	public List<PreFatturazione> findAll() {
		return preFatturazioneRepository.findAll();
	}

	@Override
	public List<PreFatturazione> findPrefatturazioniByFiltri(String numeroPrefatturazione,
			LocalDateTime dataPrefatturazione, Double totale, LocalDateTime scadenzaPrefatturazione, Boolean fatturato,
			String cliente, String fornitore, Double penale, Double importo) {
		
		return preFatturazioneRepository.findPrefatturazioniByFiltri(numeroPrefatturazione, dataPrefatturazione, totale, scadenzaPrefatturazione, fatturato, cliente, fornitore, penale, importo);
	}

	@Override
	public List<PreFatturazione> findPrefatturazioneByFiltri(String numeroPrefatturazione,
			LocalDateTime dataPrefatturazione, Double totale, LocalDateTime scadenzaPrefatturazione, String cliente,
			String fornitore, Double penale, Double importo) {
		return preFatturazioneRepository.findPrefatturazioneByFiltri(numeroPrefatturazione, dataPrefatturazione, totale, scadenzaPrefatturazione,  cliente, fornitore, penale, importo);
	}
}
