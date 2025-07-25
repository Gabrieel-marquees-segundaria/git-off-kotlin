class ModernSpinner {
    constructor(options = {}) {
        this.options = {
            type: options.type || 'classic', // classic, dots, wave, pulse
            text: options.text || '',
            background: options.background || 'rgba(0, 0, 0, 0.7)',
            zIndex: options.zIndex || 10000,
            color: options.color || '#ffffff',
            size: options.size || 60,
            ...options
        };

        this.overlay = null;
        this.isVisible = false;
        this.init();
    }

    init() {
        this.createOverlay();
        this.injectStyles();
    }

    injectStyles() {
        if (document.getElementById('modern-spinner-styles')) return;

        const styles = `
            .modern-spinner-overlay {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: ${this.options.background};
                backdrop-filter: blur(5px);
                display: flex;
                justify-content: center;
                align-items: center;
                z-index: ${this.options.zIndex};
                opacity: 0;
                visibility: hidden;
                transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            }

            .modern-spinner-overlay.show {
                opacity: 1;
                visibility: visible;
            }

            .modern-spinner-container {
                display: flex;
                flex-direction: column;
                align-items: center;
                gap: 20px;
                background: rgba(255, 255, 255, 0.1);
                backdrop-filter: blur(20px);
                padding: 40px;
                border-radius: 20px;
                box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
                transform: scale(0.8);
                transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            }

            .modern-spinner-overlay.show .modern-spinner-container {
                transform: scale(1);
            }

            .modern-spinner-classic {
                width: ${this.options.size}px;
                height: ${this.options.size}px;
                border: 4px solid rgba(255, 255, 255, 0.3);
                border-top: 4px solid ${this.options.color};
                border-radius: 50%;
                animation: modern-spinner-spin 1s cubic-bezier(0.68, -0.55, 0.265, 1.55) infinite;
            }

            .modern-spinner-dots {
                display: flex;
                gap: 8px;
                align-items: center;
            }

            .modern-spinner-dots div {
                width: 12px;
                height: 12px;
                background: ${this.options.color};
                border-radius: 50%;
                animation: modern-spinner-dot-pulse 1.5s ease-in-out infinite;
            }

            .modern-spinner-dots div:nth-child(1) { animation-delay: 0s; }
            .modern-spinner-dots div:nth-child(2) { animation-delay: 0.2s; }
            .modern-spinner-dots div:nth-child(3) { animation-delay: 0.4s; }

            .modern-spinner-wave {
                display: flex;
                gap: 5px;
                align-items: center;
            }

            .modern-spinner-wave div {
                width: 8px;
                height: 40px;
                background: linear-gradient(to bottom, ${this.options.color}, rgba(255, 255, 255, 0.6));
                border-radius: 4px;
                animation: modern-spinner-wave 1.2s ease-in-out infinite;
            }

            .modern-spinner-wave div:nth-child(1) { animation-delay: 0s; }
            .modern-spinner-wave div:nth-child(2) { animation-delay: 0.1s; }
            .modern-spinner-wave div:nth-child(3) { animation-delay: 0.2s; }
            .modern-spinner-wave div:nth-child(4) { animation-delay: 0.3s; }
            .modern-spinner-wave div:nth-child(5) { animation-delay: 0.4s; }

            .modern-spinner-pulse {
                width: ${this.options.size}px;
                height: ${this.options.size}px;
                background: ${this.options.color};
                border-radius: 50%;
                animation: modern-spinner-pulse-anim 1.5s ease-in-out infinite;
            }

            .modern-spinner-text {
                color: ${this.options.color};
                font-size: 18px;
                font-weight: 500;
                text-align: center;
                opacity: 0.9;
                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            }

            @keyframes modern-spinner-spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }

            @keyframes modern-spinner-dot-pulse {
                0%, 60%, 100% {
                    transform: scale(1);
                    opacity: 1;
                }
                30% {
                    transform: scale(1.4);
                    opacity: 0.7;
                }
            }

            @keyframes modern-spinner-wave {
                0%, 40%, 100% {
                    transform: scaleY(0.4);
                }
                20% {
                    transform: scaleY(1);
                }
            }

            @keyframes modern-spinner-pulse-anim {
                0% {
                    transform: scale(1);
                    opacity: 1;
                }
                50% {
                    transform: scale(1.2);
                    opacity: 0.7;
                }
                100% {
                    transform: scale(1);
                    opacity: 1;
                }
            }
        `;

        const styleSheet = document.createElement('style');
        styleSheet.id = 'modern-spinner-styles';
        styleSheet.textContent = styles;
        document.head.appendChild(styleSheet);
    }

    createOverlay() {
        this.overlay = document.createElement('div');
        this.overlay.className = 'modern-spinner-overlay';

        const container = document.createElement('div');
        container.className = 'modern-spinner-container';

        const spinner = this.createSpinner();
        container.appendChild(spinner);

        if (this.options.text) {
            const textElement = document.createElement('div');
            textElement.className = 'modern-spinner-text';
            textElement.textContent = this.options.text;
            container.appendChild(textElement);
        }

        this.overlay.appendChild(container);

        // Event listeners
        this.overlay.addEventListener('keydown', (e) => {
            if (e.key === 'Escape') {
                this.hide();
            }
        });

        this.overlay.addEventListener('click', (e) => {
            if (e.target === this.overlay) {
                this.hide();
            }
        });

        document.body.appendChild(this.overlay);
    }

    createSpinner() {
        const spinnerElement = document.createElement('div');

        switch (this.options.type) {
            case 'dots':
                spinnerElement.className = 'modern-spinner-dots';
                for (let i = 0; i < 3; i++) {
                    const dot = document.createElement('div');
                    spinnerElement.appendChild(dot);
                }
                break;
            case 'wave':
                spinnerElement.className = 'modern-spinner-wave';
                for (let i = 0; i < 5; i++) {
                    const bar = document.createElement('div');
                    spinnerElement.appendChild(bar);
                }
                break;
            case 'pulse':
                spinnerElement.className = 'modern-spinner-pulse';
                break;
            default:
                spinnerElement.className = 'modern-spinner-classic';
        }

        return spinnerElement;
    }

    show() {
        if (this.isVisible) return this;

        this.isVisible = true;
        this.overlay.style.display = 'flex';

        // Force reflow para animação
        this.overlay.offsetHeight;

        this.overlay.classList.add('show');
        document.body.style.overflow = 'hidden';

        // Focar para capturar teclas
        this.overlay.setAttribute('tabindex', '-1');
        this.overlay.focus();

        return this;
    }

    hide() {
        if (!this.isVisible) return this;

        this.isVisible = false;
        this.overlay.classList.remove('show');
        document.body.style.overflow = '';

        setTimeout(() => {
            if (!this.isVisible && this.overlay) {
                this.overlay.style.display = 'none';
            }
        }, 300);

        return this;
    }

    updateText(newText) {
        const textElement = this.overlay.querySelector('.modern-spinner-text');
        if (textElement) {
            textElement.textContent = newText;
        } else if (newText) {
            const container = this.overlay.querySelector('.modern-spinner-container');
            const textElement = document.createElement('div');
            textElement.className = 'modern-spinner-text';
            textElement.textContent = newText;
            container.appendChild(textElement);
        }
        return this;
    }

    updateType(newType) {
        const container = this.overlay.querySelector('.modern-spinner-container');
        const oldSpinner = container.firstChild;

        this.options.type = newType;
        const newSpinner = this.createSpinner();

        container.replaceChild(newSpinner, oldSpinner);
        return this;
    }

    destroy() {
        this.hide();
        setTimeout(() => {
            if (this.overlay && this.overlay.parentNode) {
                this.overlay.parentNode.removeChild(this.overlay);
            }
        }, 300);
    }

    // Método para uso com Promise
    async showFor(duration) {
        this.show();
        return new Promise(resolve => {
            setTimeout(() => {
                this.hide();
                resolve();
            }, duration);
        });
    }

    // Método estático para uso rápido
    static show(options = {}) {
        const spinner = new ModernSpinner(options);
        spinner.show();
        return spinner;
    }

    // Método estático para mostrar por tempo determinado
    static async showFor(duration, options = {}) {
        const spinner = new ModernSpinner(options);
        await spinner.showFor(duration);
        spinner.destroy();
        return spinner;
    }
}
let spinner = null
// Exemplos de uso:

// Uso básico
// const spinner = ModernSpinner.show({ text: 'Carregando...' });

// Com opções customizadas
// const spinner = new ModernSpinner({
//     type: 'wave',
//     text: 'Processando dados...',
//     color: '#ff6b6b',
//     size: 80
// }).show();

// Atualizar texto
// spinner.updateText('Nova mensagem');

// Usar com async/await
// async function exemploAsync() {
//     const spinner = ModernSpinner.show({ text: 'Conectando...' });
//
//     try {
//         await fetch('/api/data');
//         spinner.updateText('Processando...');
//         await processData();
//         spinner.hide();
//     } catch (error) {
//         spinner.updateText('Erro: ' + error.message);
//         setTimeout(() => spinner.hide(), 2000);
//     }
// }

// Mostrar por tempo determinado
// ModernSpinner.showFor(3000, { type: 'pulse', text: 'Salvando...' });