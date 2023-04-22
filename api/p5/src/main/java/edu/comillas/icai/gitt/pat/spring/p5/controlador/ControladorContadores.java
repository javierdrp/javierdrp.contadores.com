package edu.comillas.icai.gitt.pat.spring.p5.controlador;

import edu.comillas.icai.gitt.pat.spring.p5.entidad.Contador;
import edu.comillas.icai.gitt.pat.spring.p5.entidad.Operacion;
import edu.comillas.icai.gitt.pat.spring.p5.entidad.Usuario;
import edu.comillas.icai.gitt.pat.spring.p5.servicio.ServicioContadores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class ControladorContadores {
    @Autowired
    ServicioContadores servicioContadores;

    @PostMapping("/api/contadores")
    @ResponseStatus(HttpStatus.CREATED)
    public Contador crea(@RequestBody Contador contadorNuevo,
                         @RequestHeader("Authorization") String credenciales) {
        Usuario usuario = servicioContadores.autentica(credenciales);
        return servicioContadores.crea(contadorNuevo, usuario);
    }

    @GetMapping("/api/contadores/{nombre}")
    public Contador lee(@PathVariable String nombre,
                        @RequestHeader("Authorization") String credenciales) {
        Usuario usuario = servicioContadores.autentica(credenciales);
        return servicioContadores.lee(nombre, usuario);
    }

    @PutMapping("/api/contadores/{nombre}/incremento/{incremento}")
    public Contador incrementa(@PathVariable String nombre,
                               @PathVariable Long incremento,
                               @RequestHeader("Authorization") String credenciales) {
        Usuario usuario = servicioContadores.autentica(credenciales);
        Contador contador = servicioContadores.lee(nombre, usuario);
        return servicioContadores.incrementa(contador, incremento, usuario);
    }

    @DeleteMapping("/api/contadores/{nombre}/borra")
    public void borra(@PathVariable String nombre,
                      @RequestHeader("Authorization") String credenciales) {
        Usuario usuario = servicioContadores.autentica(credenciales);
        Contador contador = servicioContadores.lee(nombre, usuario);
        servicioContadores.borra(contador, usuario);
    }

    @GetMapping("/api/contadores/{nombre}/operaciones")
    public List<Operacion> operaciones(@PathVariable String nombre,
                                       @RequestHeader("Authorization") String credenciales) {
        Usuario usuario = servicioContadores.autentica(credenciales);
        Contador contador = servicioContadores.lee(nombre, usuario);
        return servicioContadores.operaciones(contador);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public String conflict() {
        return "ERROR: Ya existe un contador con el mismo nombre";
    }
}

