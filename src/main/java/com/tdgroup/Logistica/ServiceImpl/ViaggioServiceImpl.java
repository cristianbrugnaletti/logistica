package com.tdgroup.Logistica.ServiceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tdgroup.Logistica.Model.Viaggio;
import com.tdgroup.Logistica.Repository.ViaggioRepository;
import com.tdgroup.Logistica.Service.ViaggioService;

@Service
public class ViaggioServiceImpl implements ViaggioService{

	
	@Autowired
	ViaggioRepository viaggioRepository;
	
	
	@Override
	public Optional<Viaggio> getViaggioById(Long id) {
		return viaggioRepository.findById(id);
	}

}
