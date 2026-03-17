    export    const fileContents = {
            'main.js': `// Main application entry point
function initApp() {
    console.log('Initializing git off application...');
    
    // Setup event listeners
    document.addEventListener('DOMContentLoaded', function() {
        loadConfiguration();
        initializeUI();
        bindEvents();
    });
}

function loadConfiguration() {
    // Load app configuration
    const config = {
        theme: 'dark',
        language: 'en',
        debug: true
    };
    
    return config;
}

function initializeUI() {
    // Initialize user interface components
    const sidebar = document.querySelector('.sidebar');
    const mainContent = document.querySelector('.main-content');
    
    if (sidebar && mainContent) {
        console.log('UI components initialized successfully');
    }
}

function bindEvents() {
    // Bind application events
    window.addEventListener('resize', handleResize);
    document.addEventListener('keydown', handleKeyboard);
}

function handleResize() {
    // Handle window resize events
    console.log('Window resized');
}

function handleKeyboard(event) {
    // Handle keyboard shortcuts
    if (event.ctrlKey && event.key === 's') {
        event.preventDefault();
        console.log('Save shortcut triggered');
    }
}

// Initialize the application
initApp();`,

            'config.json': `{
  "application": {
    "name": "git off",
    "version": "1.0.0",
    "description": "A modern file management interface",
    "author": "Developer"
  },
  "settings": {
    "theme": {
      "default": "dark",
      "available": ["dark", "light", "auto"]
    },
    "interface": {
      "animations": true,
      "shortcuts": true,
      "tooltips": true
    },
    "performance": {
      "lazyLoad": true,
      "cacheFiles": true,
      "maxFileSize": "10MB"
    }
  },
  "features": {
    "fileViewer": true,
    "textEditor": true,
    "searchFunction": true,
    "filterOptions": true
  },
  "api": {
    "endpoints": {
      "files": "/api/files",
      "upload": "/api/upload",
      "download": "/api/download"
    },
    "timeout": 30000,
    "retries": 3
  },
  "logging": {
    "level": "info",
    "file": "app.log",
    "maxSize": "100MB"
  }
}`,

            'README.md': `# Git Off

A modern, sleek file management interface with a beautiful dark theme and smooth animations.

## Features

- **Intuitive Interface**: Clean and modern design with smooth animations
- **File Management**: Easy file browsing and viewing capabilities
- **Responsive Design**: Works perfectly on all device sizes
- **Dark Theme**: Eye-friendly dark interface with vibrant accents
- **Modal Windows**: Popup windows for detailed file viewing
- **Interactive Elements**: Hover effects and smooth transitions

## Getting Started

1. Clone the repository
2. Open index.html in your browser
3. Start exploring files!

## File Structure

\`\`\`
git-off/
├── index.html          # Main HTML file
├── styles.css          # Stylesheet
├── main.js            # Main JavaScript
├── config.json        # Configuration
└── README.md          # Documentation
\`\`\`

## Usage

- Click the "Add New File" button to add files to the list
- Click on any file in the list to view its contents
- Use the close button (×) to close file viewers
- Enjoy the smooth animations and modern interface!

## Customization

You can customize the appearance by modifying the CSS variables:

\`\`\`css
:root {
  --primary-color: #667eea;
  --secondary-color: #4ecdc4;
  --background: #1a1a2e;
}
\`\`\`

## Browser Support

- Chrome 60+
- Firefox 55+
- Safari 12+
- Edge 79+

## Contributing

Feel free to submit issues and enhancement requests!

## License

MIT License - feel free to use this project however you'd like.`,

            'styles.css': `/* Git Off - Modern File Interface Styles */

:root {
  --primary-gradient: linear-gradient(45deg, #667eea, #764ba2);
  --secondary-gradient: linear-gradient(45deg, #ff6b6b, #4ecdc4);
  --bg-gradient: linear-gradient(135deg, #1a1a2e, #16213e);
  --glass-bg: rgba(255, 255, 255, 0.1);
  --border-color: rgba(255, 255, 255, 0.2);
  --text-primary: #ffffff;
  --text-secondary: #e0e0e0;
  --accent-color: #4ecdc4;
  --danger-color: #ff6b6b;
}

/* Global Styles */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background: var(--bg-gradient);
  color: var(--text-primary);
  min-height: 100vh;
  overflow-x: hidden;
}

/* Animations */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

@keyframes slideIn {
  from { transform: translateX(-100%); }
  to { transform: translateX(0); }
}

@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.05); }
}

.fade-in {
  animation: fadeIn 0.6s ease-out;
}

/* Responsive Design */
@media (max-width: 768px) {
  .container {
    padding: 1rem;
  }
  
  .modal-content {
    width: 95%;
    height: 80%;
  }
  
  .header h1 {
    font-size: 2rem;
  }
  
  .text-input {
    width: 250px;
  }
}`,

            'data.txt': `Git Off Application Data
========================

User Sessions:
- Session 1: 2024-01-15 14:23:45 - User browsed 12 files
- Session 2: 2024-01-15 15:17:32 - User added 3 new files
- Session 3: 2024-01-15 16:42:18 - User viewed README.md for 5 minutes

Recent Files:
1. main.js (Last modified: 2024-01-15 14:30:22)
2. config.json (Last modified: 2024-01-15 14:25:15)
3. styles.css (Last modified: 2024-01-15 14:35:45)
4. README.md (Last modified: 2024-01-15 14:20:10)
5. data.txt (Last modified: 2024-01-15 14:40:33)

System Information:
- Browser: Chrome 119.0
- Screen Resolution: 1920x1080
- Operating System: Windows 10
- Available Memory: 8GB
- Storage Used: 2.3MB

Performance Metrics:
- Average Load Time: 1.2s
- File Open Speed: 0.3s
- Animation Frame Rate: 60fps
- Memory Usage: 45MB
- CPU Usage: 12%

Application Statistics:
- Total Files: 25
- Total Views: 147
- Average Session Duration: 8.5 minutes
- User Satisfaction: 9.2/10
- Feature Usage:
  * File Viewer: 89%
  * Add File: 34%
  * Modal Windows: 67%
  * Search Function: 23%

Error Log:
[2024-01-15 14:25:30] INFO: Application started successfully
[2024-01-15 14:30:15] INFO: File main.js opened
[2024-01-15 14:35:22] INFO: New file added: test.js
[2024-01-15 14:40:10] INFO: Modal window opened for config.json
[2024-01-15 14:45:33] INFO: Session ended gracefully

Configuration Settings:
- Theme: Dark Mode
- Animations: Enabled
- Auto-save: Disabled
- File Caching: Enabled
- Debug Mode: False
- Max File Size: 10MB
- Backup Frequency: Daily

Network Activity:
- API Calls Made: 23
- Data Transferred: 1.2MB
- Average Response Time: 245ms
- Successful Requests: 100%
- Failed Requests: 0%

This data file contains various information about the Git Off application usage, performance metrics, and system information for monitoring and analysis purposes.`
        };

        
