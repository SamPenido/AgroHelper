from fastapi import FastAPI, HTTPException, Depends, Header
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List, Optional, Dict, Any
import os
import json
import openai
from dotenv import load_dotenv
import jwt
from datetime import datetime, timedelta

# Importar o sistema de contexto agrícola
from agriculture_context import enhance_prompt_with_agricultural_context

# Carregar variáveis de ambiente
load_dotenv('../.env')

# Configurar OpenAI
openai.api_key = os.getenv("OPENAI_API_KEY")

# Verificar se a chave da API foi carregada corretamente
if not openai.api_key:
    print("AVISO: Chave da API OpenAI não encontrada no arquivo .env!")
else:
    print(f"API OpenAI configurada. Chave começa com: {openai.api_key[:10]}...")

app = FastAPI(title="AgroHelper AI Chatbot")

# Configurar CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Substitua por origens específicas em produção
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Modelos de dados
class Message(BaseModel):
    role: str
    content: str

class ChatRequest(BaseModel):
    message: str
    history: Optional[List[Message]] = []
    user_id: Optional[int] = None

class ProductSuggestion(BaseModel):
    id: int
    title: str
    price: float
    imageUrl: Optional[str] = None

class ChatResponse(BaseModel):
    message: str
    productSuggestion: Optional[ProductSuggestion] = None

# Banco de dados simulado de produtos
# Em produção, isso seria conectado ao banco de dados real
SAMPLE_PRODUCTS = [
    {
        "id": 1,
        "title": "Semente de Milho Híbrido",
        "price": 259.99,
        "description": "Sementes de milho de alta produtividade, resistentes a pragas",
        "category": "SEEDS",
        "imageUrl": "https://via.placeholder.com/300x220?text=Milho+Hibrido"
    },
    {
        "id": 2,
        "title": "Fertilizante Orgânico Premium",
        "price": 89.90,
        "description": "Fertilizante 100% orgânico para hortas e plantações",
        "category": "INPUTS",
        "imageUrl": "https://via.placeholder.com/300x220?text=Fertilizante"
    },
    {
        "id": 3,
        "title": "Trator Compacto para Pequenas Propriedades",
        "price": 45000.00,
        "description": "Trator pequeno ideal para agricultura familiar e pequenas áreas",
        "category": "EQUIPMENT",
        "imageUrl": "https://via.placeholder.com/300x220?text=Trator"
    }
]

# Contexto específico para o assistente agrícola
AGRICULTURE_SYSTEM_PROMPT = """
Você é um assistente agrícola especializado chamado AgroHelper. Seu objetivo é fornecer informações úteis para agricultores e compradores de produtos agrícolas.

REGRAS IMPORTANTES:
1. Forneça informações precisas sobre cultivo, manejo, técnicas agrícolas, e produtos.
2. Quando falar sobre produtos, mencione características importantes como qualidade, durabilidade, e adequação para diferentes tipos de cultivo.
3. Sempre dê respostas curtas, práticas e diretas.
4. Use linguagem simples e acessível, evitando termos muito técnicos quando não for necessário.
5. Se o usuário perguntar sobre um produto específico que não conhecemos, sugira categorias similares.

CONHECIMENTO ESPECIALIZADO:
- Cultivo: técnicas de plantio, irrigação, manejo de pragas e doenças
- Equipamentos: tratores, implementos, ferramentas, sistemas de irrigação
- Insumos: sementes, fertilizantes, defensivos (prefira sugerir opções orgânicas quando possível)
- Culturas: milho, soja, feijão, hortaliças, frutas

Responda como se estivesse conversando com um agricultor ou comprador diretamente, sendo amigável mas profissional.
"""

# Função para verificar a autenticação (simplificada para este exemplo)
async def verify_token(authorization: Optional[str] = Header(None)):
    if not authorization:
        return None
    
    try:
        # Remover "Bearer " do token
        token = authorization.replace("Bearer ", "")
        payload = jwt.decode(token, os.getenv("JWT_SECRET"), algorithms=["HS256"])
        return payload
    except:
        return None

