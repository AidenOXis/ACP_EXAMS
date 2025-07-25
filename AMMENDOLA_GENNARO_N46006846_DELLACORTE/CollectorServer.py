import stomp 
from queue import Queue 
from SensorSkeleton import CollectorSkeleton 
from collector_impl import CollectorImpl 



STOMP_HOST = 'localhost' 
STOMP_PORT = 61613 
TOPICS = { 'temp': '/topic/temp', 'press': '/topic/press', 'humid': '/topic/humid' }

class CollectorSender: 
    def init(self): 
        self.conn = stomp.Connection([(STOMP_HOST, STOMP_PORT)]) 
        self.conn.connect(wait=True)

def send_measurements(self, messaggi):
    for msg in messaggi:
        tipo, valore = msg.split('-')
        topic = TOPICS[tipo]
        if tipo == 'temp':
            self.conn.begin("tx-1")
            try:
                self.conn.send(topic, msg, transaction="tx-1")
                self.conn.commit("tx-1")
            except:
                self.conn.abort("tx-1")
        else:
            self.conn.send(topic, msg)

if __name__ == '__main__': 
    queue = Queue(maxsize=6) 
    impl = CollectorImpl(queue) 
    skeleton = CollectorSkeleton(impl) 
    sender = CollectorSender() 
    skeleton.serve_forever(sender.send_measurements)
