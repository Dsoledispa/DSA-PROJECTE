package edu.upc.dsa.manager;

import edu.upc.dsa.models.Insignia;

import java.util.List;

public interface InsigniaManager {
    Insignia addInsignia(Insignia insignia);
    Insignia addInsignia(String id, String nombre, String avatar);
    Insignia getInsignia(String id_insignia);
    List<Insignia> getAllInsignias();
    void updateInsignia(Insignia insignia);
    void deleteInsignia(String id_insignia);
    void deleteAllInsignias();
    int sizeInsignias();
}
