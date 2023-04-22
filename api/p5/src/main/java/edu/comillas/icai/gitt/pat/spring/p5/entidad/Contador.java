package edu.comillas.icai.gitt.pat.spring.p5.entidad;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Contador {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @JsonIgnore public Long id;
    @Column(nullable = false, unique = true) public String nombre;
    @Column(nullable = false) public Long valor;
}
