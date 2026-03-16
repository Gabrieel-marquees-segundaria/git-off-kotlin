// ========================================
// MÓDULO 1: Sistema de Dependências
// ========================================
(function() {
    'use strict';

    window.FloatingMenuSystem = {
        modules: {},
        dependencies: {
            'FloatingMenuStyles': [],
            'FloatingMenuConfig': [],
            'FloatingMenuUtils': ['FloatingMenuConfig'],
            'FloatingMenuButton': ['FloatingMenuConfig', 'FloatingMenuUtils'],
            'FloatingMenuContainer': ['FloatingMenuConfig', 'FloatingMenuUtils'],
            'FloatingMenuCore': ['FloatingMenuButton', 'FloatingMenuContainer'],
            'FloatingMenuApp': ['FloatingMenuCore']
        },

        register(name, module) {
            this.modules[name] = module;
            this.checkAndInitialize(name);
        },

        checkAndInitialize(name) {
            const deps = this.dependencies[name] || [];
            const allDepsReady = deps.every(dep => this.modules[dep]);

            if (allDepsReady && this.modules[name] && !this.modules[name].initialized) {
                if (typeof this.modules[name].init === 'function') {
                    this.modules[name].init();
                }
                this.modules[name].initialized = true;
                console.log(`${name} inicializado com sucesso!`);
            }
        },

        getDependency(name) {
            return this.modules[name];
        }
    };
})();
