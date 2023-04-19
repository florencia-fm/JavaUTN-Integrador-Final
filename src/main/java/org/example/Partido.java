package org.example;

import lombok.*;

@AllArgsConstructor @RequiredArgsConstructor @Data
public class Partido {
    // Atributos
    @NonNull @Getter @Setter
    private Equipo equipo1;
    @NonNull @Getter @Setter
    private Equipo equipo2;
    @Getter @Setter
    private Integer golesEquipo1;
    @Getter @Setter
    private Integer golesEquipo2;

    // Métodos
     public ResultadoEnum resultado() {
         if (golesEquipo1 > golesEquipo2) {
             return ResultadoEnum.GANA_EQUIPO1;
         } else if (golesEquipo2 > golesEquipo1) {
             return ResultadoEnum.GANA_EQUIPO2;
         } else if (golesEquipo1 == 0 & golesEquipo2 == 0){
             return ResultadoEnum.EMPATE;
         } else {
             return ResultadoEnum.EMPATE;
         }
     }

    @Override
    public String toString() {
        return equipo1.getNombre() +
                " - " + equipo2.getNombre() +
                ", resultado: " + golesEquipo1 +
                " - " + golesEquipo2;
    }
}
