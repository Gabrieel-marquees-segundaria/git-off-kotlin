// android-interface.js - Interface com o Android
class AndroidInterface {
    constructor() {
        this.isAndroidAvailable = this.checkAndroidAvailability();
    }

    checkAndroidAvailability() {
        return window.Android &&
               typeof window.Android.abrirPasta === 'function' &&
               typeof window.Android.lerArquivo === 'function' &&
               typeof window.Android.listarArquivos === 'function';
    }

    abrirPasta() {
        if (this.isAndroidAvailable) {
            window.Android.abrirPasta();
            return true;
        }
        return false;
    }

    lerArquivo(nome) {
        if (this.isAndroidAvailable) {
            window.Android.lerArquivo(nome);
            return true;
        }
        return false;
    }

    listarArquivos() {
        if (this.isAndroidAvailable) {
            window.Android.listarArquivos();
            return true;
        }
        return false;
    }

    setExternModels(){
            if (this.isAndroidAvailable) {
                window.Android.setExternModels();
                return true;
            }
            return false;
    }

    // Métodos para simulação em navegador (desenvolvimento)
    simularAbrirPasta() {
        return new Promise((resolve) => {
            setTimeout(() => {
                const arquivosTeste = [
                    "documento.txt",
                    "dados.json",
                    "config.xml",
                    "readme.md"
                ];
                resolve(arquivosTeste);
            }, 1000);
        });
    }

    simularLerArquivo(nome) {
        return new Promise((resolve) => {
            setTimeout(() => {
                const conteudoTeste = `Conteúdo simulado do arquivo: ${nome}\n\nEste é um exemplo de conteúdo que seria lido do arquivo selecionado.\n\nLinhas adicionais para demonstrar o layout...`;
                resolve(conteudoTeste);
            }, 1000);
        });
    }
}

// Instância global
const androidInterface = new AndroidInterface();