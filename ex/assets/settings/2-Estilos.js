
// ========================================
// MÓDULO 2: Gerenciador de Estilos
// ========================================
(function() {
    'use strict';

    const FloatingMenuStyles = {
        initialized: false,

        init() {
            if (this.initialized) return;
            this.addBurgerStyles();
            this.initialized = true;
        },

        addBurgerStyles() {
            if (document.getElementById('floating-menu-styles')) return;

            const style = document.createElement("style");
            style.id = 'floating-menu-styles';
            style.textContent = `
                .floating-menu-button {
                    position: fixed !important;
                }

                .burger-icon {
                    width: 24px;
                    height: 18px;
                    position: relative;
                    display: flex;
                    flex-direction: column;
                    justify-content: space-between;
                }

                .burger-icon span {
                    display: block;
                    height: 2px;
                    width: 100%;
                    background: white;
                    border-radius: 1px;
                    transition: all 0.3s ease;
                }

                .burger-icon.open span:nth-child(1) {
                    transform: rotate(45deg) translate(5px, 5px);
                }

                .burger-icon.open span:nth-child(2) {
                    opacity: 0;
                }

                .burger-icon.open span:nth-child(3) {
                    transform: rotate(-45deg) translate(7px, -6px);
                }

                .menu-item-icon {
                    font-size: 18px;
                    margin-right: 12px;
                }

                .menu-item-text {
                    font-size: 14px;
                    font-weight: 500;
                    color: #2c3e50;
                }

                .floating-menu-container {
                    position: fixed !important;
                }

                .floating-menu-item {
                    user-select: none;
                    -webkit-user-select: none;
                }
            `;

            document.head.appendChild(style);
        }
    };

    window.FloatingMenuSystem.register('FloatingMenuStyles', FloatingMenuStyles);
})();
