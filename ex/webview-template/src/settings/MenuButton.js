// MenuButton.js - Responsável pela criação do botão principal
export class MenuButton {
    static create() {
        const menuButton = document.createElement("button");
        menuButton.className = "floating-menu-button";
        menuButton.innerHTML = `
            <div class="burger-icon">
                <span></span>
                <span></span>
                <span></span>
            </div>
        `;

        // Estilos inline para o botão principal
        Object.assign(menuButton.style, {
            position: "fixed",
            top: "40px",
            left: "20px",
            width: "60px",
            height: "60px",
            borderRadius: "50%",
            background: "linear-gradient(135deg, #3498db, #2980b9)",
            border: "none",
            cursor: "pointer",
            zIndex: "1000",
            boxShadow: "0 4px 20px rgba(52, 152, 219, 0.4)",
            transition: "all 0.3s ease",
            display: "flex",
            alignItems: "center",
            justifyContent: "center"
        });

        document.body.appendChild(menuButton);
        return menuButton;
    }
}