package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.models.Denuncia;
import java.util.List;

public interface DenunciaDAO {

    int addDenuncia(Denuncia denuncia);

    Denuncia getDenuncia(String id_denuncia);

    void updateDenuncia(Denuncia denuncia);

    void deleteDenuncia(String id_denuncia);

    List<Denuncia> getDenuncias();

    List<Denuncia> getDenunciasByUsuario(String id_usuario);
}
