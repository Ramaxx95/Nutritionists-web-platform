package com.inutri.repositorio;

import com.inutri.modelo.Alimento;
import org.springframework.data.jpa.repository.*;

import java.util.Optional;
import java.util.List;

public interface AlimentoRepository extends JpaRepository<Alimento, Integer>, JpaSpecificationExecutor<Alimento> {
    Optional<Alimento> findById(Integer id);
    Optional<Alimento> findByNombre(String nombre);
    boolean existsByNombreAndUsuarioIsNull(String nombre);
    boolean existsByNombreAndUsuario_Id(String nombre, Long usuarioId);
    List<Alimento> findByNombreContainingIgnoreCase(String texto);
    List<Alimento> findByCategoriaIgnoreCase(String categoria);
    @Query("SELECT DISTINCT a.categoria FROM Alimento a ORDER BY a.categoria")
    List<String> findDistinctCategorias();
}