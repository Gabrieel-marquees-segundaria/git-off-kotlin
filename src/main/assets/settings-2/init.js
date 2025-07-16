// init.js - Inicialização do menu flutuante
document.addEventListener("DOMContentLoaded", () => {
    // Verificar se todas as dependências estão carregadas
    if (typeof FloatingMenu === 'undefined') {
        console.error('FloatingMenu class not found. Make sure all files are loaded.');
        return;
    }

    // Inicializar o menu flutuante
    try {
        new FloatingMenu();
        console.log('FloatingMenu initialized successfully');
    } catch (error) {
        console.error('Error initializing FloatingMenu:', error);
    }
});