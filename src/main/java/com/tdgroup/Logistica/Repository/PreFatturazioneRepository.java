package com.tdgroup.Logistica.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tdgroup.Logistica.Model.PreFatturazione;

public interface PreFatturazioneRepository extends JpaRepository<PreFatturazione, Long>{

	@Query("SELECT MAX(CAST(SUBSTRING(p.numeroPrefatturazione, 4) AS Long)) FROM PreFatturazione p WHERE SUBSTRING(p.numeroPrefatturazione, 1, 3) = :prefisso")
	Long findLastNumeroPreFatturaByPrefix(@Param("prefisso") String prefisso);

	Optional<PreFatturazione> findByNumeroPrefatturazione(String numeroPrefatturazione);
	
	

    @Query("SELECT p FROM PreFatturazione p WHERE " +
            "(:numeroPrefatturazione IS NULL OR p.numeroPrefatturazione LIKE CONCAT(:numeroPrefatturazione, '%')) " +
            "AND (:dataPrefatturazione IS NULL OR DATE(p.dataPrefatturazione) = DATE(:dataPrefatturazione)) " +
            "AND (:totale IS NULL OR p.totale = :totale) " +
            "AND (:scadenzaPrefatturazione IS NULL OR DATE(p.scadenzaPrefatturazione) = DATE(:scadenzaPrefatturazione)) " +
            "AND (:fatturato IS NULL OR p.fatturato = :fatturato) " +
            "AND (:cliente IS NULL OR p.cliente LIKE CONCAT(:cliente, '%')) " +
            "AND (:fornitore IS NULL OR p.fornitore LIKE CONCAT(:fornitore, '%')) " +
            "AND (:penale IS NULL OR p.penale = :penale) " +
            "AND (:importo IS NULL OR p.importo = :importo)")
    List<PreFatturazione> findPrefatturazioniByFiltri(
            @Param("numeroPrefatturazione") String numeroPrefatturazione,
            @Param("dataPrefatturazione") LocalDateTime dataPrefatturazione,
            @Param("totale") Double totale,
            @Param("scadenzaPrefatturazione") LocalDateTime scadenzaPrefatturazione,
            @Param("fatturato") boolean fatturato,
            @Param("cliente") String cliente,
            @Param("fornitore") String fornitore,
            @Param("penale") Double penale,
            @Param("importo") Double importo);
    
    
    
    
    
    
    
    
    
    @Query("SELECT p FROM PreFatturazione p WHERE " +
            "(:numeroPrefatturazione IS NULL OR p.numeroPrefatturazione LIKE CONCAT('%',:numeroPrefatturazione, '%')) " +
            "AND (:dataPrefatturazione IS NULL OR DATE(p.dataPrefatturazione) = DATE(:dataPrefatturazione)) " +
            "AND (:totale IS NULL OR p.totale = :totale) " +
            "AND (:scadenzaPrefatturazione IS NULL OR DATE(p.scadenzaPrefatturazione) = DATE(:scadenzaPrefatturazione)) " +
            "AND (:cliente IS NULL OR p.cliente LIKE CONCAT('%',:cliente, '%')) " +
            "AND (:fornitore IS NULL OR p.fornitore LIKE CONCAT('%',:fornitore, '%')) " +
            "AND (:penale IS NULL OR p.penale = :penale) " +
            "AND (:importo IS NULL OR p.importo = :importo)")
    List<PreFatturazione> findPrefatturazioneByFiltri(
            @Param("numeroPrefatturazione") String numeroPrefatturazione,
            @Param("dataPrefatturazione") LocalDateTime dataPrefatturazione,
            @Param("totale") Double totale,
            @Param("scadenzaPrefatturazione") LocalDateTime scadenzaPrefatturazione,
           
            @Param("cliente") String cliente,
            @Param("fornitore") String fornitore,
            @Param("penale") Double penale,
            @Param("importo") Double importo);
    
    
}
