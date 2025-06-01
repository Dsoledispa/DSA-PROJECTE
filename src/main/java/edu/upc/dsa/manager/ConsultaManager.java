package edu.upc.dsa.manager;

import edu.upc.dsa.models.Consulta;

import java.util.List;

public interface ConsultaManager {

    Consulta addConsulta(Consulta consulta);
    Consulta addConsulta(String id_consulta, String titulo, String mensaje, String id_usuario);

    List<Consulta> getConsultas();
    List<Consulta> getConsultasByUsuario(String id_usuario);

    Consulta getConsulta(String id_consulta);

    void updateConsulta(Consulta consulta);

    void deleteConsulta(String id_consulta);

    int sizeConsultas();
}
