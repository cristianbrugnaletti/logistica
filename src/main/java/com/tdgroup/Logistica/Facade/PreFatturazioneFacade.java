package com.tdgroup.Logistica.Facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import com.tdgroup.Logistica.DTORequest.PreFatturazioneRequest;
import com.tdgroup.Logistica.DTOResponse.FatturazioneDTO;
import com.tdgroup.Logistica.DTOResponse.PreFatturazioneDTO;
import com.tdgroup.Logistica.Mapper.PreFatturazioneMapper;
import com.tdgroup.Logistica.Model.Fatturazione;
import com.tdgroup.Logistica.Model.PreFatturazione;
import com.tdgroup.Logistica.Model.Viaggio;
import com.tdgroup.Logistica.Repository.PreFatturazioneRepository;

import com.tdgroup.Logistica.Service.PreFatturazioneService;
import com.tdgroup.Logistica.Service.ViaggioService;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PreFatturazioneFacade {


	@Autowired
	PreFatturazioneService preFatturazioneService;
	@Autowired
	PreFatturazioneMapper preFatturazioneMapper;

	@Autowired
	PreFatturazioneRepository preFatturazioneRepository;

	@Autowired
	ViaggioService viaggioService;

	private static final Logger logger = LoggerFactory.getLogger(PreFatturazioneFacade.class);

	public PreFatturazioneDTO aggiungiPreFatturazione(PreFatturazioneRequest request) {
		try {
			// Verifica la validità della richiesta
			logger.info("Inizio elaborazione richiesta di aggiunta pre-fatturazione");
			if (request == null ||
					request.getDataPrefatturazione() == null ||
					request.getScadenzaPrefatturazione() == null ||
					request.getImporto() == null ||
					request.getPenale() == null ||

					request.getCliente() == null ||
					StringUtils.isBlank(request.getFornitore()) ||
					request.getIdViaggio() == null) {
				throw new IllegalArgumentException("Attributi mancanti o non validi nella richiesta di aggiunta pre-fatturazione");
			}
			logger.info("Validità della richiesta verificata con successo");
			// Estrai i dati dalla richiesta
			LocalDateTime dataPrefatturazione = request.getDataPrefatturazione();
			Double importo = request.getImporto();
			Double penale = request.getPenale();

			Long cliente = request.getCliente();
			String fornitore = request.getFornitore();
			Long idViaggio = request.getIdViaggio();
			LocalDateTime scadenzaPreFatturazione = request.getScadenzaPrefatturazione();
			// Genera il nuovo numero di prefatturazione autoincrementato
			logger.info("Generazione del nuovo numero di prefatturazione");
			String nuovoNumeroPrefatturazione = generaNumeroPreFatturazione();
			Double totale = importo + penale;
			if (scadenzaPreFatturazione.isBefore(dataPrefatturazione)) {
				throw new IllegalArgumentException("La scadenza della prefatturazione deve essere successiva alla data di prefatturazione");
			}

			if (importo < 0) {
				throw new IllegalArgumentException("L'importo non può essere negativo");
			}


			if (penale < 0) {
				throw new IllegalArgumentException("La penale non può essere negativa");
			}
			// Ottieni il viaggio associato all'ID
			Optional<Viaggio> viaggioOptional = viaggioService.getViaggioById(idViaggio);
			if (!viaggioOptional.isPresent()) {
				throw new IllegalArgumentException("Viaggio non trovato per l'ID specificato: " + idViaggio);
			}
			Viaggio viaggio = viaggioOptional.get();

			// Creo un'istanza di PreFatturazione
			PreFatturazione preFatturazione = new PreFatturazione();
			preFatturazione.setNumeroPrefatturazione(nuovoNumeroPrefatturazione);
			preFatturazione.setDataPrefatturazione(dataPrefatturazione);
			preFatturazione.setImporto(importo);
			preFatturazione.setPenale(penale);
			preFatturazione.setTotale(totale);
			preFatturazione.setCliente(cliente);
			preFatturazione.setFornitore(fornitore);
			preFatturazione.setViaggio(viaggio);
			preFatturazione.setScadenzaPrefatturazione(scadenzaPreFatturazione);
			// Aggiungo la pre-fatturazione
			PreFatturazione preFatturazioneAggiunta = preFatturazioneService.aggiungiPreFatturazione(preFatturazione);
			logger.info("Aggiunta della pre-fatturazione completata con successo");
			// Converto la pre-fatturazione aggiunta in DTO e la restituisco

			return preFatturazioneMapper.preFatturazioneToDTO(preFatturazioneAggiunta);

		} catch (IllegalArgumentException e) {
			logger.error("Errore durante l'aggiunta della pre-fatturazione: attributi mancanti o non validi", e);
			throw new IllegalArgumentException(e.getMessage());
		} catch (Exception e) {
			logger.error("Errore imprevisto nell'aggiunta della pre-fatturazione", e);
			throw new RuntimeException("Errore imprevisto nell'aggiunta della pre-fatturazione");
		}
	}

	public String generaNumeroPreFatturazione() {
		String prefisso = "NPF";  

		Long ultimoNumeroPrefattura = preFatturazioneRepository.findLastNumeroPreFatturaByPrefix(prefisso);

		// Se l'ultimo numero di prefattura non è null, incremento di 1
		Long nuovoNumeroPrefattura = (ultimoNumeroPrefattura != null) ? ultimoNumeroPrefattura + 1 : 1;

		// Genero il numero di prefattura includendo il prefisso e il numero di prefattura a 4 cifre
		String numeroPrefattura = prefisso + String.format("%04d", nuovoNumeroPrefattura);

		logger.debug("Numero di prefattura generato: {}", numeroPrefattura);

		return numeroPrefattura;
	}




	public void rimuoviPreFattura(String numeroPreFatturazione) {
		try {
			if (StringUtils.isBlank(numeroPreFatturazione)) {
				logger.error("Il numero di fattura è vuoto o nullo");
				throw new ResponseStatusException(HttpStatus.OK, "Il numero di fattura non può essere vuoto");
			}

			logger.info("Rimozione della fattura con numero: {}", numeroPreFatturazione);

			PreFatturazione preFatturazioneEsistente = preFatturazioneService.findPreFatturazione(numeroPreFatturazione)
					.orElseThrow(()-> new ResponseStatusException(HttpStatus.OK, "Fatturazione non trovata per il numero: " + numeroPreFatturazione));

			preFatturazioneService.eliminaPreFatturazione(preFatturazioneEsistente);

			logger.info("Fattura rimossa con successo: {}", numeroPreFatturazione);

		} catch (ResponseStatusException e) {
			logger.error("Errore durante la rimozione della fattura: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Errore imprevisto durante la rimozione della fattura", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Errore durante la rimozione della fattura", e);
		}
	}



	public PreFatturazioneDTO modificaPreFatturazione(String numeroPrefatturazione, PreFatturazioneRequest request) {
		try {
			logger.info("Inizio elaborazione richiesta di modifica pre-fatturazione");

			logger.debug("Recupero della pre-fatturazione per il numero: {}", numeroPrefatturazione);
			PreFatturazione preFatturazioneEsistente = preFatturazioneService
					.findPreFatturazione(numeroPrefatturazione)
					.orElseThrow(() -> new IllegalArgumentException("La pre-fatturazione " + numeroPrefatturazione + " non esiste"));

			LocalDateTime dataPrefatturazione = request.getDataPrefatturazione();
			Double importo = request.getImporto();
			Double penale = request.getPenale();
			Long cliente = request.getCliente();
			String fornitore = request.getFornitore();
			Long idViaggio = request.getIdViaggio();
			LocalDateTime scadenzaPreFatturazione = request.getScadenzaPrefatturazione();
			Double totale = importo + penale;
			
			if (scadenzaPreFatturazione.isBefore(dataPrefatturazione)) {
				throw new IllegalArgumentException("La scadenza della prefatturazione deve essere successiva alla data di prefatturazione");
			}
			
			if (importo < 0) {
				logger.error("L'importo non può essere negativo");
				throw new IllegalArgumentException("L'importo non può essere negativo");
			}

			if (penale < 0) {
				logger.error("La penale non può essere negativa");
				throw new IllegalArgumentException("La penale non può essere negativa");
			}

			Optional<Viaggio> viaggioOptional = viaggioService.getViaggioById(idViaggio);
			if (!viaggioOptional.isPresent()) {
				logger.error("Viaggio non trovato per l'ID specificato: {}", idViaggio);
				throw new IllegalArgumentException("Viaggio non trovato per l'ID specificato: " + idViaggio);
			}
			Viaggio viaggio = viaggioOptional.get();

			preFatturazioneEsistente.setDataPrefatturazione(dataPrefatturazione);
			preFatturazioneEsistente.setImporto(importo);
			preFatturazioneEsistente.setPenale(penale);
			preFatturazioneEsistente.setTotale(totale);
			preFatturazioneEsistente.setCliente(cliente);
			preFatturazioneEsistente.setFornitore(fornitore);
			preFatturazioneEsistente.setViaggio(viaggio);
			preFatturazioneEsistente.setScadenzaPrefatturazione(scadenzaPreFatturazione);

			logger.info("Salva le modifiche alla pre-fatturazione");
			PreFatturazione preFatturazioneModificata = preFatturazioneService.modificaPreFatturazione(preFatturazioneEsistente);

			logger.info("Conversione della pre-fatturazione modificata in DTO");

			logger.info("Fine elaborazione richiesta di modifica pre-fatturazione");

			return preFatturazioneMapper.preFatturazioneToDTO(preFatturazioneModificata);

		} catch (IllegalArgumentException e) {
			logger.error("Errore durante la validazione della richiesta", e);
			throw new IllegalArgumentException(e.getMessage());
		} catch (Exception e) {
			logger.error("Errore imprevisto nella modifica della pre-fatturazione", e);
			throw new RuntimeException("Errore imprevisto nella modifica della pre-fatturazione");
		}
	}



	public List<PreFatturazioneDTO> VisualizzaTutteLePreFatture() {
		try {

			logger.info("Inizio visualizzazione di tutte le pre-fatture");

			List<PreFatturazione> preFatturazioni = preFatturazioneService.findAll();

			if (preFatturazioni.isEmpty()) {

				logger.error("Nessuna fattura trovata");
				throw new IllegalArgumentException("Nessuna fattura trovata");
			}


			logger.info("Conversione delle pre-fatturazioni in DTO");
			return preFatturazioneMapper.PreFatturazioneToDTOList(preFatturazioni);

		} catch (IllegalArgumentException e) {

			logger.error("Errore durante la visualizzazione delle pre-fatture", e);
			throw e;  
		} catch (Exception e) {

			logger.error("Errore imprevisto nella visualizzazione delle pre-fatture", e);
			throw new RuntimeException("Errore imprevisto nella visualizzazione delle pre-fatture");
		}
	}



	public PreFatturazioneDTO visualizzaPreFatturazione(String numeroPreFatturazione) {
		if (StringUtils.isBlank( numeroPreFatturazione)) {
			throw new ResponseStatusException(HttpStatus.OK, "Il numero di prefattura non può essere vuoto");
		}

		try {
			logger.info("Inizio ricerca della fatturazione con numero di fattura: {}", numeroPreFatturazione);

			PreFatturazioneDTO preFatturazioneDTO = preFatturazioneService.findPreFatturazione(numeroPreFatturazione)
					.map(preFatturazioneMapper::preFatturazioneToDTO)
					.orElseThrow(() -> {
						logger.warn("Fatturazione non trovata con il numero di fattura: {}", numeroPreFatturazione);
						return new ResponseStatusException(HttpStatus.OK, "Fatturazione non trovata con numero: "+numeroPreFatturazione);
					});

			logger.info("Fatturazione trovata con il numero di fattura: {}", numeroPreFatturazione);

			return preFatturazioneDTO;
		} catch (ResponseStatusException e) {
			throw e; 
		} catch (Exception e) {
			logger.error("Errore durante la ricerca della prefatturazione con il numero di prefattura: {}", numeroPreFatturazione, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Errore durante la ricerca della prefatturazione", e);
		}
	}

}
