package com.tdgroup.Logistica.Facade;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tdgroup.Logistica.DTORequest.FatturazioneRequest;
import com.tdgroup.Logistica.DTORequest.PreFatturazioneRequest;
import com.tdgroup.Logistica.DTOResponse.FatturazioneDTO;
import com.tdgroup.Logistica.Mapper.FatturazioneMapper;
import com.tdgroup.Logistica.Model.Fatturazione;
import com.tdgroup.Logistica.Model.PreFatturazione;
import com.tdgroup.Logistica.Model.Viaggio;
import com.tdgroup.Logistica.Repository.FatturazioneRepository;
import com.tdgroup.Logistica.Service.ChiamataHttp;
import com.tdgroup.Logistica.Service.FatturazioneService;
import com.tdgroup.Logistica.Service.PreFatturazioneService;
import com.tdgroup.Logistica.Service.ViaggioService;

@Service
public class FatturazioneFacade {

	@Autowired
	ChiamataHttp chiamataHttp;
	
	@Autowired
	FatturazioneService fatturazioneService;
	@Autowired
	FatturazioneMapper fatturazioneMapper;

	@Autowired
	FatturazioneRepository fatturazioneRepository;

	@Autowired
	ViaggioService viaggioService;
	
	@Autowired
	PreFatturazioneService preFatturazioneService;

	private static final Logger logger = LoggerFactory.getLogger(FatturazioneFacade.class);




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
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il numero di fattura non può essere vuoto");
            }

            logger.info("Rimozione della fattura con numero: {}", numeroFattura);

            Fatturazione fatturazioneEsistente = fatturazioneRepository.findByNumeroFattura(numeroFattura)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fatturazione non trovata per il numero: " + numeroFattura));

            // Verifica se la fattura è associata a un viaggio e rimuovila, se necessario
            List<Viaggio> viaggiAssociati = fatturazioneEsistente.getViaggi();
            if (viaggiAssociati != null) {
                for (Viaggio viaggio : viaggiAssociati) {
                    viaggio.setFatturazione(null);
                }
                fatturazioneEsistente.setViaggi(null);
                fatturazioneService.eliminaFatturazione(fatturazioneEsistente);
            }

            fatturazioneRepository.delete(fatturazioneEsistente);

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

	public FatturazioneDTO aggiungiFatturazione(FatturazioneRequest request, String numeroPrefatturazione) {
	    try {
	        logger.info("Inizio procedura di aggiunta di fatturazione.");

	        // Verifica che i dati nella richiesta siano validi
	        if (request == null || request.getDataEmissione() == null) {
	            logger.error("Attributi mancanti o non validi nella richiesta di aggiunta fatturazione");
	            throw new IllegalArgumentException("Attributi mancanti o non validi nella richiesta di aggiunta fatturazione");
	        }
	        
	        logger.info("Dati richiesta validati con successo.");

	        // Esegui una query per ottenere i dati della prefattura dal tuo database
	        Optional<PreFatturazione> prefattura = preFatturazioneService.findPreFatturazione(numeroPrefatturazione);

	        if (!prefattura.isPresent()) {
	            logger.error("Errore: La prefattura specificata non esiste. Numero Prefatturazione: " + numeroPrefatturazione);
	            throw new IllegalArgumentException("La prefattura specificata non esiste.");
	        }
	        
	        logger.info("Prefatturazione trovata con successo.");

	        // Estrai i dati dalla prefatturazione
	        PreFatturazione preFatturazione = prefattura.get();

	        // Controlla se la prefatturazione è già stata fatturata
	        if (preFatturazione.isFatturato()) {
	            logger.error("Errore: La prefatturazione è già stata fatturata. Numero Prefatturazione: " + numeroPrefatturazione);
	            throw new IllegalArgumentException("La prefatturazione è già stata fatturata.");
	        }

	        logger.info("Prefatturazione non ancora fatturata.");

	        // Crea una nuova istanza di Fatturazione e imposta i dati
	        Fatturazione fatturazione = new Fatturazione();
	        fatturazione.setDataEmissione(request.getDataEmissione());
	        fatturazione.setImporto(preFatturazione.getImporto());
	        fatturazione.setPenale(preFatturazione.getPenale());
	        fatturazione.setTotale(preFatturazione.getTotale());
	        fatturazione.setCliente(preFatturazione.getCliente());
	        fatturazione.setFornitore(preFatturazione.getFornitore());

	        // Genera un nuovo numero di fattura
	        String nuovoNumeroFattura = generaNumeroFattura();
	        logger.info("Nuovo numero di fattura generato: {}", nuovoNumeroFattura);
	        fatturazione.setNumeroFattura(nuovoNumeroFattura);

	        // Aggiungi la nuova fatturazione al database
	        Fatturazione fatturazioneAggiunta = fatturazioneService.aggiungiFatturazione(fatturazione);
	        logger.info("Fatturazione aggiunta al database con successo.");

	        // Copia e disassocia i viaggi dalla prefatturazione
	        List<Viaggio> viaggi = new ArrayList<>(preFatturazione.getViaggi());
	        preFatturazione.getViaggi().clear();

	        // Associa i viaggi alla nuova fattura
	        for (Viaggio viaggio : viaggi) {
	            viaggio.setFatturazione(fatturazioneAggiunta);
	            viaggioService.aggiungiViaggio(viaggio);
	        }
	        preFatturazione.setFatturato(true);
	        fatturazioneAggiunta.setViaggi(viaggi);
	        // Aggiungi la fatturazione con i viaggi associati al database
	        fatturazioneAggiunta = fatturazioneService.aggiungiFatturazione(fatturazioneAggiunta);
	        logger.info("Fatturazione associata ai viaggi e aggiornata nel database.");

	        logger.info("Fatturazione aggiunta con successo: {}", fatturazioneAggiunta.getNumeroFattura());

	        return fatturazioneMapper.fatturazioneToDTO(fatturazioneAggiunta);

	    } catch (IllegalArgumentException e) {
	        logger.error("Errore durante l'aggiunta della fatturazione: " + e.getMessage(), e);
	        throw new IllegalArgumentException(e.getMessage());
	    } catch (Exception e) {
	        logger.error("Errore imprevisto nell'aggiunta della fatturazione", e);
	        throw new RuntimeException("Errore imprevisto nell'aggiunta della fatturazione", e);
	    }
	}


	
	
	
	
	
	
	
    public List<FatturazioneDTO> findFatturazioniByFiltri(
            String numeroFattura,
            LocalDateTime dataEmissione ,
            Double totale,
            Double penale,
            String cliente,
            String fornitore) {
        try {
            return fatturazioneMapper.fatturazioneToDTOList(
                fatturazioneService.findFatturazioneByFiltri(
                    numeroFattura, dataEmissione, totale, penale, cliente, fornitore
                )
            );
        } catch (Exception e) {
            e.printStackTrace(); // Gestisci le gestione delle eccezioni in modo appropriato
            throw e; // Lancia nuovamente l'eccezione o gestiscila in base alle tue esigenze
        }
    }

}




