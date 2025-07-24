import socket 
import stomp 
import multiprocessing as mp 
import sys 
from abc import ABC, abstractmethod
from IPrinter import Service


host = 'localhost'



def proc_fun(c,service): 
    data = c.recv(1024).decode("utf-8")
    if "-" not in data: 
        print(f"[Dispatcher] Messaggio malformato: '{data}' ")
        c.close()
        return 
    parts = data.split("-")
    if len(parts) !=2: 
        print(f"[Dispatcher] Formato non valido: '{data}' ")
        c.close()
        return
    
    path,tipo = parts 
    service.print(path,tipo)
    c.close()

def consumer_process(queue): 
    conn = stomp.Connection([('127.0.0.1',62613)])
    conn.connect(wait=True)




    while True: 
        msg = queue.get()
        tipo = msg.split("-")[1]
        if "color" in tipo: 
            destination = '/queue/color'
        else: 
            destination = '/queue/bw'
        print(f"[Consumer] Sending to {destination}:{msg}")
        conn.send(destination=destination, body = msg)


class ServiceSkeleton(Service): 
    def __init__(self,port,queue): 
        self.port = port
        self.queue = queue


    @abstractmethod
    def print(self,path,tipo): 
        pass

    def run_skeleton(self):
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as server: 
            server.bind((host,self.port))
            server.listen(5)
            print("Socket is listening")
            while True: 
                c,add = server.accept()
                p = mp.Process(target=proc_fun, args = (c, self))
                p.start()


class ServiceImpl(ServiceSkeleton): 
    def print(self,path,tipo): 
        msg = f"{path}-{tipo}"
        self.queue.put(msg)

if __name__ == "__main__":
    try: 
        PORT = sys.argv[1]
    except IndexError:
        print("Please,specify Port arg")
    
    print("Server running")
    manager = mp.Manager()
    q=manager.Queue(5)

    service = ServiceImpl(host,int[PORT], q)
    service.run_skeleton()
    p = mp.Process(target = consumer_process, args = (q,))
    p.start()