# Função que busca produtos relacionados com base na mensagem
def find_related_products(message: str) -> Optional[Dict]:
    message_lower = message.lower()
    
    # Palavras-chave para cada produto
    keywords = {
        1: ["milho", "semente", "plantar", "grão", "híbrido", "cultivar"],
        2: ["fertilizante", "adubo", "orgânico", "solo", "nutrição", "planta"],
        3: ["trator", "máquina", "equipamento", "pequeno", "propriedade", "compacto"]
    }
    
    # Contar ocorrências de palavras-chave
    scores = {product_id: 0 for product_id in keywords.keys()}
    
    for product_id, words in keywords.items():
        for word in words:
            if word in message_lower:
                scores[product_id] += 1
    
    # Encontrar o produto com maior pontuação
    best_match_id = max(scores.items(), key=lambda x: x[1])
    
    # Apenas sugerir se houver alguma correspondência
    if best_match_id[1] > 0:
        for product in SAMPLE_PRODUCTS:
            if product["id"] == best_match_id[0]:
                return product
    
    return None

@app.post("/api/chatbot", response_model=ChatResponse)
async def chat_with_ai(request: ChatRequest, user_info: dict = Depends(verify_token)):
    try:
        # Verificar tipo de usuário (opcional, dependendo do seu requisito)
        # if user_info and user_info.get("userType") != "BUYER":
        #     raise HTTPException(status_code=403, detail="Apenas compradores podem usar o chatbot")
        
        # Obter contexto agrícola aprimorado e recomendações de produtos
        enhanced_context, recommended_product_ids = enhance_prompt_with_agricultural_context(request.message)
        
        # Criar prompt personalizado com contexto agrícola
        custom_prompt = AGRICULTURE_SYSTEM_PROMPT
        if enhanced_context:
            custom_prompt += f"\n\nCONTEXTO RELEVANTE PARA ESTA PERGUNTA:\n{enhanced_context}"
        
        # Preparar histórico de conversas
        conversation = [{"role": "system", "content": custom_prompt}]
        
        # Adicionar histórico anterior (limite de 10 mensagens para economizar tokens)
        for msg in request.history[-10:]:
            conversation.append({"role": msg.role, "content": msg.content})
        
        # Adicionar mensagem atual
        conversation.append({"role": "user", "content": request.message})
        
        # Chamar a API da OpenAI (usando nova sintaxe do cliente OpenAI v1.x)
        try:
            response = openai.chat.completions.create(
                model=os.getenv("CHATBOT_MODEL", "gpt-3.5-turbo"),
                messages=conversation,
                max_tokens=500,
                temperature=0.7
            )
            
            # Extrair a resposta
            ai_message = response.choices[0].message.content
            print(f"Resposta recebida da OpenAI: {ai_message[:50]}...")
        except Exception as openai_error:
            print(f"Erro na chamada à API da OpenAI: {openai_error}")
            raise Exception(f"Erro na API da OpenAI: {openai_error}")
        
        # Buscar produto relacionado baseado na análise de contexto
        product = None
        if recommended_product_ids:
            # Priorizar produtos da análise de contexto agrícola
            for prod_id in recommended_product_ids:
                for sample_product in SAMPLE_PRODUCTS:
                    if sample_product["id"] == prod_id:
                        product = sample_product
                        break
                if product:
                    break
        
        # Se não encontrou produtos recomendados pela análise de contexto, usa busca por palavras-chave
        if not product:
            product = find_related_products(request.message)
        
        # Preparar resposta
        chat_response = {"message": ai_message}
        
        # Adicionar sugestão de produto se encontrado
        if product:
            chat_response["productSuggestion"] = {
                "id": product["id"],
                "title": product["title"],
                "price": product["price"],
                "imageUrl": product["imageUrl"]
            }
        
        return chat_response
    
    except Exception as e:
        print(f"Erro no processamento da requisição: {str(e)}")
        import traceback
        traceback.print_exc()
        raise HTTPException(status_code=500, detail=f"Erro no processamento: {str(e)}")

@app.get("/")
def read_root():
    return {"message": "AgroHelper AI Chatbot API"}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(
        "app:app", 
        host=os.getenv("CHATBOT_HOST", "0.0.0.0"), 
        port=int(os.getenv("CHATBOT_PORT", 8000)),
        reload=True
    )