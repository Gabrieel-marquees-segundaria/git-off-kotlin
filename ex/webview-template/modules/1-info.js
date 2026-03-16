function mostrarInfo() {
    const infoFixo = `
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

    // Pega as infos do Android
    const appInfo = JSON.parse(Android.getAllInfo());

    mostrarStatus("Informações carregadas!", "info");

    // Cria elementos scrolláveis para cada valor do appInfo
    let appDetailsHtml = "";
    for (let key in appInfo) {
        const value = JSON.stringify(appInfo[key], null, 2); // Formatação com indentação
        
        appDetailsHtml += `
            <div style="margin-bottom: 15px;">
                <h4 style="
                    margin: 0 0 8px 0; 
                    color: #34495e; 
                    font-size: 16px; 
                    font-weight: 600;
                    padding: 8px 12px;
                    background: #ecf0f1;
                    border-radius: 6px 6px 0 0;
                    border-left: 4px solid #3498db;
                ">${key}</h4>
                <div style="
                    max-height: 150px;
                    overflow-y: auto;
                    background: #ffffff;
                    border: 1px solid #bdc3c7;
                    border-radius: 0 0 6px 6px;
                    padding: 12px;
                    font-family: 'Courier New', monospace;
                    font-size: 13px;
                    line-height: 1.4;
                    color: #2c3e50;
                    box-shadow: inset 0 1px 3px rgba(0,0,0,0.1);
                    scrollbar-width: thin;
                    scrollbar-color: #bdc3c7 #ecf0f1;
                " class="scroll-container">
                    <pre style="margin: 0; white-space: pre-wrap; word-wrap: break-word;">${value}</pre>
                </div>
            </div>
        `;
    }

    const htmlInfo = `
        <div style="padding: 20px; background: #f8f9fa; border-radius: 12px; margin-top: 20px;">
            <h3 style="margin-top: 0; color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px;">
                📋 Informações da Aplicação
            </h3>
            
            <div style="
                background: #ffffff; 
                padding: 15px; 
                border-radius: 8px; 
                margin-bottom: 20px;
                border-left: 4px solid #27ae60;
            ">
                <pre style="white-space: pre-wrap; font-family: inherit; margin: 0; line-height: 1.6; color: #2c3e50;">
${infoFixo}
                </pre>
            </div>

            <h4 style="color: #2c3e50; margin-bottom: 15px; font-size: 18px;">
                🔧 Detalhes da Aplicação Android
            </h4>
            
            ${appDetailsHtml}
        </div>

        <style>
            .scroll-container::-webkit-scrollbar {
                width: 8px;
            }
            
            .scroll-container::-webkit-scrollbar-track {
                background: #ecf0f1;
                border-radius: 4px;
            }
            
            .scroll-container::-webkit-scrollbar-thumb {
                background: #bdc3c7;
                border-radius: 4px;
            }
            
            .scroll-container::-webkit-scrollbar-thumb:hover {
                background: #95a5a6;
            }
        </style>
    `;

    document.getElementById("resultado").innerHTML = htmlInfo;
}
