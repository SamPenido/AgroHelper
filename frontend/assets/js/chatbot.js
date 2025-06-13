/**
 * AgroHelper AI Chatbot
 * Fornece assistência inteligente para agricultores com integração à API Python/OpenAI
 */

class AgroHelperChatbot {
    constructor() {
        this.container = null;
        this.messagesContainer = null;
        this.inputField = null;
        this.sendButton = null;
        this.toggleButton = null;
        this.typingIndicator = null;
        this.isChatbotActive = false;
        this.isWaitingForResponse = false;
        this.messageHistory = [];
        
        // Verifica se o usuário é do tipo BUYER (apenas compradores podem usar o chatbot)
        this.canUseBot = this.checkUserPermission();
        
        // Inicializa o chatbot
        this.init();
    }
    
    // Verifica se o usuário tem permissão para usar o chatbot (apenas BUYER)
    checkUserPermission() {
        try {
            const userData = localStorage.getItem('userData');
            if (userData) {
                const result = JSON.parse(userData);
                const user = result.user;
                return user.userType === 'BUYER';
            }
            return false;
        } catch (error) {
            console.error('Erro ao verificar permissão de usuário:', error);
            return false;
        }
    }
    
    // Inicializa o chatbot
    init() {
        this.createChatbotElements();
        this.attachEventListeners();
        
        // Adiciona mensagem de boas-vindas após um pequeno delay
        setTimeout(() => {
            if (this.canUseBot) {
                this.addBotMessage("Olá! Sou o assistente agrícola da AgroHelper. Como posso ajudar você hoje? Posso fornecer informações sobre cultivo, manejo, técnicas agrícolas, ou auxiliar em suas decisões de compra.");
            } else {
                this.addBotMessage("Este recurso está disponível apenas para compradores. Se você deseja utilizar o assistente agrícola, faça login como comprador.");
            }
        }, 500);
    }
    
    // Cria os elementos HTML do chatbot
    createChatbotElements() {
        // Botão de toggle
        this.toggleButton = document.createElement('div');
        this.toggleButton.className = 'chatbot-toggle';
        this.toggleButton.innerHTML = `
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
            </svg>
        `;
        document.body.appendChild(this.toggleButton);
        
        // Container principal
        this.container = document.createElement('div');
        this.container.className = 'chatbot-container';
        this.container.innerHTML = `
            <div class="chatbot-header">
                <div class="chatbot-title">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
                    </svg>
                    Assistente Agrícola
                </div>
                <div class="chatbot-close">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                    </svg>
                </div>
            </div>
            <div class="chatbot-messages"></div>
            <div class="chatbot-typing">
                <div class="typing-dots">
                    <div class="typing-dot"></div>
                    <div class="typing-dot"></div>
                    <div class="typing-dot"></div>
                </div>
            </div>
            <div class="chatbot-input">
                <input type="text" placeholder="Digite sua pergunta sobre agricultura..." ${!this.canUseBot ? 'disabled' : ''}>
                <button ${!this.canUseBot ? 'disabled' : ''}>
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8" />
                    </svg>
                </button>
            </div>
        `;
        document.body.appendChild(this.container);
        
        // Guarda referências para elementos importantes
        this.messagesContainer = this.container.querySelector('.chatbot-messages');
        this.inputField = this.container.querySelector('input');
        this.sendButton = this.container.querySelector('button');
        this.typingIndicator = this.container.querySelector('.chatbot-typing');
    }
    
