class EventRegister {
    constructor(event) {
        this.value;
    }
}

class Sockets {
    constructor(callback = null) {
        this.io = io(); // Inicializa o socket
        this.registerEvents();
        this.events = {};
        this.status = false;
        this.to_schedule = { listener: {}, speaker: {} };
    }

    registerEvents() {
        this.io.on("connect", () => {
            console.log("Connected to server");
            this.status = true;
            if (this.to_schedule.listener) {
                Object.entries(this.to_schedule.listener).forEach(
                    ([event, callback]) => {
                        console.log(event, callback);
                        this.io.on(event, callback);
                    }
                );
            }
            if (this.to_schedule.speaker) {
                Object.entries(this.to_schedule.speaker).forEach(
                    ([event, data]) => {
                        console.log(event, data);
                        this.io.emit(event, data);
                    }
                );
            }
            console.log(this.to_schedule);
        });

        this.io.on("disconnect", () => {
            console.log("Disconnected from server");
            this.status = false;
        });
    }

    /**
     * Cria um novo listener (ouvinte)
     * @param {string} event - Nome do evento
     * @param {function} callback - Função a ser executada quando o evento ocorrer
     */
    listener(event, callback) {
        if (!this.status) {
            this.to_schedule.listener[event] = callback;
            return;
        }

        this.io.on(event, callback);
    }

    /**
     * Cria um novo speaker (transmissor de dados)
     * @param {string} event - Nome do evento
     * @param {any} data - Dados a serem enviados
     */
    speaker(event, data) {
        console.log("speaker");
        if (!this.status) {
            this.to_schedule.speaker[event] = data;
            return;
        }
        this.io.emit(event, data);
    }

    eventRegister(event) {
        if (!event) {
            console.error("event not found");
        }
        this.events[event] = event;
        return event;
    }
}

export default Sockets;
export { Sockets };
