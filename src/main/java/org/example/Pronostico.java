package org.example;
import lombok.*;

@AllArgsConstructor @RequiredArgsConstructor @Data
public class Pronostico {
    //Atributos
    @NonNull @Getter @Setter
    private Partido partido;
    @Getter @Setter
    private Equipo equipo;
    @NonNull @Getter @Setter
    private ResultadoEnum resultado;

    // Métodos
    @Override
    public String toString() {
        return "Partido: " + partido.getEquipo1().getNombre() + " - " + partido.getEquipo2().getNombre() +
                ", resultado; " + resultado;
    }
}


