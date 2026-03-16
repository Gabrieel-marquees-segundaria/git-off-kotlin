
// ========================================
// MÓDULO 6: Gerenciador de Container
// ========================================
(function() {
    'use strict';

    const FloatingMenuContainer = {
        initialized: false,
        element: null,
        menuItems: [],

        init() {
            if (this.initialized) return;

            try {
                this.create();
                this.addMenuItems();
                this.initialized = true;
            } catch (error) {
                console.error('Erro ao inicializar FloatingMenuContainer:', error);
            }
        },

        create() {
            const config = window.FloatingMenuSystem.getDependency('FloatingMenuConfig');
            const utils = window.FloatingMenuSystem.getDependency('FloatingMenuUtils');

            this.element = utils.createElement("div", "floating-menu-container");
            utils.applyStyles(this.element, config.container.styles);

            // Verificar se o body existe
            if (document.body) {
                document.body.appendChild(this.element);
            } else {
                document.addEventListener('DOMContentLoaded', () => {
                    document.body.appendChild(this.element);
                });
            }
        },

        addMenuItems() {
            const config = window.FloatingMenuSystem.getDependency('FloatingMenuConfig');

            config.menuItems.forEach((item, index) => {
                const menuItem = this.createMenuItem(item, index);
                if (menuItem) {
                    this.element.appendChild(menuItem);
                    this.menuItems.push(menuItem);
                }
            });
        },

        createMenuItem(item, index) {
            const utils = window.FloatingMenuSystem.getDependency('FloatingMenuUtils');
            const config = window.FloatingMenuSystem.getDependency('FloatingMenuConfig');

            const menuItem = utils.createElement(
                "div",
                "floating-menu-item",
                `<div class="menu-item-icon">${item.icon}</div>
                 <div class="menu-item-text">${item.text}</div>`
            );

            // Estilos do item
            const styles = {
                display: "flex",
                alignItems: "center",
                padding: "6px 8px",
                backgroundColor: "rgba(255, 255, 255, 0.95)",
                backdropFilter: "blur(10px)",
                borderRadius: "12px",
                marginBottom: `${config.animations.itemMargin}px`,
                cursor: "pointer",
                transition: "all 0.2s ease",
                transform: `translateY(${(index + 1) * 10}px) scale(0.8)`,
                opacity: "0",
                boxShadow: "0 4px 15px rgba(0, 0, 0, 0.1)",
                minWidth: "180px"
            };

            utils.applyStyles(menuItem, styles);

            // Event listeners
            this.addItemEventListeners(menuItem, item, index);

            return menuItem;
        },

        addItemEventListeners(menuItem, item, index) {
            const config = window.FloatingMenuSystem.getDependency('FloatingMenuConfig');
            const utils = window.FloatingMenuSystem.getDependency('FloatingMenuUtils');

            // Hover effects
            menuItem.addEventListener("mouseenter", () => {
                menuItem.style.backgroundColor = "rgba(52, 152, 219, 0.1)";
                menuItem.style.transform = menuItem.style.transform.replace("scale(0.8)", "scale(1)");
            });

            menuItem.addEventListener("mouseleave", () => {
                menuItem.style.backgroundColor = "rgba(255, 255, 255, 0.95)";
                const core = window.FloatingMenuSystem.getDependency('FloatingMenuCore');
                if (core && core.isOpen) {
                    const spacing = config.animations.itemSpacing;
                    menuItem.style.transform = `translateY(${(index + 1) * spacing}px) scale(1)`;
                }
            });

            // Click action
            menuItem.addEventListener("click", () => {
                utils.executeAction(item.action);
                const core = window.FloatingMenuSystem.getDependency('FloatingMenuCore');
                if (core && core.closeMenu) {
                    core.closeMenu();
                }
            });
        },

        show() {
            if (!this.element) return;

            const config = window.FloatingMenuSystem.getDependency('FloatingMenuConfig');
            const utils = window.FloatingMenuSystem.getDependency('FloatingMenuUtils');

            this.element.style.opacity = "1";
            this.element.style.visibility = "visible";

            // Animar itens
            this.menuItems.forEach((item, index) => {
                utils.safeTimeout(() => {
                    const spacing = config.animations.itemSpacing;
                    item.style.transform = `translateY(${(index + 1) * spacing}px) scale(1)`;
                    item.style.opacity = "1";
                }, index * config.animations.animationDelay);
            });
        },

        hide() {
            if (!this.element) return;

            const utils = window.FloatingMenuSystem.getDependency('FloatingMenuUtils');

            // Esconder itens
            this.menuItems.forEach((item, index) => {
                item.style.transform = `translateY(${(index + 1) * 10}px) scale(0.8)`;
                item.style.opacity = "0";
            });

            // Esconder container após animação
            utils.safeTimeout(() => {
                this.element.style.opacity = "0";
                this.element.style.visibility = "hidden";
            }, 200);
        }
    };

    window.FloatingMenuSystem.register('FloatingMenuContainer', FloatingMenuContainer);
})();
