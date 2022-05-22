-- Este es el script que usé para crear la bd
CREATE TABLE Usuario(
    email varchar(50),
    nombre varchar(50),
    contrasenia blob,
    PRIMARY KEY (email)
);
CREATE TABLE Lugar (
    longitud float,
    latitud float,
    altitud float,
    enlace varchar(300),
    nombre varchar(100),
    fecha date,
    email_usuario varchar(50),
    PRIMARY KEY (longitud,latitud,altitud),
    FOREIGN KEY (email_usuario) REFERENCES Usuario(email)
    );
CREATE TABLE Ruta (
    nombre varchar(50),
    email_usuario varchar(50),
    PRIMARY KEY (nombre,email_usuario),
    FOREIGN KEY (email_usuario) REFERENCES Usuario(email)
    );
CREATE TABLE Lugar_Ruta (
    longitud float,
    latitud float,
    altitud float,
    nombre_ruta varchar(100),
    PRIMARY KEY (longitud,latitud,altitud,nombre_ruta),
    CONSTRAINT fk_lugar_ruta FOREIGN KEY (longitud,latitud,altitud) REFERENCES Lugar(longitud,latitud,altitud),
    CONSTRAINT fk_ruta_lugar FOREIGN KEY (nombre_ruta) REFERENCES Ruta(nombre)
    );
CREATE TABLE Imagen (
    id int AUTO_INCREMENT,
    imagen longblob,
    longitud float,
    latitud float,
    altitud float,
    PRIMARY KEY (id),
    CONSTRAINT fk_lugar_imagen FOREIGN KEY (longitud,latitud,altitud) REFERENCES Lugar(longitud,latitud,altitud)
    );
CREATE TABLE Categoria (
    nombre varchar(50),
    descripcion varchar(150),
    PRIMARY KEY (nombre)
    );
CREATE TABLE Lugar_Categoria (
    longitud float,
    latitud float,
    altitud float,
    nombre_categoria varchar(50),
    PRIMARY KEY (longitud,latitud,altitud,nombre_categoria),
    CONSTRAINT fk_lugar_categoria FOREIGN KEY (longitud,latitud,altitud) REFERENCES Lugar(longitud,latitud,altitud),
    CONSTRAINT fk_categoria_lugar FOREIGN KEY (nombre_categoria) REFERENCES Categoria(nombre)
    );
CREATE TABLE Ruta_Categoria (
    nombre_ruta varchar(50),
    nombre_categoria varchar(50),
    PRIMARY KEY (nombre_ruta,nombre_categoria),
    CONSTRAINT fk_ruta_categoria FOREIGN KEY (nombre_ruta) REFERENCES Ruta(nombre),
    CONSTRAINT fk_categoria_ruta FOREIGN KEY (nombre_categoria) REFERENCES Categoria(nombre)    
    );