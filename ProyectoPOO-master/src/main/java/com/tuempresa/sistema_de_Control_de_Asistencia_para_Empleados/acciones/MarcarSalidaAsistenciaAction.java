package com.tuempresa.sistema_de_Control_de_Asistencia_para_Empleados.acciones;

import org.openxava.actions.SaveAction;

public class MarcarSalidaAsistenciaAction extends SaveAction {

    @Override
    public void execute() throws Exception {
        addMessage("Acción SALIDA ejecutándose...");


        if (getView().isKeyEditable()) {
            addError("Primero debe registrar la ENTRADA");
            return;
        }


        super.execute();

        addMessage("Salida registrada correctamente");
    }
}
