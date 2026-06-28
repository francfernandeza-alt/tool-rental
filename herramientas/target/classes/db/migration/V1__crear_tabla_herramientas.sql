CREATE TABLE marca(
    id_marca INT AUTO_INCREMENT PRIMARY KEY,
    nombre_marca VARCHAR(100) NOT NULL,
    descripcion_marca VARCHAR(1000)
);

CREATE TABLE mantencion (
    id_mantencion INT AUTO_INCREMENT PRIMARY KEY,
    fecha_ultima_mantencion DATE NOT NULL,
    vigencia_meses INT NOT NULL,
    descripcion_mantencion VARCHAR(300) NOT NULL,
    estado_mantencion VARCHAR(50)
);

CREATE TABLE material(
    id_material INT AUTO_INCREMENT PRIMARY KEY,
    nombre_material VARCHAR(100) NOT NULL,
    descripcion_material VARCHAR(255) NOT NULL
);

CREATE TABLE tipoherramienta(
    id_tipo_herramienta INT AUTO_INCREMENT PRIMARY KEY,
    nombre_tipo_herramienta VARCHAR(100) NOT NULL,
    descripcion_tipo_herramienta VARCHAR(255) NOT NULL
);

CREATE TABLE herramienta (
    id_herramienta INT AUTO_INCREMENT PRIMARY KEY,
    nombre_herramienta VARCHAR(100) NOT NULL,
    descripcion_herramienta VARCHAR(500) NOT NULL,
    estado_herramienta VARCHAR(100) NOT NULL,
    cantidad_total INT NOT NULL,
    cantidad_disponible INT NOT NULL,
    fecha_actualizacion DATE NOT NULL,
    id_marca INT NOT NULL,
    CONSTRAINT fk_marca FOREIGN KEY (id_marca) REFERENCES marca(id_marca)
);

CREATE TABLE mantenciones (
    id_mantenciones INT AUTO_INCREMENT PRIMARY KEY,
    id_herramienta INT NOT NULL,
    CONSTRAINT fk_herramienta FOREIGN KEY (id_herramienta) REFERENCES herramienta(id_herramienta),
    id_mantencion INT NOT NULL,
    CONSTRAINT fk_mantencion FOREIGN KEY (id_mantencion) REFERENCES mantencion(id_mantencion)
);

CREATE TABLE materiales (
    id_materiales INT AUTO_INCREMENT PRIMARY KEY,
    id_herramienta INT NOT NULL,
    CONSTRAINT fk_herramienta FOREIGN KEY (id_herramienta) REFERENCES herramienta(id_herramienta),
    id_material INT NOT NULL,
    CONSTRAINT fk_material FOREIGN KEY (id_material) REFERENCES material(id_material)
);

CREATE TABLE tiposherramientas (
    id_tipos_herramientas INT AUTO_INCREMENT PRIMARY KEY,
    id_herramienta INT NOT NULL,
    CONSTRAINT fk_herramienta FOREIGN KEY (id_herramienta) REFERENCES herramienta(id_herramienta),
    id_tipo_herramienta INT NOT NULL,
    CONSTRAINT fk_tipo_herramienta FOREIGN KEY (id_tipo_herramienta) REFERENCES tipo_herramienta(id_tipo_herramienta)
);