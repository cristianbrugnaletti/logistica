package com.tdgroup.Logistica.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
public class ChiamataHttp {

	   private final RestTemplate restTemplate;

	    public ChiamataHttp() {
	        this.restTemplate = new RestTemplate();
	    }

	    public Object chiamataHttp(String url, Class<?> responseType) {
	        // Esegui una chiamata HTTP al servizio esterno per ottenere le informazioni specifiche
	        return restTemplate.getForObject(url, responseType);
	    }
	    
	    public Long getIdCliente(Long idCliente) {
	        // Specifica l'indirizzo IP e il percorso
	        String ipAddress = "192.168.239.130";
	        String path = "/ClienteController/visualizzaID/" + idCliente;
	        
	        // Componi l'URL completo
	        String url = "http://" + ipAddress + path;
	        
	        // Esegui la chiamata HTTP
	        return restTemplate.getForObject(url, Long.class);
	    }
}
