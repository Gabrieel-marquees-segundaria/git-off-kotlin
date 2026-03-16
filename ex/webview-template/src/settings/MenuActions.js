// MenuActions.js - Responsável pelas ações dos itens do menu
export class MenuActions {
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

    static getMenuItemsData() {
        return [
            {
                icon: "🗂️",
                text: "Selecionar Pasta",
                action: () => {
                    if (typeof abrirPastaAndroid === 'function') {
                        abrirPastaAndroid();
                    } else {
                        console.error('Função abrirPastaAndroid não encontrada');
                    }
                }
            },
            {
                icon: "📋️",
                text: "listar arquivos",
                action: () => {
                    if (typeof listar_arquivos === 'function') {
                        listar_arquivos();
                    } else {
                        console.error('Função listar_arquivos não encontrada');
                    }
                }
            },
            {
                icon: "✏️",
                text: "Alterar Título",
                action: () => MenuActions.alterarTitulo()
            },
            {
                icon: "🗑️",
                text: "Limpar",
                action: () => {
                    if (typeof limparResultado === 'function') {
                        limparResultado();
                    } else {
                        console.error('Função limparResultado não encontrada');
                    }
                }
            }
        ];
    }
}