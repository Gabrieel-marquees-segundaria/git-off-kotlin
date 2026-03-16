// FloatingMenu.js - Classe principal do menu flutuante
import { MenuButton } from './MenuButton.js';
import { MenuContainer } from './MenuContainer.js';
import { MenuItems } from './MenuItems.js';
import { MenuStyles } from './MenuStyles.js';

export class FloatingMenu {
    constructor() {
        this.isOpen = false;
        this.menuButton = null;
        this.menuContainer = null;
        this.menuItems = [];
        this.init();
    }

    init() {
        // Inicializar estilos
        MenuStyles.init();
        
        // Criar componentes
        this.menuButton = MenuButton.create();
        this.menuContainer = MenuContainer.create();
        this.menuItems = MenuItems.create(this.menuContainer);
        
        // Configurar eventos
        this.addEventListeners();
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
            this.menuButton.style.boxShadow = "0 6px 25px rgba(52, 152, 219, 0.5)";
        });

        this.menuButton.addEventListener("mouseleave", () => {
            if (!this.isOpen) {
                this.menuButton.style.transform = "scale(1)";
                this.menuButton.style.boxShadow = "0 4px 20px rgba(52, 152, 219, 0.4)";
            }
        });
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
                item.style.transform = `translateY(${(index + 1) * 50}px) scale(1)`;
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
            item.style.transform = `translateY(${(index + 1) * 10}px) scale(0.8)`;
            item.style.opacity = "0";
        });

        // Hide menu container after animation
        setTimeout(() => {
            this.menuContainer.style.opacity = "0";
            this.menuContainer.style.visibility = "hidden";
        }, 200);
    }

    alterarTitulo() {
        const titleElement = document.getElementById("title");
        if (titleElement) {
            const novoTitulo = prompt(
                "Digite o novo título:",
                titleElement.textContent
            );
            if (novoTitulo !== null && novoTitulo.trim() !== "") {
                titleElement.textContent = novoTitulo.trim();
            }
        } else {
            alert('Elemento com id "title" não encontrado na página.');
        }
    }
}