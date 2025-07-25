import socket 
import json

class CollectorProxy: 
    def __init__(self, host='localhost', port=9000): 
        self.addr = (host, port)

def measure(self, tipo, valore):
    msg = json.dumps({'tipo': tipo, 'valore': valore})
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sock.sendto(msg.encode(), self.addr)
    sock.close()
