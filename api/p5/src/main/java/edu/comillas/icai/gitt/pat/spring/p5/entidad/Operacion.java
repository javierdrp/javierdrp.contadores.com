package edu.comillas.icai.gitt.pat.spring.p5.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;

@Entity
public class Operacion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @ManyToOne(optional = false) public Usuario usuario;
    @ManyToOne(optional = false) @OnDelete(action = OnDeleteAction.CASCADE) public Contador contador;
    @Column(nullable = false) public String tipo;
    @Column(nullable = false) public Timestamp fecha;
}
