import Html from "./BigTree/html.js";
const MenuStyle = `
            .burger-menu {
                width: 30px;
                height: 25px;
                cursor: pointer;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                transition: transform 0.3s ease;
            }

            .burger-menu:hover {
                transform: scale(1.1);
            }

            .burger-line {
                width: 100%;
                height: 3px;
                background: white;
                border-radius: 2px;
                transition: all 0.3s ease;
            }

            .burger-menu.active .burger-line:nth-child(1) {
                transform: rotate(45deg) translate(8px, 8px);
            }

            .burger-menu.active .burger-line:nth-child(2) {
                opacity: 0;
            }

            .burger-menu.active .burger-line:nth-child(3) {
                transform: rotate(-45deg) translate(8px, -8px);
            }
`;
// <!-- Sidebar Menu -->
// <div class="sidebar" id="sidebar">
//     <div class="sidebar-item">🏠 Início</div>
// </div>

class SidebarItem extends Html {
    constructor(text, document, tag = "div", args = { class: "sidebar-item" }) {
        super(tag, document, args);
        this.set_text(text);
    }
}
class Sidebar extends Html {
    constructor(
        items,
        document,
        tag = "div",
        args = { class: "sidebar", id: "sidebar" }
    ) {
        super(tag, document, args);
        this._create(items, document);
    }
    _create(items, document) {
        items.forEach(item => this.add_child(SidebarItem(item, document)));
    }
}

class MenuBurger {
    constructor(sidebar_item, document, pai_class) {
        this.style = MenuStyle;
        document.querySelector("." + pai_class).appendChild(this.style);
        this.sidebar = Sidebar(sidebar_item, document);
    }
}
export default MenuBurger;
