package com.tdgroup.Logistica.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
	 
	 
	
	 
	 
	 @Query("SELECT f FROM Fatturazione f WHERE " +
		        "(:numeroFattura IS NULL OR f.numeroFattura LIKE CONCAT('%',:numeroFattura, '%')) " +
		        "AND (:dataEmissione IS NULL OR DATE(f.dataEmissione) = DATE(:dataEmissione)) " +
		        "AND (:totale IS NULL OR f.totale = :totale) " +
		        "AND (:penale IS NULL OR f.penale = :penale) " +
		        "AND (:cliente IS NULL OR f.cliente LIKE CONCAT('%',:cliente, '%')) " +
		        "AND (:fornitore IS NULL OR f.fornitore LIKE CONCAT('%',:fornitore, '%')) " )
		        
		List<Fatturazione> findFatturazioneByFiltri(
		        @Param("numeroFattura") String numeroFattura,
		        @Param("dataEmissione") LocalDateTime dataEmissione,
		        @Param("totale") Double totale,
		        @Param("penale") Double penale,
		        @Param("cliente") String cliente,
		        @Param("fornitore") String fornitore);
		        

	 
	
}
