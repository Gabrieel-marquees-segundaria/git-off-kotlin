// ========================================
// MÓDULO 1: Sistema de Dependências
// ========================================
(function() {
    'use strict';

    window.FloatingMenuSystem = {
        modules: {},
        dependencies: {
            'FloatingMenuStyles': [],
            'FloatingMenuConfig': [],
            'FloatingMenuUtils': ['FloatingMenuConfig'],
            'FloatingMenuButton': ['FloatingMenuConfig', 'FloatingMenuUtils'],
            'FloatingMenuContainer': ['FloatingMenuConfig', 'FloatingMenuUtils'],
            'FloatingMenuCore': ['FloatingMenuButton', 'FloatingMenuContainer'],
            'FloatingMenuApp': ['FloatingMenuCore']
        },

        register(name, module) {
            this.modules[name] = module;
            this.checkAndInitialize(name);
        },

        checkAndInitialize(name) {
            const deps = this.dependencies[name] || [];
            const allDepsReady = deps.every(dep => this.modules[dep]);

            if (allDepsReady && this.modules[name] && !this.modules[name].initialized) {
                if (typeof this.modules[name].init === 'function') {
                    this.modules[name].init();
                }
                this.modules[name].initialized = true;
                console.log(`${name} inicializado com sucesso!`);
            }
        },

        getDependency(name) {
            return this.modules[name];
        }
    };
})();

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

// ========================================
// MÓDULO 3: Configurações e Constantes
// ========================================
(function() {
    'use strict';

    const FloatingMenuConfig = {
        initialized: false,

        init() {
            if (this.initialized) return;
            this.initialized = true;
        },

        button: {
            position: { top: "40px", left: "20px" },
            size: { width: "60px", height: "60px" },
            styles: {
                borderRadius: "50%",
                background: "linear-gradient(135deg, #3498db, #2980b9)",
                border: "none",
                cursor: "pointer",
                zIndex: "1000",
                boxShadow: "0 4px 20px rgba(52, 152, 219, 0.4)",
                transition: "all 0.3s ease",
                display: "flex",
                alignItems: "center",
                justifyContent: "center"
            }
        },

        container: {
            styles: {
                position: "fixed",
                top: "40px",
                left: "20px",
                zIndex: "999",
                opacity: "0",
                visibility: "hidden",
                transition: "all 0.3s ease",
                transformOrigin: "top left"
            }
        },

        menuItems: [
            {
                icon: "🗂️",
                text: "Selecionar Pasta",
                action: "abrirPastaAndroid"
            },
            {
                icon: "📋️",
                text: "Listar Arquivos",
                action: "listar_arquivos"
            },
            {
                icon: "✏️",
                text: "Alterar Título",
                action: "alterarTitulo"
            },
            {
                icon: "🗑️",
                text: "Limpar",
                action: "limparResultado"
            }
        ],

        animations: {
            itemSpacing: 50,
            itemMargin: 4,
            animationDelay: 100
        }
    };

    window.FloatingMenuSystem.register('FloatingMenuConfig', FloatingMenuConfig);
})();

// ========================================
// MÓDULO 4: Utilitários e Helpers
// ========================================
(function() {
    'use strict';

    const FloatingMenuUtils = {
        initialized: false,

        init() {
            if (this.initialized) return;
            this.initialized = true;
        },

        applyStyles(element, styles) {
            if (!element || !styles) return;
            Object.assign(element.style, styles);
        },

        createElement(tag, className, innerHTML) {
            const element = document.createElement(tag);
            if (className) element.className = className;
            if (innerHTML) element.innerHTML = innerHTML;
            return element;
        },

        alterarTitulo() {
            const titleElement = document.getElementById("title");
            if (titleElement) {
                const novoTitulo = prompt(
                    "Digite o novo título:",
                    titleElement.textContent || titleElement.innerText || ""
                );
                if (novoTitulo !== null && novoTitulo.trim() !== "") {
                    titleElement.textContent = novoTitulo.trim();
                }
            } else {
                alert('Elemento com id "title" não encontrado na página.');
            }
        },

        executeAction(actionName) {
            try {
                if (actionName === 'alterarTitulo') {
                    this.alterarTitulo();
                } else if (typeof window[actionName] === 'function') {
                    window[actionName]();
                } else {
                    console.warn(`Ação "${actionName}" não encontrada`);
                }
            } catch (error) {
                console.error(`Erro ao executar ação "${actionName}":`, error);
            }
        },

        safeTimeout(callback, delay) {
            return setTimeout(() => {
                try {
                    callback();
                } catch (error) {
                    console.error('Erro no timeout:', error);
                }
            }, delay);
        }
    };

    window.FloatingMenuSystem.register('FloatingMenuUtils', FloatingMenuUtils);
})();

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

// ========================================
// MÓDULO 8: Inicializador Global
// ========================================
(function() {
    'use strict';

    const FloatingMenuApp = {
        initialized: false,

        init() {
            if (this.initialized) return;

            try {
                // Verificar se todos os módulos estão disponíveis
                const core = window.FloatingMenuSystem.getDependency('FloatingMenuCore');

                if (core && core.initialized) {
                    this.initialized = true;
                    console.log('FloatingMenu App inicializado com sucesso!');
                } else {
                    // Tentar novamente em 100ms
                    setTimeout(() => this.init(), 100);
                }
            } catch (error) {
                console.error('Erro ao inicializar FloatingMenuApp:', error);
            }
        }
    };

    window.FloatingMenuSystem.register('FloatingMenuApp', FloatingMenuApp);

    // Auto-executar quando a página carregar
    function startApp() {
        if (document.readyState === 'loading') {
            document.addEventListener('DOMContentLoaded', () => {
                FloatingMenuApp.init();
            });
        } else {
            FloatingMenuApp.init();
        }
    }

    // Iniciar imediatamente
    startApp();
})();

// ========================================
// MÓDULO 9: Verificador de Integridade
// ========================================
(function() {
    'use strict';

    // Verificar se todos os módulos foram carregados após 2 segundos
    setTimeout(() => {
        const system = window.FloatingMenuSystem;
        const expectedModules = [
            'FloatingMenuStyles',
            'FloatingMenuConfig',
            'FloatingMenuUtils',
            'FloatingMenuButton',
            'FloatingMenuContainer',
            'FloatingMenuCore',
            'FloatingMenuApp'
        ];

        let allLoaded = true;
        expectedModules.forEach(module => {
            if (!system.modules[module] || !system.modules[module].initialized) {
                console.warn(`Módulo ${module} não foi inicializado corretamente`);
                allLoaded = false;
            }
        });

        if (allLoaded) {
            console.log('✅ Todos os módulos do FloatingMenu foram carregados com sucesso!');
        } else {
            console.warn('⚠️ Alguns módulos do FloatingMenu falharam ao carregar');
        }
    }, 2000);
})();