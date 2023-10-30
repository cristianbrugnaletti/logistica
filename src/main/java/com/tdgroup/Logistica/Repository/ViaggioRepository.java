package com.tdgroup.Logistica.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tdgroup.Logistica.Model.Viaggio;

public interface ViaggioRepository extends JpaRepository<Viaggio, Long>{

	Long countByIdViaggioEsterno(Long idViaggioEsterno);
}
