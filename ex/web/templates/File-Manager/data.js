// Sample file data
const files = [
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

// Sidebar menu items
const sidebarItems = [
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
const contentHeaders = {
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