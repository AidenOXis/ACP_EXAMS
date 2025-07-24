import stomp, random,sys,threading,time


Operator = sys.argv[1]
Topic_req = "/topic/request"
Topic_res = "/topic/response"

hotels = ["Vesuvio","Partenope"]


def send_create(conn): 
    hotel = random.choice(hotels)
    client = f"user{random.randint(1,100)}"
    nights = random.randint(1,9)
    people = random.randint(1,9)
    cost = random.randint(100,400)
    msg = (
        f"CREATE;"
        f"client={client};"
        f"hotel={hotel};"
        f"operator={Operator};"
        f"nights={nights};"
        f"people={people};"
        f"cost={cost}"
    )
    print("[sEND CREATE]",msg)
    conn.send(destination = Topic_req, body = msg)

def send_update(conn): 
    nights = random.randint(1,5)
    discount = random.randint(10,50)

    msg = (
        f"Update"
        f"operator={Operator};"
        f"nights={nights};"
        f"discount = {discount}"
    )

class MyListener(stomp.ConnectionListener): 
    def on_message(self, frame):
        print("[Response] Received: ", frame.body)

if __name__ == "__main__": 
    conn = stomp.Connection()
    conn.set_listener("",MyListener())
    conn.connect(wait = True)
    conn.subscribe(destination=Topic_req,id = 1, ack = "auto")

    threads = []

    for i in range(5): 
        t = threading.Thread(target=send_create, args = (conn,))
        t.start()
        threads.append(t)

    for t in threads: 
        t.join()

    conn.disconnect()