import socket 
import stomp 
import multiprocessing as mp 
import time 
from IPrinter import Service
import sys

def consumer_process(queue): 
    conn = stomp.Connection([('127.0.0.1',61613)])
    conn.connect(wait=True)



    while True: 
        msg = queue.get()
        tipo = msg.split('-')[1]
        if "color" in tipo: 
            destination = '/queue/color'
        else: 
            destination = '/queue/bw'
        print(f"[Cnsumer]Sending to {destination}: {msg}")
        conn.send(destination = destination, body = msg)

class ServiceSkeleton(Service): 
    def __init__(self,ip,port,delegate): 
        self.ip = ip 
        self.port = port 
        self.delegate = delegate


    def print(self,path,tipo): 
        return self.delegate.print(path,tipo)
    

    def run(self): 
        print(f"[ServerSkeleton] Server attivo su {self.ip}:{self.port}")
        with socket.socket(socket.AF_INET,socket.SOCK_DGRAM) as server: 
            server.bind(('localhost', self.port))
            
            while True: 
                data, addr = server.recvfrom(1024)
                msg = data.decode()
                if '-' not in msg: 
                    print(f"[Skeleton] Messaggio malformato: {msg}")
                    continue
                parts = msg.split("-")

                if len(parts) !=2: 
                    print(f"[Skeleton] Formato errato : {msg}")
                    continue 
                path,tipo = parts

class ServiceImpl(Service): 
    def __init__(self,queue): 
        self.queue = queue


    def print(self,path,tipo): 
        msg = f"{path}-{tipo}"
        self.queue.get(msg)

        






if __name__ == "__main__": 
    try: 
        PORT = int(sys.argv[1])
    except IndexError: 
        print("Usage: python main_server.py <PORT>")
        sys.exit(1)

    q=mp.Queue()
    service = ServiceImpl(q)
    skeleton = ServiceSkeleton(PORT,service)
    mp.Process(target = consumer_process, args = (q,)).start()
    skeleton.run()