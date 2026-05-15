package com.inutri.util;

import com.inutri.dto.alimento.AlimentoSugerenciaIAResponse;
import com.inutri.dto.plan.SugerenciaAlimentosEnComidaResponse;
import com.inutri.modelo.CriterioPatologia;
import com.inutri.modelo.Paciente;
import com.inutri.modelo.Patologia;
import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import java.util.*;

public class AjustadorDeMacros {
    private final double metaKcal, metaCarbo, metaProt, metaGrasa;
    private double kcalActual, carboActual, protActual, grasaActual,
        colesActual, sodioActual, azucActual, fibraActual;

    public AjustadorDeMacros(double metaKcal, double metaCarbo, double metaProt, double metaGrasa) {
        this.metaKcal = metaKcal;
        this.metaCarbo = metaCarbo;
        this.metaProt = metaProt;
        this.metaGrasa = metaGrasa;
    }

    public void ajustar (List<SugerenciaAlimentosEnComidaResponse> comidas, Paciente paciente){

        calcularMacrosTotales(comidas);
        System.out.println("[DEBUG - AjustadorDeMacros] Antes de ajustar");
        mostrarResultadoPorConsola(paciente);

        int cantTotalAlimentos = comidas.getFirst().getAlimentos().size() +
                comidas.get(1).getAlimentos().size() +
                comidas.get(2).getAlimentos().size() +
                comidas.getLast().getAlimentos().size();
        int indiceComienzoAlmuerzo = comidas.getFirst().getAlimentos().size(),
                indiceComienzoMerienda = indiceComienzoAlmuerzo + comidas.get(1).getAlimentos().size(),
                indiceComienzoCena = indiceComienzoMerienda + comidas.get(2).getAlimentos().size();
        double[] kcal = new double[cantTotalAlimentos],
                carbo = new double[cantTotalAlimentos],
                prot = new double[cantTotalAlimentos],
                grasa = new double[cantTotalAlimentos],
                coles = new double[cantTotalAlimentos],
                sodio = new double[cantTotalAlimentos],
                azuc = new double[cantTotalAlimentos],
                fibra = new double[cantTotalAlimentos];
        String HIPERTENSION = "Hipertensión", ENFERMEDAD_CARDIACA = "Enfermedad cardiaca", DIABETES = "Diabetes",
               HIPERCOLESTEROLEMIA = "Hipercolesterolemia", HIPERTRIGLICERIDEMIA = "Hipertrigliceridemia",
               HIGADO_GRASO = "Hígado graso (Esteatosis hepática no alcohólica)",
                ENFERMEDAD_RENAL = "Enfermedad renal";

        armarCoeficientes(kcal, carbo, prot, grasa, coles, sodio, azuc, fibra, comidas);
        LinearObjectiveFunction funcionObjetivo = new LinearObjectiveFunction(new double[cantTotalAlimentos], 0);
        Collection<LinearConstraint> restricciones = new ArrayList<>();

        // Restricciones de macros
        restricciones.add(new LinearConstraint(kcal, Relationship.GEQ, this.metaKcal));
        restricciones.add(new LinearConstraint(carbo, Relationship.GEQ, this.metaCarbo));
        restricciones.add(new LinearConstraint(prot, Relationship.GEQ, this.metaProt));
        restricciones.add(new LinearConstraint(grasa, Relationship.GEQ, this.metaGrasa));

        // Restricciones de patologias
        for(Patologia patologia : paciente.getPatologias()){
            if(patologia.getNombre().equals(HIPERTENSION) ||
                    patologia.getNombre().equals(ENFERMEDAD_CARDIACA) ||
                    patologia.getNombre().equals(ENFERMEDAD_RENAL)){
                restricciones.add(new LinearConstraint(sodio, Relationship.LEQ, 1500));
            }
            else if(patologia.getNombre().equals(DIABETES)){
                restricciones.add(new LinearConstraint(azuc, Relationship.LEQ, 20));
                restricciones.add(new LinearConstraint(fibra, Relationship.GEQ, 12));
            }
            else if(patologia.getNombre().equals(HIPERCOLESTEROLEMIA)){
                restricciones.add(new LinearConstraint(coles, Relationship.LEQ, 200000));
            }
            else if(patologia.getNombre().equals(HIPERTRIGLICERIDEMIA) || patologia.getNombre().equals(HIGADO_GRASO)){
                restricciones.add(new LinearConstraint(azuc, Relationship.LEQ, 20));
                restricciones.add(new LinearConstraint(fibra, Relationship.GEQ, 12));
                restricciones.add(new LinearConstraint(coles, Relationship.LEQ, 200000));
            }
        }

        restricciones = filtrarRestriccionesDuplicadas(restricciones);

        // Las cantidades no pueden ser menores a 25g ni mayores a 100g para desayuno y merienda
        // No pueden ser menores a 50g ni mayores a 250g para almuerzo y cena
        for (int i = 0; i < cantTotalAlimentos; i++) {
            double[] coef = new double[cantTotalAlimentos];
            coef[i] = 1.0;
            if((i < indiceComienzoAlmuerzo) ||
                    (indiceComienzoMerienda <= i && i < indiceComienzoCena)){
                restricciones.add(new LinearConstraint(coef, Relationship.GEQ, 25));
                restricciones.add(new LinearConstraint(coef, Relationship.LEQ, 100));
            }
            else{
                restricciones.add(new LinearConstraint(coef, Relationship.GEQ, 50));
                restricciones.add(new LinearConstraint(coef, Relationship.LEQ, 250));
            }

        }

        // Resolver con Simplex
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solucion = solver.optimize(
                new MaxIter(100),
                funcionObjetivo,
                new LinearConstraintSet(restricciones),
                GoalType.MINIMIZE,
                new NonNegativeConstraint(true)
        );

        // Guardamos las cantidades de la solucion
        double[] cantidades = solucion.getPoint();
        aplicarCantidades(cantidades, comidas);

        System.out.println("[DEBUG - AjustadorDeMacros] Despues de ajustar");
        calcularMacrosTotales(comidas);
        mostrarResultadoPorConsola(paciente);

    }

