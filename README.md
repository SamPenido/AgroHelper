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

## ⚡ Como Rodar o Projeto  

### 1️⃣ **Clone o repositório**  
```bash
git clone https://github.com/SamPenido/agrohelper.git
cd agrohelper
```

