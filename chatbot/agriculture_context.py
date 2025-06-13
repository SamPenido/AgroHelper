"""
Sistema de gerenciamento de contexto agrícola para o chatbot AgroHelper
Este módulo contém informações especializadas sobre agricultura para melhorar as respostas do chatbot.
"""

from typing import Dict, List, Optional, Tuple
import re

# Base de conhecimento agrícola
CROPS = {
    "milho": {
        "plantio": "O plantio do milho deve ser realizado no início da estação chuvosa, com espaçamento de 70cm entre linhas.",
        "irrigacao": "O milho necessita de 4-6mm de água por dia durante estágios críticos de crescimento.",
        "pragas": "As principais pragas do milho incluem a lagarta-do-cartucho e a cigarrinha-do-milho.",
        "colheita": "A colheita ocorre geralmente 90-120 dias após o plantio, quando os grãos estão com umidade entre 18-25%.",
        "produtos_recomendados": [1]  # IDs dos produtos relacionados
    },
    "soja": {
        "plantio": "A soja deve ser plantada com espaçamento de 45-50cm entre linhas, com profundidade de 3-5cm.",
        "irrigacao": "A soja necessita de 450-800mm de água durante todo seu ciclo, dependendo do clima.",
        "pragas": "As principais pragas da soja incluem percevejos, lagartas e ácaros.",
        "colheita": "A colheita ocorre quando 95% das vagens estão maduras, geralmente 100-160 dias após o plantio.",
        "produtos_recomendados": [2]
    },
    "feijao": {
        "plantio": "O feijão pode ser plantado com espaçamento de 40-50cm entre linhas.",
        "irrigacao": "O feijão necessita de irrigação regular, especialmente durante a floração e formação de vagens.",
        "pragas": "As principais pragas do feijão incluem o pulgão, a mosca-branca e a vaquinha.",
        "colheita": "A colheita ocorre 60-100 dias após o plantio, dependendo da variedade.",
        "produtos_recomendados": [2]
    },
    "tomate": {
        "plantio": "O tomateiro deve ser plantado com espaçamento de 50-100cm entre plantas.",
        "irrigacao": "O tomate necessita de irrigação regular, evitando molhar as folhas para prevenir doenças.",
        "pragas": "As principais pragas do tomate incluem a traça, a broca-pequena e os ácaros.",
        "colheita": "Os frutos podem ser colhidos 70-90 dias após o transplante, quando estão com a cor característica.",
        "produtos_recomendados": [2]
    },
    "cafe": {
        "plantio": "O café deve ser plantado no início da estação chuvosa, com espaçamento de 2-4m entre plantas.",
        "irrigacao": "O café necessita de 1500-2000mm de água por ano, bem distribuídos.",
        "pragas": "As principais pragas do café incluem o bicho-mineiro, a broca-do-café e os ácaros.",
        "colheita": "A colheita ocorre quando os frutos estão maduros, geralmente 8-11 meses após a florada.",
        "produtos_recomendados": [2]
    }
}

AGRICULTURAL_TECHNIQUES = {
    "plantio_direto": "Técnica de cultivo sem revolvimento do solo, mantendo a cobertura vegetal para proteção.",
    "irrigacao_gotejamento": "Sistema de irrigação que aplica água diretamente na zona radicular, economizando água.",
    "controle_biologico": "Utilização de inimigos naturais para controle de pragas, reduzindo o uso de agrotóxicos.",
    "rotacao_culturas": "Alternância de culturas em uma mesma área para melhorar a fertilidade do solo e reduzir pragas.",
    "compostagem": "Processo de decomposição de matéria orgânica para produção de adubo natural."
}

EQUIPMENT_INFO = {
    "trator": {
        "uso": "Preparação do solo, plantio, aplicação de defensivos e colheita.",
        "manutencao": "Realizar manutenção preventiva a cada 250 horas de uso.",
        "tipos": "Existem tratores de pequeno, médio e grande porte, cada um adequado para diferentes tamanhos de propriedade.",
        "produtos_recomendados": [3]
    },
    "arado": {
        "uso": "Preparo inicial do solo, revolvendo-o para plantio.",
        "manutencao": "Verificar desgaste das lâminas e lubrificar regularmente.",
        "tipos": "Arado de discos, aivecas ou subsolador, cada um com uso específico."
    },
    "pulverizador": {
        "uso": "Aplicação de defensivos agrícolas e fertilizantes foliares.",
        "manutencao": "Limpar após cada uso e verificar bicos e filtros regularmente.",
        "tipos": "Pulverizadores costais, de barra ou autopropelidos."
    },
    "colheitadeira": {
        "uso": "Colheita mecanizada de grãos como soja, milho e trigo.",
        "manutencao": "Verificar sistemas de corte, trilha e limpeza antes da safra.",
        "tipos": "Existem colheitadeiras específicas para diferentes culturas."
    },
    "sistema_irrigacao": {
        "uso": "Fornecimento controlado de água para as culturas.",
        "manutencao": "Verificar vazamentos, entupimentos e uniformidade de aplicação.",
        "tipos": "Sistemas por aspersão, gotejamento, microaspersão ou pivô central."
    }
}

