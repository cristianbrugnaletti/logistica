package com.tdgroup.Logistica.ServiceImpl;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tdgroup.Logistica.Model.Fatturazione;
import com.tdgroup.Logistica.Model.Viaggio;
import com.tdgroup.Logistica.Repository.FatturazioneRepository;
import com.tdgroup.Logistica.Repository.ViaggioRepository;
import com.tdgroup.Logistica.Service.FatturazioneService;
import com.tdgroup.Logistica.Service.ViaggioService;

@Service
public class FatturazioneServiceImpl implements FatturazioneService {

	@Autowired
	FatturazioneRepository fatturazioneRepository;
	
	@Autowired
	ViaggioRepository viaggioRepository;
	
	@Autowired
	ViaggioService viaggioService;
	
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


	@Override
	public List<Fatturazione> findFatturazioneByFiltri(String numeroFattura, LocalDateTime dataEmissione, Double totale,
			Double penale, String cliente, String fornitore) {
		return fatturazioneRepository.findFatturazioneByFiltri(numeroFattura, dataEmissione, totale, penale, cliente, fornitore);
	}







	



}