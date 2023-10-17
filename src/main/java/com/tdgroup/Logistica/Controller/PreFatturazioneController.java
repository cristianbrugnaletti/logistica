package com.tdgroup.Logistica.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tdgroup.Logistica.DTORequest.PreFatturazioneRequest;
import com.tdgroup.Logistica.DTOResponse.FatturazioneDTO;
import com.tdgroup.Logistica.DTOResponse.PreFatturazioneDTO;
import com.tdgroup.Logistica.Facade.PreFatturazioneFacade;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/PreFatturazione")
public class PreFatturazioneController {
	private static final Logger logger = LoggerFactory.getLogger(PreFatturazioneController.class);

	@Autowired
	PreFatturazioneFacade preFatturazioneFacade;


	@PostMapping("/aggiungiPreFattura")
	public ResponseEntity<Object> aggiungiPreFattura(@RequestBody @Valid PreFatturazioneRequest request) {
	    try {
	        preFatturazioneFacade.aggiungiPreFatturazione(request);
	        return ResponseEntity.ok("Prefattura aggiunta con successo");
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().body("Errore: " + e.getMessage());
	    } catch (RuntimeException e) {
	        return ResponseEntity.badRequest().body("Errore specifico: " + e.getMessage());
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Si è verificato un errore imprevisto");
	    }
	}


	@DeleteMapping("RimuoviPrefatturazione/{numeroPreFatturazione}")
	public ResponseEntity<Object> rimuoviPreFattura(@PathVariable String numeroPreFatturazione) {
		try {
			if (numeroPreFatturazione == null || numeroPreFatturazione.isBlank()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Il numero di fattura non può essere vuoto o nullo");
			}

			
			preFatturazioneFacade.rimuoviPreFattura(numeroPreFatturazione);

			return ResponseEntity.status(HttpStatus.OK)
					.body("Fattura rimossa con successo");

		} catch (Exception e) {
			return ResponseEntity.ok("Errore durante la rimozione della fattura: fattura non trovato con numero " +numeroPreFatturazione);
		}
	}

	@PutMapping("/modifica/{numeroPrefatturazione}")
	public ResponseEntity<Object> modificaPreFatturazione(
			@PathVariable("numeroPrefatturazione") String numeroPrefatturazione,
			@RequestBody PreFatturazioneRequest request) {
		try {
			
			PreFatturazioneDTO preFatturazioneDTO = preFatturazioneFacade.modificaPreFatturazione(numeroPrefatturazione, request);

			
			return ResponseEntity.ok(preFatturazioneDTO);

		} catch (IllegalArgumentException e) {
			logger.error("Errore durante la modifica della pre-fatturazione: attributi mancanti o non validi", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Errore: attributi mancanti o non validi nella richiesta di modifica pre-fatturazione");
		} catch (Exception e) {
			logger.error("Errore imprevisto nella modifica della pre-fatturazione", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Errore imprevisto nella modifica della pre-fatturazione");
		}
	}


	@GetMapping("/visualizzaTutteLePreFatture")
	public ResponseEntity<Object> visualizzaTutteLeFatture() {
		List<PreFatturazioneDTO> fatturazioni = preFatturazioneFacade.VisualizzaTutteLePreFatture();

		if (fatturazioni.isEmpty()) {
			return ResponseEntity.ok("Nessuna prefattura trovata");
		}

		return ResponseEntity.ok(fatturazioni);
	}


	@GetMapping("/visualizzaPreFatturazione/{numeroPreFatturazione}")
	public ResponseEntity<Object> visualizzaPreFatturazione(@PathVariable("numeroPreFatturazione") String numeroPreFatturazione) {
		try {
			PreFatturazioneDTO preFatturazioneDTO = preFatturazioneFacade.visualizzaPreFatturazione(numeroPreFatturazione);
			return ResponseEntity.ok(preFatturazioneDTO);
		} catch (ResponseStatusException e) {
			return ResponseEntity.ok("Nessuna fatturazione trovata con il numero di prefattura: " + numeroPreFatturazione);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la visualizzazione della prefatturazione");
		}
	}
}