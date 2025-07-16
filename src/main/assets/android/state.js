// state.js - Gerenciamento do estado da aplicação
class AppState {
    constructor() {
        this.arquivosCarregados = [];
        this.conteudoAtual = null;
    }

    setArquivosCarregados(arquivos) {
        this.arquivosCarregados = arquivos;
    }

    getArquivosCarregados() {
        return this.arquivosCarregados;
    }

    setConteudoAtual(conteudo, nome) {
        this.conteudoAtual = { conteudo, nome };
    }

    getConteudoAtual() {
        return this.conteudoAtual;
    }

    limparEstado() {
        this.arquivosCarregados = [];
        this.conteudoAtual = null;
    }

    temArquivosCarregados() {
        return this.arquivosCarregados.length > 0;
    }
}

// Instância global
const appState = new AppState();