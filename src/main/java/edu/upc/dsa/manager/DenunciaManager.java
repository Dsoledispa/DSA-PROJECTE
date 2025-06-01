package edu.upc.dsa.manager;

import edu.upc.dsa.models.Denuncia;

import java.util.List;

public interface DenunciaManager {

    Denuncia addDenuncia(Denuncia denuncia);
    Denuncia addDenuncia(String id_denuncia, String titulo, String mensaje, String id_usuario);

    List<Denuncia> getDenuncias();
    List<Denuncia> getDenunciasByUsuario(String id_usuario);

    Denuncia getDenuncia(String id_denuncia);

    void updateDenuncia(Denuncia denuncia);

    void deleteDenuncia(String id_denuncia);

    int sizeDenuncias();
}
