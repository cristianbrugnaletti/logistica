package com.tdgroup.Logistica.Mapper;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Component;

import com.tdgroup.Logistica.DTOResponse.FatturazioneDTO;
import com.tdgroup.Logistica.Model.Fatturazione;
import com.tdgroup.Logistica.Model.Viaggio;

@Component
public class FatturazioneMapper {

	public FatturazioneDTO fatturazioneToDTO(Fatturazione fatturazione) {
		FatturazioneDTO fatturazioneDTO = new FatturazioneDTO();
	    fatturazioneDTO.setCliente(fatturazione.getCliente());
	    fatturazioneDTO.setDataEmissione(fatturazione.getDataEmissione());
	    fatturazioneDTO.setFornitore(fatturazione.getFornitore());
	    fatturazioneDTO.setImporto(fatturazione.getImporto());
	    fatturazioneDTO.setPenale(fatturazione.getPenale());
	    fatturazioneDTO.setTotale(fatturazione.getTotale());
	    
	    List<Long> viaggioID = fatturazione.getViaggi().stream()
	            .map(Viaggio::getID)
	            .collect(Collectors.toList());
	    
	    fatturazioneDTO.setIdViaggio(viaggioID);
	    fatturazioneDTO.setNumeroFattura(fatturazione.getNumeroFattura());
	    
	    return fatturazioneDTO;
	}
	
	 public List<FatturazioneDTO> fatturazioneToDTOList(List<Fatturazione> fatturazioni) {
	     return fatturazioni.stream()
	             .map(this::fatturazioneToDTO)
	             .collect(Collectors.toList());
	 }
}
