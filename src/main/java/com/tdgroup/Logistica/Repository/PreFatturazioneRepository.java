package com.tdgroup.Logistica.Repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tdgroup.Logistica.Model.PreFatturazione;

public interface PreFatturazioneRepository extends JpaRepository<PreFatturazione, Long>{

	@Query("SELECT MAX(CAST(SUBSTRING(p.numeroPrefatturazione, 4) AS Long)) FROM PreFatturazione p WHERE SUBSTRING(p.numeroPrefatturazione, 1, 3) = :prefisso")
	Long findLastNumeroPreFatturaByPrefix(@Param("prefisso") String prefisso);

	Optional<PreFatturazione> findByNumeroPrefatturazione(String numeroPrefatturazione);
	
	

}
