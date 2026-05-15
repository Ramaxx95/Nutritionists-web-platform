package com.inutri.dto.plan;

import com.inutri.modelo.Menu;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SugerenciaMultiplesMenusResponse {
    private List<MenusDeLaAlternativaResponse> alternativasMenu;
    private Integer tokens;

    public SugerenciaMultiplesMenusResponse(){
        this.alternativasMenu = new ArrayList<>();
        this.tokens = 0;
    }

    public SugerenciaMultiplesMenusResponse(List<MenusDeLaAlternativaResponse> menus, Integer tokens){
        this.alternativasMenu = menus;
        this.tokens = tokens;
    }

    public void agregarMenu(Menu menu, Long alternativaId){
        MenusDeLaAlternativaResponse nuevoMenu = new MenusDeLaAlternativaResponse();
        nuevoMenu.agregarMenu(menu);
        nuevoMenu.cargarAlternativaId(alternativaId);
        this.alternativasMenu.add(nuevoMenu);
    }

    public void agregarTokensUsados(Integer tokens){
        this.tokens = tokens;
    }
}
