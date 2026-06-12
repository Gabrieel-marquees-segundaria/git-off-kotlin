from http.server import HTTPServer, SimpleHTTPRequestHandler
import os
import threading

_server = None

def start_server(path, port=8080):
    global _server
    if _server:
        return
    os.chdir(path)

    _server = HTTPServer(
        ("0.0.0.0", port),
        SimpleHTTPRequestHandler
    )

    threading.Thread(
        target=_server.serve_forever,
        daemon=True
    ).start()

    return f"http://127.0.0.1:{port}"



def stop_server():
    global _server

    if _server:
        _server.shutdown()
        _server.server_close()
        _server = None

    return "Servidor parado"