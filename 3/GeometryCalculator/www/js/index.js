// Основной объект приложения
const GeometryApp = {
    // Конфигурация фигур
    figures: {
        '2d': [
            { id: 'square', name: 'Квадрат', params: ['side'], icon: 'fas fa-square' },
            { id: 'rectangle', name: 'Прямоугольник', params: ['length', 'width'], icon: 'fas fa-rectangle-landscape' },
            { id: 'triangle', name: 'Треугольник', params: ['sideA', 'sideB', 'sideC'], icon: 'fas fa-play' },
            { id: 'circle', name: 'Круг', params: ['radius'], icon: 'fas fa-circle' },
            { id: 'rhombus', name: 'Ромб', params: ['side', 'height'], icon: 'fas fa-diamond' },
            { id: 'trapezoid', name: 'Трапеция', params: ['base1', 'base2', 'height', 'side1', 'side2'], icon: 'fas fa-shapes' }
        ],
        '3d': [
            { id: 'cube', name: 'Куб', params: ['side'], icon: 'fas fa-cube' },
            { id: 'sphere', name: 'Шар', params: ['radius'], icon: 'fas fa-globe' },
            { id: 'cylinder', name: 'Цилиндр', params: ['radius', 'height'], icon: 'fas fa-drum' },
            { id: 'cone', name: 'Конус', params: ['radius', 'height'], icon: 'fas fa-ice-cream' },
            { id: 'pyramid', name: 'Пирамида', params: ['baseSide', 'height'], icon: 'fas fa-mountain' },
            { id: 'parallelepiped', name: 'Параллелепипед', params: ['length', 'width', 'height'], icon: 'fas fa-box' }
        ]
    },

    // Инициализация приложения
    init: function() {
        this.initDOM();
        this.bindEvents();
        this.showWelcomeMessage();
    },

    // Инициализация DOM элементов
    initDOM: function() {
        this.categorySelect = document.getElementById('category');
        this.figureSelect = document.getElementById('figure');
        this.paramsContainer = document.getElementById('parameters-container');
        this.calculateBtn = document.getElementById('calculate-btn');
        this.aboutBtn = document.getElementById('about-btn');
        this.resetBtn = document.getElementById('reset-btn');
        this.resultsContainer = document.getElementById('results');
        this.resultsOutput = document.getElementById('results-output');
        this.figureInfo = document.getElementById('figure-info');
        this.modal = document.getElementById('about-modal');
        this.closeModal = document.querySelector('.close-modal');
    },

    // Привязка событий
    bindEvents: function() {
        this.categorySelect.addEventListener('change', (e) => this.onCategoryChange(e));
        this.figureSelect.addEventListener('change', (e) => this.onFigureChange(e));
        this.calculateBtn.addEventListener('click', () => this.calculate());
        this.aboutBtn.addEventListener('click', () => this.showAboutModal());
        this.resetBtn.addEventListener('click', () => this.resetForm());
        this.closeModal.addEventListener('click', () => this.hideAboutModal());
        
        // Закрытие модального окна при клике вне его
        window.addEventListener('click', (e) => {
            if (e.target === this.modal) {
                this.hideAboutModal();
            }
        });
    },

    // Обработка изменения категории
    onCategoryChange: function(event) {
        const category = event.target.value;
        
        if (!category) {
            this.figureSelect.innerHTML = '<option value="">Сначала выберите категорию</option>';
            this.figureSelect.disabled = true;
            this.clearParameters();
            this.disableCalculateBtn();
            return;
        }
        
        // Заполнение списка фигур
        this.figureSelect.innerHTML = '<option value="">Выберите фигуру</option>';
        this.figures[category].forEach(figure => {
            const option = document.createElement('option');
            option.value = figure.id;
            option.textContent = figure.name;
            this.figureSelect.appendChild(option);
        });
        
        this.figureSelect.disabled = false;
        this.clearParameters();
        this.disableCalculateBtn();
    },

    // Обработка изменения фигуры
    onFigureChange: function(event) {
        const category = this.categorySelect.value;
        const figureId = event.target.value;
        
        if (!figureId) {
            this.clearParameters();
            this.disableCalculateBtn();
            return;
        }
        
        // Находим выбранную фигуру
        const figure = this.figures[category].find(f => f.id === figureId);
        
        // Отображаем параметры
        this.displayParameters(figure);
        
        // Показываем информацию о фигуре
        this.displayFigureInfo(figure);
        
        // Активируем кнопку расчета
        this.enableCalculateBtn();
    },

    // Отображение полей для ввода параметров
    displayParameters: function(figure) {
        this.clearParameters();
        
        const paramsConfig = {
            'side': { label: 'Сторона', unit: 'см', icon: 'fas fa-ruler' },
            'length': { label: 'Длина', unit: 'см', icon: 'fas fa-ruler-vertical' },
            'width': { label: 'Ширина', unit: 'см', icon: 'fas fa-ruler-horizontal' },
            'height': { label: 'Высота', unit: 'см', icon: 'fas fa-mountain' },
            'radius': { label: 'Радиус', unit: 'см', icon: 'fas fa-circle' },
            'sideA': { label: 'Сторона A', unit: 'см', icon: 'fas fa-ruler' },
            'sideB': { label: 'Сторона B', unit: 'см', icon: 'fas fa-ruler' },
            'sideC': { label: 'Сторона C', unit: 'см', icon: 'fas fa-ruler' },
            'base1': { label: 'Основание 1', unit: 'см', icon: 'fas fa-ruler' },
            'base2': { label: 'Основание 2', unit: 'см', icon: 'fas fa-ruler' },
            'side1': { label: 'Боковая сторона 1', unit: 'см', icon: 'fas fa-ruler' },
            'side2': { label: 'Боковая сторона 2', unit: 'см', icon: 'fas fa-ruler' },
            'baseSide': { label: 'Сторона основания', unit: 'см', icon: 'fas fa-ruler' }
        };
        
        figure.params.forEach(param => {
            const config = paramsConfig[param];
            if (!config) return;
            
            const paramDiv = document.createElement('div');
            paramDiv.className = 'form-group';
            
            paramDiv.innerHTML = `
                <label for="param-${param}">
                    <i class="${config.icon}"></i> ${config.label}:
                </label>
                <div class="input-with-unit">
                    <input type="number" 
                           id="param-${param}" 
                           class="form-input" 
                           placeholder="Введите ${config.label.toLowerCase()}" 
                           min="0.01" 
                           step="0.01" 
                           required>
                    <span class="input-unit">${config.unit}</span>
                </div>
                <div class="input-hint">Введите положительное число</div>
            `;
            
            this.paramsContainer.appendChild(paramDiv);
        });
        
        // Добавляем валидацию в реальном времени
        this.addInputValidation();
    },

    // Вычисление параметров фигуры
    calculate: function() {
        const category = this.categorySelect.value;
        const figureId = this.figureSelect.value;
        const figure = this.figures[category].find(f => f.id === figureId);
        
        // Собираем параметры
        const params = {};
        let isValid = true;
        
        figure.params.forEach(param => {
            const input = document.getElementById(`param-${param}`);
            const value = parseFloat(input.value);
            
            if (isNaN(value) || value <= 0) {
                this.showInputError(input, 'Введите положительное число');
                isValid = false;
            } else {
                this.clearInputError(input);
                params[param] = value;
            }
        });
        
        if (!isValid) {
            this.showNotification('Пожалуйста, проверьте введенные данные', 'error');
            return;
        }
        
        // Выполняем расчет
        const startTime = performance.now();
        const results = this.calculateFigure(figureId, params, category);
        const endTime = performance.now();
        const calcTime = (endTime - startTime).toFixed(3);
        
        // Отображаем результаты
        this.displayResults(results, calcTime);
        
        // Показываем контейнер результатов
        this.resultsContainer.style.display = 'block';
        
        // Прокручиваем к результатам
        this.resultsContainer.scrollIntoView({ behavior: 'smooth' });
        
        this.showNotification('Расчет выполнен успешно!', 'success');
    },

    // Логика расчета для разных фигур
    calculateFigure: function(figureId, params, category) {
        const results = {};
        
        if (category === '2d') {
            switch(figureId) {
                case 'square':
                    results.perimeter = 4 * params.side;
                    results.area = params.side * params.side;
                    break;
                case 'rectangle':
                    results.perimeter = 2 * (params.length + params.width);
                    results.area = params.length * params.width;
                    break;
                case 'triangle':
                    const s = (params.sideA + params.sideB + params.sideC) / 2;
                    results.perimeter = params.sideA + params.sideB + params.sideC;
                    results.area = Math.sqrt(s * (s - params.sideA) * (s - params.sideB) * (s - params.sideC));
                    break;
                case 'circle':
                    results.perimeter = 2 * Math.PI * params.radius;
                    results.area = Math.PI * params.radius * params.radius;
                    break;
                case 'rhombus':
                    results.perimeter = 4 * params.side;
                    results.area = params.side * params.height;
                    break;
                case 'trapezoid':
                    results.perimeter = params.base1 + params.base2 + params.side1 + params.side2;
                    results.area = ((params.base1 + params.base2) / 2) * params.height;
                    break;
            }
        } else {
            switch(figureId) {
                case 'cube':
                    results.surfaceArea = 6 * params.side * params.side;
                    results.volume = params.side * params.side * params.side;
                    break;
                case 'sphere':
                    results.surfaceArea = 4 * Math.PI * params.radius * params.radius;
                    results.volume = (4/3) * Math.PI * Math.pow(params.radius, 3);
                    break;
                case 'cylinder':
                    results.surfaceArea = 2 * Math.PI * params.radius * (params.radius + params.height);
                    results.volume = Math.PI * params.radius * params.radius * params.height;
                    break;
                case 'cone':
                    const slantHeight = Math.sqrt(params.radius * params.radius + params.height * params.height);
                    results.surfaceArea = Math.PI * params.radius * (params.radius + slantHeight);
                    results.volume = (1/3) * Math.PI * params.radius * params.radius * params.height;
                    break;
                case 'pyramid':
                    const baseArea = params.baseSide * params.baseSide;
                    const triangleArea = (params.baseSide * params.height) / 2;
                    results.surfaceArea = baseArea + (4 * triangleArea);
                    results.volume = (1/3) * baseArea * params.height;
                    break;
                case 'parallelepiped':
                    results.surfaceArea = 2 * (params.length * params.width + 
                                             params.length * params.height + 
                                             params.width * params.height);
                    results.volume = params.length * params.width * params.height;
                    break;
            }
        }
        
        return results;
    },

    // Отображение результатов
    displayResults: function(results, calcTime) {
        const category = this.categorySelect.value;
        const figureId = this.figureSelect.value;
        const figure = this.figures[category].find(f => f.id === figureId);
        
        let html = `<div class="results-grid">`;
        
        if (category === '2d') {
            html += `
                <div class="result-item">
                    <i class="fas fa-ruler-combined"></i>
                    <div class="result-value">${results.perimeter.toFixed(2)} см</div>
                    <div class="result-label">Периметр</div>
                </div>
                <div class="result-item">
                    <i class="fas fa-border-style"></i>
                    <div class="result-value">${results.area.toFixed(2)} см²</div>
                    <div class="result-label">Площадь</div>
                </div>
            `;
        } else {
            html += `
                <div class="result-item">
                    <i class="fas fa-layer-group"></i>
                    <div class="result-value">${results.surfaceArea.toFixed(2)} см²</div>
                    <div class="result-label">Площадь поверхности</div>
                </div>
                <div class="result-item">
                    <i class="fas fa-weight-hanging"></i>
                    <div class="result-value">${results.volume.toFixed(2)} см³</div>
                    <div class="result-label">Объем</div>
                </div>
            `;
        }
        
        html += `</div>`;
        
        this.resultsOutput.innerHTML = html;
        document.getElementById('calc-time').textContent = `Время расчета: ${calcTime} сек`;
    },

    // Показ информации о фигуре
    displayFigureInfo: function(figure) {
        const info = {
            'square': 'Квадрат - правильный четырёхугольник, у которого все стороны равны и все углы прямые.',
            'rectangle': 'Прямоугольник - четырёхугольник, у которого все углы прямые.',
            'triangle': 'Треугольник - геометрическая фигура, образованная тремя отрезками.',
            'circle': 'Круг - геометрическое место точек плоскости, равноудаленных от заданной точки.',
            'cube': 'Куб - правильный многогранник, каждая грань которого представляет собой квадрат.',
            'sphere': 'Шар - геометрическое тело, совокупность всех точек пространства, находящихся от центра на расстоянии не больше заданного.',
            'cylinder': 'Цилиндр - геометрическое тело, ограниченное цилиндрической поверхностью и двумя параллельными плоскостями.',
            'cone': 'Конус - тело, полученное объединением всех лучей, исходящих из одной точки и проходящих через плоскую поверхность.'
        };
        
        this.figureInfo.innerHTML = `
            <div class="info-card">
                <div class="info-header">
                    <i class="${figure.icon}"></i>
                    <h4>${figure.name}</h4>
                </div>
                <p>${info[figure.id] || 'Выберите параметры для расчета.'}</p>
            </div>
        `;
    },

    // Вспомогательные методы
    clearParameters: function() {
        this.paramsContainer.innerHTML = '';
        this.figureInfo.innerHTML = '';
    },

    enableCalculateBtn: function() {
        this.calculateBtn.disabled = false;
        this.calculateBtn.classList.add('btn-active');
    },

    disableCalculateBtn: function() {
        this.calculateBtn.disabled = true;
        this.calculateBtn.classList.remove('btn-active');
    },

    showAboutModal: function() {
        this.modal.style.display = 'block';
        document.body.style.overflow = 'hidden';
    },

    hideAboutModal: function() {
        this.modal.style.display = 'none';
        document.body.style.overflow = 'auto';
    },

    resetForm: function() {
        this.categorySelect.selectedIndex = 0;
        this.figureSelect.innerHTML = '<option value="">Сначала выберите категорию</option>';
        this.figureSelect.disabled = true;
        this.clearParameters();
        this.disableCalculateBtn();
        this.resultsContainer.style.display = 'none';
        this.showNotification('Форма сброшена', 'info');
    },

    showNotification: function(message, type) {
        // Создаем уведомление
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.innerHTML = `
            <i class="fas fa-${type === 'success' ? 'check-circle' : 
                              type === 'error' ? 'exclamation-circle' : 
                              'info-circle'}"></i>
            <span>${message}</span>
        `;
        
        document.body.appendChild(notification);
        
        // Показываем с анимацией
        setTimeout(() => notification.classList.add('show'), 10);
        
        // Удаляем через 3 секунды
        setTimeout(() => {
            notification.classList.remove('show');
            setTimeout(() => notification.remove(), 300);
        }, 3000);
    },

    showWelcomeMessage: function() {
        // Показываем приветственное сообщение при загрузке
        setTimeout(() => {
            this.showNotification('Добро пожаловать в Geometry Calculator!', 'info');
        }, 1000);
    },

    addInputValidation: function() {
        const inputs = document.querySelectorAll('.form-input');
        inputs.forEach(input => {
            input.addEventListener('input', (e) => {
                const value = parseFloat(e.target.value);
                if (isNaN(value) || value <= 0) {
                    this.showInputError(e.target, 'Введите положительное число');
                } else {
                    this.clearInputError(e.target);
                }
            });
        });
    },

    showInputError: function(input, message) {
        const hint = input.parentElement.nextElementSibling;
        hint.textContent = message;
        hint.classList.add('error');
        input.classList.add('input-error');
    },

    clearInputError: function(input) {
        const hint = input.parentElement.nextElementSibling;
        hint.textContent = 'Введите положительное число';
        hint.classList.remove('error');
        input.classList.remove('input-error');
    }
};

// Инициализация приложения при загрузке DOM
document.addEventListener('DOMContentLoaded', function() {
    // Инициализация Cordova
    document.addEventListener('deviceready', function() {
        console.log('Cordova готов к работе');
        GeometryApp.init();
    }, false);
    
    // Для отладки в браузере
    if (typeof cordova === 'undefined') {
        GeometryApp.init();
    }
});