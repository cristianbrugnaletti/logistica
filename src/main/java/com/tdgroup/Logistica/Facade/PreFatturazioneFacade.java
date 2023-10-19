package com.tdgroup.Logistica.Facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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

import jakarta.transaction.Transactional;

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
	    	   if (request == null || request.getDataPrefatturazione() == null || request.getImporto() == null ||
	   	            request.getPenale() == null || request.getCliente() == null ||
	   	            StringUtils.isBlank(request.getFornitore()) || request.getIdViaggio() == null || request.getIdViaggio().isEmpty()) {
	   	            logger.error("Attributi mancanti o non validi nella richiesta di aggiunta fatturazione");
	   	            throw new IllegalArgumentException("Attributi mancanti o non validi nella richiesta di aggiunta fatturazione");
	   	        }

	   	        LocalDateTime dataPrefatturazione = request.getDataPrefatturazione();
	   	        Double importo = request.getImporto();
	   	        Double penale = request.getPenale();
	   	        LocalDateTime scadenzaPrefatturazione = request.getScadenzaPrefatturazione();
	   	        Long cliente = request.getCliente();
	   	        String fornitore = request.getFornitore();
	   	        List<Long> idViaggi = request.getIdViaggio();
	   	        Double totale = importo + penale;

	   	        if (importo < 0) {
	   	            logger.error("L'importo non può essere negativo");
	   	            throw new IllegalArgumentException("L'importo non può essere negativo");
	   	        }

	   	        if (penale < 0) {
	   	            logger.error("La penale non può essere negativa");
	   	            throw new IllegalArgumentException("La penale non può essere negativa");
	   	        }

	   	        // Crea una nuova istanza di Fatturazione
	   	        PreFatturazione preFatturazione = new PreFatturazione();
	   	        preFatturazione.setDataPrefatturazione(dataPrefatturazione);
	   	        preFatturazione.setImporto(importo);
	   	        preFatturazione.setPenale(penale);
	   	        preFatturazione.setTotale(totale);
	   	        preFatturazione.setCliente(cliente);
	   	        preFatturazione.setFornitore(fornitore);
	   	        preFatturazione.setScadenzaPrefatturazione(scadenzaPrefatturazione);
	   	        String nuovoNumeroFattura = generaNumeroPreFatturazione();
	   	        logger.info("Nuovo numero di fattura generato: {}", nuovoNumeroFattura);
	   	        preFatturazione.setNumeroPrefatturazione(nuovoNumeroFattura);

	   	        // Salva la Fatturazione
	   	        PreFatturazione preFatturazioneAggiunta = preFatturazioneService.aggiungiPreFatturazione(preFatturazione);

	   	        // Crea una lista di Viaggio e assegna la Fatturazione a ciascun Viaggio
	   	        List<Viaggio> viaggi = new ArrayList<>();
	   	        for (Long idViaggio : idViaggi) {
	   	            Optional<Viaggio> viaggioOptional = viaggioService.getViaggioById(idViaggio);
	   	            if (!viaggioOptional.isPresent()) {
	   	                logger.error("Viaggio non trovato per l'ID specificato: " + idViaggio);
	   	                throw new IllegalArgumentException("Viaggio non trovato per l'ID specificato: " + idViaggio);
	   	            }

	   	            Viaggio viaggio = viaggioOptional.get();
	   	            viaggio.setPreFatturazione(preFatturazioneAggiunta);
	   	            viaggi.add(viaggio);
	   	        }

	   	        // Aggiungi la lista di Viaggio alla Fatturazione
	   	        preFatturazioneAggiunta.setViaggi(viaggi);

	   	        // Ora puoi salvare nuovamente la Fatturazione con le associazioni ai Viaggi
	   	        preFatturazioneAggiunta = preFatturazioneService.aggiungiPreFatturazione(preFatturazioneAggiunta);
	   	        logger.info("Fatturazione aggiunta con successo: {}", preFatturazioneAggiunta.getNumeroPrefatturazione());

	   	        return preFatturazioneMapper.preFatturazioneToDTO(preFatturazioneAggiunta);

	    } catch (IllegalArgumentException e) {
	        logger.error("Errore durante l'aggiunta della pre-fatturazione: attributi mancanti o non validi", e);
	        throw new IllegalArgumentException(e.getMessage());
	    } catch (Exception e) {
	        logger.error("Errore imprevisto nell'aggiunta della pre-fatturazione", e);
	        throw new RuntimeException("Errore imprevisto nell'aggiunta della pre-fatturazione", e);
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
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il numero di prefattura non può essere vuoto");
            }

            logger.info("Rimozione della fattura con numero: {}", numeroPreFatturazione);

            PreFatturazione preFatturazioneEsistente = preFatturazioneRepository.findByNumeroPrefatturazione(numeroPreFatturazione)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fatturazione non trovata per il numero: " + numeroPreFatturazione));

            // Verifica se la fattura è associata a un viaggio e rimuovila, se necessario
            List<Viaggio> viaggiAssociati = preFatturazioneEsistente.getViaggi();
            if (viaggiAssociati != null) {
                for (Viaggio viaggio : viaggiAssociati) {
                    viaggio.setPreFatturazione(null);
                }
                preFatturazioneEsistente.setViaggi(null);
                preFatturazioneService.eliminaPreFatturazione(preFatturazioneEsistente);
            }

            preFatturazioneRepository.delete(preFatturazioneEsistente);

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

	        // Step 1: Verifica della validità della richiesta
	        if (request == null ||
	            request.getDataPrefatturazione() == null ||
	            request.getScadenzaPrefatturazione() == null ||
	            request.getImporto() == null ||
	            request.getPenale() == null ||
	            request.getCliente() == null ||
	            StringUtils.isBlank(request.getFornitore()) ||
	            request.getIdViaggio() == null) {
	            logger.error("Attributi mancanti o non validi nella richiesta di modifica pre-fatturazione");
	            throw new IllegalArgumentException("Attributi mancanti o non validi nella richiesta di modifica pre-fatturazione");
	        }

	        // Step 2: Recupero dell'entità esistente
	        logger.debug("Recupero della pre-fatturazione per il numero: {}", numeroPrefatturazione);
	        PreFatturazione preFatturazioneEsistente = preFatturazioneService
	            .findPreFatturazione(numeroPrefatturazione)
	            .orElseThrow(() -> new IllegalArgumentException("La pre-fatturazione " + numeroPrefatturazione + " non esiste"));

	        // Step 3: Validazione dei dati
	        LocalDateTime dataPrefatturazione = request.getDataPrefatturazione();
	        Double importo = request.getImporto();
	        Double penale = request.getPenale();
	        Long cliente = request.getCliente();
	        String fornitore = request.getFornitore();
	        List<Long> idViaggi = request.getIdViaggio();
	        LocalDateTime scadenzaPreFatturazione = request.getScadenzaPrefatturazione();
	        Double totale = importo + penale;

	        // Controlli aggiuntivi
	        if (scadenzaPreFatturazione.isBefore(dataPrefatturazione)) {
	            logger.error("La scadenza della prefatturazione deve essere successiva alla data di prefatturazione");
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

	        // Step 4: Recupero dei viaggi associati
	        List<Viaggio> viaggi = new ArrayList<>();
	        for (Long idViaggio : idViaggi) {
	            Optional<Viaggio> viaggioOptional = viaggioService.getViaggioById(idViaggio);
	            if (!viaggioOptional.isPresent()) {
	                logger.error("Viaggio non trovato per l'ID specificato: " + idViaggio);
	                throw new IllegalArgumentException("Viaggio non trovato per l'ID specificato: " + idViaggio);
	            }
	            viaggi.add(viaggioOptional.get());
	        }

	        // Step 5: Aggiornamento dei dati dell'entità esistente
	        preFatturazioneEsistente.setDataPrefatturazione(dataPrefatturazione);
	        preFatturazioneEsistente.setImporto(importo);
	        preFatturazioneEsistente.setPenale(penale);
	        preFatturazioneEsistente.setTotale(totale);
	        preFatturazioneEsistente.setCliente(cliente);
	        preFatturazioneEsistente.setFornitore(fornitore);
	        preFatturazioneEsistente.setViaggi(viaggi);
	        preFatturazioneEsistente.setScadenzaPrefatturazione(scadenzaPreFatturazione);

	        // Step 6: Salvataggio delle modifiche
	        logger.info("Salva le modifiche alla pre-fatturazione");
	        PreFatturazione preFatturazioneModificata = preFatturazioneService.modificaPreFatturazione(preFatturazioneEsistente);

	        // Step 7: Conversione dell'entità modificata in un DTO
	        logger.info("Conversione della pre-fatturazione modificata in DTO");

	        // Step 8: Fine dell'elaborazione e restituzione del DTO
	        logger.info("Fine elaborazione richiesta di modifica pre-fatturazione");
	        return preFatturazioneMapper.preFatturazioneToDTO(preFatturazioneModificata);

	    } catch (IllegalArgumentException e) {
	        logger.error("Errore durante la validazione della richiesta", e);
	        throw new IllegalArgumentException(e.getMessage());
	    } catch (Exception e) {
	        logger.error("Errore imprevisto nella modifica della pre-fatturazione", e);
	        throw new RuntimeException("Errore imprevisto nella modifica della pre-fatturazione", e);
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
