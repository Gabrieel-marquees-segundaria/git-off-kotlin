
// Função para mostrar informações
function mostrarInfo() {
    const info = `
📱 Leitor de Arquivos Android

Versão: 1.0
Desenvolvido para comunicação entre Android e JavaScript

Funcionalidades:
• Seleção de pastas via Android
• Leitura de arquivos
• Interface responsiva
• Menu flutuante moderno

Controles:
• Toque no botão "Configurações" para abrir este menu
• Use "Selecionar Pasta" para escolher uma pasta
• Use "Limpar" para resetar os resultados
    `;

    mostrarStatus("Informações carregadas!", "info");

    const htmlInfo = `
        <div style="padding: 20px; background: #f8f9fa; border-radius: 12px; margin-top: 20px;">
            <h3 style="margin-top: 0; color: #2c3e50;">📋 Informações da Aplicação</h3>
            <pre style="white-space: pre-wrap; font-family: inherit; margin: 0; line-height: 1.6;">${info}</pre>
        </div>
    `;

    document.getElementById("resultado").innerHTML = htmlInfo;
}
