-- ===============================================
-- AGROHELPER - ESTRUTURA DO BANCO
-- PostgreSQL Schema para Sistema Agrícola
-- ===============================================

-- Não precisamos mais da extensão UUID
-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ===============================================
-- 1. TABELA DE USUÁRIOS
-- ===============================================

-- Sequência para IDs sequenciais de usuários
CREATE SEQUENCE user_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE users (
    id BIGINT PRIMARY KEY DEFAULT nextval('user_id_seq'),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===============================================
-- 2. TABELA DE PRODUTOS
-- ===============================================

-- Enum para categorias de produtos
CREATE TYPE product_category AS ENUM ('GRAINS', 'FRUITS', 'VEGETABLES', 'EQUIPMENT', 'SERVICES', 'INPUTS');

-- Sequência para IDs sequenciais de produtos
CREATE SEQUENCE product_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE products (
    id BIGINT PRIMARY KEY DEFAULT nextval('product_id_seq'),
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    category product_category NOT NULL,
    location VARCHAR(100),
    seller_name VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    image_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT positive_price CHECK (price > 0),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ===============================================
-- 3. ÍNDICES PARA PERFORMANCE
-- ===============================================

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_price ON products(price);
CREATE INDEX idx_products_created_at ON products(created_at);
CREATE INDEX idx_products_user_id ON products(user_id);