package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

public class Main {
    static final String DB_URL = "jdbc:h2:mem:;INIT=runscript from 'classpath:init.sql'";
    public static void main(String[] args) throws IOException {
        // Declaramos las variables
        ArrayList resultados = new ArrayList<>();
        ArrayList pronosticos = new ArrayList<>();

        // Conectamos a la base de datos
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Pronosticos");
            // Encabezado de la tabla
            System.out.println("\nMostrando la tabla de Pronosticos: \n");
            System.out.printf("%-20s", "idPronostico");
            System.out.printf("%-20s" , "Participante");
            System.out.printf("%-20s" , "Equipo1");
            System.out.printf("%-20s" , "Resultado");
            System.out.printf("%-20s %n" , "Equipo2");

            while(rs.next()) {
                // Definimos cada columna
                int idPronostico  = rs.getInt("idPronostico");
                String part = rs.getString("participante");
                String eq1 = rs.getString("equipo1");
                String resultado = rs.getString("resultado");
                String eq2 = rs.getString("equipo2");

                // Mostrando por consola
                System.out.printf("%-20s", idPronostico);
                System.out.printf("%-20s" , part);
                System.out.printf("%-20s" , eq1);
                System.out.printf("%-20s" ,resultado);
                System.out.printf("%-20s %n" , eq2 );
            }

            rs = stmt.executeQuery("SELECT * FROM Resultados");
            // Encabezado de la tabla
            System.out.println("\nMostrando la tabla de Resultados: \n");
            System.out.printf("%-20s", "idResultado");
            System.out.printf("%-20s", "Ronda");
            System.out.printf("%-20s" , "Equipo1");
            System.out.printf("%-20s" , "cantGoles1");
            System.out.printf("%-20s" , "cantGoles2");
            System.out.printf("%-20s %n" , "Equipo2");

            while(rs.next()) {
                // Definimos las columnas
                int idResultado  = rs.getInt("idResultado");
                int numRonda  = rs.getInt("ronda");
                String eq1 = rs.getString("equipo1");
                int cantGoles1  = rs.getInt("cantGoles1");
                int cantGoles2  = rs.getInt("cantGoles2");
                String eq2 = rs.getString("equipo2");

                // Mostrando por consola
                System.out.printf("%-20s", idResultado);
                System.out.printf("%-20s", numRonda);
                System.out.printf("%-20s" , eq1);
                System.out.printf("%-20s" ,cantGoles1);
                System.out.printf("%-20s" ,cantGoles2);
                System.out.printf("%-20s %n" , eq2);
            }

            rs = stmt.executeQuery("SELECT * FROM Pronosticos");
            pronosticos = leerPronostico(rs);

            rs = stmt.executeQuery("SELECT * FROM Resultados");
            resultados = leerResultados(rs);

            compararResultados(pronosticos, resultados);
         }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Función para leer e instanciar los resultados
    private static ArrayList leerResultados(ResultSet rs) {
        // Definimos las variables que vamos a usar
        int cantRondas = 0;
        ArrayList<Ronda> rondas = new ArrayList<>();
        ArrayList<Partido> partidos = new ArrayList<>();

        try {
            while(rs.next()) {
                // Definimos las columnas
                int idResultado  = rs.getInt("idResultado");
                int numRonda  = rs.getInt("ronda");
                String eq1 = rs.getString("equipo1");
                int cantGoles1  = rs.getInt("cantGoles1");
                int cantGoles2  = rs.getInt("cantGoles2");
                String eq2 = rs.getString("equipo2");

                // Instanciando los objetos
                try {
                    if (cantRondas == 0){
                        cantRondas = numRonda;
                    }
                    if (cantRondas != numRonda){
                        Ronda ronda = new Ronda(cantRondas - 1, partidos);
                        rondas.add(ronda);
                        partidos = new ArrayList<>();
                        cantRondas = numRonda;
                    }
                    Equipo equipo1 = new Equipo(eq1);
                    Equipo equipo2 = new Equipo(eq2);
                    Partido partido = new Partido(equipo1, equipo2);
                    partido.setGolesEquipo1(cantGoles1);
                    partido.setGolesEquipo2(cantGoles2);
                    partido.resultado(cantGoles1, cantGoles2);
                    partidos.add(partido);
                }
                catch (Exception e){
                    System.out.println("Tabla sin datos");
                    e.printStackTrace();
                }
            }
            try{
                Ronda ronda = new Ronda(cantRondas, partidos);
                rondas.add(ronda);
            }
            catch (Exception e){
                System.out.println("Tabla sin datos");
                e.printStackTrace();
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return rondas;
    }

    // Función para leer e instanciar los pronósticos
    private static ArrayList leerPronostico(ResultSet rs) {
        // Definimos las variables que vamos a usar
        String participante = "";
        ArrayList<Persona> personas = new ArrayList<>();
        ArrayList<Pronostico> pronosticos = new ArrayList<>();

        ResultadoEnum resultadoEnum = null;

        // Conexión a la base de datos
        try {
            while(rs.next()) {
                // Definimos cada columna
                int idPronostico  = rs.getInt("idPronostico");
                String part = rs.getString("participante");
                String eq1 = rs.getString("equipo1");
                String resultado = rs.getString("resultado");
                String eq2 = rs.getString("equipo2");

                try {
                    // Instanciando los objetos
                    if (participante.equals("")){
                        participante = part;
                    }

                    if (!participante.equals(part)){
                        Persona persona = new Persona(participante, pronosticos, 0, 0, 0);
                        personas.add(persona);
                        pronosticos = new ArrayList<>();
                        participante = part;
                    }

                    Equipo equipo1 = new Equipo(eq1);
                    Equipo equipo2 = new Equipo(eq2);
                    Partido partido = new Partido(equipo1, equipo2);

                    switch (resultado){
                        case "Gana 1":
                            resultadoEnum = ResultadoEnum.GANA_EQUIPO1;
                            break;
                        case "Gana 2":
                            resultadoEnum = ResultadoEnum.GANA_EQUIPO2;
                            break;
                        case "Empata":
                            resultadoEnum = ResultadoEnum.EMPATE;
                            break;
                    }
                    Pronostico pronostico = new Pronostico(partido, resultadoEnum);
                    pronosticos.add(pronostico);


                } catch (Exception e) {
                    System.out.println("Tabla sin datos");
                    e.printStackTrace();
                }
            }
            try {
            Persona persona = new Persona(participante, pronosticos, 0, 0, 0);
            personas.add(persona);
            } catch (Exception e) {
                System.out.println("Tabla sin datos");
                e.printStackTrace();
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return personas;
    }

    // Comprar resultados vs pronósticos
    private static void compararResultados(ArrayList pronosticos, ArrayList resultados) throws IOException {
        // Object Mapper
        ObjectMapper objectMapper = new ObjectMapper();
        String rutaConfig = "src/main/resources/config.json";
        Puntos puntos = objectMapper.readValue(Paths.get(rutaConfig).toFile(), Puntos.class);
        int aciertosPorRonda = 0;
        try{
            System.out.println("\nPuntos por persona:\n");
            for (int i = 0; i < pronosticos.size(); i++) {
                Persona persona = (Persona) pronosticos.get(i);
                for (int j = 0; j < persona.getPronosticos().size(); j++) {
                    Pronostico pronostico = (Pronostico) persona.getPronosticos().get(j);
                    for (int k = 0; k < resultados.size(); k++) {
                        Ronda ronda = (Ronda) resultados.get(k);
                        for (int l = 0; l < ronda.getPartidos().size(); l++) {
                            Partido partido = (Partido) ronda.getPartidos().get(l);
                            if (pronostico.getPartido().getEquipo1().getNombre().equals(partido.getEquipo1().getNombre()) && pronostico.getPartido().getEquipo2().getNombre().equals(partido.getEquipo2().getNombre())) {
                                if (pronostico.getResultado().equals(ResultadoEnum.GANA_EQUIPO1) && partido.resultado(partido.getGolesEquipo1(), partido.getGolesEquipo2()).equals(ResultadoEnum.GANA_EQUIPO1)) {
                                    persona.setPuntos(persona.getPuntos() + puntos.getPuntosPorAcierto());
                                    persona.setAciertos(persona.getAciertos() + 1);
                                    aciertosPorRonda += 1;
                                } else if (pronostico.getResultado().equals(ResultadoEnum.EMPATE) && partido.resultado(partido.getGolesEquipo1(), partido.getGolesEquipo2()).equals(ResultadoEnum.EMPATE)) {
                                    persona.setPuntos(persona.getPuntos() + puntos.getPuntosPorAcierto());
                                    persona.setAciertos(persona.getAciertos() + 1);
                                    aciertosPorRonda += 1;
                                } else if (pronostico.getResultado().equals(ResultadoEnum.GANA_EQUIPO2) && partido.resultado(partido.getGolesEquipo1(), partido.getGolesEquipo2()).equals(ResultadoEnum.GANA_EQUIPO2)) {
                                    persona.setPuntos(persona.getPuntos() + puntos.getPuntosPorAcierto());
                                    persona.setAciertos(persona.getAciertos() + 1);
                                    aciertosPorRonda += 1;
                                }
                            }
                        }
                         if (aciertosPorRonda == ronda.getPartidos().size()){
                            persona.setRondasAcertadas(persona.getRondasAcertadas() + 1);
                            persona.setPuntos(persona.getPuntos() + puntos.getPuntosExtra());
                             aciertosPorRonda = 0;
                        }
                    }
                }
                aciertosPorRonda = 0;
                 System.out.println(persona.getNombre() + ": "
                        + "\n- Puntos: " + persona.getPuntos()
                        + "\n- Aciertos: "
                        + persona.getAciertos()
                        + "\n- Rondas acertadas: " + persona.getRondasAcertadas()
                        + "\n- Puntos extra por rondas acertadas: " + persona.getRondasAcertadas() * puntos.getPuntosExtra()
                );
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}