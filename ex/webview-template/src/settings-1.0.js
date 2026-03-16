// Estado do menu
let menuAberto = false;

// Função para criar o menu flutuante
function criarMenuFlutuante() {
    // Criar overlay
    const overlay = document.createElement("div");
    overlay.id = "menuOverlay";
    overlay.className = "menu-overlay";
    overlay.onclick = fecharMenu;

    // Criar container do menu
    const menuContainer = document.createElement("div");
    menuContainer.id = "menuContainer";
    menuContainer.className = "menu-container";

    // Criar cabeçalho do menu
    const menuHeader = document.createElement("div");
    menuHeader.className = "menu-header";
    menuHeader.innerHTML = `
        <h3>⚙️ Configurações</h3>
        <button class="menu-close-btn" onclick="fecharMenu()">✕</button>
    `;

    // Criar corpo do menu
    const menuBody = document.createElement("div");
    menuBody.className = "menu-body";

    // Criar botões do menu
    const menuButtons = [
        {
            id: "menuBtnSelecionar",
            text: "🗂️ Selecionar Pasta",
            action: "abrirPastaAndroid()",
            description: "Abrir seletor de pasta do Android"
        },
        {
            id: "menuBtnLimpar",
            text: "🗑️ Limpar Resultado",
            action: "limparResultado()",
            description: "Limpar a área de resultado"
        },
        {
            id: "menuBtnInfo",
            text: "📋 Informações",
            action: "mostrarInfo()",
            description: "Mostrar informações sobre a aplicação"
        }
    ];

    // Adicionar botões ao menu
    menuButtons.forEach(button => {
        const menuItem = document.createElement("div");
        menuItem.className = "menu-item";
        menuItem.innerHTML = `
            <button class="menu-btn" onclick="${button.action}; fecharMenu();" id="${button.id}">
                ${button.text}
            </button>
            <p class="menu-btn-description">${button.description}</p>
        `;
        menuBody.appendChild(menuItem);
    });

    // Montar estrutura do menu
    menuContainer.appendChild(menuHeader);
    menuContainer.appendChild(menuBody);
    overlay.appendChild(menuContainer);

    // Adicionar ao DOM
    document.body.appendChild(overlay);

    // Aplicar estilos CSS dinamicamente
    aplicarEstilosMenu();
}

// Função para aplicar estilos CSS do menu
function aplicarEstilosMenu() {
    const style = document.createElement("style");
    style.textContent = `
        .menu-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            backdrop-filter: blur(5px);
            z-index: 1000;
            display: flex;
            justify-content: center;
            align-items: center;
            opacity: 0;
            transition: opacity 0.3s ease;
        }
        
        .menu-overlay.active {
            opacity: 1;
        }
        
        .menu-container {
            background: white;
            border-radius: 16px;
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
            max-width: 400px;
            width: 90%;
            max-height: 80vh;
            overflow-y: auto;
            transform: translateY(-20px) scale(0.95);
            transition: transform 0.3s ease;
        }
        
        .menu-overlay.active .menu-container {
            transform: translateY(0) scale(1);
        }
        
        .menu-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 20px;
            border-bottom: 1px solid #e9ecef;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 16px 16px 0 0;
            color: white;
        }
        
        .menu-header h3 {
            margin: 0;
            font-size: 1.2em;
            font-weight: 600;
        }
        
        .menu-close-btn {
            background: rgba(255, 255, 255, 0.2);
            border: none;
            color: white;
            padding: 8px 12px;
            border-radius: 50%;
            cursor: pointer;
            font-size: 16px;
            transition: all 0.3s ease;
            width: 36px;
            height: 36px;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .menu-close-btn:hover {
            background: rgba(255, 255, 255, 0.3);
            transform: rotate(90deg);
        }
        
        .menu-body {
            padding: 20px;
        }
        
        .menu-item {
            margin-bottom: 20px;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 12px;
            border: 1px solid #e9ecef;
            transition: all 0.3s ease;
        }
        
        .menu-item:hover {
            background: #e7f3ff;
            border-color: #3498db;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(52, 152, 219, 0.1);
        }
        
        .menu-item:last-child {
            margin-bottom: 0;
        }
        
        .menu-btn {
            background: linear-gradient(45deg, #3498db, #2980b9);
            border: none;
            color: white;
            padding: 12px 20px;
            cursor: pointer;
            font-size: 16px;
            border-radius: 8px;
            transition: all 0.3s ease;
            font-weight: 500;
            box-shadow: 0 4px 15px rgba(52, 152, 219, 0.3);
            width: 100%;
            text-align: left;
        }
        
        .menu-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(52, 152, 219, 0.4);
        }
        
        .menu-btn:active {
            transform: translateY(0);
        }
        
        .menu-btn-description {
            margin: 10px 0 0 0;
            font-size: 14px;
            color: #6c757d;
            line-height: 1.4;
        }
        
        @media (max-width: 480px) {
            .menu-container {
                width: 95%;
                max-width: none;
            }
            
            .menu-header {
                padding: 15px;
            }
            
            .menu-body {
                padding: 15px;
            }
            
            .menu-item {
                padding: 12px;
            }
        }
    `;

    document.head.appendChild(style);
}

// Função para abrir/fechar menu
function toggleMenu() {
    if (menuAberto) {
        fecharMenu();
    } else {
        abrirMenu();
    }
}

// Função para abrir menu
function abrirMenu() {
    if (!document.getElementById("menuOverlay")) {
        criarMenuFlutuante();
    }

    const overlay = document.getElementById("menuOverlay");
    overlay.style.display = "flex";

    // Forçar reflow para garantir que a transição funcione
    overlay.offsetHeight;

    overlay.classList.add("active");
    menuAberto = true;

    // Prevenir scroll do body
    document.body.style.overflow = "hidden";
}

// Função para fechar menu
function fecharMenu() {
    const overlay = document.getElementById("menuOverlay");
    if (overlay) {
        overlay.classList.remove("active");

        setTimeout(() => {
            overlay.remove();
            menuAberto = false;

            // Restaurar scroll do body
            document.body.style.overflow = "";
        }, 300);
    }
}

// Função para mostrar informações
function mostrarInfo() {
    const info = `
📱 Leitor de Arquivos Android

Versão: 1.0
Desenvolvido para comunicação entre Android e JavaScript

Funcionalidades:
• Seleção de pastas via Android
• Leitura de arquivos
• Interface responsiva
• Menu flutuante moderno

Controles:
• Toque no botão "Configurações" para abrir este menu
• Use "Selecionar Pasta" para escolher uma pasta
• Use "Limpar" para resetar os resultados
    `;

    mostrarStatus("Informações carregadas!", "info");

    const htmlInfo = `
        <div style="padding: 20px; background: #f8f9fa; border-radius: 12px; margin-top: 20px;">
            <h3 style="margin-top: 0; color: #2c3e50;">📋 Informações da Aplicação</h3>
            <pre style="white-space: pre-wrap; font-family: inherit; margin: 0; line-height: 1.6;">${info}</pre>
        </div>
    `;

    document.getElementById("resultado").innerHTML = htmlInfo;
}

// Fechar menu com tecla ESC
document.addEventListener("keydown", function (e) {
    if (e.key === "Escape" && menuAberto) {
        fecharMenu();
    }
});

// Inicialização do sistema de menu
document.addEventListener("DOMContentLoaded", function () {
    console.log("Sistema de menu carregado");
});
