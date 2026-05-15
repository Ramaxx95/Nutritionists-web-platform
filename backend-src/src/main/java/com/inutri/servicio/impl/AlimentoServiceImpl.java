package com.inutri.servicio.impl;

import com.inutri.dto.alimento.*;
import com.inutri.exception.alimento.*;
import com.inutri.exception.usuario.UsuarioNoEncontradoException;
import com.inutri.modelo.*;
import com.inutri.modelo.enums.TipoSuscripcion;
import com.inutri.repositorio.*;
import com.inutri.servicio.AlimentoService;
import com.inutri.specification.AlimentoSpecification;
import com.inutri.validacion.SuscripcionValidator;

import jakarta.transaction.Transactional;

import java.util.stream.Collectors;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

@Service
public class AlimentoServiceImpl implements AlimentoService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private AlimentoRepository alimentoRepository;

    @Override
    public Page<AlimentoListadoResponse> listarAlimentos(Pageable pageable, AlimentoListadoRequest filtros, String mailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(mailUsuario)
            .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        Specification<Alimento> spec = AlimentoSpecification.conFiltros(filtros, usuario.getId(), usuario.getTipoSubscripcion());

        return alimentoRepository.findAll(spec, pageable)
                .map(this::toResponse);
    }

    @Override
    public AlimentoDetalleResponse obtenerAlimento(Integer id, String mailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(mailUsuario)
            .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado."));
        Alimento alimento = alimentoRepository.findById(id)
            .orElseThrow(() -> new AlimentoNoEncontradoException("Alimento no encontrado."));

        if (alimento.getUsuario() != null && !alimento.getUsuario().getId().equals(usuario.getId())) {
            throw new AlimentoNoEncontradoException("Alimento no encontrado.");
        }
        if ("personalizados".equalsIgnoreCase(alimento.getCategoria()) && usuario.getTipoSubscripcion() == TipoSuscripcion.FREE) {
            throw new AlimentoNoEncontradoException("Alimento no encontrado.");
        }
        return toDetalleResponse(alimento);
    }

    @Override
    @Transactional
    public AgregarAlimentoResponse agregarAlimentoPersonalizado(AgregarAlimentoRequest request, String mailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(mailUsuario)
            .orElseThrow(() -> new UsuarioNoEncontradoException("El mail ingresado no corresponde a ningún usuario."));
        SuscripcionValidator.validarEsPremium(usuario);
        boolean existeGlobal = alimentoRepository.existsByNombreAndUsuarioIsNull(request.getNombre());
        boolean existePersonalizado = alimentoRepository.existsByNombreAndUsuario_Id(request.getNombre(), usuario.getId());
        if (existeGlobal || existePersonalizado) {
            throw new AlimentoYaExistenteException("Ya existe un alimento con ese nombre.");
        }

        Alimento nuevoAlimento = new Alimento();
        nuevoAlimento.setNombre(request.getNombre());
        nuevoAlimento.setValorEnergetico(request.getKcal());
        nuevoAlimento.setProteinas(request.getProteinas());
        nuevoAlimento.setCarbohidratosTotales(request.getCarbohidratos());
        nuevoAlimento.setGrasas(request.getGrasas());
        nuevoAlimento.setCategoria("Personalizados");
        nuevoAlimento.setUsuario(usuario);
        Alimento guardado = alimentoRepository.save(nuevoAlimento);

        return toAgregarAlimentoResponse(guardado);
    }

    @Override
    @Transactional
    public AlimentoDetalleResponse actualizarAlimentoPersonalizado(Integer id, ActualizarAlimentoRequest request, String mailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(mailUsuario)
            .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado."));
        SuscripcionValidator.validarEsPremium(usuario);
        Alimento alimento = alimentoRepository.findById(id)
            .orElseThrow(() -> new AlimentoNoEncontradoException("Alimento no encontrado."));

        if (alimento.getUsuario() == null || !alimento.getUsuario().getId().equals(usuario.getId())) {
            throw new AlimentoNoEncontradoException("No tienes permiso para modificar este alimento.");
        }

        if (request.getNombre() != null && !request.getNombre().equalsIgnoreCase(alimento.getNombre())) {
            boolean existeGlobal = alimentoRepository.existsByNombreAndUsuarioIsNull(request.getNombre());
            boolean existePersonalizado = alimentoRepository.existsByNombreAndUsuario_Id(request.getNombre(), usuario.getId());
            if (existeGlobal || existePersonalizado) {
                throw new AlimentoYaExistenteException("Ya existe un alimento con ese nombre.");
            }
        }

        actualizarAtributosAlimento(alimento, request);
        alimentoRepository.save(alimento);

        return toDetalleResponse(alimento);
    }

    private void actualizarAtributosAlimento(Alimento alimento, ActualizarAlimentoRequest request) {
        if (request.getNombre() != null) alimento.setNombre(request.getNombre());
        if (request.getValorEnergetico() != null) alimento.setValorEnergetico(request.getValorEnergetico());
        if (request.getAgua() != null) alimento.setAgua(request.getAgua());
        if (request.getProteinas() != null) alimento.setProteinas(request.getProteinas());
        if (request.getGrasas() != null) alimento.setGrasas(request.getGrasas());
        if (request.getColesterol() != null) alimento.setColesterol(request.getColesterol());
        if (request.getSaturados() != null) alimento.setSaturados(request.getSaturados());
        if (request.getMonoinsaturados() != null) alimento.setMonoinsaturados(request.getMonoinsaturados());
        if (request.getPolininsaturados() != null) alimento.setPolininsaturados(request.getPolininsaturados());
        if (request.getTrans() != null) alimento.setTrans(request.getTrans());
        if (request.getCisLinoleico() != null) alimento.setCisLinoleico(request.getCisLinoleico());
        if (request.getCisAlfaLinolenico() != null) alimento.setCisAlfaLinolenico(request.getCisAlfaLinolenico());
        if (request.getAraquidonico() != null) alimento.setAraquidonico(request.getAraquidonico());
        if (request.getEicosapentaenoico() != null) alimento.setEicosapentaenoico(request.getEicosapentaenoico());
        if (request.getDocosahexaenoico() != null) alimento.setDocosahexaenoico(request.getDocosahexaenoico());
        if (request.getCarbohidratosDisponibles() != null) alimento.setCarbohidratosDisponibles(request.getCarbohidratosDisponibles());
        if (request.getCarbohidratosTotales() != null) alimento.setCarbohidratosTotales(request.getCarbohidratosTotales());
        if (request.getAzucarTotal() != null) alimento.setAzucarTotal(request.getAzucarTotal());
        if (request.getAzucarAgregado() != null) alimento.setAzucarAgregado(request.getAzucarAgregado());
        if (request.getFibraAlimentaria() != null) alimento.setFibraAlimentaria(request.getFibraAlimentaria());
        if (request.getAlcohol() != null) alimento.setAlcohol(request.getAlcohol());
        if (request.getCenizas() != null) alimento.setCenizas(request.getCenizas());
        if (request.getSodio() != null) alimento.setSodio(request.getSodio());
        if (request.getPotasio() != null) alimento.setPotasio(request.getPotasio());
        if (request.getCalcio() != null) alimento.setCalcio(request.getCalcio());
        if (request.getCobre() != null) alimento.setCobre(request.getCobre());
        if (request.getFosforo() != null) alimento.setFosforo(request.getFosforo());
        if (request.getHierro() != null) alimento.setHierro(request.getHierro());
        if (request.getMagnesio() != null) alimento.setMagnesio(request.getMagnesio());
        if (request.getZinc() != null) alimento.setZinc(request.getZinc());
        if (request.getNiacina() != null) alimento.setNiacina(request.getNiacina());
        if (request.getFolatoEFD() != null) alimento.setFolatoEFD(request.getFolatoEFD());
        if (request.getAcidoFolico() != null) alimento.setAcidoFolico(request.getAcidoFolico());
        if (request.getVitaminaA() != null) alimento.setVitaminaA(request.getVitaminaA());
        if (request.getRetinol() != null) alimento.setRetinol(request.getRetinol());
        if (request.getTiamina() != null) alimento.setTiamina(request.getTiamina());
        if (request.getRiboflavina() != null) alimento.setRiboflavina(request.getRiboflavina());
        if (request.getVitaminaB12() != null) alimento.setVitaminaB12(request.getVitaminaB12());
        if (request.getVitaminaC() != null) alimento.setVitaminaC(request.getVitaminaC());
        if (request.getVitaminaD() != null) alimento.setVitaminaD(request.getVitaminaD());
    }

    @Override
    @Transactional
    public void eliminarAlimentoPersonalizado(Integer id, String mailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(mailUsuario)
            .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado."));
        SuscripcionValidator.validarEsPremium(usuario);
        Alimento alimento = alimentoRepository.findById(id)
            .orElseThrow(() -> new AlimentoNoEncontradoException("Alimento no encontrado."));

        if (alimento.getUsuario() == null || !alimento.getUsuario().getId().equals(usuario.getId())) {
            throw new AlimentoNoEncontradoException("No tienes permiso para eliminar este alimento.");
        }

        alimentoRepository.delete(alimento);
    }

    private AlimentoListadoResponse toResponse(Alimento alimento) {
        return new AlimentoListadoResponse(alimento.getId(), alimento.getCategoria(), alimento.getNombre(), alimento.getValorEnergetico(), alimento.getProteinas(), alimento.getCarbohidratosTotales(), alimento.getGrasas(), alimento.getColesterol(), alimento.getSodio(), alimento.getAzucarAgregado(), alimento.getFibraAlimentaria(), alimento.isNoAptoCeliaco(), alimento.isUltraprocesado());
    }

    private AlimentoDetalleResponse toDetalleResponse(Alimento alimento) {
        return new AlimentoDetalleResponse(alimento.getId(), alimento.getCategoria(), alimento.getNombre(), alimento.getValorEnergetico(), alimento.getAgua(), alimento.getProteinas(), alimento.getGrasas(), alimento.getColesterol(), alimento.getSaturados(), alimento.getMonoinsaturados(), alimento.getPolininsaturados(), alimento.getTrans(), alimento.getCisLinoleico(), alimento.getCisAlfaLinolenico(), alimento.getAraquidonico(), alimento.getEicosapentaenoico(), alimento.getDocosahexaenoico(), alimento.getCarbohidratosDisponibles(), alimento.getCarbohidratosTotales(), alimento.getAzucarTotal(), alimento.getAzucarAgregado(), alimento.getFibraAlimentaria(), alimento.getAlcohol(), alimento.getCenizas(), alimento.getSodio(), alimento.getPotasio(), alimento.getCalcio(), alimento.getCobre(), alimento.getFosforo(), alimento.getHierro(), alimento.getMagnesio(), alimento.getZinc(), alimento.getNiacina(), alimento.getFolatoEFD(), alimento.getAcidoFolico(), alimento.getVitaminaA(), alimento.getRetinol(), alimento.getTiamina(), alimento.getRiboflavina(), alimento.getVitaminaB12(), alimento.getVitaminaC(), alimento.getVitaminaD());
    }

    private AgregarAlimentoResponse toAgregarAlimentoResponse(Alimento alimento) {
        return new AgregarAlimentoResponse(
            alimento.getId(),
            alimento.getCategoria(),
            alimento.getNombre(),
            alimento.getValorEnergetico(),
            alimento.getProteinas(),
            alimento.getCarbohidratosTotales(),
            alimento.getGrasas()
        );
    }

    public List<CategoriaListadoResponse> obtenerCategorias() {
        return alimentoRepository.findDistinctCategorias()
                .stream()
                .map(CategoriaListadoResponse::new)
                .collect(Collectors.toList());
    }
}