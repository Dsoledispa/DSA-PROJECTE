package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.models.Consulta;
import java.util.List;

public interface ConsultaDAO {

    int addConsulta(Consulta consulta);

    Consulta getConsulta(String id_consulta);

    void updateConsulta(Consulta consulta);

    void deleteConsulta(String id_consulta);

    List<Consulta> getConsultas();

    List<Consulta> getConsultasByUsuario(String id_usuario);
}
