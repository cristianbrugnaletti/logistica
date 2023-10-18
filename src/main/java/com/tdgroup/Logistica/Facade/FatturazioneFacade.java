package com.tdgroup.Logistica.Facade;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tdgroup.Logistica.DTORequest.FatturazioneRequest;
import com.tdgroup.Logistica.DTOResponse.FatturazioneDTO;
import com.tdgroup.Logistica.Mapper.FatturazioneMapper;
import com.tdgroup.Logistica.Model.Fatturazione;
import com.tdgroup.Logistica.Model.Viaggio;
import com.tdgroup.Logistica.Repository.FatturazioneRepository;
import com.tdgroup.Logistica.Service.FatturazioneService;
import com.tdgroup.Logistica.Service.ViaggioService;

@Service
public class FatturazioneFacade {


	@Autowired
	FatturazioneService fatturazioneService;
	@Autowired
	FatturazioneMapper fatturazioneMapper;

	@Autowired
	FatturazioneRepository fatturazioneRepository;

	@Autowired
	ViaggioService viaggioService;

	private static final Logger logger = LoggerFactory.getLogger(FatturazioneFacade.class);

	public FatturazioneDTO aggiungiFatturazione(FatturazioneRequest request) {
		try {
			if (request == null ||
					request.getDataEmissione() == null ||
					request.getImporto() == null ||
					request.getPenale() == null ||
					request.getCliente() == null ||
					StringUtils.isBlank(request.getFornitore()) ||
					request.getIdViaggio() == null) {
				logger.error("Attributi mancanti o non validi nella richiesta di aggiunta fatturazione");
				throw new IllegalArgumentException("Attributi mancanti o non validi nella richiesta di aggiunta fatturazione");
			}

			LocalDateTime dataEmissione = request.getDataEmissione();
			Double importo = request.getImporto();
			Double penale = request.getPenale();

			Long cliente = request.getCliente();
			String fornitore = request.getFornitore();
			Long idViaggio = request.getIdViaggio();
			Double totale = importo + penale;

			if (importo < 0) {
				logger.error("L'importo non può essere negativo");
				throw new IllegalArgumentException("L'importo non può essere negativo");
			}

			if (penale < 0) {
				logger.error("La penale non può essere negativa");
				throw new IllegalArgumentException("La penale non può essere negativa");
			}


			String nuovoNumeroFattura = generaNumeroFattura();
			logger.info("Nuovo numero di fattura generato: {}", nuovoNumeroFattura);
			Optional<Viaggio> viaggioOptional = viaggioService.getViaggioById(idViaggio);
			if (!viaggioOptional.isPresent()) {
				logger.error("Viaggio non trovato per l'ID specificato: " + idViaggio);
				throw new IllegalArgumentException("Viaggio non trovato per l'ID specificato: " + idViaggio);
			}
			Viaggio viaggio = viaggioOptional.get();

			Fatturazione fatturazione = new Fatturazione();
			fatturazione.setDataEmissione(dataEmissione);
			fatturazione.setImporto(importo);
			fatturazione.setPenale(penale);
			fatturazione.setTotale(totale);
			fatturazione.setCliente(cliente);
			fatturazione.setFornitore(fornitore);
			fatturazione.setViaggio(viaggio);
			fatturazione.setNumeroFattura(nuovoNumeroFattura);

			Fatturazione fatturazioneAggiunta = fatturazioneService.aggiungiFatturazione(fatturazione);
			logger.info("Fatturazione aggiunta con successo: {}", fatturazioneAggiunta.getNumeroFattura());

			return fatturazioneMapper.fatturazioneToDTO(fatturazioneAggiunta);

		} catch (IllegalArgumentException e) {
			logger.error("Errore durante l'aggiunta della fatturazione: attributi mancanti o non validi", e);
			throw new IllegalArgumentException(e.getMessage());
		} catch (Exception e) {
			logger.error("Errore imprevisto nell'aggiunta della fatturazione", e);
			throw new RuntimeException("Errore imprevisto nell'aggiunta della fatturazione", e);
		}
	}

	public String generaNumeroFattura() {
		String prefisso = "NF";  

		Long ultimoNumeroFattura = fatturazioneRepository.findLastNumeroFatturaByPrefix(prefisso);

		// Se l'ultimo numero di fattura non è null, incremento di 1
		Long nuovoNumeroFattura = (ultimoNumeroFattura != null) ? ultimoNumeroFattura + 1 : 1;

		// Genero il numero di fattura includendo il prefisso e il numero di fattura a 4 cifre
		String numeroFattura = prefisso + String.format("%04d", nuovoNumeroFattura);

		logger.info("Numero di fattura generato: {}", numeroFattura);

		return numeroFattura;
	}




	public void rimuoviFattura(String numeroFattura) {
		try {
			if (StringUtils.isBlank(numeroFattura)) {
				logger.error("Il numero di fattura è vuoto o nullo");
				throw new ResponseStatusException(HttpStatus.OK, "Il numero di fattura non può essere vuoto");
			}

			logger.info("Rimozione della fattura con numero: {}", numeroFattura);

			Fatturazione fatturazioneEsistente = fatturazioneService.findFatturazione(numeroFattura)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, "Fatturazione non trovata per il numero: " + numeroFattura));

			fatturazioneService.eliminaFatturazione(fatturazioneEsistente);
			logger.info("Fattura rimossa con successo: {}", numeroFattura);

		} catch (ResponseStatusException e) {
			logger.error("Errore durante la rimozione della fattura: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Errore imprevisto durante la rimozione della fattura", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Errore durante la rimozione della fattura", e);
		}
	}


	public List<FatturazioneDTO> VisualizzaTutteLeFatture() {
		List<Fatturazione> fatturazioni = fatturazioneService.findAll();
		logger.info("chiamata per tutte le fatture");
		if (fatturazioni == null || fatturazioni.isEmpty()) {
			logger.error("nessuna fatturazione trovata");
			throw new IllegalArgumentException("Nessuna fattura presente");

		}

		return fatturazioneMapper.fatturazioneToDTOList(fatturazioni);

	}

	public FatturazioneDTO visualizzaFatturazione(String numeroFattura) {
		if (StringUtils.isBlank(numeroFattura)) {
			logger.error("Il numero di fattura non può essere vuoto");
			throw new ResponseStatusException(HttpStatus.OK, "Il numero di fattura non può essere vuoto");
		}

		try {
			logger.info("Inizio ricerca della fatturazione con numero di fattura: {}", numeroFattura);

			FatturazioneDTO fatturazioneDTO = fatturazioneService.findFatturaByNumeroFattura(numeroFattura)
					.map(fatturazioneMapper::fatturazioneToDTO)
					.orElseThrow(() -> {
						logger.warn("Fatturazione non trovata con il numero di fattura: {}", numeroFattura);
						return new ResponseStatusException(HttpStatus.OK, "Fatturazione non trovata con numero di fattura: "+numeroFattura);
					});

			logger.info("Fatturazione trovata con il numero di fattura: {}", numeroFattura);

			return fatturazioneDTO;
		} catch (ResponseStatusException e) {
			throw e; 
		} catch (Exception e) {
			logger.error("Errore durante la ricerca della fatturazione con il numero di fattura: {}", numeroFattura, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Errore durante la ricerca della fatturazione", e);
		}
	}



}
