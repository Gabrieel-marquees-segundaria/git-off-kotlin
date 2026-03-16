let style_main = [
    `* {
    box-sizing: border-box;
}

body {
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "Roboto", sans-serif;
    margin: 0;
    padding: 0;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    height: 100vh;
    width: 100vw;
    color: #333;
    line-height: 1.6;
    overflow: hidden;
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
}

.container {
    width: 98%;
    margin: 0 auto;
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    border-radius: 16px;
    padding: 30px;
    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
}

h2 {
    color: #2c3e50;
    margin-bottom: 30px;
    font-size: 2em;
    text-align: center;
    font-weight: 600;
}

button {
    background: linear-gradient(45deg, #3498db, #2980b9);
    border: none;
    color: white;
    padding: 15px 25px;
    cursor: pointer;
    font-size: 16px;
    border-radius: 8px;
    transition: all 0.3s ease;
    font-weight: 500;
    box-shadow: 0 4px 15px rgba(52, 152, 219, 0.3);
}

button:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(52, 152, 219, 0.4);
}

button:active {
    transform: translateY(0);
}

button:disabled {
    opacity: 0.6;
    cursor: not-allowed;
    transform: none;
}

.status {
    padding: 15px;
    margin: 20px 0;
    border-radius: 8px;
    font-weight: 500;
    text-align: center;
    display: none;
}

.status.success {
    background: #d4edda;
    color: #155724;
    border: 1px solid #c3e6cb;
}

.status.error {
    background: #f8d7da;
    color: #721c24;
    border: 1px solid #f5c6cb;
}

.status.info {
    background: #d1ecf1;
    color: #0c5460;
    border: 1px solid #bee5eb;
}

#resultado {
    margin: 20px 0;
    padding: 10px;
    background: white;
    border: 1px solid #e9ecef;
    border-radius: 12px;
    min-height: 60px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
    transition: all 0.3s ease;
}

#resultado:empty::before {
    content: "Nenhum arquivo selecionado ainda...";
    color: #6c757d;
    font-style: italic;
}

.file-list {
    list-style: none;
    padding: 0;
    margin: 0;
    max-height: 600px;
    overflow-y: auto;
}

.file-item {
    display: flex;
    align-items: center;
    padding: 15px;
    margin-bottom: 10px;
    background: #f8f9fa;
    border: 1px solid #e9ecef;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.2s ease;
    position: relative;
}

.file-item:hover {
    background: #e7f3ff;
    border-color: #3498db;
    transform: translateX(5px);
}

.file-item::before {
    content: "📄";
    font-size: 1.2em;
    margin-right: 12px;
}

.file-item.loading::after {
    content: "⏳";
    position: absolute;
    right: 15px;
    animation: spin 1s linear infinite;
}

@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}

/* SEÇÃO DE ROLAGEM OTIMIZADA PARA .file-content */
.file-content {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    width: 100vw;
    height: 100vh;
    background: #2d3748;
    color: #e2e8f0;
    padding: 60px 20px 20px 20px; /* Mais padding-top para o botão fechar */
    font-family: "Courier New", monospace;
    font-size: 14px;
    line-height: 1.4;
    
    /* ROLAGEM PRINCIPAL */
    overflow: auto; /* Permite rolagem em ambas as direções conforme necessário */
    overscroll-behavior: contain; /* Evita que a rolagem afete elementos pai */
    -webkit-overflow-scrolling: touch; /* Rolagem suave no iOS */
    
    /* Melhora a performance da rolagem */
    will-change: scroll-position;
    transform: translateZ(0); /* Força aceleração de hardware */
    
    box-sizing: border-box;
    z-index: 99999;
    
    /* Quebra de linha para texto longo */
    word-wrap: break-word;
    white-space: pre-wrap; /* Mantém formatação mas permite quebra de linha */
}

/* SCROLLBARS CUSTOMIZADAS */
.file-content::-webkit-scrollbar {
    width: 12px;
    height: 12px;
}

.file-content::-webkit-scrollbar-track {
    background: #4a5568;
    border-radius: 6px;
}

.file-content::-webkit-scrollbar-thumb {
    background: #718096;
    border-radius: 6px;
    border: 2px solid #4a5568;
}

.file-content::-webkit-scrollbar-thumb:hover {
    background: #a0aec0;
}

.file-content::-webkit-scrollbar-corner {
    background: #4a5568;
}

/* Firefox scrollbar */
.file-content {
    scrollbar-width: auto;
    scrollbar-color: #718096 #4a5568;
}

/* BOTÃO DE FECHAR OTIMIZADO */
.file-content::before {
    content: "✕";
    position: fixed; /* Fixed em relação à viewport, não ao elemento pai */
    top: 15px;
    right: 20px;
    background: rgba(255, 255, 255, 0.15);
    color: #e2e8f0;
    width: 35px;
    height: 35px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    font-size: 18px;
    font-weight: bold;
    transition: all 0.2s ease;
    z-index: 100000;
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.1);
}

.file-content::before:hover {
    background: rgba(255, 255, 255, 0.25);
    transform: scale(1.1);
}

.back-button {
    background: linear-gradient(45deg, #6c757d, #5a6268);
    margin-bottom: 15px;
    min-width: auto;
    padding: 10px 20px;
    font-size: 14px;
}

.back-button:hover {
    box-shadow: 0 4px 15px rgba(108, 117, 125, 0.3);
}

.header {
    position: relative;
    margin-bottom: 30px;
    display: flex;
    align-items: center;
    justify-content: center;
    min-height: 46px;
}

#title {
    margin: 0;
    color: #2c3e50;
    font-size: 1.8em;
    text-align: center;
    flex: 1;
}

.search-container {
    position: absolute;
    top: 5%;
    right: 0;
    transform: translateY(-50%);
    display: flex;
    align-items: center;
    gap: 10px;
    z-index: 10;
}

.search-btn {
    background: #3498db;
    border: none;
    padding: 8px 10px;
    border-radius: 50%;
    cursor: pointer;
    color: white;
    font-size: 14px;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 2px 8px rgba(52, 152, 219, 0.3);
    min-width: 46px;
    height: 46px;
}

.search-btn:hover {
    background: #2980b9;
    transform: translateY(-50%) translateY(-2px);
    box-shadow: 0 4px 12px rgba(52, 152, 219, 0.4);
}

.search-btn:active {
    transform: translateY(-50%) translateY(0);
}

.search-input {
    padding: 10px 16px;
    border: 2px solid #e0e0e0;
    border-radius: 25px;
    font-size: 16px;
    width: 300px;
    outline: none;
    transition: all 0.3s ease;
    display: none;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.search-input:focus {
    border-color: #3498db;
    box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
}

.search-input.show {
    display: block;
    animation: slideIn 0.3s ease-out;
}

@keyframes slideIn {
    from {
        opacity: 0;
        transform: translateX(20px);
    }
    to {
        opacity: 1;
        transform: translateX(0);
    }
}

.header::before {
    content: "";
    position: absolute;
    left: 0;
    top: 50%;
    transform: translateY(-50%);
    width: 46px;
    height: 46px;
}

/* RESPONSIVIDADE COM ROLAGEM OTIMIZADA */
@media (max-width: 768px) {
    .container {
        padding: 20px;
        margin: 10px;
        width: calc(100% - 20px);
    }

    h2 {
        font-size: 1.5em;
    }
    
    .header {
        flex-direction: column;
        gap: 15px;
        min-height: auto;
    }
    
    .search-container {
        position: static;
        transform: none;
        align-self: flex-end;
    }

    .search-btn:hover {
        transform: translateY(-2px);
    }

    .search-btn:active {
        transform: translateY(0);
    }

    .search-input {
        width: 250px;
        font-size: 15px;
        padding: 10px 15px;
    }

    #title {
        font-size: 1.5em;
        padding: 0;
    }
    
    .header::before {
        display: none;
    }
    
    /* Ajustes de rolagem para mobile */
    .file-content {
        padding: 50px 15px 15px 15px;
        font-size: 13px;
        /* No mobile, force overflow visível para evitar problemas de rolagem */
        overflow: auto;
    }
    
    .file-content::-webkit-scrollbar {
        width: 8px;
        height: 8px;
    }
}

@media (max-width: 480px) {
    .container {
        width: calc(100% - 10px);
        margin: 5px;
        padding: 15px;
    }
    
    .search-input {
        width: 200px;
    }
    
    .file-content {
        padding: 45px 10px 10px 10px;
        font-size: 12px;
    }
    
    .file-content::before {
        width: 30px;
        height: 30px;
        font-size: 16px;
        top: 10px;
        right: 15px;
    }
}`
];
const style_element_main = document.createElement("style");
style_element_main.type = "text/css";
style_element_main.textContent = style_main[0];
document.head.appendChild(style_element_main);
aplicarHighlight();
mostrarStatus(`carregando Highlight ${["hello world"]}`);



