package com.inutri.dto.plan;

import com.inutri.modelo.enums.TipoComida;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SugerenciaMenuResponse {

    protected List<Menu> menus;

    public SugerenciaMenuResponse() {
        this.menus = new ArrayList<>();
    }

    public SugerenciaMenuResponse(List<Menu> menus) {
        this.menus = menus;
    }

    /**
     * Clase estatica dentro de 'SugerenciaMenuResponse' que contiene los menus sugeridos para cada comida.
     * <p>
     * Atributos:
     * <ul>
     *     <li>
     *         <b>tipoComida</b>: menu perteneciente a DESAYUNO, ALMUERZO, MERIENDA o CENA
     *     </li>
     *     <li>
     *         <b>menuSugerido</b>: respuesta cruda que devuelve la IA
     *     </li>
     * </ul>
     */
    public record Menu(TipoComida tipoComida, String menuSugerido) {
    }

    public void agregarMenu(com.inutri.modelo.Menu menuGuardado){
        if(menuGuardado != null){
            this.menus.add(new Menu(TipoComida.DESAYUNO, menuGuardado.getDesayuno()));
            this.menus.add(new Menu(TipoComida.ALMUERZO, menuGuardado.getAlmuerzo()));
            this.menus.add(new Menu(TipoComida.MERIENDA, menuGuardado.getMerienda()));
            this.menus.add(new Menu(TipoComida.CENA, menuGuardado.getCena()));
        }
    }
}
