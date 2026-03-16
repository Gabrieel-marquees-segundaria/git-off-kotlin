// file-manager.js - Gerenciador de arquivos
export class FileManager {
    constructor(state, ui, androidInterface) {
        this.state = state;
        this.ui = ui;
        this.android = androidInterface;
    }

    async abrirPasta() {
        try {
            this.ui.mostrarStatus("Abrindo seletor de pasta...", "info");

            const sucesso = this.android.abrirPasta();
            
            if (!sucesso) {
                // Fallback para desenvolvimento em navegador
                this.ui.mostrarStatus(
                    "Interface Android não encontrada. Simulando arquivos para teste...",
                    "error"
                );
                
                const arquivos = await this.android.simularAbrirPasta();
                this.receberArquivos(JSON.stringify(arquivos));
            }
        } catch (error) {
            this.ui.mostrarStatus("Erro ao abrir pasta: " + error.message, "error");
        }
    }

    listarArquivos() {
        const sucesso = this.android.listarArquivos();
        
        if (!sucesso) {
            this.ui.mostrarStatus(
                "Interface Android não encontrada. Simulando arquivos para teste...",
                "error"
            );
        }
    }

    receberArquivos(arquivosJson) {
        try {
            const arquivos = JSON.parse(arquivosJson);
            this.state.setArquivosCarregados(arquivos);

            if (arquivos.length === 0) {
                this.ui.mostrarStatus(
                    "Nenhum arquivo encontrado na pasta selecionada.",
                    "info"
                );
                this.ui.limparResultado();
                return;
            }

            this.ui.mostrarStatus(
                `${arquivos.length} arquivo(s) encontrado(s)!`,
                "success"
            );

            this.ui.renderizarListaArquivos(arquivos);
        } catch (error) {
            this.ui.mostrarStatus(
                "Erro ao processar lista de arquivos: " + error.message,
                "error"
            );
        }
    }

    async lerArquivo(nome) {
        try {
            this.ui.adicionarIndicadorCarregamento(nome);
            this.ui.mostrarStatus(`Lendo arquivo: ${nome}...`, "info");

            const sucesso = this.android.lerArquivo(nome);
            
            if (!sucesso) {
                // Fallback para desenvolvimento em navegador
                const conteudo = await this.android.simularLerArquivo(nome);
                this.mostrarConteudo(conteudo, nome);
            }
        } catch (error) {
            this.ui.mostrarStatus("Erro ao ler arquivo: " + error.message, "error");
            this.ui.removerIndicadorCarregamento(nome);
        }
    }

    mostrarConteudo(conteudo, nomeArquivo = "arquivo") {
        try {
            this.ui.removerIndicadorCarregamento();
            this.state.setConteudoAtual(conteudo, nomeArquivo);

            this.ui.mostrarStatus(
                `Arquivo "${nomeArquivo}" carregado com sucesso!`,
                "success"
            );

            this.ui.renderizarConteudoArquivo(conteudo, nomeArquivo);
        } catch (error) {
            this.ui.mostrarStatus("Erro ao exibir conteúdo: " + error.message, "error");
        }
    }

    voltarParaLista() {
        if (this.state.temArquivosCarregados()) {
            this.receberArquivos(JSON.stringify(this.state.getArquivosCarregados()));
        } else {
            this.limparResultado();
        }
    }

    limparResultado() {
        this.ui.limparResultado();
        this.ui.ocultarStatus();
        this.state.limparEstado();
        this.ui.mostrarStatus("Resultado limpo.", "info");
    }
}