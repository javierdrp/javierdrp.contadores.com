package edu.comillas.icai.gitt.pat.spring.p5.servicio;

import edu.comillas.icai.gitt.pat.spring.p5.entidad.Contador;
import edu.comillas.icai.gitt.pat.spring.p5.entidad.Operacion;
import edu.comillas.icai.gitt.pat.spring.p5.entidad.Usuario;
import edu.comillas.icai.gitt.pat.spring.p5.repositorio.RepoContador;
import edu.comillas.icai.gitt.pat.spring.p5.repositorio.RepoOperacion;
import edu.comillas.icai.gitt.pat.spring.p5.repositorio.RepoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Service
public class ServicioContadoresImpl implements ServicioContadores {
    @Autowired
    RepoContador repoContador;
    @Autowired
    RepoOperacion repoOperacion;
    @Autowired
    RepoUsuario repoUsuario;

    @Override
    public Usuario autentica(String credenciales) {
        Usuario usuario = repoUsuario.findByCredenciales(credenciales);
        if(usuario == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return usuario;
    }

    @Override
    @Transactional
    public Contador crea(Contador contadorNuevo, Usuario usuario) {
        Contador contador = repoContador.save(contadorNuevo);
        guardarOperacion(contador, usuario, "creacion");
        return contador;
    }

    @Override
    @Transactional
    public Contador lee(String nombre, Usuario usuario) {
        Contador contador = repoContador.findByNombre(nombre);
        if(contador == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        guardarOperacion(contador, usuario, "lectura");
        return contador;
    }

    @Override
    @Transactional
    public Contador incrementa(Contador contador, Long incremento, Usuario usuario) {
        Contador c = lee(contador.nombre, usuario);
        c.valor += incremento;
        repoContador.save(c);
        guardarOperacion(contador, usuario, "modificacion");
        return c;
    }

    @Override
    @Transactional
    public void borra(Contador contador, Usuario usuario) {
        Contador c = lee(contador.nombre, usuario);
        repoContador.borraPorNombre(c.nombre);
        guardarOperacion(contador, usuario, "borrado");
    }

    public void guardarOperacion(Contador contador, Usuario usuario, String tipo)
    {
        Operacion operacion = new Operacion();
        operacion.contador = contador;
        operacion.usuario = usuario;
        operacion.tipo = tipo;
        operacion.fecha = Timestamp.from(Instant.now());
        // if(tipo.equals("creacion")) repoOperacion.save(null); // para forzar una excepción (ej.1 transacciones)
        repoOperacion.save(operacion);
    }

    @Override
    public List<Operacion> operaciones(Contador contador) {
        return repoOperacion.findAllByContadorId(contador.id);
    }
}
