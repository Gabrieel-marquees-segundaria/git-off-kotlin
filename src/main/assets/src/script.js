// Estado da aplicação
let arquivosCarregados = [];
let conteudoAtual = null;

// Função para mostrar status
function mostrarStatus(mensagem, tipo = "info") {
    const statusEl = document.getElementById("status");
    statusEl.textContent = mensagem;
    statusEl.className = `status ${tipo}`;
    statusEl.style.display = "block";

    // Auto-hide após 3 segundos para mensagens de sucesso
    if (tipo === "success") {
        setTimeout(() => {
            statusEl.style.display = "none";
        }, 3000);
    }
}
function listar_arquivos(){
          if (window.Android && window.Android.listarArquivos) {
            window.Android.listarArquivos();
        } else {
            // Fallback para teste em navegador
            mostrarStatus(
                "Interface Android não encontrada. Simulando arquivos para teste...",
                "error"
            );
        }
}
// Função para abrir pasta no Android
function abrirPastaAndroid() {
    try {
        // atualizarEstadoBotoes("carregando");
        mostrarStatus("Abrindo seletor de pasta...", "info");

        if (window.Android && window.Android.abrirPasta) {
            window.Android.abrirPasta();
        } else {
            // Fallback para teste em navegador
            mostrarStatus(
                "Interface Android não encontrada. Simulando arquivos para teste...",
                "error"
            );
            setTimeout(() => {
                const arquivosTeste = [
                    "documento.txt",
                    "dados.json",
                    "config.xml",
                    "readme.md"
                ];
                receberArquivos(JSON.stringify(arquivosTeste));
            }, 1000);
        }
    } catch (error) {
        mostrarStatus("Erro ao abrir pasta: " + error.message, "error");
        // atualizarEstadoBotoes("normal");
    }
}

// Função chamada pelo Android para receber lista de arquivos
function receberArquivos(arquivosJson) {
    try {
        const arquivos = JSON.parse(arquivosJson);
        arquivosCarregados = arquivos;

        if (arquivos.length === 0) {
            mostrarStatus(
                "Nenhum arquivo encontrado na pasta selecionada.",
                "info"
            );
            document.getElementById("resultado").innerHTML = "";
            return;
        }

        mostrarStatus(
            `${arquivos.length} arquivo(s) encontrado(s)!`,
            "success"
        );
        //  atualizarEstadoBotoes("arquivos-carregados");

        let listaHtml = '<ul class="file-list">';
        arquivos.forEach(nome => {
            listaHtml += `<li class="file-item" onclick="lerArquivo('${nome}')" data-file="${nome}">
                <span>${nome}</span>
            </li>`;
        });
        listaHtml += "</ul>";

        document.getElementById("resultado").innerHTML = listaHtml;
    } catch (error) {
        mostrarStatus(
            "Erro ao processar lista de arquivos: " + error.message,
            "error"
        );
    }
}

// Função para ler arquivo específico
function lerArquivo(nome) {
    try {
        // Adicionar indicador de carregamento
        const fileItem = document.querySelector(`[data-file="${nome}"]`);
        if (fileItem) {
            fileItem.classList.add("loading");
        }

        mostrarStatus(`Lendo arquivo: ${nome}...`, "info");

        if (window.Android && window.Android.lerArquivo) {
            window.Android.lerArquivo(nome);
        } else {
            // Fallback para teste em navegador
            setTimeout(() => {
                const conteudoTeste = `Conteúdo simulado do arquivo: ${nome}\n\nEste é um exemplo de conteúdo que seria lido do arquivo selecionado.\n\nLinhas adicionais para demonstrar o layout...`;
                mostrarConteudo(conteudoTeste, nome);
            }, 1000);
        }
    } catch (error) {
        mostrarStatus("Erro ao ler arquivo: " + error.message, "error");
        // Remover indicador de carregamento
        const fileItem = document.querySelector(`[data-file="${nome}"]`);
        if (fileItem) {
            fileItem.classList.remove("loading");
        }
    }
}

// Função chamada pelo Android para mostrar conteúdo
function mostrarConteudo(conteudo, nomeArquivo = "arquivo") {
    try {
        // Remover indicadores de carregamento
        document.querySelectorAll(".file-item.loading").forEach(item => {
            item.classList.remove("loading");
        });

        conteudoAtual = { conteudo, nome: nomeArquivo };

        mostrarStatus(
            `Arquivo "${nomeArquivo}" carregado com sucesso!`,
            "success"
        );

        const htmlConteudo = `
            <button class="back-button" onclick="voltarParaLista()">
                ← Voltar para lista
            </button>
            <h3>📄 ${nomeArquivo}</h3>
            <div class="file-content">${escapeHtml(conteudo)}</div>
        `;

        document.getElementById("resultado").innerHTML = htmlConteudo;
    } catch (error) {
        mostrarStatus("Erro ao exibir conteúdo: " + error.message, "error");
    }
}

// Função para voltar à lista de arquivos
function voltarParaLista() {
    if (arquivosCarregados.length > 0) {
        receberArquivos(JSON.stringify(arquivosCarregados));
    } else {
        limparResultado();
    }
}

// Função para limpar resultado
function limparResultado() {
    document.getElementById("resultado").innerHTML = "";
    document.getElementById("status").style.display = "none";
    arquivosCarregados = [];
    conteudoAtual = null;
    mostrarStatus("Resultado limpo.", "info");
    // atualizarEstadoBotoes("normal");
}

// Função utilitária para escape HTML
function escapeHtml(text) {
    const div = document.createElement("div");
    div.textContent = text;
    return div.innerHTML;
}

// Tratamento de erros globais
window.addEventListener("error", function (e) {
    mostrarStatus("Erro inesperado: " + e.message, "error");
});

// Inicialização
document.addEventListener("DOMContentLoaded", function () {
    mostrarStatus(
        'Aplicação carregada. Clique em "Selecionar Pasta" para começar.',
        "info"
    );
});
