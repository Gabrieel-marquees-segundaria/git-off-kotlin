// ui-controller.js - Controlador da interface do usuário
class UIController {
    constructor() {
        this.statusEl = document.getElementById("status");
        this.resultadoEl = document.getElementById("resultado");
    }

    mostrarStatus(mensagem, tipo = "info") {
        this.statusEl.textContent = mensagem;
        this.statusEl.className = `status ${tipo}`;
        this.statusEl.style.display = "block";

        // Auto-hide após 3 segundos para mensagens de sucesso
        if (tipo === "success") {
            setTimeout(() => {
                this.statusEl.style.display = "none";
            }, 3000);
        }
    }

    ocultarStatus() {
        this.statusEl.style.display = "none";
    }

    renderizarListaArquivos(arquivos) {

        let listaHtml = '<ul class="file-list">';
        arquivos.forEach(file => {
            listaHtml += `<li class="file-item" onclick="getUriData('${file.uri}','${file.type}')" data-file="${file.id}">
                <span>${file.name}</span>
            </li>`;
        });
        listaHtml += "</ul>";

        this.resultadoEl.innerHTML = listaHtml;
    }

    renderizarConteudoArquivo(conteudo, nomeArquivo) {
        const htmlConteudo = `
            <button class="back-button" onclick="voltarParaLista()">
                ← Voltar para lista
            </button>
            <h3>📄 ${nomeArquivo}</h3>
            <div class="file-content">${Utils.escapeHtml(conteudo)}</div>
        `;

        this.resultadoEl.innerHTML = htmlConteudo;
    }

    limparResultado() {
        this.resultadoEl.innerHTML = "";
    }

    adicionarIndicadorCarregamento(nomeArquivo) {
        const fileItem = document.querySelector(`[data-file="${nomeArquivo}"]`);
        if (fileItem) {
            fileItem.classList.add("loading");
        }
    }

    removerIndicadorCarregamento(nomeArquivo = null) {
        if (nomeArquivo) {
            const fileItem = document.querySelector(`[data-file="${nomeArquivo}"]`);
            if (fileItem) {
                fileItem.classList.remove("loading");
            }
        } else {
            // Remove de todos os itens
            document.querySelectorAll(".file-item.loading").forEach(item => {
                item.classList.remove("loading");
            });
        }
    }
}

// Instância global
const uiController = new UIController();