    private void calcularMacrosTotales(List<SugerenciaAlimentosEnComidaResponse> comidas){
        this.kcalActual = 0;
        this.carboActual = 0;
        this.protActual = 0;
        this.grasaActual = 0;
        this.colesActual = 0;
        this.sodioActual = 0;
        this.azucActual = 0;
        this.fibraActual = 0;
        for(SugerenciaAlimentosEnComidaResponse comida : comidas){
            List<AlimentoSugerenciaIAResponse> alimentos = comida.getAlimentos();
            for(AlimentoSugerenciaIAResponse alimento : alimentos){
                this.kcalActual += (double) alimento.getValorEnergetico() * alimento.getCantidad() / 100;
                this.carboActual += alimento.getCarbohidratos().doubleValue() * alimento.getCantidad() / 100;
                this.protActual += alimento.getProteinas().doubleValue() * alimento.getCantidad() / 100;
                this.grasaActual += alimento.getGrasas().doubleValue() * alimento.getCantidad() / 100;
                this.colesActual += alimento.getColesterol().doubleValue() * alimento.getCantidad() / 100;
                this.sodioActual += alimento.getSodio().doubleValue() * alimento.getCantidad() / 100;
                this.azucActual += alimento.getAzucarAgregado().doubleValue() * alimento.getCantidad() / 100;
                this.fibraActual += alimento.getFibraAlimentaria().doubleValue() * alimento.getCantidad() / 100;
            }
        }
    }

