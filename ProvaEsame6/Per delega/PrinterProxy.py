from IPrinter import Service 
import socket 



class ServiceImpl(Service): 
    def __init__(self,ip,port): 
        self.ip = ip 
        self.port = port 
        self.buf_size = 1024

    def print(self,path,tipo): 
        msg = f"{path}-{tipo}"

        print("[Proxy] Sending:", msg)

        with socket.socket(socket.AF_INET,socket.SOCK_DGRAM)as server: 
            server.sendto(msg.encode(), (self.ip,self.port))