# Detector de tópicos agrícolas
def detect_agricultural_topics(message: str) -> List[Tuple[str, str]]:
    """
    Detecta tópicos agrícolas na mensagem do usuário
    Retorna uma lista de tuplas (categoria, tópico)
    """
    message = message.lower()
    detected_topics = []
    
    # Verificar menções a culturas
    for crop in CROPS.keys():
        if crop in message or any(variation in message for variation in [f"{crop}s", f"plantação de {crop}", f"cultivo de {crop}"]):
            for aspect in ["plantio", "irrigacao", "pragas", "colheita"]:
                if aspect in message or related_term(aspect, message):
                    detected_topics.append(("crop", f"{crop}_{aspect}"))
            # Se não houver aspecto específico, adicionar a cultura geral
            if not any(topic[1].startswith(crop) for topic in detected_topics):
                detected_topics.append(("crop", crop))
    
    # Verificar menções a técnicas agrícolas
    for technique in AGRICULTURAL_TECHNIQUES.keys():
        readable_technique = technique.replace("_", " ")
        if readable_technique in message:
            detected_topics.append(("technique", technique))
    
    # Verificar menções a equipamentos
    for equipment in EQUIPMENT_INFO.keys():
        if equipment in message or any(variation in message for variation in [f"{equipment}s", f"uso de {equipment}", f"manutenção de {equipment}"]):
            for aspect in ["uso", "manutencao", "tipos"]:
                if aspect in message or related_term(aspect, message):
                    detected_topics.append(("equipment", f"{equipment}_{aspect}"))
            # Se não houver aspecto específico, adicionar o equipamento geral
            if not any(topic[1].startswith(equipment) for topic in detected_topics):
                detected_topics.append(("equipment", equipment))
    
    return detected_topics

def related_term(aspect: str, message: str) -> bool:
    """Verifica termos relacionados a um aspecto"""
    related_terms = {
        "plantio": ["plantar", "semear", "cultivar", "semeadura", "plantação"],
        "irrigacao": ["irrigar", "regar", "água", "umidade", "rega"],
        "pragas": ["praga", "inseto", "doença", "fungo", "controle", "combate"],
        "colheita": ["colher", "safra", "produção", "rendimento", "produtividade"],
        "uso": ["usar", "utilizar", "aplicação", "como funciona", "para que serve"],
        "manutencao": ["manter", "limpar", "conservar", "cuidar", "consertar", "reparar"],
        "tipos": ["tipo", "modelo", "marca", "versão", "diferença"]
    }
    
    if aspect in related_terms:
        return any(term in message for term in related_terms[aspect])
    return False

def get_enhanced_context(message: str, detected_topics: List[Tuple[str, str]]) -> str:
    """
    Gera um contexto especializado baseado nos tópicos detectados
    """
    if not detected_topics:
        return ""
    
    context_parts = []
    
    for category, topic in detected_topics:
        if category == "crop":
            if "_" in topic:  # Tópico específico como "milho_plantio"
                crop, aspect = topic.split("_")
                if crop in CROPS and aspect in CROPS[crop]:
                    context_parts.append(f"Sobre {crop} ({aspect}): {CROPS[crop][aspect]}")
            else:  # Tópico geral como "milho"
                if topic in CROPS:
                    context_parts.append(f"Cultura: {topic.capitalize()}")
                    for aspect, info in CROPS[topic].items():
                        if aspect != "produtos_recomendados":
                            context_parts.append(f"- {aspect.replace('_', ' ').capitalize()}: {info}")
        
        elif category == "technique":
            if topic in AGRICULTURAL_TECHNIQUES:
                context_parts.append(f"Técnica agrícola: {topic.replace('_', ' ').capitalize()}")
                context_parts.append(f"- {AGRICULTURAL_TECHNIQUES[topic]}")
        
        elif category == "equipment":
            if "_" in topic:  # Tópico específico como "trator_uso"
                equip, aspect = topic.split("_")
                if equip in EQUIPMENT_INFO and aspect in EQUIPMENT_INFO[equip]:
                    context_parts.append(f"Sobre {equip} ({aspect}): {EQUIPMENT_INFO[equip][aspect]}")
            else:  # Tópico geral como "trator"
                if topic in EQUIPMENT_INFO:
                    context_parts.append(f"Equipamento: {topic.capitalize()}")
                    for aspect, info in EQUIPMENT_INFO[topic].items():
                        if aspect != "produtos_recomendados":
                            context_parts.append(f"- {aspect.replace('_', ' ').capitalize()}: {info}")
    
    return "\n".join(context_parts)

def get_recommended_products(detected_topics: List[Tuple[str, str]]) -> List[int]:
    """
    Retorna IDs de produtos recomendados com base nos tópicos detectados
    """
    product_ids = set()
    
    for category, topic in detected_topics:
        if category == "crop":
            crop = topic.split("_")[0] if "_" in topic else topic
            if crop in CROPS and "produtos_recomendados" in CROPS[crop]:
                product_ids.update(CROPS[crop]["produtos_recomendados"])
        
        elif category == "equipment":
            equip = topic.split("_")[0] if "_" in topic else topic
            if equip in EQUIPMENT_INFO and "produtos_recomendados" in EQUIPMENT_INFO[equip]:
                product_ids.update(EQUIPMENT_INFO[equip]["produtos_recomendados"])
    
    return list(product_ids)

def enhance_prompt_with_agricultural_context(user_message: str) -> Tuple[str, List[int]]:
    """
    Enriquece o prompt com contexto agrícola específico
    Retorna o contexto melhorado e lista de IDs de produtos recomendados
    """
    detected_topics = detect_agricultural_topics(user_message)
    enhanced_context = get_enhanced_context(user_message, detected_topics)
    recommended_products = get_recommended_products(detected_topics)
    
    return enhanced_context, recommended_products