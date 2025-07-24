from IPrinter import Service 
import socket 


class Proxy(Service):
    def __init__(self,ip,port):
        self.ip = ip 
        self.port = port 
        self.buf_size = 1024


    def print(self,path,tipo):
        msg = f"{path}-{tipo}"
        print("[PROXY] Sending:", msg)

        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as server: 
            server.connect((self.ip, self.port))
            server.send(msg.encode("utf-8"))
            server.close()