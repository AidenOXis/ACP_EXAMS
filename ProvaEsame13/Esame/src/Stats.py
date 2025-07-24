import stomp
import multiprocessing
from collections import defaultdict

ticket_queue = multiprocessing.Queue()

def process_stats_and_write(q):
    sold_dict = defaultdict(int)
    while not q.empty():
        artist = q.get()
        sold_dict[artist] += 1
    with open("stats.txt", "w") as file:
        for artist, count in sold_dict.items():
            file.write(f"{artist}:{count} tickets sold\n")

def process_stats(message):
    print(f"Processing stats message: {message}")
    if message == "Sold":
        # Avvia un nuovo processo che svuota la coda e scrive il file
        stats_proc = multiprocessing.Process(target=process_stats_and_write, args=(ticket_queue,))
        stats_proc.start()
        stats_proc.join()

class MyListener(stomp.ConnectionListener):
    def on_message(self, headers, message):
        print(f"Messaggio ricevuto: {message}")
        if 'tickets' in headers['destination']:
            ticket_queue.put(message)
        elif 'stats' in headers['destination']:
            process_stats(message)

conn = stomp.Connection([('127.0.0.1', 61613)])
conn.set_listener('', MyListener())
conn.connect()

conn.subscribe(destination='/topic/tickets', id=1, ack='auto')
conn.subscribe(destination='/topic/stats', id=2, ack='auto')