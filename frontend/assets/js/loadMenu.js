document.addEventListener('DOMContentLoaded', () => {
    // Carrega o template do menu
    fetch('components/menu.html')
        .then(response => response.text())
        .then(html => {
            // Adiciona o template ao body
            document.body.insertAdjacentHTML('afterbegin', html);
            
            // Inicializa o menu mobile
            const hamburger = document.querySelector('.hamburger');
            const navLinks = document.querySelector('.nav-links');

            if (hamburger && navLinks) {
                hamburger.addEventListener('click', () => {
                    navLinks.classList.toggle('active');
                    hamburger.classList.toggle('active');
                });
            }
        })
        .catch(error => console.error('Erro ao carregar o menu:', error));
});
