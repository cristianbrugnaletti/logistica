package com.tdgroup.Logistica.Repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tdgroup.Logistica.Model.Fatturazione;
import com.tdgroup.Logistica.Model.Viaggio;


public interface FatturazioneRepository extends JpaRepository<Fatturazione, Long>{
	 Optional<Fatturazione> findByNumeroFattura(String numeroFattura);
	
	 @Query("SELECT MAX(CAST(SUBSTRING(f.numeroFattura, 3, 7) AS Long)) FROM Fatturazione f WHERE SUBSTRING(f.numeroFattura, 1, 2) = :prefisso")
	 Long findLastNumeroFatturaByPrefix(@Param("prefisso") String prefisso);
	 
	 
	
	 
	
}
