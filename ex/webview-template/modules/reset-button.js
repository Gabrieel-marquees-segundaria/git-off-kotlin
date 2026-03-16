// const menuItem = document.createElement("div");
// menuItem.className = "floating-menu-item";
// menuItem.innerHTML = `
//                 <div class="menu-item-icon">${item.icon}</div>
//                 <div class="menu-item-text">${item.text}</div>
//             `;

// menuItem.style.display = "flex";
// menuItem.style.alignItems = "center";
// menuItem.style.padding = "12px 16px";
// menuItem.style.backgroundColor = "rgba(255, 255, 255, 0.95)";
// menuItem.style.backdropFilter = "blur(10px)";
// menuItem.style.borderRadius = "12px";
// menuItem.style.marginBottom = "8px";
// menuItem.style.cursor = "pointer";
// menuItem.style.transition = "all 0.2s ease";
// menuItem.style.transform = `translateY(${(index + 1) * 10}px) scale(0.8)`;
// menuItem.style.opacity = "0";
// menuItem.style.boxShadow = "0 4px 15px rgba(0, 0, 0, 0.1)";
// menuItem.style.minWidth = "180px";

// // Hover effect
// menuItem.addEventListener("mouseenter", () => {
//     menuItem.style.backgroundColor = "rgba(52, 152, 219, 0.1)";
//     menuItem.style.transform = menuItem.style.transform.replace(
//         "scale(0.8)",
//         "scale(1)"
//     );
// });

// menuItem.addEventListener("mouseleave", () => {
//     menuItem.style.backgroundColor = "rgba(255, 255, 255, 0.95)";
//     if (this.isOpen) {
//         menuItem.style.transform = `translateY(${(index + 1) * 80}px) scale(1)`;
//     }
// });

// // Click action
// menuItem.addEventListener("click", () => {
//     item.action();
//     this.closeMenu();
// });

// document.querySelector(".floating-menu-container").appendChild(menuItem);
// menuItems.push(menuItem);
