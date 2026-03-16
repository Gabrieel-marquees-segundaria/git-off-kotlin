import Sockets from "/js/git_socket.js";

class Manager {
    constructor(cls_socket = Sockets) {
        this.cls_socket = new cls_socket();
        this.callbacks = {};
    }
    setup_events() {
        console.log(this.callbacks);
        Object.entries(this.callbacks).forEach(([key, value]) => {
            console.log(this.callbacks[key]);
            this.cls_socket.listener(
                this.cls_socket.eventRegister(key),
                this.callbacks[key]
            );
            this.cls_socket.speaker(this.cls_socket.events[key], "hello world");
        });
    }
    /**
     * @exemple
     * function callback(data){
     *    code....
     *                      }
     */
    register_callback(event, callback) {
        console.log(callback);
        if (this.callbacks.length == 0) {
            this.callbacks = { event: callback };
        }
        this.callbacks[event] = callback;
    }
}

class Path {
    is_dir(path) {
        if (path.includes(".")) {
            return true;
        }
        return false;
    }
}
export const path = new Path();

class FilterMenu {
    constructor() {
        this.menu = document.getElementById("filterMenu");
        this.button = document.createElement("button");
    }
    get_clone_btn() {
        return this.button.cloneNode();
    }
    set_buttons(list) {
        console.log(list);
        if (!list) return;

        list.forEach(item => {
            if (path.is_dir(item)) {
                return;
            }
            const clone = this.get_clone_btn();
            clone.innerHTML = item;
            this.menu.appendChild(clone);
        });
    }
}

export { Manager, FilterMenu };
