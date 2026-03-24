CREATE TABLE clientes (
                          id SERIAL PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          apellidos VARCHAR(150) NOT NULL,
                          dni VARCHAR(20) UNIQUE NOT NULL,
                          email VARCHAR(150) UNIQUE NOT NULL,
                          telefono VARCHAR(20) UNIQUE NOT NULL,
                          fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cuentas (
                         id SERIAL PRIMARY KEY,
                         numero_cuenta VARCHAR(34) UNIQUE NOT NULL,
                         cliente_id INT NOT NULL,
                         saldo NUMERIC(15,2) NOT NULL DEFAULT 0.00 CHECK (saldo >= 0),
                         fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

CREATE TABLE movimientos (
                             id SERIAL PRIMARY KEY,
                             cuenta_id INT NOT NULL,
                             tipo VARCHAR(50) NOT NULL,
                             cantidad NUMERIC(15,2) NOT NULL CHECK (cantidad > 0),
                             fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (cuenta_id) REFERENCES cuentas(id)
);