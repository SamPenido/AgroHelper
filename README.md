# 🌱 AgroHelper - Plataforma Inteligente para Agricultura 🚜  

O **AgroHelper** é um sistema web desenvolvido para auxiliar pequenos e médios agricultores na **gestão de suas propriedades**, fornecendo **previsões climáticas, diagnóstico de pragas por IA, um chatbot agrícola e um marketplace para compra e venda de produtos agrícolas**.

---

## 📌 Tecnologias Utilizadas  

### 🖥️ **Back-end**  
- Java (Spring Boot)  
- Spring Security + JWT (Autenticação)  

### 🌍 **Front-end**  
- HTML, CSS, JavaScript  

### 📊 **Banco de Dados**  
- PostgreSQL  

### 🤖 **Inteligência Artificial & Automação**  
- Python (Análise de Imagens e Previsões Climáticas)  
- Chatbot para suporte agrícola  

### ☁️ **Infraestrutura & Hospedagem**  
- AWS ou Heroku  

---

## 🚀 Funcionalidades Principais  
✅ **Cadastro/Login** de usuários (Agricultores, Técnicos, Compradores)  
✅ **Gestão de Propriedades Agrícolas** (Registro de terrenos e culturas)  
✅ **Previsão Climática Inteligente**  
✅ **Diagnóstico de Pragas via IA** (Análise de imagens)  
✅ **Chatbot para suporte agrícola**  
✅ **Marketplace** para compra e venda de produtos agrícolas  
✅ **Relatórios de Produção e Custos**  
✅ **Alertas Inteligentes** (Notificações sobre clima e pragas)  

---

## 📂 Estrutura do Projeto  

### 📌 Backend (Java + Spring Boot)
```
agrohelper-backend/
│── src/
│   ├── main/java/com/agrohelper/
│   │   ├── controllers/    # Controladores REST
│   │   ├── models/         # Modelos das entidades do banco de dados
│   │   ├── repositories/   # Interfaces para interação com o banco de dados
│   │   ├── services/       # Lógica de negócio
│   ├── resources/
│   │   ├── application.properties  # Configurações do banco de dados
│── pom.xml    # Dependências do Maven
│── Dockerfile # Configuração para containerização
│── README.md  # Documentação do projeto
```

### 📌 Front-end (HTML, CSS e JavaScript)
```
agrohelper-frontend/
│── assets/          # Arquivos estáticos (imagens, ícones)
│── css/             # Estilos CSS
│   ├── style.css    # Estilo principal
│── js/              # Scripts JavaScript
│   ├── main.js      # Lógica principal da aplicação
│   ├── api.js       # Conexão com backend (fetch API)
│   ├── auth.js      # Controle de login e autenticação
│── pages/           # Páginas individuais
│   ├── index.html   # Página inicial
│   ├── login.html   # Tela de login
│   ├── dashboard.html  # Área do usuário
│   ├── marketplace.html # Tela de compra e venda
│   ├── clima.html   # Previsão do tempo
│   ├── pragas.html  # Diagnóstico de pragas
│── index.html       # Estrutura principal do site
│── README.md        # Documentação do frontend
```

---

## 👨‍💻 Integrantes  
🔹 **Artur Rizzi Martinho**  
🔹 **Erick Lima Hardeman**  
🔹 **Gabriel Drumond Franklin de Miranda e Rezende**  
🔹 **Samuel Penido**  

---

## 🔍 Detalhes de Implementação Técnica

### **1️⃣ Previsão Climática Inteligente** 🌦️
#### ✅ **Como Fazer de Forma Simples?**
* Utilizar APIs de previsão do tempo que já fornecem dados meteorológicos detalhados.
* Podemos integrar modelos de **aprendizado de máquina** apenas se precisarmos de previsões personalizadas.

#### 🔧 **APIs Recomendadas:**
* **OpenWeatherMap API** → Dados meteorológicos e previsões climáticas.
* **Weatherstack API** → Informações meteorológicas em tempo real.
* **NOAA API (EUA)** → Dados climáticos históricos e previsões.

#### 🚀 **Passos:**
1. Criar uma conta em uma dessas APIs e obter a chave de acesso.
2. Fazer chamadas à API para obter previsão do tempo com base na localização da propriedade.
3. Exibir os dados na interface do usuário.

### **2️⃣ Diagnóstico de Pragas e Doenças via IA e Processamento de Imagens** 🐛
#### ✅ **Como Fazer de Forma Simples?**
* Utilizar um **modelo pré-treinado de IA** para identificar pragas em imagens.
* Podemos usar **APIs de visão computacional** que já fazem análise de imagens.

#### 🔧 **APIs Recomendadas:**
* **Google Cloud Vision API** → Analisa imagens e pode ser treinada para detectar pragas.
* **Microsoft Azure Custom Vision** → Permite treinar um modelo específico para diagnóstico agrícola.
* **Plant.id API** → Especializada na identificação de doenças em plantas.

#### 🚀 **Passos:**
1. Capturar imagens das folhas e enviar para a API.
2. A API retorna a identificação da praga e possíveis recomendações.
3. Exibir os resultados no app para o usuário.

### **3️⃣ Chatbot para Suporte Técnico** 🤖
#### ✅ **Como Fazer de Forma Simples?**
* Utilizar um **chatbot pronto** que pode ser treinado com perguntas e respostas específicas.
* Integrar um bot baseado em **GPT-4** ou APIs de chatbots agrícolas.

#### 🔧 **APIs Recomendadas:**
* **OpenAI API (ChatGPT)** → Podemos configurar um chatbot agrícola.
* **Dialogflow (Google)** → Plataforma para criar chatbots personalizados.
* **Rasa (Open Source)** → Para um chatbot offline e mais customizável.

#### 🚀 **Passos:**
1. Criar um banco de dados com perguntas frequentes sobre agricultura.
2. Treinar um modelo de IA com esse banco de dados.
3. Conectar o chatbot à interface do usuário via API.

### **4️⃣ Análise Preditiva de Produtividade** 🌾📊
#### ✅ **Como Fazer de Forma Simples?**
* Utilizar APIs de análise de dados agrícolas para prever produtividade.
* Combinar dados climáticos, tipo de solo e histórico de produção.

#### 🔧 **APIs Recomendadas:**
* **Agro API (Agricultural Data)** → Fornece dados sobre colheitas, produtividade e clima.
* **NASA Earthdata API** → Dados sobre condições do solo e mudanças climáticas.
* **Google Earth Engine API** → Para análises agrícolas avançadas.

#### 🚀 **Passos:**
1. Coletar dados de clima, solo e produtividade passada via APIs.
2. Utilizar algoritmos simples de regressão (ou APIs de IA) para prever produtividade.
3. Exibir recomendações no painel do usuário.


## ⚡ Como Rodar o Projeto  

### 1️⃣ **Clone o repositório**  
```bash
git clone https://github.com/SamPenido/agrohelper.git
cd agrohelper
```

