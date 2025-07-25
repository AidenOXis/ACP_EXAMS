import stomp 
import threading 

STOMP_HOST = 'localhost' 
STOMP_PORT = 61613 
TOPICS = { 'temp': '/topic/temp', 'press': '/topic/press', 'humid': '/topic/humid' }


class MeasureListener(stomp.ConnectionListener): 
    def init(self, tipo): 
        self.tipo = tipo 
        self.valori = []

    def calcola_statistiche(valori): 
        return { 'media': sum(valori) / len(valori), 'min': min(valori), 'max': max(valori) }


    def on_message(self, frame):
        print(f"[Checker {self.tipo}] Ricevuto: {frame.body}")
        _, val = frame.body.split('-')
        self.valori.append(float(val))
        stats = calcola_statistiche(self.valori)
        print(f"[{self.tipo.upper()}] Media: {stats['media']:.2f}, Min: {stats['min']}, Max: {stats['max']}")

    def start_checker(tipo): 
        conn = stomp.Connection([(STOMP_HOST, STOMP_PORT)]) 
        listener = MeasureListener(tipo) 
        conn.set_listener('', listener) 
        conn.connect(wait=True) 
        conn.subscribe(destination=TOPICS[tipo], id=f'{tipo}-sub', ack='auto')

if __name__ == '__main__':
    for tipo in ['temp', 'press', 'humid']: 
        thread = threading.Thread(target=start_checker, args=(tipo,))
        thread.start()