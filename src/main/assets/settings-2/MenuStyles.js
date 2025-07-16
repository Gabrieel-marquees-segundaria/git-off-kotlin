// MenuStyles.js - Estilos e CSS do menu flutuante
class MenuStyles {
    static getButtonStyles() {
        const position = MenuConfig.getMenuPosition();
        return {
            position: "fixed",
            top: position.top,
            left: position.left,
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
        };
    }

    static getContainerStyles() {
        const position = MenuConfig.getMenuPosition();
        return {
            position: "fixed",
            top: position.top,
            left: position.left,
            zIndex: "999",
            opacity: "0",
            visibility: "hidden",
            transition: "all 0.3s ease",
            transformOrigin: "top left"
        };
    }

    static getItemStyles(index) {
        return {
            display: "flex",
            alignItems: "center",
            padding: "6px 8px",
            backgroundColor: "rgba(255, 255, 255, 0.95)",
            backdropFilter: "blur(10px)",
            borderRadius: "12px",
            marginBottom: "4px",
            cursor: "pointer",
            transition: "all 0.2s ease",
            transform: `translateY(${(index + 1) * 10}px) scale(0.8)`,
            opacity: "0",
            boxShadow: "0 4px 15px rgba(0, 0, 0, 0.1)",
            minWidth: "180px"
        };
    }

    static getButtonHoverStyles() {
        return {
            transform: "scale(1.1)",
            boxShadow: "0 6px 25px rgba(52, 152, 219, 0.5)"
        };
    }

    static getButtonNormalStyles() {
        return {
            transform: "scale(1)",
            boxShadow: "0 4px 20px rgba(52, 152, 219, 0.4)"
        };
    }

    static getItemHoverStyles() {
        return {
            backgroundColor: "rgba(52, 152, 219, 0.1)"
        };
    }

    static getItemNormalStyles() {
        return {
            backgroundColor: "rgba(255, 255, 255, 0.95)"
        };
    }

    static addStyles() {
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
        `;

        document.head.appendChild(style);
    }
}