package com.tdgroup.Logistica.Mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.tdgroup.Logistica.DTOResponse.PreFatturazioneDTO;

import com.tdgroup.Logistica.Model.PreFatturazione;
import com.tdgroup.Logistica.Model.Viaggio;

@Component
public class PreFatturazioneMapper {

	public PreFatturazioneDTO preFatturazioneToDTO(PreFatturazione preFatturazione) {
		PreFatturazioneDTO preFatturazioneDTO = new PreFatturazioneDTO();
		preFatturazioneDTO.setNumeroPrefatturazione(preFatturazione.getNumeroPrefatturazione());
		preFatturazioneDTO.setDataPrefatturazione(preFatturazione.getDataPrefatturazione());
		preFatturazioneDTO.setImporto(preFatturazione.getImporto());
		preFatturazioneDTO.setPenale(preFatturazione.getPenale());
		preFatturazioneDTO.setTotale(preFatturazione.getTotale());
		preFatturazioneDTO.setCliente(preFatturazione.getCliente());
		preFatturazioneDTO.setFornitore(preFatturazione.getFornitore());
		preFatturazioneDTO.setScadenzaPrefatturazione(preFatturazione.getScadenzaPrefatturazione());
		preFatturazioneDTO.setFatturato(preFatturazione.isFatturato());

		 
	    List<Long> viaggioID = preFatturazione.getViaggi().stream()
	            .map(Viaggio::getID)
	            .collect(Collectors.toList());
	    
	    preFatturazioneDTO.setIdViaggio(viaggioID);
		
		return preFatturazioneDTO;
	}

	public List<PreFatturazioneDTO> PreFatturazioneToDTOList(List<PreFatturazione> preFatturazioni) {
		return preFatturazioni.stream()
				.map(this::preFatturazioneToDTO)
				.collect(Collectors.toList());
	}
}
