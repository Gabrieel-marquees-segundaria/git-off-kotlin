class Html {
    constructor(tag, document, args = null) {
        this.args = ["style", "class", "id"];
        this.element = document.createElement(tag);
        if (args) {
        }
    }
    get_tag() {
        return this.element;
    }
    add_child(child) {
        if (child) return;
        this.element.appendChild(child);
    }
    set_args(args) {
        Object.entries(args).forEach(([chave, valor]) => {
            this.element.setAttribute(chave, valor);
        });
    }
    set_text(text) {
        this.element.textContent = text;
    }
}

export default Html;
export { Html };
