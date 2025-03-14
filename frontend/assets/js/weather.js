const API_KEY = 'SUA_CHAVE_API_AQUI'; // Substituir pela chave real
const BASE_URL = 'https://api.openweathermap.org/data/2.5/weather';

async function getWeatherData(lat, lon) {
    try {
        const response = await fetch(`${BASE_URL}?lat=${lat}&lon=${lon}&appid=${API_KEY}&units=metric&lang=pt_br`);
        
        if (!response.ok) {
            throw new Error('Erro ao obter dados meteorológicos');
        }

        const data = await response.json();
        return {
            temperature: data.main.temp,
            humidity: data.main.humidity,
            description: data.weather[0].description,
            icon: data.weather[0].icon,
            wind: data.wind.speed
        };
    } catch (error) {
        console.error('Erro:', error);
        return null;
    }
}

function displayWeather(weatherData) {
    const weatherContainer = document.getElementById('weather-widget');
    
    if (!weatherData) {
        weatherContainer.innerHTML = `
            <div class="weather-error">
                <p>Não foi possível carregar os dados meteorológicos</p>
            </div>
        `;
        return;
    }

    weatherContainer.innerHTML = `
        <div class="weather-card">
            <div class="weather-icon">
                <img src="http://openweathermap.org/img/wn/${weatherData.icon}@2x.png" alt="${weatherData.description}">
            </div>
            <div class="weather-info">
                <p class="temperature">${weatherData.temperature}°C</p>
                <p class="description">${weatherData.description}</p>
                <div class="details">
                    <p>Umidade: ${weatherData.humidity}%</p>
                    <p>Vento: ${weatherData.wind} m/s</p>
                </div>
            </div>
        </div>
    `;
}

// Exemplo de uso
document.addEventListener('DOMContentLoaded', async () => {
    // Coordenadas de exemplo (pode ser obtido via geolocalização do navegador)
    const lat = -23.5505;
    const lon = -46.6333;
    
    const weatherData = await getWeatherData(lat, lon);
    displayWeather(weatherData);
});
