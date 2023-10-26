package com.tdgroup.Logistica.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;



@Service
public class ChiamataHttp {

	private final RestTemplate restTemplate;
	private final String microservizioUrl = "http://192.168.239.130:8080/ClienteController/visualizzaNome/";
	private final String microservizioFornitore = "http://192.168.239.130:8080/fornitori/visualizza/";
	private final String microservizioViaggioUrl = "http://192.168.239.133:8080/viaggio/";
	
	public ChiamataHttp(RestTemplate restTemplate) {
	    this.restTemplate = restTemplate;
	}

	public CompletableFuture<Boolean> verificaEsistenzaClienteAsync(String cliente) {
	    String url = microservizioUrl  + cliente;

	    return CompletableFuture.supplyAsync(() -> {
	        try {
	            restTemplate.getForEntity(url, String.class);
	            return true;
	        } catch (HttpClientErrorException e) {
	            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
	                return false;
	            }
	            throw e;
	        } catch (RestClientException e) {
	            throw e;
	        }
	    });
	}
	
	
	public CompletableFuture<Boolean> verificaEsistenzaFornitoreAsync(String partitaIva) {
	    String url = microservizioFornitore  + partitaIva;

	    return CompletableFuture.supplyAsync(() -> {
	        try {
	            restTemplate.getForEntity(url, String.class);
	            return true;
	        } catch (HttpClientErrorException e) {
	            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
	                return false;
	            }
	            throw e;
	        } catch (RestClientException e) {
	            throw e;
	        }
	    });
	}
	
	
	public Boolean verificaEsistenzaViaggioAsync(List<Long> idViaggio) {
	    List<CompletableFuture<Boolean>> results = idViaggio.stream()
	        .map(this::verificaEsistenzaViaggioSingoloAsync)
	        .collect(Collectors.toList());

	    CompletableFuture<Void> allOf = CompletableFuture.allOf(results.toArray(new CompletableFuture[0]));
	    
	    try {
	        allOf.get(); // Attendere il completamento
	    } catch (InterruptedException | ExecutionException e) {
	        throw new RuntimeException("Errore nell'esecuzione delle verifiche asincrone.", e);
	    }

	    return results.stream().allMatch(CompletableFuture::join);
	}
	
	public CompletableFuture<Boolean> verificaEsistenzaViaggioSingoloAsync(Long idViaggio) {
	    String url = microservizioViaggioUrl + idViaggio;

	    return CompletableFuture.supplyAsync(() -> {
	        try {
	            restTemplate.getForEntity(url, String.class);
	            return true;
	        } catch (HttpClientErrorException e) {
	            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
	                return false;
	            }
	            throw e;
	        } catch (RestClientException e) {
	            throw e;
	        }
	    });
	}
	
	
}
