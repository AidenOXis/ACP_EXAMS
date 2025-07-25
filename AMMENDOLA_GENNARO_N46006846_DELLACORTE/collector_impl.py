from ICollector import Service

class CollectorImpl(Service): 
    def __init__(self, queue): 
        self.queue = queue

def measure(self, tipo, valore):
    msg = f"{tipo}-{valore}"
    self.queue.put(msg)