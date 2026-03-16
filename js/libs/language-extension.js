const linguagens = {
    py: "python",
    "js": "javascript",
    "ts": "typescript",
    "java": "java",
    "c": "c",
    "cpp": "cpp",
    "cs": "csharp",
    "php": "php",
    "html": "html",
    "css": "css",
    "scss": "scss",
    "json": "json",
    "sql": "sql",
    "ruby": "ruby",
    "go": "go",
    "rs": "rust",
    "swift": "swift",
    "kt": "kotlin",
    "sh": "bash",
    "pl": "perl",
    "lua": "lua",
    "r": "r",
    "yml": "yaml",
    "xml": "xml",
    "md": "markdown"
};
function getLangFromFile(filename) {
    const extension = filename.split(".").pop();
    const lang = linguagens[extension];
    if (lang) {
        return lang;
    }
    return null;
}
