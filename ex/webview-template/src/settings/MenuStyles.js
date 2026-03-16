// MenuStyles.js - Responsável pelos estilos CSS do menu
export class MenuStyles {
    static init() {
        const style = document.createElement("style");
        style.textContent = `
            .burger-icon {
                width: 24px;
                height: 18px;
                position: relative;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
            }
            
            .burger-icon span {
                display: block;
                height: 2px;
                width: 100%;
                background: white;
                border-radius: 1px;
                transition: all 0.3s ease;
            }
            
            .burger-icon.open span:nth-child(1) {
                transform: rotate(45deg) translate(5px, 5px);
            }
            
            .burger-icon.open span:nth-child(2) {
                opacity: 0;
            }
            
            .burger-icon.open span:nth-child(3) {
                transform: rotate(-45deg) translate(7px, -6px);
            }
            
            .menu-item-icon {
                font-size: 18px;
                margin-right: 12px;
            }
            
            .menu-item-text {
                font-size: 14px;
                font-weight: 500;
                color: #2c3e50;
            }
            
            .floating-menu-button:hover {
                transform: scale(1.1);
                box-shadow: 0 6px 25px rgba(52, 152, 219, 0.5);
            }
            
            .floating-menu-item:hover {
                background-color: rgba(52, 152, 219, 0.1);
                transform: translateY(var(--item-y)) scale(1);
            }
        `;

        document.head.appendChild(style);
    }
}