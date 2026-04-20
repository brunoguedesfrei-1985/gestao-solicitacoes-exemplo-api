-- init.sql: Exemplo de script de inicialização para PostgreSQL

CREATE TABLE IF NOT EXISTS cargo (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS usuario (
    id SERIAL PRIMARY KEY,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    cargo_id INTEGER REFERENCES cargo(id)
);

CREATE TABLE IF NOT EXISTS endereco (
    id SERIAL PRIMARY KEY,
    endereco VARCHAR(255) NOT NULL,
    cep VARCHAR(20) NOT NULL,
    usuario_id INTEGER NOT NULL REFERENCES usuario(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS solicitacao (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    categoria VARCHAR(100),
    status VARCHAR(50) NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT now(),
    usuario_atribuido_id INTEGER REFERENCES usuario(id),
    usuario_atendente_id INTEGER REFERENCES usuario(id)
);

INSERT INTO cargo (nome) VALUES ('gerente') ON CONFLICT DO NOTHING;
INSERT INTO cargo (nome) VALUES ('analista') ON CONFLICT DO NOTHING;
INSERT INTO cargo (nome) VALUES ('tecnico') ON CONFLICT DO NOTHING;