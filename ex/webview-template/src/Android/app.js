// app.js - Arquivo principal da aplicação
import { AppState } from './state.js';
import { UIController } from './ui-controller.js';
import { AndroidInterface } from './android-interface.js';
import { FileManager } from './file-manager.js';
import { Utils } from './utils.js';

class App {
    constructor() {
        this.state = new AppState();
        this.ui = new UIController();
        this.android = new AndroidInterface();
        this.fileManager = new FileManager(this.state, this.ui, this.android);
        
        this.init();
    }

    init() {
        this.setupEventListeners();
        this.setupGlobalErrorHandler();
        this.ui.mostrarStatus(
            'Aplicação carregada. Clique em "Selecionar Pasta" para começar.',
            "info"
        );
    }

    setupEventListeners() {
        document.addEventListener("DOMContentLoaded", () => {
            this.ui.mostrarStatus(
                'Aplicação carregada. Clique em "Selecionar Pasta" para começar.',
                "info"
            );
        });
    }

    setupGlobalErrorHandler() {
        window.addEventListener("error", (e) => {
            this.ui.mostrarStatus("Erro inesperado: " + e.message, "error");
        });
    }

    // Métodos públicos para serem chamados pelo HTML
    abrirPasta() {
        this.fileManager.abrirPasta();
    }

    listarArquivos() {
        this.fileManager.listarArquivos();
    }

    lerArquivo(nome) {
        this.fileManager.lerArquivo(nome);
    }

    voltarParaLista() {
        this.fileManager.voltarParaLista();
    }

    limparResultado() {
        this.fileManager.limparResultado();
    }

    // Métodos chamados pelo Android
    receberArquivos(arquivosJson) {
        this.fileManager.receberArquivos(arquivosJson);
    }

    mostrarConteudo(conteudo, nomeArquivo) {
        this.fileManager.mostrarConteudo(conteudo, nomeArquivo);
    }
}

// Instância global da aplicação
const app = new App();

// Expor métodos globalmente para o HTML e Android
window.abrirPastaAndroid = () => app.abrirPasta();
window.listar_arquivos = () => app.listarArquivos();
window.lerArquivo = (nome) => app.lerArquivo(nome);
window.voltarParaLista = () => app.voltarParaLista();
window.limparResultado = () => app.limparResultado();
window.receberArquivos = (arquivosJson) => app.receberArquivos(arquivosJson);
window.mostrarConteudo = (conteudo, nomeArquivo) => app.mostrarConteudo(conteudo, nomeArquivo);