// FUNÇÃO OTIMIZADA PARA ROLAGEM (substitui ajustarTelaCheia)
function otimizarFileContent() {
    const fileContent = document.querySelector(".file-content");
    if (!fileContent) return;

    // Remove estilos inline que podem interferir na rolagem
    fileContent.style.height = "";
    fileContent.style.width = "";

    // Garante que a rolagem funcione corretamente
    fileContent.style.position = "fixed";
    fileContent.style.top = "0";
    fileContent.style.left = "0";
    fileContent.style.right = "0";
    fileContent.style.bottom = "0";
    fileContent.style.overflow = "auto";

    // Força re-renderização para garantir que as mudanças sejam aplicadas
    fileContent.offsetHeight;

    // Opcional: Adiciona listener de clique no botão fechar (se necessário)
    adicionarBotaoFechar(fileContent);
}

// Função para adicionar funcionalidade ao botão de fechar
function adicionarBotaoFechar(fileContent) {
    // Remove listeners anteriores
    fileContent.removeEventListener("click", handleCloseClick);

    function handleCloseClick(event) {
        // Verifica se o clique foi no pseudo-elemento ::before (botão fechar)
        const rect = fileContent.getBoundingClientRect();
        const clickX = event.clientX - rect.left;
        const clickY = event.clientY - rect.top;

        // Área aproximada do botão fechar (canto superior direito)
        const closeButtonArea = {
            left: rect.width - 55, // 35px width + 20px right
            top: 0,
            right: rect.width,
            bottom: 50 // 35px height + 15px top
        };

        if (
            clickX >= closeButtonArea.left &&
            clickX <= closeButtonArea.right &&
            clickY >= closeButtonArea.top &&
            clickY <= closeButtonArea.bottom
        ) {
            // Aqui você pode adicionar sua lógica para fechar o arquivo
            console.log("Fechando visualização do arquivo...");
            fileContent.style.display = "none";
            // ou fileContent.remove(); dependendo da sua implementação
        }
    }

    fileContent.addEventListener("click", handleCloseClick);
}

