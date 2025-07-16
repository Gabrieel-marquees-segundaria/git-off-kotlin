// MenuEvents.js - Gerenciamento de eventos do menu
class MenuEvents {
    static addMenuEventListeners(menuInstance) {
        // Toggle menu on button click
        menuInstance.menuButton.addEventListener("click", e => {
            e.stopPropagation();
            menuInstance.toggleMenu();
        });

        // Close menu when clicking outside
        document.addEventListener("click", e => {
            if (
                !menuInstance.menuContainer.contains(e.target) &&
                !menuInstance.menuButton.contains(e.target)
            ) {
                menuInstance.closeMenu();
            }
        });

        // Add hover effects to main button
        menuInstance.menuButton.addEventListener("mouseenter", () => {
            Object.assign(menuInstance.menuButton.style, MenuStyles.getButtonHoverStyles());
        });

        menuInstance.menuButton.addEventListener("mouseleave", () => {
            if (!menuInstance.isOpen) {
                Object.assign(menuInstance.menuButton.style, MenuStyles.getButtonNormalStyles());
            }
        });
    }

    static addItemEventListeners(menuItem, item, index, menuInstance) {
        // Hover effect
        menuItem.addEventListener("mouseenter", () => {
            Object.assign(menuItem.style, MenuStyles.getItemHoverStyles());
            menuItem.style.transform = menuItem.style.transform.replace(
                "scale(0.8)",
                "scale(1)"
            );
        });

        menuItem.addEventListener("mouseleave", () => {
            Object.assign(menuItem.style, MenuStyles.getItemNormalStyles());
            if (menuInstance.isOpen) {
                const spacing = MenuConfig.getAnimationSettings().itemSpacing;
                menuItem.style.transform = `translateY(${
                    (index + 1) * spacing
                }px) scale(1)`;
            }
        });

        // Click action
        menuItem.addEventListener("click", () => {
            item.action();
            menuInstance.closeMenu();
        });
    }
}