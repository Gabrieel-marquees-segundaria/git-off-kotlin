console.log("main");
import { Manager, FilterMenu } from "./manager.js";
import Sockets from "/js/git_socket.js";



class ScreenManager {
    constructor() {
        console.log("constructor");
        this.manager = new Manager();
        //this.socket = new Sockets();
        this.filter_menu = new FilterMenu();

        this.setup_screen();
    }

    setup_screen() {
        console.log("setup_screen");
        this.manager.setup_events();
    }
    setFilterButtons() {
        fetch("./langs")
            .then(res => res.json())
            .then(data => {
                this.filter_menu.set_buttons(data.langs);
            })
            .catch(err => console.error(err));
    }
}

const screen_manager = new ScreenManager();
screen_manager.setFilterButtons();
