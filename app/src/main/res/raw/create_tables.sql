-- Este es el script que usé para crear la bd
CREATE TABLE usuario(
    email varchar(50),
    nombre varchar(50),
    contrasenia blob,
    pregunta_seguridad varchar(100),
    respuesta varchar(50),
    PRIMARY KEY (email)
);
CREATE TABLE lugar (
    longitud varchar(100),
    latitud varchar(100),
    altitud varchar(100),
    enlace varchar(300),
    nombre varchar(100),
    PRIMARY KEY (longitud,latitud,altitud,enlace)
    );
CREATE TABLE lugar_usuario(
    longitud varchar(100),
    latitud varchar(100),
    altitud varchar(100),
    enlace varchar(300),
    email_usuario varchar(50),
    PRIMARY KEY(longitud,latitud,altitud,enlace,email_usuario),
    CONSTRAINT fk_lugar_usuario FOREIGN KEY (longitud,latitud,altitud,enlace) REFERENCES lugar(longitud,latitud,altitud,enlace) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_usuario_lugar FOREIGN KEY (email_usuario) REFERENCES usuario(email) ON DELETE CASCADE ON UPDATE CASCADE
    );
CREATE TABLE ruta (
    nombre varchar(50),
    email_usuario varchar(50),
    PRIMARY KEY (nombre,email_usuario),
    FOREIGN KEY (email_usuario) REFERENCES usuario(email) ON DELETE NO ACTION ON UPDATE CASCADE
    );
CREATE TABLE lugar_ruta (
    longitud varchar(100),
    latitud varchar(100),
    altitud varchar(100),
    enlace varchar(300),
    nombre_ruta varchar(100),
    email_ruta varchar(50),
    PRIMARY KEY (longitud,latitud,altitud,enlace,nombre_ruta,email_ruta),
    CONSTRAINT fk_lugar_ruta FOREIGN KEY (longitud,latitud,altitud,enlace) REFERENCES lugar(longitud,latitud,altitud,enlace) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_ruta_lugar FOREIGN KEY (nombre_ruta,email_ruta) REFERENCES ruta(nombre,email_usuario) ON DELETE CASCADE ON UPDATE CASCADE
    );
CREATE TABLE imagen (
    id int AUTO_INCREMENT,
    imagen longblob,
    longitud varchar(100),
    latitud varchar(100),
    altitud varchar(100),
    enlace varchar(300),
    PRIMARY KEY (id),
    CONSTRAINT fk_lugar_imagen FOREIGN KEY (longitud,latitud,altitud,enlace) REFERENCES lugar(longitud,latitud,altitud,enlace) ON DELETE CASCADE ON UPDATE CASCADE
    );
CREATE TABLE categoria (
    nombre varchar(50),
    descripcion varchar(150),
    PRIMARY KEY (nombre)
    );
CREATE TABLE lugar_categoria (
    longitud varchar(100),
    latitud varchar(100),
    altitud varchar(100),
    enlace varchar(300),
    nombre_categoria varchar(50),
    PRIMARY KEY (longitud,latitud,altitud,enlace,nombre_categoria),
    CONSTRAINT fk_lugar_categoria FOREIGN KEY (longitud,latitud,altitud,enlace) REFERENCES lugar(longitud,latitud,altitud,enlace) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_categoria_lugar FOREIGN KEY (nombre_categoria) REFERENCES categoria(nombre) ON DELETE CASCADE ON UPDATE CASCADE
    );
CREATE TABLE ruta_categoria (
    nombre_ruta varchar(50),
    email_ruta varchar(50),
    nombre_categoria varchar(50),
    PRIMARY KEY (nombre_ruta,email_ruta,nombre_categoria),
    CONSTRAINT fk_ruta_categoria FOREIGN KEY (nombre_ruta,email_ruta) REFERENCES ruta(nombre,email_usuario) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_categoria_ruta FOREIGN KEY (nombre_categoria) REFERENCES categoria(nombre) ON DELETE CASCADE ON UPDATE CASCADE
    );
CREATE TABLE lugares_compartidos (
    usuario_emisor varchar(50),
    latitud varchar(100),
    longitud varchar(100),
    altitud varchar(100),
    enlace varchar(300),
    usuario_receptor varchar(50),
    PRIMARY KEY (usuario_emisor,latitud,longitud,altitud,enlace,usuario_receptor),
    CONSTRAINT fk_usuario_emisor FOREIGN KEY (usuario_emisor) REFERENCES usuario(email) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_usuario_receptor FOREIGN KEY (usuario_receptor) REFERENCES usuario(email) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_lugar FOREIGN KEY (longitud,latitud,altitud,enlace) REFERENCES lugar(longitud,latitud,altitud,enlace) ON DELETE CASCADE ON UPDATE CASCADE
    );
CREATE TABLE rutas_compartidas (
    usuario_emisor varchar(50),
    nombre_ruta varchar(50),
    email_ruta varchar(50),
    usuario_receptor varchar(50),
    PRIMARY KEY (usuario_emisor,nombre_ruta,email_ruta,usuario_receptor),
    CONSTRAINT fk_usuario_emisor_ruta FOREIGN KEY (usuario_emisor) REFERENCES usuario(email) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_usuario_receptor_ruta FOREIGN KEY (usuario_receptor) REFERENCES usuario(email) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_ruta FOREIGN KEY (nombre_ruta,email_ruta) REFERENCES ruta(nombre,email_usuario) ON DELETE CASCADE ON UPDATE CASCADE
    );