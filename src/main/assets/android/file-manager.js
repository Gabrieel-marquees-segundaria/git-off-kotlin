// file-manager.js - Gerenciador de arquivos
class FileManager {
    constructor() {
        // As dependências serão acessadas via variáveis globais
    }

    async abrirPasta() {
        try {
            uiController.mostrarStatus("Abrindo seletor de pasta...", "info");

            const sucesso = androidInterface.abrirPasta();

            if (!sucesso) {
                // Fallback para desenvolvimento em navegador
                uiController.mostrarStatus(
                    "Interface Android não encontrada. Simulando arquivos para teste...",
                    "error"
                );

                const arquivos = await androidInterface.simularAbrirPasta();
                this.receberArquivos(JSON.stringify(arquivos));
            }
        } catch (error) {
            uiController.mostrarStatus("Erro ao abrir pasta: " + error.message, "error");
        }
    }

    listarArquivos() {
        const sucesso = androidInterface.listarArquivos();

        if (!sucesso) {
            uiController.mostrarStatus(
                "Interface Android não encontrada. Simulando arquivos para teste...",
                "error"
            );
        }
    }

    receberArquivos(arquivosJson) {
        try {
            const arquivos = JSON.parse(arquivosJson);
            appState.setArquivosCarregados(arquivos);

            if (arquivos.length === 0) {
                uiController.mostrarStatus(
                    "Nenhum arquivo encontrado na pasta selecionada.",
                    "info"
                );
                uiController.limparResultado();
                return;
            }

            uiController.mostrarStatus(
                `${arquivos.length} arquivo(s) encontrado(s)!`,
                "success"
            );

            uiController.renderizarListaArquivos(arquivos);
        } catch (error) {
            uiController.mostrarStatus(
                "Erro ao processar lista de arquivos: " + error.message,
                "error"
            );
        }
    }

    async lerArquivo(nome) {
        try {
            uiController.adicionarIndicadorCarregamento(nome);
            uiController.mostrarStatus(`Lendo arquivo: ${nome}...`, "info");

            const sucesso = androidInterface.lerArquivo(nome);

            if (!sucesso) {
                // Fallback para desenvolvimento em navegador
                const conteudo = await androidInterface.simularLerArquivo(nome);
                this.mostrarConteudo(conteudo, nome);
            }
        } catch (error) {
            uiController.mostrarStatus("Erro ao ler arquivo: " + error.message, "error");
            uiController.removerIndicadorCarregamento(nome);
        }
    }

    mostrarConteudo(conteudo, nomeArquivo = "arquivo") {
        try {
            uiController.removerIndicadorCarregamento();
            appState.setConteudoAtual(conteudo, nomeArquivo);

            uiController.mostrarStatus(
                `Arquivo "${nomeArquivo}" carregado com sucesso!`,
                "success"
            );

            uiController.renderizarConteudoArquivo(conteudo, nomeArquivo);
        } catch (error) {
            uiController.mostrarStatus("Erro ao exibir conteúdo: " + error.message, "error");
        }
    }

    voltarParaLista() {
        if (appState.temArquivosCarregados()) {
            this.receberArquivos(JSON.stringify(appState.getArquivosCarregados()));
        } else {
            this.limparResultado();
        }
    }

    limparResultado() {
        uiController.limparResultado();
        uiController.ocultarStatus();
        appState.limparEstado();
        uiController.mostrarStatus("Resultado limpo.", "info");
    }
    setExternModels(){
 androidInterface.setExternModels()
   uiController.mostrarStatus("Resultado limpo.", "info");
}
}

// Instância global
const fileManager = new FileManager();