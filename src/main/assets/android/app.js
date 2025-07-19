// app.js - Arquivo principal da aplicação
class App {
    constructor() {
        this.init();
    }

    init() {
        this.setupEventListeners();
        this.setupGlobalErrorHandler();
    }

    setupEventListeners() {
        document.addEventListener("DOMContentLoaded", () => {
            uiController.mostrarStatus(
                'Aplicação carregada. Clique em "Selecionar Pasta" para começar.',
                "info"
            );
        });
    }

    setupGlobalErrorHandler() {
        window.addEventListener("error", (e) => {
            uiController.mostrarStatus("Erro inesperado: " + e.message, "error");
        });
    }
}
const TAG = "GLOBAL"
// Funções globais para serem chamadas pelo HTML e Android
function abrirPastaAndroid() {
    fileManager.abrirPasta();
}

function listar_arquivos() {
    fileManager.listarArquivos();
}

function lerArquivo(nome) {
    fileManager.lerArquivo(nome);
}

function voltarParaLista() {
    fileManager.voltarParaLista();
}

function limparResultado() {
    fileManager.limparResultado();
}

function receberArquivos(arquivosJson) {
    fileManager.receberArquivos(arquivosJson);
}

function mostrarConteudo(conteudo, nomeArquivo) {

    fileManager.mostrarConteudo(conteudo, nomeArquivo);
}

// Função para mostrar status (compatibilidade com código original)
function mostrarStatus(mensagem, tipo = "info") {
    uiController.mostrarStatus(mensagem, tipo);
}
function setExternModels(){
fileManager.setExternModels()
}
// Inicialização da aplicação
const app = new App();