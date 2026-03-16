
// ========================================
// MÓDULO 9: Verificador de Integridade
// ========================================
(function() {
    'use strict';

    // Verificar se todos os módulos foram carregados após 2 segundos
    setTimeout(() => {
        const system = window.FloatingMenuSystem;
        const expectedModules = [
            'FloatingMenuStyles',
            'FloatingMenuConfig',
            'FloatingMenuUtils',
            'FloatingMenuButton',
            'FloatingMenuContainer',
            'FloatingMenuCore',
            'FloatingMenuApp'
        ];

        let allLoaded = true;
        expectedModules.forEach(module => {
            if (!system.modules[module] || !system.modules[module].initialized) {
                console.warn(`Módulo ${module} não foi inicializado corretamente`);
                allLoaded = false;
            }
        });

        if (allLoaded) {
            console.log('✅ Todos os módulos do FloatingMenu foram carregados com sucesso!');
        } else {
            console.warn('⚠️ Alguns módulos do FloatingMenu falharam ao carregar');
        }
    }, 2000);
})();