// Configurações iniciais
document.addEventListener('DOMContentLoaded', () => {
    console.log('AgroHelper iniciado!');
    
    // Verifica autenticação
    const token = localStorage.getItem('authToken');
    if (token) {
        // Usuário autenticado
        console.log('Usuário autenticado');
    } else {
        // Usuário não autenticado
        console.log('Usuário não autenticado');
    }
});

// Função para navegação entre páginas
function navigateTo(page) {
    fetch(`pages/${page}.html`)
        .then(response => response.text())
        .then(html => {
            document.getElementById('main-content').innerHTML = html;
        })
        .catch(error => console.error('Erro ao carregar página:', error));
}
