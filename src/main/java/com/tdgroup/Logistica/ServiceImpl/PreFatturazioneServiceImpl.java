package com.tdgroup.Logistica.ServiceImpl;

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
	public PreFatturazione modificaPreFatturazione(PreFatturazione preFatturazione) {
		return preFatturazioneRepository.save(preFatturazione);
	}

	@Override
	public List<PreFatturazione> findAll() {
		return preFatturazioneRepository.findAll();
	}
}
