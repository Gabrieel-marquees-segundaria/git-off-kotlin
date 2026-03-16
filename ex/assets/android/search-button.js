class SearchButton {
    constructor(options = {}) {
        // Configurações padrão
        this.config = {
            id: 'search-button',
            top: '30px',
            right: '20px',
            width: '55px',
            height: '55px',
            backgroundColor: '#007bff',
            hoverColor: '#0056b3',
            icon: '🔍',
            title: 'search',
            fontSize: '24px',
            zIndex: 9990,
            ...options
        };

        this.button = null;
        this.isCreated = false;
        this.callback = null

      this.init();
    }
    init() {
        // Executar imediatamente quando o script carregar
        if (document.readyState === 'loading') {
            document.addEventListener('DOMContentLoaded', () => this.create());
        } else {
            this.create();
        }

        // Garantir que o botão seja criado após um pequeno delay
        setTimeout(() => this.create(), 100);
        console.log("ui-screen-return online");
    }

    applyStyles() {
        this.button.style.cssText = `
            position: fixed !important;
            top: ${this.config.top} !important;
            right: ${this.config.right} !important;
            width: ${this.config.width} !important;
            height: ${this.config.height} !important;
            border-radius: 50% !important;
            border: 2px solid #fff !important;
            background-color: ${this.config.backgroundColor} !important;
            color: white !important;
            font-size: ${this.config.fontSize} !important;
            cursor: pointer !important;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3) !important;
            z-index: ${this.config.zIndex} !important;
            transition: all 0.3s ease !important;
            display: flex !important;
            align-items: center !important;
            justify-content: center !important;
            outline: none !important;
            font-weight: bold !important;
        `;
    }
        create() {
            // Verificar se já existe um botão
            const existingButton = document.getElementById(this.config.id);
            if (existingButton) {
                existingButton.remove();
            }

            this.button = document.createElement('button');
            this.button.id = this.config.id;

            // Configurar o conteúdo do botão
            this.button.innerHTML = this.config.icon;
            this.button.title = this.config.title;

            // Aplicar estilos
            this.applyStyles();

            // Adicionar event listeners


            // Adicionar o botão ao documento
            document.body.appendChild(this.button);
            this.isCreated = true;
            console.log('Botão return criado e adicionado ao DOM');

            return this.button;
        }
}

const searchButton = new SearchButton();
