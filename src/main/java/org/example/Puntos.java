package org.example;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Data
public class Puntos {
    @NonNull @Getter @Setter
    private int puntosPorAcierto;
    @NonNull @Getter @Setter
    private int puntosExtra;
}