// Função para melhorar a performance de rolagem
function melhorarPerformanceRolagem() {
    const fileContent = document.querySelector(".file-content");
    if (!fileContent) return;

    // Adiciona debounce na rolagem para melhor performance
    let scrollTimeout;
    fileContent.addEventListener("scroll", function () {
        // Adiciona classe durante a rolagem
        fileContent.classList.add("scrolling");

        clearTimeout(scrollTimeout);
        scrollTimeout = setTimeout(() => {
            fileContent.classList.remove("scrolling");
        }, 150);
    });
}

// CSS adicional para melhorar performance durante rolagem (opcional)
function adicionarEstilosPerformance() {
    const style = document.createElement("style");
    style.textContent = `
        .file-content.scrolling {
            pointer-events: none; /* Melhora performance durante rolagem */
        }
        
        .file-content.scrolling::before {
            pointer-events: auto; /* Mantém botão fechar clicável */
        }
        
        /* Suaviza a rolagem em todos os navegadores */
        .file-content {
            scroll-behavior: smooth;
        }
        
        /* Evita seleção de texto durante rolagem rápida */
        .file-content.scrolling {
            user-select: none;
            -webkit-user-select: none;
            -moz-user-select: none;
        }
    `;
    document.head.appendChild(style);
}

// Event listeners otimizados
window.addEventListener("load", function () {
    otimizarFileContent();
    melhorarPerformanceRolagem();
    adicionarEstilosPerformance();
});

window.addEventListener("resize", function () {
    // Debounce do resize para evitar muitas chamadas
    clearTimeout(window.resizeTimeout);
    window.resizeTimeout = setTimeout(otimizarFileContent, 250);
});

// Função para detectar quando o file-content é criado dinamicamente
function observarFileContent() {
    const observer = new MutationObserver(function (mutations) {
        mutations.forEach(function (mutation) {
            mutation.addedNodes.forEach(function (node) {
                if (node.nodeType === Node.ELEMENT_NODE) {
                    if (
                        node.classList &&
                        node.classList.contains("file-content")
                    ) {
                        otimizarFileContent();
                        melhorarPerformanceRolagem();
                    }
                    // Também verifica nos filhos do nó adicionado
                    const fileContentInside =
                        node.querySelector &&
                        node.querySelector(".file-content");
                    if (fileContentInside) {
                        otimizarFileContent();
                        melhorarPerformanceRolagem();
                    }
                }
            });
        });
    });

    observer.observe(document.body, {
        childList: true,
        subtree: true
    });
}

// Inicia observação
document.addEventListener("DOMContentLoaded", observarFileContent);
