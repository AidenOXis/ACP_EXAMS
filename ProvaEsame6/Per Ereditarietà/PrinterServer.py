from IPrinter import Service
import socket, sys
import multiprocessing as mp 
import stomp 
from abc import ABC, abstractmethod



def proc_fun(c,service): 
    data = c.recv(1024).decode()
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
    conn=stomp.Connection([('127.0.0.1',61613)])
    conn.connect(wait = True)

    while True: 
        msg = queue.get()
        tipo = msg.split('-')[1]
        if "color" in tipo: 
            destination = '/queue/color'
        else: 
            destination = '/queue/bw'

        print(f"[Consumer] Sending to {destination}: {msg}")
        conn.send(destination=destination,body = msg)


class ServiceSkeleton(Service): 

    def __init__(self,port,queue): 
        self.port = port 
        self.queue = queue

    @abstractmethod
    def print(self,path,tipo): 
        pass


    def run_skeleton(self): 
        host = 'localhost'
        with socket.socket(socket.AF_INET,socket.SOCK_STREAM)as server: 
            server.bind(host, self.port)
            server.listen(5)
            print("Socket is listening")
            while True: 
                c,addr = server.accept()
                p = mp.Process(target= proc_fun, args = (c,self))
                p.start()


class ServiceImpl(ServiceSkeleton): 
    def print(self,path,tipo): 
        msg = f"{path}-{tipo}"
        self.queue.put(msg)


if __name__ == "__main__": 

    try: 
        PORT = sys.argv[1]
    except IndexError: 
        print("Please,specificy Port arg")

    print("Server running")
    q = mp.Queue(5)

    ServiceImpl = ServiceImpl(int[PORT],q)
    ServiceImpl.run_skeleton()
    p = mp.Process(target=consumer_process, args=(q,))
    p.start()