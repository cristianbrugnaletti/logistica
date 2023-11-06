package com.tdgroup.Logistica.Controller;


import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tdgroup.Logistica.DTORequest.FatturazioneRequest;
import com.tdgroup.Logistica.DTOResponse.FatturazioneDTO;
import com.tdgroup.Logistica.Facade.FatturazioneFacade;
import com.tdgroup.Logistica.GestioneRisposte.Risposte;

import jakarta.validation.Valid;

@RestController
//@RequestMapping("/fatturazione")
public class FatturazioneController {

	private static final Logger logger = LoggerFactory.getLogger(FatturazioneController.class);



	@Autowired
	FatturazioneFacade fatturazioneFacade;

	   @PostMapping("/aggiungiFattura")
	    public ResponseEntity<Object> aggiungiFattura(@RequestBody @Valid FatturazioneRequest request, String numeroPreFatturazione) {
		   
	        logger.info("Richiesta ricevuta per aggiungere una nuova fattura.");
	        try {
	            fatturazioneFacade.aggiungiFatturazione(request,numeroPreFatturazione);
	            logger.info("Fattura aggiunta con successo.");

	            return ResponseEntity.ok(Risposte.SuccessResponse("Fattura aggiunta con successo", "/aggiungiFattura"));
	        } catch (IllegalArgumentException e) {
	            logger.error("Errore durante l'aggiunta della fattura: attributi mancanti o non validi", e);

	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                    .body(Risposte.ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), "/aggiungiFattura"));
	        } catch (Exception e) {
	            logger.error("Errore imprevisto durante l'aggiunta della fattura", e);

	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(Risposte.ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), "/aggiungiFattura"));
	        }
	    }


	   @CrossOrigin(origins = "http://localhost:4200")
	   @DeleteMapping("/RimuoviFattura/{numeroFattura}")
	   public ResponseEntity<Object> rimuoviFattura(@PathVariable String numeroFattura) {
	       logger.info("Richiesta ricevuta per rimuovere la fattura con numero: {}", numeroFattura);
	       try {
	           fatturazioneFacade.rimuoviFattura(numeroFattura);
	           logger.info("Fattura rimossa con successo: {}", numeroFattura);

	           // SuccessResponse richiede un messaggio, passiamo un messaggio generico
	           return ResponseEntity.status(HttpStatus.OK)
	                   .body(Risposte.SuccessResponse("Fattura rimossa con successo", "/RimuoviFattura/" + numeroFattura));
	       } catch (ResponseStatusException e) {
	           logger.error("Errore durante la rimozione della fattura: {}", e.getMessage());

	           // Utilizza il metodo ErrorResponseHttp dalla classe Risposte direttamente nel return
	           return ResponseEntity.status(e.getStatusCode())
	                   .body(Risposte.ErrorResponse(e.getStatusCode().value(), e.getReason(), "/RimuoviFattura/" + numeroFattura));
	       }
	   }


	   @CrossOrigin(origins = "http://localhost:4200")
	  @GetMapping("/visualizzaTutteLeFatture")
	    public ResponseEntity<Object> visualizzaTutteLeFatture() {
	        logger.info("Richiesta ricevuta per visualizzare tutte le fatture.");
	        try {
	            List<FatturazioneDTO> fatturazioni = fatturazioneFacade.VisualizzaTutteLeFatture();
	            logger.info("Visualizzazione di tutte le fatture effettuata.");
	            return ResponseEntity.ok(fatturazioni);
	        } catch (Exception e) {
	            logger.error("Errore durante la visualizzazione di tutte le fatture", e);
	            return ResponseEntity.ok(Risposte.ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Errore durante la visualizzazione di tutte le fatture", "/visualizzaTutteLeFatture"));
	                    
	        }
	    }

	    @GetMapping("/visualizzaFatturazione/{numeroFattura}")
	    public ResponseEntity<Object> visualizzaFatturazione(@PathVariable("numeroFattura") String numeroFattura) {
	        logger.info("Richiesta ricevuta per visualizzare la fatturazione con numero di fattura: {}", numeroFattura);
	        try {
	            FatturazioneDTO fatturazioneDTO = fatturazioneFacade.visualizzaFatturazione(numeroFattura);
	            logger.info("Visualizzazione della fatturazione effettuata per la fattura con numero: {}", numeroFattura);

	            return ResponseEntity.ok(fatturazioneDTO);
	        } catch (ResponseStatusException e) {
	            logger.warn("Nessuna fatturazione trovata con il numero di fattura: {}", numeroFattura);

	            return ResponseEntity.status(e.getStatusCode())
	                    .body(Risposte.ErrorResponseHttp(e.getStatusCode(), e.getReason(), "/visualizzaFatturazione/" + numeroFattura));
	        } catch (Exception e) {
	            logger.error("Errore durante la visualizzazione della fatturazione", e);

	            return ResponseEntity.ok(Risposte.ErrorResponse(500, "Errore durante la visualizzazione della fatturazione", "/visualizzaFatturazione/" + numeroFattura));
	        }
	    }
	    
	    
	    
	    
	    
	    @CrossOrigin(origins = "http://localhost:4200")
	    @GetMapping("/cercaFatturazioni")
	    public ResponseEntity<Object> cercaFatturazioni(
	    		
	    			    @RequestParam(name = "numeroFattura", required = false) String numeroFattura,
	    			    @RequestParam(name = "dataEmissione", required = false) String dataEmissioneString, // Accetta la data come stringa
	    			    @RequestParam(name = "totale", required = false) Double totale,
	    			    @RequestParam(name = "penale", required = false) Double penale,
	    			    @RequestParam(name = "cliente", required = false) String cliente,
	    			    @RequestParam(name = "fornitore", required = false) String fornitore) {
	    			    try {
	    			        LocalDateTime dataEmissione = null;

	    			        if (dataEmissioneString != null && !dataEmissioneString.isEmpty()) {
	    			            // Conversione della stringa in una data locale senza considerare l'orario
	    			            dataEmissione = LocalDateTime.parse(dataEmissioneString, DateTimeFormatter.ISO_DATE_TIME);
	    			        }

	    			        List<FatturazioneDTO> fatturazioni = fatturazioneFacade.findFatturazioniByFiltri(
	    			            numeroFattura,
	    			            dataEmissione, // Utilizza LocalDate invece di LocalDateTime
	    			            totale,
	    			            penale,
	    			            cliente,
	    			            fornitore
	    			        );

	            if (fatturazioni.isEmpty()) {
	                return ResponseEntity.ok(Collections.emptyList()); // Restituisce una lista vuota invece di una stringa
	            }

	            return ResponseEntity.ok(fatturazioni);
	        } catch (Exception e) {
	            logger.error("Errore durante la ricerca delle fatturazioni con filtri", e);
	            Map<String, String> errorResponse = new HashMap<>();
	            errorResponse.put("error", "Errore durante la ricerca delle fatturazioni con filtri: " + e.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	        }
	    }



}
