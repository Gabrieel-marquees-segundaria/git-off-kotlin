// Component creation utilities

// Create sidebar item element
export function createSidebarItem(item) {
    const itemElement = document.createElement("div");
    itemElement.className = `sidebar-item ${item.active ? "active" : ""}`;
    itemElement.setAttribute("data-id", item.id);

    itemElement.innerHTML = `
        <span class="sidebar-icon">${item.icon}</span>
        <span class="sidebar-label">${item.label}</span>
    `;

    // Add click event
    itemElement.addEventListener("click", () => {
        handleSidebarClick(item);
    });

    return itemElement;
}

// Create file card element
export function createFileCard(file, formatDate) {
    const card = document.createElement("div");
    card.className = "file-card";

    card.innerHTML = `
        <div class="file-icon">${file.icon}</div>
        <div class="file-name">${file.name}</div>
        <div class="file-size">${file.size}</div>
        <div class="file-date">${formatDate(file.date)}</div>
    `;

    // Add click event
    card.addEventListener("click", () => {
        handleFileClick(file);
    });

    return card;
}

// Render sidebar
export function renderSidebar(sidebarItems) {
    const sidebar = document.getElementById("sidebar");
    sidebar.innerHTML = "";

    sidebarItems.forEach(item => {
        const sidebarItem = createSidebarItem(item);
        sidebar.appendChild(sidebarItem);
    });
}

// Render files
export function renderFiles(filesToRender, formatDate) {
    const namesGrid = document.getElementById("namesGrid");
    namesGrid.innerHTML = "";

    if (filesToRender.length === 0) {
        const emptyMessage = document.createElement("div");
        emptyMessage.className = "empty-message";
        emptyMessage.innerHTML = `
            <div style="text-align: center; color: #999; font-size: 1.2rem; margin-top: 50px;">
                <div style="font-size: 4rem; margin-bottom: 20px;">📂</div>
                <div>Nenhum arquivo encontrado</div>
            </div>
        `;
        namesGrid.appendChild(emptyMessage);
        return;
    }

    filesToRender.forEach(file => {
        const fileCard = createFileCard(file, formatDate);
        namesGrid.appendChild(fileCard);
    });
}

// Update content header based on category
export function updateContentHeader(category) {
    const contentTitle = document.querySelector(".content-title");
    const contentSubtitle = document.querySelector(".content-subtitle");

    const header = contentHeaders[category] || contentHeaders["home"];
    contentTitle.textContent = header.title;
    contentSubtitle.textContent = header.subtitle;
}
