package com.tdgroup.Logistica.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.google.common.base.Optional;
import com.tdgroup.Logistica.DTORequest.PreFatturazioneRequest;
import com.tdgroup.Logistica.DTOResponse.FatturazioneDTO;
import com.tdgroup.Logistica.DTOResponse.PreFatturazioneDTO;
import com.tdgroup.Logistica.Facade.PreFatturazioneFacade;
import com.tdgroup.Logistica.GestioneRisposte.Risposte;

import jakarta.validation.Valid;

@RestController
//@RequestMapping("/PreFatturazione")
public class PreFatturazioneController {
	private static final Logger logger = LoggerFactory.getLogger(PreFatturazioneController.class);

	@Autowired
	PreFatturazioneFacade preFatturazioneFacade;


	@PostMapping("/aggiungiPreFattura")
	public ResponseEntity<Object> aggiungiPreFattura(@RequestBody @Valid PreFatturazioneRequest request) {
		 logger.info("Richiesta ricevuta per aggiungere una nuova fattura.");
	        try {
	            preFatturazioneFacade.aggiungiPreFatturazione(request);
	            logger.info("preFattura aggiunta con successo.");

	            return ResponseEntity.ok(Risposte.SuccessResponse("preFattura aggiunta con successo", "/aggiungiFattura"));
	        } catch (IllegalArgumentException e) {
	            logger.error("Errore durante l'aggiunta della prefattura: attributi mancanti o non validi", e);

	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                    .body(Risposte.ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), "/aggiungiFattura"));
	        } catch (Exception e) {
	            logger.error("Errore imprevisto durante l'aggiunta della fattura", e);

	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(Risposte.ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), "/aggiungiFattura"));
	        }
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@DeleteMapping("RimuoviPrefatturazione/{numeroPreFatturazione}")
	public ResponseEntity<Object> rimuoviPreFattura(@PathVariable String numeroPreFatturazione) {
		 logger.info("Richiesta ricevuta per rimuovere la fattura con numero: {}", numeroPreFatturazione);
	       try {
	           preFatturazioneFacade.rimuoviPreFattura(numeroPreFatturazione);
	           logger.info("Fattura rimossa con successo: {}", numeroPreFatturazione);

	           
	           return ResponseEntity.status(HttpStatus.OK)
	                   .body(Risposte.SuccessResponse("Fattura rimossa con successo", "/RimuoviFattura/" + numeroPreFatturazione));
	       } catch (ResponseStatusException e) {
	           logger.error("Errore durante la rimozione della fattura: {}", e.getMessage());

	           
	           return ResponseEntity.status(e.getStatusCode())
	                   .body(Risposte.ErrorResponse(e.getStatusCode().value(), e.getReason(), "/RimuoviFattura/" + numeroPreFatturazione));
	       }
	}


	@PutMapping("/modifica/{numeroPrefatturazione}")
	public ResponseEntity<Object> modificaPreFatturazione(
	        @PathVariable("numeroPrefatturazione") String numeroPrefatturazione,
	        @RequestBody PreFatturazioneRequest request) {
	    try {
	        logger.info("Inizio modifica della pre-fatturazione");

	      

	        LocalDateTime scadenzaPrefatturazione = null;
	        if (request.getScadenzaPrefatturazione() != null) {
	            scadenzaPrefatturazione = request.getScadenzaPrefatturazione();
	        }

	        PreFatturazioneDTO preFatturazioneDTO = preFatturazioneFacade.modificaPreFatturazione(numeroPrefatturazione, request, scadenzaPrefatturazione);
	        logger.info("Fine modifica della pre-fatturazione");
	        return ResponseEntity.ok(preFatturazioneDTO);
	    } catch (IllegalArgumentException e) {
	        logger.error("Errore durante la modifica della pre-fatturazione: attributi mancanti o non validi", e);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                 .body(Risposte.ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), "/aggiungipreFattura"));
	    } catch (Exception e) {
	        logger.error("Errore imprevisto nella modifica della pre-fatturazione", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Risposte.ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Errore imprevisto nella modifica della pre-fatturazione", "/modifica/" + numeroPrefatturazione));
	    }
	}




	 @CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/visualizzaTutteLePreFatture")
	public ResponseEntity<Object> visualizzaTutteLeFatture() {
	    try {
	        logger.info("Inizio visualizzazione di tutte le pre-fatture");
	        List<PreFatturazioneDTO> fatturazioni = preFatturazioneFacade.VisualizzaTutteLePreFatture();
	        if (fatturazioni.isEmpty()) {
	            logger.info("Nessuna prefattura trovata");
	            return ResponseEntity.ok("Nessuna prefattura trovata");
	        }
	        logger.info("Fine visualizzazione di tutte le pre-fatture");
	        return ResponseEntity.ok(fatturazioni);
	    } catch (Exception e) {
	        logger.error("Errore durante la visualizzazione delle pre-fatture", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Si è verificato un errore imprevisto durante la visualizzazione delle pre-fatture");
	    }
	}


	@GetMapping("/visualizzaPreFatturazione/{numeroPreFatturazione}")
	public ResponseEntity<Object> visualizzaPreFatturazione(@PathVariable("numeroPreFatturazione") String numeroPreFatturazione) {
	    logger.info("Richiesta ricevuta per visualizzare la pre-fatturazione con numero di prefattura: {}", numeroPreFatturazione);

	    try {
	        PreFatturazioneDTO preFatturazioneDTO = preFatturazioneFacade.visualizzaPreFatturazione(numeroPreFatturazione);
	        logger.info("Visualizzazione della pre-fatturazione effettuata per la prefattura con numero: {}", numeroPreFatturazione);

	        return ResponseEntity.ok(preFatturazioneDTO);
	    } catch (ResponseStatusException e) {
	        logger.warn("Nessuna pre-fatturazione trovata con il numero di prefattura: {}", numeroPreFatturazione);

	       
	        return ResponseEntity.ok(Risposte.ErrorResponseHttp(
	                e.getStatusCode(),
	                e.getReason(),
	                "/visualizzaPreFatturazione/" + numeroPreFatturazione)
	        );
	    } catch (Exception e) {
	        logger.error("Errore durante la visualizzazione della pre-fatturazione", e);

	        
	        return ResponseEntity.ok(Risposte.ErrorResponse(
	                HttpStatus.INTERNAL_SERVER_ERROR.value(),
	                "Errore durante la visualizzazione della pre-fatturazione",
	                "/visualizzaPreFatturazione/" + numeroPreFatturazione)
	        );
	    }
	}

	
	
	
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/cercaPrefatturazioni")
	public ResponseEntity<Object> cercaPrefatturazioni(
	        @RequestParam(name = "numeroPrefatturazione", required = false) String numeroPrefatturazione,
	        @RequestParam(name = "dataPrefatturazione", required = false) String dataPrefatturazioneString,
	        @RequestParam(name = "totale", required = false) Double totale,
	        @RequestParam(name = "scadenzaPrefatturazione", required = false) String scadenzaPrefatturazioneString,
	        @RequestParam(name = "fatturato", required = false) Boolean fatturato,
	        @RequestParam(name = "cliente", required = false) String cliente,
	        @RequestParam(name = "fornitore", required = false) String fornitore,
	        @RequestParam(name = "penale", required = false) Double penale,
	        @RequestParam(name = "importo", required = false) Double importo) {
	    try {
	        LocalDateTime dataPrefatturazione = null;
	        LocalDateTime scadenzaPrefatturazione = null;

	        if (dataPrefatturazioneString != null && !dataPrefatturazioneString.isEmpty()) {
	            dataPrefatturazione = LocalDateTime.parse(dataPrefatturazioneString, DateTimeFormatter.ISO_DATE_TIME);
	        }

	        if (scadenzaPrefatturazioneString != null && !scadenzaPrefatturazioneString.isEmpty()) {
	            scadenzaPrefatturazione = LocalDateTime.parse(scadenzaPrefatturazioneString, DateTimeFormatter.ISO_DATE_TIME);
	        }

	        List<PreFatturazioneDTO> prefatturazioni = preFatturazioneFacade.findPrefatturazioniByFiltri(
	            numeroPrefatturazione,
	            dataPrefatturazione,
	            totale,
	            scadenzaPrefatturazione,
	            fatturato,
	            cliente,
	            fornitore,
	            penale,
	            importo
	        );

	        if (prefatturazioni.isEmpty()) {
	            return ResponseEntity.ok(Collections.emptyList()); // Restituisce una lista vuota invece di una stringa
	        }

	        return ResponseEntity.ok(prefatturazioni);
	    } catch (Exception e) {
	        logger.error("Errore durante la ricerca delle prefatturazioni con filtri", e);
	        Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("error", "Errore durante la ricerca delle prefatturazioni con filtri: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	    }
	}


    @GetMapping("/findByNumeroPrefatturazione/{numeroPrefatturazione}")
    public ResponseEntity<Object> findByNumeroPrefatturazione(@PathVariable("numeroPrefatturazione") String numeroPrefatturazione) {
        logger.info("Richiesta ricevuta per cercare la prefatturazione con numero di prefattura: {}", numeroPrefatturazione);

        try {
            java.util.Optional<PreFatturazioneDTO> preFatturazioneDTO = preFatturazioneFacade.findPreFatturazioneByNumeroPrefatturazione(numeroPrefatturazione);

            if (preFatturazioneDTO.isPresent()) {
                logger.info("Prefatturazione trovata con il numero di prefattura: {}", numeroPrefatturazione);
                return ResponseEntity.ok(preFatturazioneDTO.get());
            } else {
                logger.warn("Nessuna prefatturazione trovata con il numero di prefattura: {}", numeroPrefatturazione);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nessuna prefatturazione trovata con il numero di prefattura: " + numeroPrefatturazione);
            }
        } catch (Exception e) {
            logger.error("Errore durante la ricerca della prefatturazione con il numero di prefattura: {}", numeroPrefatturazione, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Errore durante la ricerca della prefatturazione con il numero di prefattura: " + e.getMessage());
        }
    }
	

}