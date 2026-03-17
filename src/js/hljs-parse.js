 export const syntax_highlight_map = {
    ".py": "python",
    ".js": "javascript",
    ".cjs": "javascript",
    ".mjs": "javascript", // Módulos JavaScript
    ".ts": "typescript",
    ".tsx": "typescript",
    ".jsx": "javascript", //JSX é geralmente destacado como JavaScript
    ".html": "xml", // HTML é uma forma de XML para highlight.js
    ".htm": "xml",
    ".css": "css",
    ".scss": "scss",
    ".less": "less",
    ".json": "json",
    ".xml": "xml",
    ".yml": "yaml",
    ".yaml": "yaml",
    ".md": "markdown",
    ".sh": "bash",
    ".bash": "bash",
    ".zsh": "bash",
    ".kt": "kotlin",
    ".kts": "kotlin",
    ".java": "java",
    ".c": "c",
    ".cpp": "cpp",
    ".h": "c", // Arquivos de cabeçalho C/C++
    ".hpp": "cpp",
    ".cs": "csharp",
    ".php": "php",
    ".go": "go",
    ".rb": "ruby",
    ".rs": "rust",
    ".swift": "swift",
    ".sql": "sql",
    ".lua": "lua",
    ".pl": "perl",
    ".r": "r",
    ".swift": "swift"
};

 export class Hljs {
    constructor() {
        this.langs = syntax_highlight_map;
    }
    escapeHtml(str) {
        return str.replace(
            /[&<>"']/g,
            match =>
                ({
                    "&": "&amp;",
                    "<": "&lt;",
                    ">": "&gt;",
                    '"': "&quot;",
                    "'": "&#39;"
                })[match]
        );
    }
    parse(content, fileName) {
        let extension = "";
        if (fileName.includes(".")) {
            extension = "." + fileName.split(".").pop();
        }
        if (extension && extension in this.langs) {
            return `<pre><code class="${this.langs[extension]}">${content}</code></pre>`;
        }
        return content;
    }
}
