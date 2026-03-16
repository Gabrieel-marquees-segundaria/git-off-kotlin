// DOM Elements
const burgerMenu = document.getElementById('burgerMenu');
const sidebar = document.getElementById('sidebar');
const sidebarOverlay = document.getElementById('sidebarOverlay');
const searchInput = document.getElementById('searchInput');

// Application state
let currentCategory = 'home';
let currentFiles = files;

// Initialize app
function init() {
    setupEventListeners();
    renderSidebar();
    renderFiles(files);
}

// Setup event listeners
function setupEventListeners() {
    // Burger menu toggle
    burgerMenu.addEventListener('click', toggleSidebar);
    
    // Sidebar overlay click to close
    sidebarOverlay.addEventListener('click', closeSidebar);
    
    // Search functionality
    searchInput.addEventListener('input', handleSearch);
    
    // Handle window resize
    window.addEventListener('resize', () => {
        if (window.innerWidth > 768) {
            closeSidebar();
        }
    });
}

// Sidebar functionality
function toggleSidebar() {
    sidebar.classList.toggle('active');
    sidebarOverlay.classList.toggle('active');
    burgerMenu.classList.toggle('active');
}

function closeSidebar() {
    sidebar.classList.remove('active');
    sidebarOverlay.classList.remove('active');
    burgerMenu.classList.remove('active');
}

// Handle sidebar item click
function handleSidebarClick(item) {
    // Remove active class from all items
    const allItems = document.querySelectorAll('.sidebar-item');
    allItems.forEach(el => el.classList.remove('active'));
    
    // Add active class to clicked item
    const clickedItem = document.querySelector(`[data-id="${item.id}"]`);
    clickedItem.classList.add('active');
    
    // Update current category
    currentCategory = item.id;
    
    // Filter files based on selection
    filterFilesByCategory(item.id);
    
    // Close sidebar on mobile
    closeSidebar();
}

// Filter files by category
function filterFilesByCategory(category) {
    let filteredFiles = files;
    
    switch(category) {
        case 'documents':
            filteredFiles = files.filter(file => 
                ['pdf', 'document', 'spreadsheet', 'presentation'].includes(file.type)
            );
            break;
        case 'images':
            filteredFiles = files.filter(file => file.type === 'image');
            break;
        case 'audio':
            filteredFiles = files.filter(file => file.type === 'audio');
            break;
        case 'videos':
            filteredFiles = files.filter(file => file.type === 'video');
            break;
        case 'trash':
            filteredFiles = []; // Empty for trash
            break;
        default:
            filteredFiles = files; // Show all for home
    }
    
    currentFiles = filteredFiles;
    renderFiles(filteredFiles);
    updateContentHeader(category);
}

// Search functionality
function handleSearch(e) {
    const searchTerm = e.target.value.toLowerCase();
    
    // Filter current category files by search term
    const filteredFiles = currentFiles.filter(file => 
        file.name.toLowerCase().includes(searchTerm)
    );
    
    renderFiles(filteredFiles);
}

// File handling
function handleFileClick(file) {
    alert(`Abrindo arquivo: ${file.name}`);
    // Add file opening logic here
    console.log('File clicked:', file);
}

// Utility functions
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    });
}

// Initialize app when DOM is loaded
document.addEventListener('DOMContentLoaded', init);