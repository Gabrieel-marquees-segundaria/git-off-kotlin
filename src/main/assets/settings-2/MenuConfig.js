// MenuConfig.js - Configurações e itens do menu
class MenuConfig {
    static getMenuItems() {
        return [
            {
                icon: "🗂️",
                text: "Selecionar Pasta",
                action: () => abrirPastaAndroid()
            },
            {
                icon: "📋️",
                text: "listar arquivos",
                action: () => listar_arquivos()
            },
            {
                icon: "✏️",
                text: "Alterar Título",
                action: () => MenuActions.alterarTitulo()
            },
            {
                icon: "🗑️",
                text: "Limpar",
                action: () => limparResultado()
            }
        ];
    }

    static getMenuPosition() {
        return {
            top: "40px",
            left: "20px"
        };
    }

    static getAnimationSettings() {
        return {
            itemSpacing: 50,
            animationDelay: 100,
            transitionDuration: "0.3s"
        };
    }
}