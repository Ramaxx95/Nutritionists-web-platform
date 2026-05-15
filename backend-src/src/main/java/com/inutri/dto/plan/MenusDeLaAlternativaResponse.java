package com.inutri.dto.plan;

import com.inutri.modelo.Menu;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MenusDeLaAlternativaResponse extends SugerenciaMenuResponse{
    private Long alternativaId;

    public MenusDeLaAlternativaResponse(Long alternativaId, List<Menu> menus){
        super(menus);
        this.alternativaId = alternativaId;
    }

    public MenusDeLaAlternativaResponse(Long alternativaId, com.inutri.modelo.Menu menu){
        agregarMenu(menu);
        this.alternativaId = alternativaId;
    }

    public void cargarAlternativaId(Long alternativaId){
        this.alternativaId = alternativaId;
    }
}
