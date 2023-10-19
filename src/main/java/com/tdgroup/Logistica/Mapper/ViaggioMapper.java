package com.tdgroup.Logistica.Mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.tdgroup.Logistica.DTOResponse.ViaggioDTO;
import com.tdgroup.Logistica.Model.Viaggio;

public class ViaggioMapper {

	   public ViaggioDTO viaggioToDTO(Viaggio viaggio) {
	        ViaggioDTO viaggioDTO = new ViaggioDTO();
	        viaggioDTO.setID(viaggio.getID()); 
	        return viaggioDTO;
	    }

	    public List<ViaggioDTO> viaggioToDTOList(List<Viaggio> viaggi) {
	        return viaggi.stream()
	            .map(this::viaggioToDTO)
	            .collect(Collectors.toList());
	    }
}
