// MenuContainer.js - Responsável pela criação do container do menu
export class MenuContainer {
    static create() {
        const menuContainer = document.createElement("div");
        menuContainer.className = "floating-menu-container";

        Object.assign(menuContainer.style, {
            position: "fixed",
            top: "40px",
            left: "20px",
            zIndex: "999",
            opacity: "0",
            visibility: "hidden",
            transition: "all 0.3s ease",
            transformOrigin: "top left"
        });

        document.body.appendChild(menuContainer);
        return menuContainer;
    }
}