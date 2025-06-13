-- ===============================================
-- AGROHELPER - ESTRUTURA DO BANCO
-- PostgreSQL Schema para Sistema Agrícola
-- ===============================================

-- ===============================================
-- 1. TABELA DE USUÁRIOS
-- ===============================================

-- Sequência para IDs sequenciais de usuários
CREATE SEQUENCE user_id_seq START WITH 1 INCREMENT BY 1;

-- Enum para tipos de usuário
CREATE TYPE user_type AS ENUM ('BUYER', 'SELLER', 'ADMIN');

CREATE TABLE users (
    id BIGINT PRIMARY KEY DEFAULT nextval('user_id_seq'),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    user_type user_type NOT NULL DEFAULT 'BUYER',
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
    average_rating DECIMAL(3, 2) DEFAULT 0.00,
    review_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT positive_price CHECK (price > 0),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ===============================================
-- 3. TABELA DE AVALIAÇÕES
-- ===============================================

-- Sequência para IDs sequenciais de avaliações
CREATE SEQUENCE review_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE reviews (
    id BIGINT PRIMARY KEY DEFAULT nextval('review_id_seq'),
    product_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    rating INTEGER NOT NULL,
    comment TEXT,
    is_approved BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT valid_rating CHECK (rating BETWEEN 1 AND 5),
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_review FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ===============================================
-- 4. ÍNDICES PARA PERFORMANCE
-- ===============================================

-- Índices para usuários
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_user_type ON users(user_type);

-- Índices para produtos
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_price ON products(price);
CREATE INDEX idx_products_created_at ON products(created_at);
CREATE INDEX idx_products_user_id ON products(user_id);
CREATE INDEX idx_products_average_rating ON products(average_rating);

-- Índices para avaliações
CREATE INDEX idx_reviews_product_id ON reviews(product_id);
CREATE INDEX idx_reviews_user_id ON reviews(user_id);
CREATE INDEX idx_reviews_rating ON reviews(rating);
CREATE INDEX idx_reviews_is_approved ON reviews(is_approved);