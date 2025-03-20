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

    // Inicializa AOS
    AOS.init({
        duration: 800,
        once: true
    });

    // Inicializa carrossel de depoimentos
    initTestimonialsCarousel();
});

function initTestimonialsCarousel() {
    const container = document.querySelector('.carousel-container');
    const testimonials = document.querySelectorAll('.testimonial-card');
    const totalTestimonials = testimonials.length;
    let currentIndex = 0;

    function showTestimonial(index) {
        const offset = -index * 100;
        container.style.transform = `translateX(${offset}%)`;
    }

    document.querySelector('.carousel-btn.next').addEventListener('click', () => {
        currentIndex = (currentIndex + 1) % totalTestimonials;
        showTestimonial(currentIndex);
    });

    document.querySelector('.carousel-btn.prev').addEventListener('click', () => {
        currentIndex = (currentIndex - 1 + totalTestimonials) % totalTestimonials;
        showTestimonial(currentIndex);
    });

    // Auto play
    setInterval(() => {
        currentIndex = (currentIndex + 1) % totalTestimonials;
        showTestimonial(currentIndex);
    }, 8000);
}

// Função para navegação entre páginas
function navigateTo(page) {
    fetch(`pages/${page}.html`)
        .then(response => response.text())
        .then(html => {
            document.getElementById('main-content').innerHTML = html;
        })
        .catch(error => console.error('Erro ao carregar página:', error));
}
