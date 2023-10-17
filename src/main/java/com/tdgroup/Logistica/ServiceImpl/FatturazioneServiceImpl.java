package com.tdgroup.Logistica.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tdgroup.Logistica.Model.Fatturazione;
import com.tdgroup.Logistica.Repository.FatturazioneRepository;
import com.tdgroup.Logistica.Service.FatturazioneService;

@Service
public class FatturazioneServiceImpl implements FatturazioneService {

	@Autowired
	FatturazioneRepository fatturazioneRepository;
	
	
	@Override
	public Fatturazione aggiungiFatturazione(Fatturazione fatturazione) {
		
		return fatturazioneRepository.save(fatturazione);
	}


	@Override
	public void eliminaFatturazione(Fatturazione fatturazione) {
	
		fatturazioneRepository.delete(fatturazione);
	}


	@Override
	public Optional<Fatturazione> findFatturazione(String numeroFatturazione) {
		return fatturazioneRepository.findByNumeroFattura(numeroFatturazione);
	}


	@Override
	public List<Fatturazione> findAll() {
		return fatturazioneRepository.findAll();
	}


	@Override
	public Optional<Fatturazione> findFatturaByNumeroFattura(String numeroFattura) {
		return fatturazioneRepository.findByNumeroFattura(numeroFattura);
	}

	
}