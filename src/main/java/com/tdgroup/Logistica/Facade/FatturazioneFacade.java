package com.tdgroup.Logistica.Facade;

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
import com.tdgroup.Logistica.DTOResponse.FatturazioneDTO;
import com.tdgroup.Logistica.Mapper.FatturazioneMapper;
import com.tdgroup.Logistica.Model.Fatturazione;
import com.tdgroup.Logistica.Model.Viaggio;
import com.tdgroup.Logistica.Repository.FatturazioneRepository;
import com.tdgroup.Logistica.Service.ChiamataHttp;
import com.tdgroup.Logistica.Service.FatturazioneService;
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

	
	public FatturazioneDTO aggiungiFatturazione(FatturazioneRequest request) {
	    try {
	    	
	        if (request == null || request.getDataEmissione() == null || request.getImporto() == null ||
	            request.getPenale() == null || request.getCliente() == null ||
	            StringUtils.isBlank(request.getFornitore()) || (request.getIdViaggio() == null || request.getIdViaggio().isEmpty())) {
	            logger.error("Attributi mancanti o non validi nella richiesta di aggiunta fatturazione");
	            throw new IllegalArgumentException("Attributi mancanti o non validi nella richiesta di aggiunta fatturazione");
	        }

	        LocalDateTime dataEmissione = request.getDataEmissione();
	        Double importo = request.getImporto();
	        Double penale = request.getPenale();

	        Double totale = importo + penale;

	        if (!chiamataHttp.verificaEsistenzaFornitoreAsync(request.getFornitore()).join()) {
	            logger.error("Errore: Il fornitore specificato non esiste. Fornitore: " + request.getFornitore());
	            throw new IllegalArgumentException("Il fornitore specificato non esiste.");
	        }

	        String fornitore = request.getFornitore();

	        if (!chiamataHttp.verificaEsistenzaClienteAsync(request.getCliente()).join()) {
	            logger.error("Errore: Il cliente specificato non esiste. Cliente: " + request.getCliente());
	            throw new IllegalArgumentException("Il cliente specificato non esiste.");
	        }
	        String cliente = request.getCliente();

	        if (importo < 0) {
	            logger.error("L'importo non può essere negativo");
	            throw new IllegalArgumentException("L'importo non può essere negativo");
	        }

	        if (penale < 0) {
	            logger.error("La penale non può essere negativa");
	            throw new IllegalArgumentException("La penale non può essere negativa");
	        }

	        Fatturazione fatturazione = new Fatturazione();
	        fatturazione.setDataEmissione(dataEmissione);
	        fatturazione.setImporto(importo);
	        fatturazione.setPenale(penale);
	        fatturazione.setTotale(totale);
	        fatturazione.setCliente(cliente);
	        fatturazione.setFornitore(fornitore);

	        String nuovoNumeroFattura = generaNumeroFattura();
	        logger.info("Nuovo numero di fattura generato: {}", nuovoNumeroFattura);
	        fatturazione.setNumeroFattura(nuovoNumeroFattura);

	        Fatturazione fatturazioneAggiunta = fatturazioneService.aggiungiFatturazione(fatturazione);
	        List<Viaggio> viaggi = new ArrayList<>();
	        for (Long idViaggio : request.getIdViaggio()) {
	            if (!chiamataHttp.verificaEsistenzaViaggioSingoloAsync(idViaggio).join()) {
	                logger.error("Errore: Il viaggio specificato non esiste. ID Viaggio: " + idViaggio);
	                throw new IllegalArgumentException("Il viaggio specificato non esiste.");
	            }

	            Viaggio viaggio = new Viaggio();
	            viaggio.setFatturazione(fatturazioneAggiunta);
	            viaggi.add(viaggio);
	            viaggioService.aggiungiViaggio(viaggio);
	        }

	        fatturazioneAggiunta.setViaggi(viaggi);

	        fatturazioneAggiunta = fatturazioneService.aggiungiFatturazione(fatturazioneAggiunta);
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


}




