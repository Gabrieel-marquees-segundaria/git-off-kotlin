
// ========================================
// MÓDULO 5: Gerenciador de Botão
// ========================================
(function() {
    'use strict';

    const FloatingMenuButton = {
        initialized: false,
        element: null,
        isOpen: false,

        init() {
            if (this.initialized) return;

            try {
                this.create();
                this.addEventListeners();
                this.initialized = true;
            } catch (error) {
                console.error('Erro ao inicializar FloatingMenuButton:', error);
            }
        },

        create() {
            const config = window.FloatingMenuSystem.getDependency('FloatingMenuConfig').button;
            const utils = window.FloatingMenuSystem.getDependency('FloatingMenuUtils');

            this.element = utils.createElement(
                "button",
                "floating-menu-button",
                `<div class="burger-icon">
                    <span></span>
                    <span></span>
                    <span></span>
                </div>`
            );

            // Aplicar estilos
            const styles = {
                position: "fixed",
                top: config.position.top,
                left: config.position.left,
                width: config.size.width,
                height: config.size.height,
                ...config.styles
            };

            utils.applyStyles(this.element, styles);

            // Verificar se o body existe
            if (document.body) {
                document.body.appendChild(this.element);
            } else {
                document.addEventListener('DOMContentLoaded', () => {
                    document.body.appendChild(this.element);
                });
            }
        },

        addEventListeners() {
            if (!this.element) return;

            // Hover effects
            this.element.addEventListener("mouseenter", () => {
                this.element.style.transform = "scale(1.1)";
                this.element.style.boxShadow = "0 6px 25px rgba(52, 152, 219, 0.5)";
            });

            this.element.addEventListener("mouseleave", () => {
                if (!this.isOpen) {
                    this.element.style.transform = "scale(1)";
                    this.element.style.boxShadow = "0 4px 20px rgba(52, 152, 219, 0.4)";
                }
            });
        },

        setOpenState(isOpen) {
            this.isOpen = isOpen;
            if (!this.element) return;

            const burgerIcon = this.element.querySelector(".burger-icon");
            if (!burgerIcon) return;

            if (isOpen) {
                this.element.style.transform = "scale(1.1)";
                this.element.style.boxShadow = "0 6px 25px rgba(52, 152, 219, 0.5)";
                burgerIcon.classList.add("open");
            } else {
                this.element.style.transform = "scale(1)";
                this.element.style.boxShadow = "0 4px 20px rgba(52, 152, 219, 0.4)";
                burgerIcon.classList.remove("open");
            }
        }
    };

    window.FloatingMenuSystem.register('FloatingMenuButton', FloatingMenuButton);
})();
