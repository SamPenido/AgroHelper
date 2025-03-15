// URL da API (substituir pelo endpoint real)
const API_URL = 'https://api.agrohelper.com/v1';

// Função para buscar dados do dashboard
async function fetchDashboardData() {
    try {
        const response = await fetch(`${API_URL}/dashboard`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('authToken')}`
            }
        });
        
        if (!response.ok) {
            throw new Error('Erro ao carregar dados');
        }

        const data = await response.json();
        
        // Atualiza os valores na tela
        document.getElementById('planted-area').textContent = `${data.planted_area} hectares`;
        document.getElementById('productivity').textContent = `${data.productivity}%`;
        document.getElementById('water-usage').textContent = `${data.water_usage} L`;
        
        // Atualiza gráficos (será implementado posteriormente)
        updateCharts(data.charts);
        
    } catch (error) {
        console.error('Erro:', error);
        showError('Erro ao carregar dados do dashboard');
    }
}

// Função para mostrar erros
function showError(message) {
    const alertContainer = document.createElement('div');
    alertContainer.className = 'alert-card error';
    alertContainer.innerHTML = `
        <div class="alert-icon">❌</div>
        <div class="alert-content">
            <h3>Erro</h3>
            <p>${message}</p>
        </div>
    `;
    document.querySelector('.alerts').prepend(alertContainer);
}

// Inicialização
document.addEventListener('DOMContentLoaded', () => {
    fetchDashboardData();
    
    // Atualiza dados a cada 5 minutos
    setInterval(fetchDashboardData, 300000);
});
