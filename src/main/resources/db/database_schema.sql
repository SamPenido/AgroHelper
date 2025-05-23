-- ===============================================
-- AGROHELPER - ESTRUTURA DO BANCO
-- PostgreSQL Schema para Sistema Agrícola
-- ===============================================

-- Extensão para UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ===============================================
-- 1. TABELA DE USUÁRIOS
-- ===============================================

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
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

CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    category product_category NOT NULL,
    location VARCHAR(100),
    seller_name VARCHAR(255) NOT NULL,
    image_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT positive_price CHECK (price > 0)
);

-- ===============================================
-- 3. ÍNDICES PARA PERFORMANCE
-- ===============================================

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_price ON products(price);
CREATE INDEX idx_products_created_at ON products(created_at);