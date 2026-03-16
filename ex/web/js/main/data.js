// Sample file data
export const files = [
    {
        name: 'Relatório Mensal.pdf',
        size: '2.4 MB',
        type: 'pdf',
        icon: '📄',
        date: '2025-06-28'
    },
    {
        name: 'Apresentação.pptx',
        size: '5.1 MB',
        type: 'presentation',
        icon: '📊',
        date: '2025-06-27'
    },
    {
        name: 'Foto Férias.jpg',
        size: '3.2 MB',
        type: 'image',
        icon: '🖼️',
        date: '2025-06-26'
    },
    {
        name: 'Planilha Vendas.xlsx',
        size: '1.8 MB',
        type: 'spreadsheet',
        icon: '📈',
        date: '2025-06-25'
    },
    {
        name: 'Música Favorita.mp3',
        size: '4.5 MB',
        type: 'audio',
        icon: '🎵',
        date: '2025-06-24'
    },
    {
        name: 'Vídeo Tutorial.mp4',
        size: '15.2 MB',
        type: 'video',
        icon: '🎥',
        date: '2025-06-23'
    },
    {
        name: 'Documento Importante.docx',
        size: '890 KB',
        type: 'document',
        icon: '📝',
        date: '2025-06-22'
    },
    {
        name: 'Backup Sistema.zip',
        size: '45.7 MB',
        type: 'archive',
        icon: '🗜️',
        date: '2025-06-21'
    }
];
        export const fileTypeMap = {
            // Documentos
            'pdf': { icon: '📄', type: 'document', name: 'PDF' },
            'doc': { icon: '📝', type: 'document', name: 'Word' },
            'docx': { icon: '📝', type: 'document', name: 'Word' },
            'txt': { icon: '📄', type: 'document', name: 'Texto' },
            'rtf': { icon: '📄', type: 'document', name: 'RTF' },
            
            // Planilhas
            'xlsx': { icon: '📊', type: 'document', name: 'Excel' },
            'xls': { icon: '📊', type: 'document', name: 'Excel' },
            'csv': { icon: '📊', type: 'document', name: 'CSV' },
            
            // Apresentações
            'pptx': { icon: '📺', type: 'document', name: 'PowerPoint' },
            'ppt': { icon: '📺', type: 'document', name: 'PowerPoint' },
            
            // Imagens
            'jpg': { icon: '🖼️', type: 'image', name: 'JPEG' },
            'jpeg': { icon: '🖼️', type: 'image', name: 'JPEG' },
            'png': { icon: '🖼️', type: 'image', name: 'PNG' },
            'gif': { icon: '🎞️', type: 'image', name: 'GIF' },
            'bmp': { icon: '🖼️', type: 'image', name: 'BMP' },
            'svg': { icon: '🎨', type: 'image', name: 'SVG' },
            'webp': { icon: '🖼️', type: 'image', name: 'WebP' },
            
            // Áudio
            'mp3': { icon: '🎵', type: 'audio', name: 'MP3' },
            'wav': { icon: '🎵', type: 'audio', name: 'WAV' },
            'flac': { icon: '🎵', type: 'audio', name: 'FLAC' },
            'aac': { icon: '🎵', type: 'audio', name: 'AAC' },
            'ogg': { icon: '🎵', type: 'audio', name: 'OGG' },
            
            // Vídeo
            'mp4': { icon: '🎬', type: 'video', name: 'MP4' },
            'avi': { icon: '🎬', type: 'video', name: 'AVI' },
            'mkv': { icon: '🎬', type: 'video', name: 'MKV' },
            'mov': { icon: '🎬', type: 'video', name: 'MOV' },
            'wmv': { icon: '🎬', type: 'video', name: 'WMV' },
            'webm': { icon: '🎬', type: 'video', name: 'WebM' },
            
            // Código
            'js': { icon: '⚡', type: 'code', name: 'JavaScript' },
            'html': { icon: '🌐', type: 'code', name: 'HTML' },
            'css': { icon: '🎨', type: 'code', name: 'CSS' },
            'py': { icon: '🐍', type: 'code', name: 'Python' },
            'java': { icon: '☕', type: 'code', name: 'Java' },
            'cpp': { icon: '⚙️', type: 'code', name: 'C++' },
            'c': { icon: '⚙️', type: 'code', name: 'C' },
            'php': { icon: '🐘', type: 'code', name: 'PHP' },
            'rb': { icon: '💎', type: 'code', name: 'Ruby' },
            'go': { icon: '🚀', type: 'code', name: 'Go' },
            'json': { icon: '📋', type: 'code', name: 'JSON' },
            'xml': { icon: '📋', type: 'code', name: 'XML' },
            
            // Compactados
            'zip': { icon: '🗜️', type: 'archive', name: 'ZIP' },
            'rar': { icon: '🗜️', type: 'archive', name: 'RAR' },
            '7z': { icon: '🗜️', type: 'archive', name: '7-Zip' },
            'tar': { icon: '🗜️', type: 'archive', name: 'TAR' },
            
            // Outros
            'exe': { icon: '⚙️', type: 'executable', name: 'Executável' },
            'apk': { icon: '📱', type: 'executable', name: 'Android' },
            'dmg': { icon: '💿', type: 'executable', name: 'macOS' }
        };

// Sidebar menu items
export const sidebarItems = [
    {
        icon: '🏠',
        label: 'Início',
        id: 'home',
        active: true
    },
    {
        icon: '📁',
        label: 'Documentos',
        id: 'documents'
    },
    {
        icon: '🖼️',
        label: 'Imagens',
        id: 'images'
    },
    {
        icon: '🎵',
        label: 'Áudio',
        id: 'audio'
    },
    {
        icon: '🎥',
        label: 'Vídeos',
        id: 'videos'
    },
    {
        icon: '🗑️',
        label: 'Lixeira',
        id: 'trash'
    }
];

// Content headers for different categories
export const contentHeaders = {
    'home': {
        title: 'Lista de Arquivos',
        subtitle: 'Gerencie seus arquivo de forma inteligente'
    },
    'documents': {
        title: 'Documentos',
        subtitle: 'Seus documentos e planilhas organizados'
    },
    'images': {
        title: 'Imagens',
        subtitle: 'Suas fotos e imagens em um só lugar'
    },
    'audio': {
        title: 'Áudio',
        subtitle: 'Suas músicas e arquivos de áudio'
    },
    'videos': {
        title: 'Vídeos',
        subtitle: 'Seus vídeos e conteúdo multimídia'
    },
    'trash': {
        title: 'Lixeira',
        subtitle: 'Arquivos excluídos temporariamente'
    }
};