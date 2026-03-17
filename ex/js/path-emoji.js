class PathEmoji {
    fileExtensionsWithEmojis = {
        // Documentos de texto
        txt: "📄",
        doc: "📝",
        docx: "📝",
        pdf: "📕",
        rtf: "📄",
        odt: "📄",

        // Planilhas
        xls: "📊",
        xlsx: "📊",
        csv: "📋",
        ods: "📊",

        // Apresentações
        ppt: "📊",
        pptx: "📊",
        odp: "📊",

        // Imagens
        jpg: "🖼️",
        jpeg: "🖼️",
        png: "🖼️",
        gif: "🎞️",
        bmp: "🖼️",
        svg: "🎨",
        ico: "🖼️",
        webp: "🖼️",
        tiff: "🖼️",
        raw: "📷",

        // Vídeos
        mp4: "🎥",
        avi: "🎥",
        mkv: "🎥",
        mov: "🎥",
        wmv: "🎥",
        flv: "🎥",
        webm: "🎥",
        m4v: "🎥",
        "3gp": "🎥",

        // Áudios
        mp3: "🎵",
        wav: "🎵",
        flac: "🎵",
        aac: "🎵",
        ogg: "🎵",
        m4a: "🎵",
        wma: "🎵",

        // Códigos
        js: "💛",
        ts: "🔷",
        html: "🌐",
        css: "🎨",
        py: "🐍",
        java: "☕",
        cpp: "⚡",
        c: "⚡",
        php: "🐘",
        rb: "💎",
        go: "🐹",
        rs: "🦀",
        swift: "🍎",
        kt: "🎯",
        scala: "🌶️",
        sql: "🗄️",
        sh: "🐚",
        json: "📋",
        xml: "📋",
        yaml: "📋",
        yml: "📋",

        // Arquivos comprimidos
        zip: "🗜️",
        rar: "🗜️",
        "7z": "🗜️",
        tar: "🗜️",
        gz: "🗜️",
        bz2: "🗜️",

        // Executáveis e instaladores
        exe: "⚙️",
        msi: "⚙️",
        deb: "📦",
        rpm: "📦",
        dmg: "💾",
        pkg: "📦",
        app: "📱",
        apk: "🤖",

        // Fontes
        ttf: "🔤",
        otf: "🔤",
        woff: "🔤",
        woff2: "🔤",
        eot: "🔤",

        // Outros
        log: "📜",
        cfg: "⚙️",
        ini: "⚙️",
        conf: "⚙️",
        env: "🔧",
        key: "🔑",
        pem: "🔐",
        crt: "🔐",
        iso: "💿",
        torrent: "🌊",
        db: "🗄️",
        sqlite: "🗄️",
        backup: "💾",
        tmp: "🗑️",
        cache: "⚡"
    };

    // Função para obter emoji de uma extensão
    getEmojiForExtension(extension) {
        const ext = extension.toLowerCase().replace(".", "");
        return this.fileExtensionsWithEmojis[ext] || "📄"; // Retorna emoji padrão se não encontrado
    }

    // Função para obter emoji de um nome de arquivo
    getEmojiForFile(filename) {
        if (!filename.includes(".")) {
            return "📁 ";
        }
        const extension = filename.split(".").pop();
        return this.getEmojiForExtension(extension);
    }
}

let path_emoji = new PathEmoji();

// Exemplos de uso:
// console.log(getEmojiForExtension("js")); // 💛
// console.log(getEmojiForExtension(".pdf")); // 📕
// console.log(getEmojiForFile("document.docx")); // 📝
// console.log(getEmojiForFile("music.mp3")); // 🎵
// console.log(getEmojiForFile("video.mp4")); // 🎥

// Exportar para uso em módulos
// export { fileExtensionsWithEmojis, getEmojiForExtension, getEmojiForFile };
