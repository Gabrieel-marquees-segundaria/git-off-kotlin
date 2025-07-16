// MenuAnimations.js - Animações e transições do menu
class MenuAnimations {
    static openMenu(menuInstance) {
        menuInstance.isOpen = true;
        const animationSettings = MenuConfig.getAnimationSettings();

        // Animate button
        Object.assign(menuInstance.menuButton.style, MenuStyles.getButtonHoverStyles());
        menuInstance.menuButton.querySelector(".burger-icon").classList.add("open");

        // Show menu container
        menuInstance.menuContainer.style.opacity = "1";
        menuInstance.menuContainer.style.visibility = "visible";

        // Animate menu items
        menuInstance.menuItems.forEach((item, index) => {
            setTimeout(() => {
                item.style.transform = `translateY(${
                    (index + 1) * animationSettings.itemSpacing
                }px) scale(1)`;
                item.style.opacity = "1";
            }, index * animationSettings.animationDelay);
        });
    }

    static closeMenu(menuInstance) {
        menuInstance.isOpen = false;

        // Animate button
        Object.assign(menuInstance.menuButton.style, MenuStyles.getButtonNormalStyles());
        menuInstance.menuButton.querySelector(".burger-icon").classList.remove("open");

        // Hide menu items
        menuInstance.menuItems.forEach((item, index) => {
            item.style.transform = `translateY(${
                (index + 1) * 10
            }px) scale(0.8)`;
            item.style.opacity = "0";
        });

        // Hide menu container after animation
        setTimeout(() => {
            menuInstance.menuContainer.style.opacity = "0";
            menuInstance.menuContainer.style.visibility = "hidden";
        }, 200);
    }

    static animateItemHover(menuItem, index, isHover) {
        const animationSettings = MenuConfig.getAnimationSettings();
        const scale = isHover ? "scale(1)" : "scale(0.8)";
        const translateY = isHover ? 
            `translateY(${(index + 1) * animationSettings.itemSpacing}px)` : 
            `translateY(${(index + 1) * 10}px)`;
        
        menuItem.style.transform = `${translateY} ${scale}`;
    }
}