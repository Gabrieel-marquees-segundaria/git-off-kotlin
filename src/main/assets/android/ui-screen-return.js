
class ReturnButton {
    constructor(options = {}) {
        // Configurações padrão
        this.config = {
            id: 'returnButton',
            bottom: '30px',
            left: '20px',
            width: '55px',
            height: '55px',
            backgroundColor: '#007bff',
            hoverColor: '#0056b3',
            icon: '↩',
            title: 'Voltar',
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
        this.addEventListeners();

        // Adicionar o botão ao documento
        document.body.appendChild(this.button);
        this.isCreated = true;
        console.log('Botão return criado e adicionado ao DOM');

        return this.button;
    }
    
    applyStyles() {
        this.button.style.cssText = `
            position: fixed !important;
            bottom: ${this.config.bottom} !important;
            left: ${this.config.left} !important;
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
    
    addEventListeners() {
        // Efeito hover
        this.button.addEventListener('mouseenter', () => {
            this.button.style.backgroundColor = this.config.hoverColor;
            this.button.style.transform = 'scale(1.1)';
            this.button.style.boxShadow = '0 6px 16px rgba(0, 0, 0, 0.4)';
        });

        this.button.addEventListener('mouseleave', () => {
            this.button.style.backgroundColor = this.config.backgroundColor;
            this.button.style.transform = 'scale(1)';
            this.button.style.boxShadow = '0 4px 12px rgba(0, 0, 0, 0.3)';
        });

        // Funcionalidade de clique
        this.button.addEventListener('click', () => {
            this.handleClick();
            //if(this.Android.uriReturn) Android.uriReturn()
        });
    }
    
    handleClick() {
        console.log('Botão return clicado!');
        
        // Ação padrão do botão
        if (window.history.length > 1) {
            window.history.back();

        } else {
            alert('Botão return funcionando! Não há página anterior.');
        }
    }
    
    // Métodos públicos para controle do botão
    show() {
        if (this.button) {
            this.button.style.display = 'flex';
        }
    }
    
    hide() {
        if (this.button) {
            this.button.style.display = 'none';
        }
    }
    
    remove() {
        if (this.button) {
            this.button.remove();
            this.button = null;
            this.isCreated = false;
        }
    }
    
    // Atualizar configurações
    updateConfig(newConfig) {
        this.config = { ...this.config, ...newConfig };
        if (this.isCreated) {
            this.applyStyles();
            this.button.innerHTML = this.config.icon;
            this.button.title = this.config.title;
        }
    }
    
    // Personalizar ação do clique
    setClickHandler(callback) {
        if (this.button && typeof callback === 'function') {
            // Remove o listener anterior
            this.button.removeEventListener('click', this.handleClick);
            // Adiciona o novo
            this.button.addEventListener('click', callback);
        }
    }
    
    // Getter para verificar se está criado
    get created() {
        return this.isCreated;
    }
    
    // Getter para o elemento button
    get element() {
        return this.button;
    }
}

// Exemplo de uso:

// Uso básico
const returnBtn = new ReturnButton();
setTimeout(()=> returnBtn.hide(), 1000)

function show_returnButton(){
returnBtn.show()
}
function hide_returnButton(){
returnBtn.hide()

}
function callback_returnButton(callback){
returnBtn.setClickHandler(uriReturn)
}

// Uso com configurações personalizadas
/*
const customReturnBtn = new ReturnButton({
    bottom: '50px',
    left: '30px',
    backgroundColor: '#28a745',
    hoverColor: '#1e7e34',
    icon: '🔙',
    title: 'Voltar para página anterior'
});
*/

// Exemplos de métodos disponíveis:
/*
returnBtn.hide();           // Esconder botão
returnBtn.show();           // Mostrar botão
returnBtn.remove();         // Remover botão completamente

// Personalizar ação do clique
returnBtn.setClickHandler(() => {
    console.log('Ação personalizada!');
    window.location.href = '/';
});

// Atualizar configurações
returnBtn.updateConfig({
    backgroundColor: '#dc3545',
    icon: '❌'
});
*/


