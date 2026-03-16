// main.js - Arquivo principal que inicializa o menu
import { FloatingMenu } from './FloatingMenu.js';

// Inicializar o menu quando a página carregar
document.addEventListener("DOMContentLoaded", () => {
    new FloatingMenu();
});

// Também pode ser usado para configurações globais
window.FloatingMenuApp = {
    menu: null,
    
    init() {
        this.menu = new FloatingMenu();
        return this.menu;
    },
    
    destroy() {
        if (this.menu) {
            // Implementar lógica de cleanup se necessário
            this.menu = null;
        }
    }
};