    private void armarCoeficientes(double[] kcal,
                                   double[] carbo,
                                   double[] prot,
                                   double[] grasa,
                                   double[] coles,
                                   double[] sodio,
                                   double[] azuc,
                                   double[] fibra,
                                   List<SugerenciaAlimentosEnComidaResponse> comidas){
        int totalAlimentos = 0;
        for(SugerenciaAlimentosEnComidaResponse comida : comidas){
            for(AlimentoSugerenciaIAResponse alimento : comida.getAlimentos()){
                kcal[totalAlimentos] = (double) alimento.getValorEnergetico() / 100;
                carbo[totalAlimentos] = alimento.getCarbohidratos().doubleValue() / 100;
                prot[totalAlimentos] = alimento.getProteinas().doubleValue() / 100;
                grasa[totalAlimentos] = alimento.getGrasas().doubleValue() / 100;
                coles[totalAlimentos] = alimento.getColesterol().doubleValue() / 100;
                sodio[totalAlimentos] = alimento.getSodio().doubleValue() / 100;
                azuc[totalAlimentos] = alimento.getAzucarAgregado().doubleValue() / 100;
                fibra[totalAlimentos] = alimento.getFibraAlimentaria().doubleValue() / 100;
                totalAlimentos++;
            }
        }
    }

    private Collection<LinearConstraint> filtrarRestriccionesDuplicadas(Collection<LinearConstraint> restricciones){
        Set<String> firmas = new HashSet<>();
        Collection<LinearConstraint> resultado = new ArrayList<>();
        for (LinearConstraint lc : restricciones) {
            String firma = Arrays.toString(lc.getCoefficients().toArray()) + "|" + lc.getRelationship() + "|" + lc.getValue();
            if (firmas.add(firma)) {
                resultado.add(lc);
            }
        }
        return resultado;
    }

    private void aplicarCantidades(double[] cantidades, List<SugerenciaAlimentosEnComidaResponse> comidas){
        int indice = 0;
        for(SugerenciaAlimentosEnComidaResponse comida : comidas){
            for(AlimentoSugerenciaIAResponse alimento : comida.getAlimentos()){
                alimento.setCantidad((int) cantidades[indice]);
                indice++;
            }
        }
    }

    private void mostrarResultadoPorConsola(Paciente paciente){
        System.out.println("[DEBUG - AjustadorDeMacros] Total Kcal pedidas: " + this.metaKcal + " vs total sugeridas: " + this.kcalActual);
        if(this.kcalActual / this.metaKcal > 0.9){
            System.out.println("[DEBUG - AjustadorDeMacros] Se alcanzo la cantidad de kcal pedida!");
        }
        System.out.println("[DEBUG - AjustadorDeMacros] Total CH pedidos: " + this.metaCarbo + " vs total sugeridos: " + this.carboActual);
        if(this.carboActual / this.metaCarbo > 0.9){
            System.out.println("[DEBUG - AjustadorDeMacros] Se alcanzo la cantidad de carbohidratos pedida!");
        }
        System.out.println("[DEBUG - AjustadorDeMacros] Total Prot pedidas: " + this.metaProt + " vs total sugeridas: " + this.protActual);
        if(this.protActual / this.metaProt > 0.9){
            System.out.println("[DEBUG - AjustadorDeMacros] Se alcanzo la cantidad de proteinas pedida!");
        }
        System.out.println("[DEBUG - AjustadorDeMacros] Total Grasas pedidas: " + this.metaGrasa + " vs total sugeridas: " + this.grasaActual);
        if(this.grasaActual / this.metaGrasa > 0.9){
            System.out.println("[DEBUG - AjustadorDeMacros] Se alcanzo la cantidad de grasas pedida!");
        }
        System.out.println("[DEBUG - AjustadorDeMacros] Total Colesterol (mg): " + this.colesActual);
        System.out.println("[DEBUG - AjustadorDeMacros] Total Sodio (mg): " + this.sodioActual);
        System.out.println("[DEBUG - AjustadorDeMacros] Total Azucar (g): " + this.azucActual);
        System.out.println("[DEBUG - AjustadorDeMacros] Total Fibra (g): " + this.fibraActual);

        if (!paciente.getPatologias().isEmpty()){
            System.out.println("[DEBUG - AjustadorDeMacros] El paciente tiene las siguientes restricciones:");
            for (Patologia patologia : paciente.getPatologias()){
                StringBuilder criterios = new StringBuilder();
                for(CriterioPatologia crit : patologia.getCriterios()){
                    criterios.append(crit.getDescripcion()).append(", ");
                }
                System.out.println("[DEBUG - AjustadorDeMacros] " + criterios);
            }
        }
        System.out.println();
    }
}
