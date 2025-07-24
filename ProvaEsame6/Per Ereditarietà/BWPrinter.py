import stomp,time,sys


class BWListener(stomp.ConnectionListener):
    def __init__(self,filtro): 
        self.filtro = filtro.lower()
    
    def on_message(self,frame):
        msg = frame.body

        print(f"[BW Printer] Received message: {msg}")

        if self.filtro in msg: 
            try: 
                with open("bw.txt", "a") as f: 
                    f.write(msg+"\n")
                print(f"[BW Printer] Message written to bw.txt")
            except Exception as e: 
                print(f"[BW Printer] Error writing to file: {e}")

        else: 
            print(f"[bw printer] Ignored message '{self.filtro}' ")


if __name__ == "__main__": 
    try: 
        filtro = sys.argv[1]
        assert filtro in ["bw","gs"]
    except: 
        print("Usage:python bw_printer.py [bw|gs]")
        sys.exit(1)

    conn = stomp.Connection([('127.0.0.1', 61613)])
    conn.set_listener('',BWListener(filtro))

    conn.connect(wait = True)
    conn.subscribe(destination='/queue/bw', id = 1, ack = "auto")


    while True: 
        time.sleep(60)