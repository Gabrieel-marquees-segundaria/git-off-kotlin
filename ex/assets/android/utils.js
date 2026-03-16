// utils.js - Funções utilitárias
const Utils = {
    escapeHtml: function(text) {
        const div = document.createElement("div");
        div.textContent = text;
        return div.innerHTML;
    },

    formatFileSize: function(bytes) {
        if (bytes === 0) return '0 Bytes';

        const k = 1024;
        const sizes = ['Bytes', 'KB', 'MB', 'GB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));

        return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    },

    getFileExtension: function(filename) {
        return filename.split('.').pop().toLowerCase();
    },

    isTextFile: function(filename) {
        const textExtensions = ['txt', 'json', 'xml', 'md', 'html', 'css', 'js', 'py', 'java', 'cpp', 'c', 'h'];
        return textExtensions.includes(this.getFileExtension(filename));
    },

    debounce: function(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }
};