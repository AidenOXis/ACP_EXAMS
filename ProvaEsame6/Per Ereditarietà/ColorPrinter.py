import stomp,time,sys

class CListener(stomp.ConnectionListener):

    def __init__(self,filtro):
        self.filtro=filtro.lower()

    def on_message(self,frame): 
        msg = frame.body

        print(f"[CL PRINTER] Received message: {msg}")

        if self.filtro in msg: 
            try:
                with open("color.txt","a") as f: 
                    f.write(msg + "\n")
                print(f"[CL Printer] Message written to color.txt")
            except Exception as e: 
                print(f"[CL Printer] Error writing to file: {e}")
        else: 
            print(f"[CL printer] Ignored message '{self.filtro}' ")


if __name__ == "__main__": 
    try: 
        filtro = sys.argv[1]
        assert filtro in ["doc","txt"]
    except: 
        print("Usage: python color_printer.py [doc|txt]")
        sys.exit(1)

    conn = stomp.Connection([('127.0.0.1',61613)])
    conn.set_listener('',CListener(filtro))

    conn.connect(wait=True)
    conn.subscribe(destination='/queue/color',id =1,ack = 'auto')

    print(f"[CL Printer] Listening for message for matching: {filtro}")

    while True: 
        time.sleep(60)