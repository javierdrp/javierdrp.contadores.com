package edu.comillas.icai.gitt.pat.spring.p5.repositorio;

import edu.comillas.icai.gitt.pat.spring.p5.entidad.Operacion;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RepoOperacion extends CrudRepository<Operacion, Long> {
    List<Operacion> findAllByContadorId(Long contadorId);
}