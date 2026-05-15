package com.inutri.specification;

import com.inutri.modelo.Alimento;
import com.inutri.modelo.enums.TipoSuscripcion;
import com.inutri.dto.alimento.AlimentoListadoRequest;
import jakarta.persistence.criteria.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.data.jpa.domain.Specification;

public class AlimentoSpecification {

    public static Specification<Alimento> conFiltros(AlimentoListadoRequest filtros, Long usuarioId, TipoSuscripcion tipoSuscripcion) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();


            if (tipoSuscripcion == TipoSuscripcion.FREE) {
                predicate = cb.and(predicate,
                    cb.isNull(root.get("usuario")),
                    cb.notEqual(cb.lower(root.get("categoria")), "personalizados")
                );
            } else {
                predicate = cb.and(predicate, cb.or(
                    cb.isNull(root.get("usuario")),
                    cb.equal(root.get("usuario").get("id"), usuarioId)
                ));
            }

            if (filtros.getNombre() != null && !filtros.getNombre().isBlank()) {
                predicate = cb.and(predicate,
                    cb.like(cb.lower(root.get("nombre")), "%" + filtros.getNombre().toLowerCase() + "%"));
            }

            if (filtros.getCategoria() != null && !filtros.getCategoria().isBlank()) {
                predicate = cb.and(predicate,
                    cb.equal(root.get("categoria"), filtros.getCategoria()));
            }

            predicate = aplicarRango(cb, root, predicate, "proteinas", filtros.getMinProteinas(), filtros.getMaxProteinas());
            predicate = aplicarRango(cb, root, predicate, "carbohidratosTotales", filtros.getMinCarbohidratos(), filtros.getMaxCarbohidratos());
            predicate = aplicarRango(cb, root, predicate, "grasas", filtros.getMinGrasas(), filtros.getMaxGrasas());
            predicate = aplicarRango(cb, root, predicate, "valorEnergetico", filtros.getMinKcal(), filtros.getMaxKcal());
            predicate = aplicarRango(cb, root, predicate, "sodio", filtros.getMinSodio(), filtros.getMaxSodio());
            predicate = aplicarRango(cb, root, predicate, "colesterol", filtros.getMinColesterol(), filtros.getMaxColesterol());

            return predicate;
        };
    }

    private static <T extends Number & Comparable<T>> Predicate aplicarRango(CriteriaBuilder cb, Root<Alimento> root, Predicate predicate, String atributo, T min, T max) {
        Class<?> tipo = root.get(atributo).getJavaType();

        if (min != null) {
            if (tipo.equals(Integer.class)) {
                int valorMin = ((BigDecimal) min).setScale(0, RoundingMode.HALF_UP).intValueExact();
                predicate = cb.and(predicate, cb.ge(root.get(atributo), valorMin));
            } else {
                predicate = cb.and(predicate, cb.ge(root.get(atributo), min));
            }
        }

        if (max != null) {
            if (tipo.equals(Integer.class)) {
                int valorMax = ((BigDecimal) max).setScale(0, RoundingMode.HALF_UP).intValueExact();
                predicate = cb.and(predicate, cb.le(root.get(atributo), valorMax));
            } else {
                predicate = cb.and(predicate, cb.le(root.get(atributo), max));
            }
        }

        return predicate;
    }
}