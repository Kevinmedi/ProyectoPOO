package com.tuempresa.sistema_de_Control_de_Asistencia_para_Empleados.modelo;

import java.time.LocalDateTime;
import org.openxava.calculators.ICalculator;

public class CurrentDateTimeCalculator implements ICalculator {

    @Override
    public Object calculate() throws Exception {
        return LocalDateTime.now();
    }
}
