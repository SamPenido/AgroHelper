🌱 AgroHelper - Plataforma Inteligente para Agricultura 🚜

O AgroHelper é um sistema web projetado para auxiliar pequenos e médios agricultores na gestão de suas propriedades, fornecendo previsão climática inteligente, um chatbot agrícola e um marketplace para compra e venda de produtos agrícolas.

📌 Tecnologias Utilizadas

🖥️ Back-end

Python (Django e Django REST Framework)

Autenticação JWT

🌍 Front-end

React.js + Vite

TailwindCSS

📊 Banco de Dados

PostgreSQL

🤖 Inteligência Artificial & Automação

Python (Análise de dados climáticos)

Chatbot para suporte agrícola

☁️ Infraestrutura & Hospedagem

Railway / Render

🚀 Funcionalidades Principais

✅ Cadastro/Login de usuários (Agricultores, Técnicos, Compradores)✅ Gestão de Propriedades Agrícolas (Registro de terrenos e culturas)✅ Previsão Climática Inteligente✅ Chatbot para suporte agrícola✅ Marketplace para compra e venda de produtos agrícolas

Filtros avançados por categoria e localização

Cards de produtos com informações detalhadas

Interface intuitiva e responsiva✅ Relatórios de Produção e Custos

Dados atualizados em tempo real

Integração com APIs de previsão climática

Histórico de produtividade

Alertas inteligentes✅ Alertas Inteligentes (Notificações sobre clima)

📂 Estrutura do Projeto

📌 Backend (Django REST Framework)
```
agrohelper-backend/
│── agrohelper/
│   ├── settings.py    # Configurações do Django
│   ├── urls.py        # Rotas principais
│── api/
│   ├── models.py      # Modelos de banco de dados
│   ├── serializers.py # Serializadores para API
│   ├── views.py       # Controladores
│── manage.py          # Script de gerenciamento do Django
│── requirements.txt   # Dependências do Python
│── README.md          # Documentação do projeto
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

🔍 Implementação Técnica

1️⃣ Previsão Climática Inteligente 🌦️

✅ Solução

Utilizar APIs de previsão do tempo que fornecem dados meteorológicos detalhados sem necessidade de latitude/longitude.

🔧 API Recomendada:

WeatherAPI (https://www.weatherapi.com/) → Permite buscar previsão do tempo informando apenas o nome da cidade ou código do país.

🚀 Passos:

Criar uma conta gratuita na WeatherAPI e obter a chave de acesso.

Fazer chamadas para a API utilizando o nome da cidade.

Exibir os dados na interface do usuário.

2️⃣ Chatbot para Suporte Técnico 🤖

✅ Solução

Utilizar um chatbot pronto que pode ser treinado com perguntas e respostas específicas.

🔧 API Recomendada:

OpenAI API (ChatGPT) → Podemos configurar um chatbot agrícola.

🚀 Passos:

Criar um banco de dados com perguntas frequentes sobre agricultura.

Treinar um modelo de IA com esse banco de dados.

Conectar o chatbot à interface do usuário via API.


## ⚡ Como Rodar o Projeto  

### 1️⃣ **Clone o repositório**  
```bash
git clone https://github.com/SamPenido/agrohelper.git
cd agrohelper
```
2️⃣ Backend

cd agrohelper-backend
python -m venv venv
source venv/bin/activate  # Linux/macOS
venv\Scripts\activate    # Windows
pip install -r requirements.txt
python manage.py runserver

3️⃣ Frontend

cd agrohelper-frontend
npm install
npm run dev
