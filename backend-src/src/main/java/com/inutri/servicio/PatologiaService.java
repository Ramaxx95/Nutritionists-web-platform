package com.inutri.servicio;

import com.inutri.dto.patologia.*;
import java.util.List;

public interface PatologiaService {
    List<PatologiaResponse> listarConCriterios();
}