package edu.upc.dsa.db.orm.dao;

import edu.upc.dsa.models.Insignia;
import java.util.List;

public interface InsigniaDAO {
    int addInsignia(Insignia insignia);

    Insignia getInsignia(String id_insignia);

    List<Insignia> getInsignias();

    void updateInsignia(Insignia insignia);

    void deleteInsignia(String id_insignia);
}
