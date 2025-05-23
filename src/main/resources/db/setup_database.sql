-- Script para configuração inicial do banco AgroHelper
-- Execute este script conectado como usuário postgres

-- Criar o banco de dados
CREATE DATABASE agrohelper_db
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'pt_BR.UTF-8'
    LC_CTYPE = 'pt_BR.UTF-8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Conectar ao banco criado
\c agrohelper_db

-- Criar usuário para a aplicação
CREATE USER agrohelper_user WITH ENCRYPTED PASSWORD 'agrohelper123';

-- Conceder permissões
GRANT ALL PRIVILEGES ON DATABASE agrohelper_db TO agrohelper_user;
GRANT ALL ON SCHEMA public TO agrohelper_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO agrohelper_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO agrohelper_user;