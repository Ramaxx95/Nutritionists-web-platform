package com.inutri.dto.alimento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlergiaAlimentoResponse {
    private String alergia;
    private String alimento;
}