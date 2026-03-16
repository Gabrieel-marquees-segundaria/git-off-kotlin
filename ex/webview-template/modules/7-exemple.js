// Define o fundo do body como verde
//document.body.style.background = "green";

const fake = `<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Navegador de Arquivos</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            color: #333;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        .header {
            text-align: center;
            margin-bottom: 40px;
            color: white;
        }

        .header h1 {
            font-size: 2rem;
            margin-bottom: 10px;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
        }

        .file-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 40px;
        }

        .file-card {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 12px;
            padding: 20px;
            cursor: pointer;
            transition: all 0.3s ease;
            border: 2px solid transparent;
            backdrop-filter: blur(10px);
            box-shadow: 0 8px 32px rgba(0,0,0,0.1);
        }

        .file-card:hover {
            transform: translateY(-5px) scale(1.02);
            border-color: #667eea;
            box-shadow: 0 12px 40px rgba(102, 126, 234, 0.3);
        }

        .file-icon {
            font-size: 3rem;
            text-align: center;
            margin-bottom: 15px;
            opacity: 0.8;
        }

        .file-name {
            font-weight: bold;
            font-size: 1.1rem;
            margin-bottom: 8px;
            color: #333;
        }

        .file-info {
            font-size: 0.9rem;
            color: #666;
            line-height: 1.4;
        }

        .file-size {
            display: inline-block;
            background: #e8f0fe;
            color: #1a73e8;
            padding: 2px 8px;
            border-radius: 12px;
            font-size: 0.8rem;
            margin-top: 8px;
        }

        /* Modal/Fullscreen styles */
        .fullscreen-overlay {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(0, 0, 0, 0.95);
            z-index: 1000;
            backdrop-filter: blur(20px);
        }

        .fullscreen-overlay.active {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            animation: fadeIn 0.3s ease;
        }

        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }

        .close-button {
            position: absolute;
            top: 30px;
            right: 30px;
            background: rgba(255, 255, 255, 0.2);
            border: 2px solid rgba(255, 255, 255, 0.3);
            color: white;
            width: 50px;
            height: 50px;
            border-radius: 50%;
            cursor: pointer;
            font-size: 1.5rem;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: all 0.3s ease;
        }

        .close-button:hover {
            background: rgba(255, 255, 255, 0.3);
            transform: scale(1.1);
        }

        .fullscreen-content {
            text-align: center;
            color: white;
            max-width: 80%;
            margin: 0 auto;
        }

        .fullscreen-icon {
            font-size: 8rem;
            margin-bottom: 30px;
            opacity: 0.9;
        }

        .fullscreen-title {
            font-size: 2.5rem;
            margin-bottom: 20px;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.5);
        }

        .fullscreen-details {
            font-size: 1.2rem;
            line-height: 1.6;
            opacity: 0.8;
            max-width: 600px;
            margin: 0 auto;
        }

        .file-actions {
            margin-top: 30px;
            display: flex;
            gap: 15px;
            justify-content: center;
        }

        .action-btn {
            background: rgba(255, 255, 255, 0.2);
            border: 2px solid rgba(255, 255, 255, 0.3);
            color: white;
            padding: 12px 24px;
            border-radius: 25px;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
        }

        .action-btn:hover {
            background: rgba(255, 255, 255, 0.3);
            transform: translateY(-2px);
        }

        /* Responsive */
        @media (max-width: 768px) {
            .file-grid {
                grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
                gap: 15px;
            }
            
            .fullscreen-icon {
                font-size: 5rem;
            }
            
            .fullscreen-title {
                font-size: 2rem;
            }
            
            .close-button {
                top: 20px;
                right: 20px;
                width: 40px;
                height: 40px;
                font-size: 1.2rem;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>📁 Meus Arquivos</h1>
            <p>Clique em um arquivo para visualizar em tela cheia</p>
        </div>

        <div class="file-grid" id="fileGrid">
            <!-- Files will be generated by JavaScript -->
        </div>
    </div>

    <!-- Fullscreen Overlay -->
    <div class="fullscreen-overlay" id="fullscreenOverlay">
        <button class="close-button" onclick="closeFullscreen()">×</button>
        <div class="fullscreen-content" id="fullscreenContent">
            <!-- Content will be populated by JavaScript -->
        </div>
    </div>

    <script>
        // Sample file data
        const files = [
            {
                name: "Relatório_Vendas_2024.pdf",
                type: "pdf",
                size: "2.3 MB",
                icon: "📄",
                description: "Relatório completo de vendas do primeiro trimestre de 2024 com gráficos e análises detalhadas."
            },
            {
                name: "Apresentação_Projeto.pptx",
                type: "presentation",
                size: "15.7 MB",
                icon: "📊",
                description: "Slides da apresentação do novo projeto com cronogramas, orçamentos e especificações técnicas."
            },
            {
                name: "Fotos_Férias_Praia.zip",
                type: "archive",
                size: "124.5 MB",
                icon: "🖼️",
                description: "Coleção de fotos das férias na praia, incluindo paisagens, momentos em família e atividades aquáticas."
            },
            {
                name: "Código_Sistema_Web.js",
                type: "code",
                size: "45.2 KB",
                icon: "💻",
                description: "Arquivo JavaScript principal do sistema web com todas as funções de interface e conectividade."
            },
            {
                name: "Música_Favorita.mp3",
                type: "audio",
                size: "8.1 MB",
                icon: "🎵",
                description: "Faixa musical em alta qualidade, formato MP3 320kbps, duração aproximada de 3 minutos e 45 segundos."
            },
            {
                name: "Video_Tutorial.mp4",
                type: "video",
                size: "89.3 MB",
                icon: "🎬",
                description: "Tutorial em vídeo explicando passo a passo como usar o novo sistema, com duração de 12 minutos."
            },
            {
                name: "Documento_Contrato.docx",
                type: "document",
                size: "156 KB",
                icon: "📝",
                description: "Contrato de prestação de serviços com todas as cláusulas, termos e condições atualizadas."
            },
            {
                name: "Planilha_Orçamento.xlsx",
                type: "spreadsheet",
                size: "892 KB",
                icon: "📋",
                description: "Planilha detalhada do orçamento anual com categorias, projeções e análises comparativas."
            },
            {
                name: "Design_Logo.ai",
                type: "design",
                size: "12.4 MB",
                icon: "🎨",
                description: "Arquivo vetorial do logotipo da empresa em Adobe Illustrator com todas as variações de cor."
            },
            {
                name: "Base_Dados.sql",
                type: "database",
                size: "3.7 MB",
                icon: "🗄️",
                description: "Script SQL com estrutura completa do banco de dados, incluindo tabelas, índices e procedimentos."
            }
        ];

        // Generate file cards
        function generateFileCards() {
            const fileGrid = document.getElementById('fileGrid');
            
            files.forEach((file, index) => {
                const fileCard = document.createElement('div');
                fileCard.className = 'file-card';
                fileCard.onclick = () => openFullscreen(file);
                
                fileCard.innerHTML = \`
                    <div class="file-icon">${file.icon}</div>
                    <div class="file-name">${file.name}</div>
                    <div class="file-info">
                        Tipo: ${file.type.toUpperCase()}
                        <div class="file-size">${file.size}</div>
                    </div>
                \`;
                
                fileGrid.appendChild(fileCard);
            });
        }

        // Open fullscreen view
        function openFullscreen(file) {
            const overlay = document.getElementById('fullscreenOverlay');
            const content = document.getElementById('fullscreenContent');
            
            content.innerHTML = \`
                <div class="fullscreen-icon">${file.icon}</div>
                <div class="fullscreen-title">${file.name}</div>
                <div class="fullscreen-details">
                    <p><strong>Tipo:</strong> ${file.type.toUpperCase()}</p>
                    <p><strong>Tamanho:</strong> ${file.size}</p>
                    <p><strong>Descrição:</strong> ${file.description}</p>
                </div>
                <div class="file-actions">
                    <button class="action-btn" onclick="downloadFile('${
                        file.name
                    }')">📥 Download</button>
                    <button class="action-btn" onclick="shareFile('${
                        file.name
                    }')">📤 Compartilhar</button>
                    <button class="action-btn" onclick="viewDetails('${
                        file.name
                    }')">ℹ️ Detalhes</button>
                </div>
            \`;
            
            overlay.classList.add('active');
            document.body.style.overflow = 'hidden';
        }

        // Close fullscreen view
        function closeFullscreen() {
            const overlay = document.getElementById('fullscreenOverlay');
            overlay.classList.remove('active');
            document.body.style.overflow = 'auto';
        }

        // File actions (placeholder functions)
        function downloadFile(fileName) {
            alert(\`Iniciando download de: ${fileName}\`);
        }

        function shareFile(fileName) {
            alert(\`Compartilhando arquivo: ${fileName}\`);
        }

        function viewDetails(fileName) {
            alert(\`Exibindo detalhes de: ${fileName}\`);
        }

        // Close fullscreen with ESC key
        document.addEventListener('keydown', function(event) {
            if (event.key === 'Escape') {
                closeFullscreen();
            }
        });

        // Close fullscreen when clicking outside content
        document.getElementById('fullscreenOverlay').addEventListener('click', function(event) {
            if (event.target === this) {
                closeFullscreen();
            }
        });

        // Initialize the file grid
        generateFileCards();
    </script>
</body>
</html>`;
