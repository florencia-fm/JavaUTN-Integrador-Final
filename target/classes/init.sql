/* Creando las tablas Pronosticos y Resultados */

CREATE TABLE Pronosticos (
idPronostico INTEGER NOT NULL AUTO_INCREMENT,
participante VARCHAR(25),
equipo1 VARCHAR(25),
resultado VARCHAR(10),
equipo2 VARCHAR(25),
PRIMARY KEY ( idPronostico ));
TRUNCATE TABLE Pronosticos;

CREATE TABLE Resultados (
idResultado INTEGER not NULL AUTO_INCREMENT,
ronda INTEGER,
equipo1 VARCHAR(25),
cantGoles1 INTEGER,
cantGoles2 INTEGER,
equipo2 VARCHAR(25),
PRIMARY KEY ( idResultado ));
TRUNCATE TABLE Resultados;


/*Insertando datos en la tabla Pronosticos*/

INSERT INTO Pronosticos (participante, equipo1, resultado, equipo2) VALUES
('Mariana', 'Argentina', 'Gana 2', 'Arabia Saudita'),
('Mariana', 'Polonia', 'Empata', 'México'),
('Mariana', 'Argentina', 'Gana 1', 'México'),
('Mariana', 'Arabia Saudita', 'Gana 2', 'Polonia'),
('Pedro', 'Argentina', 'Gana 1', 'Arabia Saudita'),
('Pedro', 'Polonia', 'Gana 2', 'México'),
('Pedro', 'Argentina', 'Gana 1', 'México'),
('Pedro', 'Arabia Saudita', 'Empata', 'Polonia');
/*Si quieren agregar mas datos, simplemente sigan agregando tuplas antes del ';' */


/*Insertando datos en la tabla Resultados*/

INSERT INTO Resultados (ronda, equipo1, cantGoles1, cantGoles2, equipo2) VALUES
(1, 'Argentina', 1, 2, 'Arabia Saudita'),
(1, 'Polonia', 0, 0 , 'México'),
(1, 'Argentina', 2, 0, 'México'),
(1, 'Arabia Saudita', 0, 2, 'Polonia');
/*Si quieren agregar mas datos, simplemente sigan agregando tuplas antes del ';' */
