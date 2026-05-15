package com.inutri.util;

import com.inutri.modelo.enums.TipoComida;

import java.util.List;

public class FormatoRespuesta {

    private FormatoRespuesta(){}

    public static class RespuestaAlimentoParaPlan {

        public List<Comida> comidas;

        public static class Comida {
            public TipoComida tipoComida;
            public List<AlimentoSugerido> alimentos;
        }

        public static class AlimentoSugerido {
            public String nombre;
            public Integer cantidad;
        }
    }

    public static class RespuestaMenuSugerido {

        public List<Menu> menu;

        public static class Menu {
            public TipoComida tipo;
            public String descripcion;
        }
    }
}
