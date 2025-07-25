import socket 
import json

class CollectorSkeleton: 
    def __init__(self, delegate, port=9000): 
        self.port = port 
        self.delegate = delegate

def handle_request(self, data):
    payload = json.loads(data.decode())
    tipo = payload['tipo']
    valore = payload['valore']
    self.delegate.measure(tipo, valore)

def serve_forever(self, callback):
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sock.bind(('0.0.0.0', self.port))
    print("Collector Server in ascolto su UDP...")

    while True:
        data, _ = sock.recvfrom(1024)
        self.handle_request(data)
        if self.delegate.queue.full():
            callback(self._consume_all())

def _consume_all(self):
    results = []
    while not self.delegate.queue.empty():
        results.append(self.delegate.queue.get())
    return results
