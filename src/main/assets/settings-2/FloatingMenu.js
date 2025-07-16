// FloatingMenu.js - Classe principal do menu flutuante
class FloatingMenu {
    constructor() {
        this.isOpen = false;
        this.menuButton = null;
        this.menuContainer = null;
        this.menuItems = [];
        this.init();
    }

    init() {
        this.createMenuButton();
        this.createMenuContainer();
        this.addMenuItems();
        this.addEventListeners();
        MenuStyles.addStyles();
    }

    createMenuButton() {
        this.menuButton = document.createElement("button");
        this.menuButton.className = "floating-menu-button";
        this.menuButton.innerHTML = `
            <div class="burger-icon">
                <span></span>
                <span></span>
                <span></span>
            </div>
        `;

        // Aplicar estilos do botão principal
        Object.assign(this.menuButton.style, MenuStyles.getButtonStyles());
        document.body.appendChild(this.menuButton);
    }

    createMenuContainer() {
        this.menuContainer = document.createElement("div");
        this.menuContainer.className = "floating-menu-container";

        Object.assign(this.menuContainer.style, MenuStyles.getContainerStyles());
        document.body.appendChild(this.menuContainer);
    }

    addMenuItems() {
        const menuItems = MenuConfig.getMenuItems();

        menuItems.forEach((item, index) => {
            const menuItem = document.createElement("div");
            menuItem.className = "floating-menu-item";
            menuItem.innerHTML = `
                <div class="menu-item-icon">${item.icon}</div>
                <div class="menu-item-text">${item.text}</div>
            `;

            // Aplicar estilos do item
            Object.assign(menuItem.style, MenuStyles.getItemStyles(index));

            // Adicionar event listeners do item
            MenuEvents.addItemEventListeners(menuItem, item, index, this);

            this.menuContainer.appendChild(menuItem);
            this.menuItems.push(menuItem);
        });
    }

    addEventListeners() {
        MenuEvents.addMenuEventListeners(this);
    }

    toggleMenu() {
        if (this.isOpen) {
            this.closeMenu();
        } else {
            this.openMenu();
        }
    }

    openMenu() {
        MenuAnimations.openMenu(this);
    }

    closeMenu() {
        MenuAnimations.closeMenu(this);
    }

    alterarTitulo() {
        MenuActions.alterarTitulo();
    }
}