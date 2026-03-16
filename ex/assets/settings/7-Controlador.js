
// ========================================
// MÓDULO 7: Controlador Principal
// ========================================
(function() {
    'use strict';

    const FloatingMenuCore = {
        initialized: false,
        isOpen: false,
        button: null,
        container: null,

        init() {
            if (this.initialized) return;

            try {
                this.button = window.FloatingMenuSystem.getDependency('FloatingMenuButton');
                this.container = window.FloatingMenuSystem.getDependency('FloatingMenuContainer');

                if (this.button && this.container) {
                    this.addEventListeners();
                    this.initialized = true;
                }
            } catch (error) {
                console.error('Erro ao inicializar FloatingMenuCore:', error);
            }
        },

        addEventListeners() {
            if (!this.button || !this.button.element) return;

            // Toggle menu no clique do botão
            this.button.element.addEventListener("click", (e) => {
                e.stopPropagation();
                this.toggleMenu();
            });

            // Fechar menu ao clicar fora
            document.addEventListener("click", (e) => {
                if (this.container && this.container.element &&
                    !this.container.element.contains(e.target) &&
                    !this.button.element.contains(e.target)) {
                    this.closeMenu();
                }
            });
        },

        toggleMenu() {
            if (this.isOpen) {
                this.closeMenu();
            } else {
                this.openMenu();
            }
        },

        openMenu() {
            this.isOpen = true;
            if (this.button) this.button.setOpenState(true);
            if (this.container) this.container.show();
        },

        closeMenu() {
            this.isOpen = false;
            if (this.button) this.button.setOpenState(false);
            if (this.container) this.container.hide();
        }
    };

    window.FloatingMenuSystem.register('FloatingMenuCore', FloatingMenuCore);
})();
