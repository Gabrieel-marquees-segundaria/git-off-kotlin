
// ========================================
// MÓDULO 8: Inicializador Global
// ========================================
(function() {
    'use strict';

    const FloatingMenuApp = {
        initialized: false,

        init() {
            if (this.initialized) return;

            try {
                // Verificar se todos os módulos estão disponíveis
                const core = window.FloatingMenuSystem.getDependency('FloatingMenuCore');

                if (core && core.initialized) {
                    this.initialized = true;
                    console.log('FloatingMenu App inicializado com sucesso!');
                } else {
                    // Tentar novamente em 100ms
                    setTimeout(() => this.init(), 100);
                }
            } catch (error) {
                console.error('Erro ao inicializar FloatingMenuApp:', error);
            }
        }
    };

    window.FloatingMenuSystem.register('FloatingMenuApp', FloatingMenuApp);

    // Auto-executar quando a página carregar
    function startApp() {
        if (document.readyState === 'loading') {
            document.addEventListener('DOMContentLoaded', () => {
                FloatingMenuApp.init();
            });
        } else {
            FloatingMenuApp.init();
        }
    }

    // Iniciar imediatamente
    startApp();
})();
