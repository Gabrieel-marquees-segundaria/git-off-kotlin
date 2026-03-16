
// ========================================
// MÓDULO 3: Configurações e Constantes
// ========================================
(function() {
    'use strict';

    const FloatingMenuConfig = {
        initialized: false,

        init() {
            if (this.initialized) return;
            this.initialized = true;
        },

        button: {
            position: { top: "40px", left: "7px" },
            size: { width: "60px", height: "60px" },
            styles: {
                borderRadius: "50%",
                background: "linear-gradient(135deg, #3498db, #2980b9)",
                border: "none",
                cursor: "pointer",
                zIndex: "1000",
                boxShadow: "0 4px 20px rgba(52, 152, 219, 0.4)",
                transition: "all 0.3s ease",
                display: "flex",
                alignItems: "center",
                justifyContent: "center"
            }
        },

        container: {
            styles: {
                position: "fixed",
                top: "40px",
                left: "20px",
                zIndex: "999",
                opacity: "0",
                visibility: "hidden",
                transition: "all 0.3s ease",
                transformOrigin: "top left"
            }
        },

        menuItems: [
                            {
                                icon: "ℹ️",
                                text: "Information",
                                action: "mostrarInfo"
                            },
                    {
                        icon: "✏️",
                        text: "Alterar Título",
                        action: "alterarTitulo"
                    },
            {
                icon: "🗂️",
                text: "Selecionar Pasta",
                action: "abrirPastaAndroid"
            },{
              icon: "📚",
              text: "selecionar Modulo Externo",
              action: "setExternModels"
            },
            {
                icon: "📋️",
                text: "Listar Arquivos",
                action: "listar_arquivos"
            },

            {
                icon: "🗑️",
                text: "Limpar",
                action: "limparResultado"
            }
        ],

        animations: {
            itemSpacing: 50,
            itemMargin: 4,
            animationDelay: 100
        }
    };

    window.FloatingMenuSystem.register('FloatingMenuConfig', FloatingMenuConfig);
})();
