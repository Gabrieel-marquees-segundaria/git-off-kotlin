// Menu flutuante de configurações
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

        // Estilos inline para o botão principal
        Object.assign(this.menuButton.style, {
            position: "fixed",
            top: "40px",
            left: "20px",
            width: "60px",
            height: "60px",
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
        });

        document.body.appendChild(this.menuButton);
    }

    createMenuContainer() {
        this.menuContainer = document.createElement("div");
        this.menuContainer.className = "floating-menu-container";

        Object.assign(this.menuContainer.style, {
            position: "fixed",
            top: "40px",
            left: "20px",
            zIndex: "999",
            opacity: "0",
            visibility: "hidden",
            transition: "all 0.3s ease",
            transformOrigin: "top left"
        });

        document.body.appendChild(this.menuContainer);
    }

    addMenuItems() {
        const menuItems = [
            {
                icon: "🗂️",
                text: "Selecionar Pasta",
                action: () => abrirPastaAndroid()
            },
            {
                icon: "📋️",
                text: "listar arquivos",
                action: () => listar_arquivos()
            },
            {
                icon: "🗑️",
                text: "Limpar",
                action: () => limparResultado()
            }
        ];

        menuItems.forEach((item, index) => {
            const menuItem = document.createElement("div");
            menuItem.className = "floating-menu-item";
            menuItem.innerHTML = `
                <div class="menu-item-icon">${item.icon}</div>
                <div class="menu-item-text">${item.text}</div>
            `;

            // Estilos para cada item do menu
            Object.assign(menuItem.style, {
                display: "flex",
                alignItems: "center",
                padding: "12px 16px",
                backgroundColor: "rgba(255, 255, 255, 0.95)",
                backdropFilter: "blur(10px)",
                borderRadius: "12px",
                marginBottom: "8px",
                cursor: "pointer",
                transition: "all 0.2s ease",
                transform: `translateY(${(index + 1) * 10}px) scale(0.8)`,
                opacity: "0",
                boxShadow: "0 4px 15px rgba(0, 0, 0, 0.1)",
                minWidth: "180px"
            });

            // Hover effect
            menuItem.addEventListener("mouseenter", () => {
                menuItem.style.backgroundColor = "rgba(52, 152, 219, 0.1)";
                menuItem.style.transform = menuItem.style.transform.replace(
                    "scale(0.8)",
                    "scale(1)"
                );
            });

            menuItem.addEventListener("mouseleave", () => {
                menuItem.style.backgroundColor = "rgba(255, 255, 255, 0.95)";
                if (this.isOpen) {
                    menuItem.style.transform = `translateY(${
                        (index + 1) * 80
                    }px) scale(1)`;
                }
            });

            // Click action
            menuItem.addEventListener("click", () => {
                item.action();
                this.closeMenu();
            });

            this.menuContainer.appendChild(menuItem);
            this.menuItems.push(menuItem);
        });
    }

    addEventListeners() {
        // Toggle menu on button click
        this.menuButton.addEventListener("click", e => {
            e.stopPropagation();
            this.toggleMenu();
        });

        // Close menu when clicking outside
        document.addEventListener("click", e => {
            if (
                !this.menuContainer.contains(e.target) &&
                !this.menuButton.contains(e.target)
            ) {
                this.closeMenu();
            }
        });

        // Add hover effects to main button
        this.menuButton.addEventListener("mouseenter", () => {
            this.menuButton.style.transform = "scale(1.1)";
            this.menuButton.style.boxShadow =
                "0 6px 25px rgba(52, 152, 219, 0.5)";
        });

        this.menuButton.addEventListener("mouseleave", () => {
            if (!this.isOpen) {
                this.menuButton.style.transform = "scale(1)";
                this.menuButton.style.boxShadow =
                    "0 4px 20px rgba(52, 152, 219, 0.4)";
            }
        });

        // Add burger animation styles
        this.addBurgerStyles();
    }

    addBurgerStyles() {
        const style = document.createElement("style");
        style.textContent = `
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
        `;

        document.head.appendChild(style);
    }

    toggleMenu() {
        if (this.isOpen) {
            this.closeMenu();
        } else {
            this.openMenu();
        }
    }

    openMenu() {
        this.isOpen = true;

        // Animate button
        this.menuButton.style.transform = "scale(1.1)";
        this.menuButton.style.boxShadow = "0 6px 25px rgba(52, 152, 219, 0.5)";
        this.menuButton.querySelector(".burger-icon").classList.add("open");

        // Show menu container
        this.menuContainer.style.opacity = "1";
        this.menuContainer.style.visibility = "visible";

        // Animate menu items
        this.menuItems.forEach((item, index) => {
            setTimeout(() => {
                item.style.transform = `translateY(${
                    (index + 1) * 80
                }px) scale(1)`;
                item.style.opacity = "1";
            }, index * 100);
        });
    }

    closeMenu() {
        this.isOpen = false;

        // Animate button
        this.menuButton.style.transform = "scale(1)";
        this.menuButton.style.boxShadow = "0 4px 20px rgba(52, 152, 219, 0.4)";
        this.menuButton.querySelector(".burger-icon").classList.remove("open");

        // Hide menu items
        this.menuItems.forEach((item, index) => {
            item.style.transform = `translateY(${
                (index + 1) * 10
            }px) scale(0.8)`;
            item.style.opacity = "0";
        });

        // Hide menu container after animation
        setTimeout(() => {
            this.menuContainer.style.opacity = "0";
            this.menuContainer.style.visibility = "hidden";
        }, 200);
    }
}

// Inicializar o menu quando a página carregar
document.addEventListener("DOMContentLoaded", () => {
    new FloatingMenu();
});
