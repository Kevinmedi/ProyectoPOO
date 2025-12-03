package com.tuempresa.sistema_de_Control_de_Asistencia_para_Empleados.acciones;

import org.openxava.actions.SaveAction;

public class MarcarEntradaAsistenciaAction extends SaveAction {

    @Override
    public void execute() throws Exception {

        addMessage("Acción ENTRADA ejecutándose...");


        if (!getView().isKeyEditable()) {
            addError("La entrada ya fue registrada, use el botón de SALIDA");
            return;
        }


        super.execute();

        addMessage("Entrada registrada correctamente");
    }
}
