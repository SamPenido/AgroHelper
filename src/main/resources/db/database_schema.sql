-- ===============================================
-- AGROHELPER - ESTRUTURA DO BANCO DE DADOS
-- PostgreSQL Schema para Sistema Agrícola
-- ===============================================
-- ===============================================
-- 1. USUÁRIOS E AUTENTICAÇÃO
-- ===============================================
-- Enum para tipos de usuário
CREATE TYPE user_type_enum AS ENUM ('FARMER', 'BUYER', 'TECHNICIAN', 'ADMIN');
-- Enum para status do usuário
CREATE TYPE user_status_enum AS ENUM ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'PENDING_VERIFICATION');
-- Tabela principal de usuários
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    user_type user_type_enum NOT NULL,
    status user_status_enum DEFAULT 'PENDING_VERIFICATION',
    phone VARCHAR(20),
    cpf_cnpj VARCHAR(18) UNIQUE,
    profile_image_url TEXT,
    email_verified BOOLEAN DEFAULT FALSE,
    phone_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP WITH TIME ZONE,
    -- Índices para performance
    CONSTRAINT valid_email CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);
-- Tokens para autenticação e verificação
CREATE TABLE user_tokens (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token_hash VARCHAR(255) NOT NULL,
    token_type VARCHAR(50) NOT NULL, -- 'JWT_REFRESH', 'EMAIL_VERIFICATION', 'PASSWORD_RESET'
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    used BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
-- ===============================================
-- 2. ENDEREÇOS E LOCALIZAÇÃO
-- ===============================================

CREATE TABLE addresses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    street_address VARCHAR(255) NOT NULL,
    neighborhood VARCHAR(100),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(50) NOT NULL,
    postal_code VARCHAR(10) NOT NULL,
    country VARCHAR(50) DEFAULT 'Brasil',
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    is_primary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ===============================================
-- 3. PROPRIEDADES AGRÍCOLAS
-- ===============================================

-- Enum para tipos de solo
CREATE TYPE soil_type_enum AS ENUM ('CLAYEY', 'SANDY', 'SILTY', 'LOAMY', 'PEAT', 'CHALK');

-- Propriedades rurais
CREATE TABLE properties (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    owner_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    total_area DECIMAL(10, 2) NOT NULL, -- Em hectares
    cultivated_area DECIMAL(10, 2),
    address_id UUID REFERENCES addresses(id),
    soil_type soil_type_enum,
    irrigation_system VARCHAR(100),
    altitude DECIMAL(8, 2), -- Em metros
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT positive_area CHECK (total_area > 0 AND cultivated_area >= 0),
    CONSTRAINT cultivated_within_total CHECK (cultivated_area <= total_area)
);

-- Imagens das propriedades
CREATE TABLE property_images (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    property_id UUID NOT NULL REFERENCES properties(id) ON DELETE CASCADE,
    image_url TEXT NOT NULL,
    caption VARCHAR(255),
    is_primary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ===============================================
-- 4. CULTURAS E PLANTAÇÕES
-- ===============================================

-- Enum para status da cultura
CREATE TYPE crop_status_enum AS ENUM ('PLANTED', 'GROWING', 'HARVESTED', 'FAILED', 'PLANNED');

-- Tipos de cultura (referência)
CREATE TABLE crop_types (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL UNIQUE,
    scientific_name VARCHAR(150),
    category VARCHAR(50), -- 'GRAINS', 'FRUITS', 'VEGETABLES', 'LEGUMES'
    growing_season_days INTEGER,
    optimal_temperature_min DECIMAL(4, 1),
    optimal_temperature_max DECIMAL(4, 1),
    water_requirements VARCHAR(50), -- 'LOW', 'MEDIUM', 'HIGH'
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Culturas específicas em propriedades
CREATE TABLE crops (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    property_id UUID NOT NULL REFERENCES properties(id) ON DELETE CASCADE,
    crop_type_id UUID NOT NULL REFERENCES crop_types(id),
    name VARCHAR(255) NOT NULL, -- Nome dado pelo agricultor
    planted_area DECIMAL(10, 2) NOT NULL,
    planting_date DATE,
    expected_harvest_date DATE,
    actual_harvest_date DATE,
    status crop_status_enum DEFAULT 'PLANNED',
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT positive_planted_area CHECK (planted_area > 0)
);

-- ===============================================
-- 5. MARKETPLACE - PRODUTOS
-- ===============================================

-- Enum para categoria de produtos
CREATE TYPE product_category_enum AS ENUM ('GRAINS', 'FRUITS', 'VEGETABLES', 'EQUIPMENT', 'SERVICES', 'INPUTS', 'LIVESTOCK', 'OTHER');

-- Enum para status do produto
CREATE TYPE product_status_enum AS ENUM ('ACTIVE', 'SOLD', 'INACTIVE', 'PENDING_APPROVAL');

-- Enum para condição do produto
CREATE TYPE product_condition_enum AS ENUM ('NEW', 'USED', 'REFURBISHED');

-- Produtos no marketplace
CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    seller_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    category product_category_enum NOT NULL,
    price DECIMAL(12, 2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'BRL',
    unit VARCHAR(50), -- 'KG', 'SACA', 'UNIDADE', 'HECTARE', 'HORA'
    quantity_available INTEGER,
    condition_type product_condition_enum DEFAULT 'NEW',
    status product_status_enum DEFAULT 'PENDING_APPROVAL',
    location_id UUID REFERENCES addresses(id),
    harvest_date DATE, -- Para produtos agrícolas
    organic_certified BOOLEAN DEFAULT FALSE,
    tags TEXT[], -- Array de tags para busca
    views_count INTEGER DEFAULT 0,
    favorites_count INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP WITH TIME ZONE,
    
    CONSTRAINT positive_price CHECK (price > 0),
    CONSTRAINT positive_quantity CHECK (quantity_available IS NULL OR quantity_available >= 0)
);

-- Imagens dos produtos
CREATE TABLE product_images (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    image_url TEXT NOT NULL,
    alt_text VARCHAR(255),
    display_order INTEGER DEFAULT 0,
    is_primary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Favoritos dos usuários
CREATE TABLE user_favorites (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE(user_id, product_id)
);

-- ===============================================
-- 6. TRANSAÇÕES E NEGOCIAÇÕES
-- ===============================================

-- Enum para status da transação
CREATE TYPE transaction_status_enum AS ENUM ('PENDING', 'ACCEPTED', 'REJECTED', 'COMPLETED', 'CANCELLED', 'DISPUTED');

-- Enum para tipo de transação
CREATE TYPE transaction_type_enum AS ENUM ('PURCHASE', 'SERVICE', 'RENTAL');

-- Transações entre usuários
CREATE TABLE transactions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    product_id UUID NOT NULL REFERENCES products(id),
    buyer_id UUID NOT NULL REFERENCES users(id),
    seller_id UUID NOT NULL REFERENCES users(id),
    transaction_type transaction_type_enum DEFAULT 'PURCHASE',
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(12, 2) NOT NULL,
    total_amount DECIMAL(12, 2) NOT NULL,
    status transaction_status_enum DEFAULT 'PENDING',
    proposed_delivery_date DATE,
    actual_delivery_date DATE,
    payment_method VARCHAR(50),
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP WITH TIME ZONE,
    
    CONSTRAINT positive_values CHECK (quantity > 0 AND unit_price > 0 AND total_amount > 0),
    CONSTRAINT different_parties CHECK (buyer_id != seller_id)
);

-- Mensagens da negociação
CREATE TABLE transaction_messages (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    transaction_id UUID NOT NULL REFERENCES transactions(id) ON DELETE CASCADE,
    sender_id UUID NOT NULL REFERENCES users(id),
    message TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ===============================================
-- 7. AVALIAÇÕES E REVIEWS
-- ===============================================

-- Avaliações de usuários/produtos
CREATE TABLE reviews (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    transaction_id UUID NOT NULL REFERENCES transactions(id),
    reviewer_id UUID NOT NULL REFERENCES users(id),
    reviewed_user_id UUID NOT NULL REFERENCES users(id),
    product_id UUID REFERENCES products(id),
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE(transaction_id, reviewer_id)
);

-- ===============================================
-- 8. PREVISÃO CLIMÁTICA E ALERTAS
-- ===============================================

-- Dados climáticos históricos e previsões
CREATE TABLE weather_data (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    date_time TIMESTAMP WITH TIME ZONE NOT NULL,
    temperature DECIMAL(5, 2),
    humidity DECIMAL(5, 2),
    precipitation DECIMAL(6, 2), -- mm
    wind_speed DECIMAL(5, 2), -- m/s
    wind_direction INTEGER, -- graus
    pressure DECIMAL(7, 2), -- hPa
    uv_index DECIMAL(3, 1),
    weather_condition VARCHAR(100),
    is_forecast BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tipos de alertas
CREATE TABLE alert_types (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    severity_level INTEGER CHECK (severity_level >= 1 AND severity_level <= 5),
    icon VARCHAR(50),
    color VARCHAR(7), -- Hex color
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Alertas para usuários
CREATE TABLE alerts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    property_id UUID REFERENCES properties(id) ON DELETE CASCADE,
    alert_type_id UUID NOT NULL REFERENCES alert_types(id),
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    severity INTEGER CHECK (severity >= 1 AND severity <= 5),
    is_read BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    expires_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP WITH TIME ZONE
);

-- ===============================================
-- 9. DIAGNÓSTICO IA - PRAGAS E DOENÇAS
-- ===============================================

-- Tipos de pragas/doenças conhecidas
CREATE TABLE pest_disease_types (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(200) NOT NULL,
    scientific_name VARCHAR(250),
    type VARCHAR(50), -- 'PEST', 'DISEASE', 'DEFICIENCY'
    affected_crops TEXT[], -- Array de crops que podem ser afetados
    description TEXT,
    treatment_recommendations TEXT,
    prevention_tips TEXT,
    severity_level INTEGER CHECK (severity_level >= 1 AND severity_level <= 5),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Diagnósticos realizados
CREATE TABLE crop_diagnoses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id),
    crop_id UUID REFERENCES crops(id),
    property_id UUID REFERENCES properties(id),
    original_image_url TEXT NOT NULL,
    processed_image_url TEXT,
    diagnosis_confidence DECIMAL(5, 4), -- 0.0000 a 1.0000
    identified_pest_disease_id UUID REFERENCES pest_disease_types(id),
    ai_model_version VARCHAR(50),
    symptoms_description TEXT,
    recommended_actions TEXT,
    urgency_level INTEGER CHECK (urgency_level >= 1 AND urgency_level <= 5),
    verified_by_expert BOOLEAN DEFAULT FALSE,
    expert_notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ===============================================
-- 10. CHATBOT E SUPORTE
-- ===============================================

-- Sessões de chat
CREATE TABLE chat_sessions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id),
    session_start TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    session_end TIMESTAMP WITH TIME ZONE,
    total_messages INTEGER DEFAULT 0,
    satisfaction_rating INTEGER CHECK (satisfaction_rating >= 1 AND satisfaction_rating <= 5),
    feedback TEXT
);

-- Mensagens do chat
CREATE TABLE chat_messages (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID NOT NULL REFERENCES chat_sessions(id) ON DELETE CASCADE,
    sender_type VARCHAR(20) NOT NULL, -- 'USER', 'BOT', 'HUMAN_AGENT'
    message_text TEXT NOT NULL,
    intent VARCHAR(100), -- Intenção identificada pela IA
    confidence_score DECIMAL(5, 4),
    response_time_ms INTEGER,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ===============================================
-- 11. PRODUÇÃO E CUSTOS
-- ===============================================

-- Registros de produção
CREATE TABLE production_records (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    crop_id UUID NOT NULL REFERENCES crops(id) ON DELETE CASCADE,
    harvest_date DATE NOT NULL,
    quantity_harvested DECIMAL(12, 3) NOT NULL,
    unit VARCHAR(50) NOT NULL, -- 'KG', 'TONS', 'SACKS'
    quality_grade VARCHAR(50),
    price_per_unit DECIMAL(10, 2),
    total_revenue DECIMAL(12, 2),
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT positive_quantity CHECK (quantity_harvested > 0)
);

-- Categorias de custos
CREATE TABLE cost_categories (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    parent_category_id UUID REFERENCES cost_categories(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Registros de custos
CREATE TABLE cost_records (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    property_id UUID REFERENCES properties(id),
    crop_id UUID REFERENCES crops(id),
    category_id UUID NOT NULL REFERENCES cost_categories(id),
    description TEXT NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'BRL',
    date_incurred DATE NOT NULL,
    supplier VARCHAR(255),
    receipt_url TEXT,
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT positive_amount CHECK (amount > 0)
);

-- ===============================================
-- 12. NOTIFICAÇÕES
-- ===============================================

-- Enum para tipos de notificação
CREATE TYPE notification_type_enum AS ENUM ('ALERT', 'MESSAGE', 'TRANSACTION', 'SYSTEM', 'MARKETING');

-- Notificações do sistema
CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    type notification_type_enum NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    action_url TEXT,
    is_read BOOLEAN DEFAULT FALSE,
    is_email_sent BOOLEAN DEFAULT FALSE,
    is_push_sent BOOLEAN DEFAULT FALSE,
    priority INTEGER DEFAULT 3 CHECK (priority >= 1 AND priority <= 5),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP WITH TIME ZONE,
    expires_at TIMESTAMP WITH TIME ZONE
);

-- ===============================================
-- 13. ÍNDICES PARA PERFORMANCE
-- ===============================================

-- Índices para usuários
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_type_status ON users(user_type, status);
CREATE INDEX idx_users_created_at ON users(created_at);

-- Índices para produtos
CREATE INDEX idx_products_seller ON products(seller_id);
CREATE INDEX idx_products_category_status ON products(category, status);
CREATE INDEX idx_products_price ON products(price);
CREATE INDEX idx_products_created_at ON products(created_at);
CREATE INDEX idx_products_location ON products(location_id);
CREATE INDEX idx_products_tags ON products USING GIN(tags);

-- Índices para propriedades
CREATE INDEX idx_properties_owner ON properties(owner_id);
CREATE INDEX idx_properties_location ON properties(address_id);

-- Índices para transações
CREATE INDEX idx_transactions_buyer ON transactions(buyer_id);
CREATE INDEX idx_transactions_seller ON transactions(seller_id);
CREATE INDEX idx_transactions_status ON transactions(status);
CREATE INDEX idx_transactions_created_at ON transactions(created_at);

-- Índices para alertas e notificações
CREATE INDEX idx_alerts_user_active ON alerts(user_id, is_active);
CREATE INDEX idx_notifications_user_read ON notifications(user_id, is_read);

-- Índices para dados climáticos
CREATE INDEX idx_weather_coordinates_date ON weather_data(latitude, longitude, date_time);
CREATE INDEX idx_weather_forecast ON weather_data(is_forecast, date_time);

-- ===============================================
-- 14. TRIGGERS PARA ATUALIZAÇÕES AUTOMÁTICAS
-- ===============================================

-- Função para atualizar updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Triggers para updated_at
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();

CREATE TRIGGER update_properties_updated_at BEFORE UPDATE ON properties
    FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();

CREATE TRIGGER update_crops_updated_at BEFORE UPDATE ON crops
    FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();

CREATE TRIGGER update_products_updated_at BEFORE UPDATE ON products
    FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();

CREATE TRIGGER update_transactions_updated_at BEFORE UPDATE ON transactions
    FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();

-- ===============================================
-- 15. DADOS INICIAIS (SEEDS)
-- ===============================================

-- Inserir tipos de cultura comuns
INSERT INTO crop_types (name, scientific_name, category, growing_season_days, optimal_temperature_min, optimal_temperature_max, water_requirements) VALUES
('Milho', 'Zea mays', 'GRAINS', 120, 18.0, 30.0, 'MEDIUM'),
('Soja', 'Glycine max', 'GRAINS', 100, 20.0, 30.0, 'MEDIUM'),
('Feijão', 'Phaseolus vulgaris', 'LEGUMES', 90, 18.0, 25.0, 'MEDIUM'),
('Arroz', 'Oryza sativa', 'GRAINS', 120, 20.0, 35.0, 'HIGH'),
('Café', 'Coffea arabica', 'OTHER', 365, 18.0, 22.0, 'MEDIUM'),
('Cana-de-açúcar', 'Saccharum officinarum', 'OTHER', 365, 20.0, 30.0, 'HIGH'),
('Tomate', 'Solanum lycopersicum', 'VEGETABLES', 90, 18.0, 25.0, 'MEDIUM'),
('Laranja', 'Citrus sinensis', 'FRUITS', 365, 15.0, 30.0, 'MEDIUM');

-- Inserir categorias de custos
INSERT INTO cost_categories (name, description) VALUES
('Sementes e Mudas', 'Custos com sementes, mudas e material de plantio'),
('Fertilizantes', 'Adubos químicos e orgânicos'),
('Defensivos', 'Pesticidas, herbicidas e fungicidas'),
('Mão de Obra', 'Custos com trabalhadores e serviços'),
('Combustível', 'Diesel, gasolina e outros combustíveis'),
('Manutenção', 'Manutenção de equipamentos e instalações'),
('Energia', 'Energia elétrica e outras fontes'),
('Água', 'Custos com irrigação e abastecimento');

-- Inserir tipos de alertas
INSERT INTO alert_types (name, description, severity_level, icon, color) VALUES
('Chuva Intensa', 'Previsão de precipitação acima de 50mm', 4, '🌧️', '#3498db'),
('Geada', 'Risco de temperatura abaixo de 2°C', 5, '❄️', '#e74c3c'),
('Seca Prolongada', 'Período sem chuva superior a 15 dias', 4, '☀️', '#f39c12'),
('Vento Forte', 'Ventos superiores a 60 km/h', 3, '💨', '#95a5a6'),
('Pragas Detectadas', 'Identificação de pragas nas culturas', 4, '🐛', '#e67e22'),
('Fertilização Pendente', 'Lembrete para aplicação de fertilizantes', 2, '🌱', '#27ae60');

-- ===============================================
-- COMENTÁRIOS FINAIS
-- ===============================================

-- Esta estrutura suporta:
-- ✅ Sistema completo de usuários e autenticação
-- ✅ Gestão de propriedades e culturas
-- ✅ Marketplace robusto com transações
-- ✅ Sistema de avaliações e reviews
-- ✅ Alertas climáticos inteligentes
-- ✅ Diagnóstico IA de pragas
-- ✅ Chatbot com histórico
-- ✅ Controle de produção e custos
-- ✅ Sistema de notificações
-- ✅ Performance otimizada com índices
-- ✅ Triggers automáticos
-- ✅ Dados iniciais para desenvolvimento

-- Total: 24 tabelas principais + enums + índices + triggers