    // Adiciona os event listeners
    attachEventListeners() {
        // Toggle do chatbot
        this.toggleButton.addEventListener('click', () => this.toggleChatbot());
        
        // Fechar chatbot
        this.container.querySelector('.chatbot-close').addEventListener('click', () => this.toggleChatbot(false));
        
        // Enviar mensagem (botão)
        this.sendButton.addEventListener('click', () => this.sendMessage());
        
        // Enviar mensagem (tecla Enter)
        this.inputField.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.sendMessage();
        });
        
        // Habilitar/desabilitar botão de envio baseado no conteúdo do input
        this.inputField.addEventListener('input', () => {
            this.sendButton.disabled = !this.inputField.value.trim() || !this.canUseBot || this.isWaitingForResponse;
        });
    }
    
    // Alterna a visibilidade do chatbot
    toggleChatbot(show = null) {
        // Se show for null, inverte o estado atual
        this.isChatbotActive = show !== null ? show : !this.isChatbotActive;
        
        if (this.isChatbotActive) {
            this.container.classList.add('active');
            this.toggleButton.classList.add('active');
            // Foca no campo de input quando abrir
            setTimeout(() => this.inputField.focus(), 300);
        } else {
            this.container.classList.remove('active');
            this.toggleButton.classList.remove('active');
        }
    }
    
    // Adiciona uma mensagem do bot ao chatbot
    addBotMessage(message, includeProductSuggestion = false, product = null) {
        // Mostra indicador de digitação
        this.showTypingIndicator(true);
        
        // Simula um pequeno delay para parecer mais natural
        setTimeout(() => {
            // Esconde indicador de digitação
            this.showTypingIndicator(false);
            
            // Cria elemento de mensagem
            const messageElement = document.createElement('div');
            messageElement.className = 'chatbot-message bot';
            messageElement.textContent = message;
            
            // Adiciona ao container de mensagens
            this.messagesContainer.appendChild(messageElement);
            
            // Adiciona sugestão de produto, se necessário
            if (includeProductSuggestion && product) {
                const suggestionElement = document.createElement('div');
                suggestionElement.className = 'product-suggestion';
                suggestionElement.innerHTML = `
                    <div class="product-suggestion-header">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
                            <path d="M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16zm.93-9.412-1 4.705c-.07.34.029.533.304.533.194 0 .487-.07.686-.246l-.088.416c-.287.346-.92.598-1.465.598-.703 0-1.002-.422-.808-1.319l.738-3.468c.064-.293.006-.399-.287-.47l-.451-.081.082-.381 2.29-.287zM8 5.5a1 1 0 1 1 0-2 1 1 0 0 1 0 2z"/>
                        </svg>
                        Produto Recomendado
                    </div>
                    <div class="product-suggestion-content">
                        <img src="${product.imageUrl || 'https://via.placeholder.com/70x70?text=Produto'}" 
                             alt="${product.title}"
                             class="product-suggestion-image"
                             onerror="this.src='https://via.placeholder.com/70x70?text=Produto'">
                        <div class="product-suggestion-info">
                            <div class="product-suggestion-title">${product.title}</div>
                            <div class="product-suggestion-price">R$ ${this.formatPrice(product.price)}</div>
                            <a href="product-detail.html?id=${product.id}" class="product-suggestion-link">Ver Detalhes</a>
                        </div>
                    </div>
                `;
                this.messagesContainer.appendChild(suggestionElement);
            }
            
            // Scroll para a mensagem mais recente
            this.scrollToBottom();
            
        }, 1000 + Math.random() * 1000); // Delay entre 1-2 segundos
    }
    
    // Adiciona uma mensagem do usuário ao chatbot
    addUserMessage(message) {
        const messageElement = document.createElement('div');
        messageElement.className = 'chatbot-message user';
        messageElement.textContent = message;
        
        this.messagesContainer.appendChild(messageElement);
        this.scrollToBottom();
        
        // Adiciona à histórico de mensagens
        this.messageHistory.push({
            role: 'user',
            content: message
        });
    }
    
    // Mostra/esconde indicador de digitação
    showTypingIndicator(show) {
        if (show) {
            this.typingIndicator.classList.add('active');
        } else {
            this.typingIndicator.classList.remove('active');
        }
    }
    
    // Envia a mensagem do usuário e obtém resposta
    async sendMessage() {
        if (!this.canUseBot || this.isWaitingForResponse) return;
        
        const userMessage = this.inputField.value.trim();
        if (!userMessage) return;
        
        // Adiciona mensagem do usuário ao chat
        this.addUserMessage(userMessage);
        
        // Limpa o campo de input
        this.inputField.value = '';
        this.sendButton.disabled = true;
        this.isWaitingForResponse = true;
        
        try {
            // Faz requisição para o backend Python
            const response = await this.getAIResponse(userMessage);
            
            // Verifica se há sugestão de produto
            let includeProductSuggestion = false;
            let product = null;
            
            if (response.productSuggestion) {
                includeProductSuggestion = true;
                product = response.productSuggestion;
            }
            
            // Adiciona resposta do bot
            this.addBotMessage(response.message, includeProductSuggestion, product);
            
            // Adiciona à histórico de mensagens
            this.messageHistory.push({
                role: 'assistant',
                content: response.message
            });
            
        } catch (error) {
            console.error('Erro ao obter resposta:', error);
            this.addBotMessage('Desculpe, tive um problema ao processar sua mensagem. Por favor, tente novamente mais tarde.');
        }
        
        this.isWaitingForResponse = false;
    }
    
    // Obtém resposta da API Python/OpenAI
    async getAIResponse(message) {
        try {
            // Usa a porta e host configurados para o chatbot
            const CHATBOT_HOST = 'localhost';  // Isso pode ser configurado em um arquivo .env no futuro
            const CHATBOT_PORT = 8000;         // Isso pode ser configurado em um arquivo .env no futuro
            const CHATBOT_URL = `http://${CHATBOT_HOST}:${CHATBOT_PORT}/api/chatbot`;
            
            const response = await fetch(CHATBOT_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    message: message,
                    history: this.messageHistory
                })
            });
            
            if (!response.ok) {
                throw new Error(`Erro na API: ${response.status}`);
            }
            
            return await response.json();
            
        } catch (error) {
            console.error('Erro na API do chatbot:', error);
            throw error;
        }
    }
    
    // Formata o preço
    formatPrice(price) {
        return new Intl.NumberFormat('pt-BR', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        }).format(price);
    }
    
    // Faz scroll para a mensagem mais recente
    scrollToBottom() {
        this.messagesContainer.scrollTop = this.messagesContainer.scrollHeight;
    }
}

// Inicializa o chatbot quando a página carregar
document.addEventListener('DOMContentLoaded', () => {
    // Verifica se já existe um stylesheet para o chatbot
    if (!document.querySelector('link[href*="chatbot.css"]')) {
        console.log("Carregando CSS do chatbot dinamicamente");
        const link = document.createElement('link');
        link.rel = 'stylesheet';
        
        // Determina o caminho para o CSS dinamicamente baseado na URL atual
        const currentPath = window.location.pathname;
        const pathToRoot = currentPath.startsWith('/frontend/pages/') ? '../../' : 
                          currentPath.startsWith('/frontend/') ? './' : 
                          '/frontend/assets/css/';
        
        link.href = `${pathToRoot}assets/css/chatbot.css`;
        document.head.appendChild(link);
    } else {
        console.log("CSS do chatbot já carregado na página");
    }
    
    // Inicializa o chatbot
    window.agroHelperChatbot = new AgroHelperChatbot();
});