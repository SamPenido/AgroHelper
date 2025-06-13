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

-- Inserir usuários iniciais
-- Nota: Em um ambiente de produção, use senhas encriptadas
INSERT INTO users (email, password, full_name, user_type) VALUES 
('admin@agrohelper.com', 'admin123', 'Administrador do Sistema', 'ADMIN'),
('vendedor@agrohelper.com', 'vendedor123', 'Vendedor Demonstração', 'SELLER'),
('comprador@agrohelper.com', 'comprador123', 'Comprador Demonstração', 'BUYER');

-- Inserir produtos de demonstração
INSERT INTO products (title, description, price, category, location, seller_name, user_id, image_url) 
VALUES 
('Tomates Orgânicos', 'Tomates frescos cultivados sem agrotóxicos', 5.99, 'VEGETABLES', 'São Paulo, SP', 'Vendedor Demonstração', 2, 'https://example.com/tomatoes.jpg'),
('Trator Agrícola', 'Trator seminovo em ótimo estado', 65000.00, 'EQUIPMENT', 'Belo Horizonte, MG', 'Vendedor Demonstração', 2, 'https://example.com/tractor.jpg'),
('Fertilizante Natural', 'Fertilizante 100% natural para hortaliças', 45.50, 'INPUTS', 'Rio de Janeiro, RJ', 'Vendedor Demonstração', 2, 'https://example.com/fertilizer.jpg');