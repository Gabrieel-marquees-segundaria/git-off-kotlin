// MenuActions.js - Ações e funcionalidades do menu
class MenuActions {
    static alterarTitulo() {
        const titleElement = document.getElementById("title");
        if (titleElement) {
            const novoTitulo = prompt(
                "Digite o novo título:",
                titleElement.textContent
            );
            if (novoTitulo !== null && novoTitulo.trim() !== "") {
                titleElement.textContent = novoTitulo.trim();
            }
        } else {
            alert('Elemento com id "title" não encontrado na página.');
        }
    }

    static selecionarPasta() {
        // Implementar lógica para selecionar pasta
        if (typeof abrirPastaAndroid === 'function') {
            abrirPastaAndroid();
        } else {
            console.warn('Função abrirPastaAndroid não está disponível');
        }
    }

    static listarArquivos() {
        // Implementar lógica para listar arquivos
        if (typeof listar_arquivos === 'function') {
            listar_arquivos();
        } else {
            console.warn('Função listar_arquivos não está disponível');
        }
    }

    static limparResultado() {
        // Implementar lógica para limpar resultado
        if (typeof limparResultado === 'function') {
            limparResultado();
        } else {
            console.warn('Função limparResultado não está disponível');
        }
    }
}