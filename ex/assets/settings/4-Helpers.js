
// ========================================
// MÓDULO 4: Utilitários e Helpers
// ========================================
(function() {
    'use strict';

    const FloatingMenuUtils = {
        initialized: false,

        init() {
            if (this.initialized) return;
            this.initialized = true;
        },

        applyStyles(element, styles) {
            if (!element || !styles) return;
            Object.assign(element.style, styles);
        },

        createElement(tag, className, innerHTML) {
            const element = document.createElement(tag);
            if (className) element.className = className;
            if (innerHTML) element.innerHTML = innerHTML;
            return element;
        },

        alterarTitulo() {
            const titleElement = document.getElementById("title");
            if (titleElement) {
                const novoTitulo = prompt(
                    "Digite o novo título:",
                    titleElement.textContent || titleElement.innerText || ""
                );
                if (novoTitulo !== null && novoTitulo.trim() !== "") {
                    titleElement.textContent = novoTitulo.trim();
                }
            } else {
                alert('Elemento com id "title" não encontrado na página.');
            }
        },

        executeAction(actionName) {
            try {
                if (actionName === 'alterarTitulo') {
                    this.alterarTitulo();
                } else if (typeof window[actionName] === 'function') {
                    window[actionName]();
                } else {
                    console.warn(`Ação "${actionName}" não encontrada`);
                }
            } catch (error) {
                console.error(`Erro ao executar ação "${actionName}":`, error);
            }
        },

        safeTimeout(callback, delay) {
            return setTimeout(() => {
                try {
                    callback();
                } catch (error) {
                    console.error('Erro no timeout:', error);
                }
            }, delay);
        }
    };

    window.FloatingMenuSystem.register('FloatingMenuUtils', FloatingMenuUtils);
})();