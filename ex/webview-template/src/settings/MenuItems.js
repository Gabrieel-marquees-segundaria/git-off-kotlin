// MenuItems.js - Responsável pela criação dos itens do menu
export class MenuItems {
    static create(menuContainer) {
        const menuItems = [];

        const itemsData = [
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
                icon: "✏️",
                text: "Alterar Título",
                action: () => this.alterarTitulo()
            },
            {
                icon: "🗑️",
                text: "Limpar",
                action: () => limparResultado()
            }
        ];

        itemsData.forEach((item, index) => {
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
                padding: "6px 8px",
                backgroundColor: "rgba(255, 255, 255, 0.95)",
                backdropFilter: "blur(10px)",
                borderRadius: "12px",
                marginBottom: "4px",
                cursor: "pointer",
                transition: "all 0.2s ease",
                transform: `translateY(${(index + 1) * 10}px) scale(0.8)`,
                opacity: "0",
                boxShadow: "0 4px 15px rgba(0, 0, 0, 0.1)",
                minWidth: "180px"
            });

            // Hover effects
            menuItem.addEventListener("mouseenter", () => {
                menuItem.style.backgroundColor = "rgba(52, 152, 219, 0.1)";
                menuItem.style.transform = menuItem.style.transform.replace(
                    "scale(0.8)",
                    "scale(1)"
                );
            });

            menuItem.addEventListener("mouseleave", () => {
                menuItem.style.backgroundColor = "rgba(255, 255, 255, 0.95)";
                // Verificar se o menu está aberto para aplicar transform correto
                const isOpen = menuContainer.style.opacity === "1";
                if (isOpen) {
                    menuItem.style.transform = `translateY(${
                        (index + 1) * 50
                    }px) scale(1)`;
                }
            });

            // Click action
            menuItem.addEventListener("click", () => {
                item.action();
                // Fechar menu após ação (precisará ser implementado na classe principal)
            });

            menuContainer.appendChild(menuItem);
            menuItems.push(menuItem);
        });

        return menuItems;
    }

    static alterarTitulo() {
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
