import stomp,time,sys



class BWListener(stomp.ConnectionListener): 
    def __init__(self,filtro):
        self.filtro = filtro.lower()

    def on_message(self,frame): 
        msg = frame.body



        print(f"[BW Printer] Received Message: {msg}")

        if self.filtro in msg: 
            try: 
                with open("color.txt","a")as file: 
                    file.write(msg +"\n")
                print(f"[BW Printer] Message writtern to color.txt")
            except Exception as e: 
                print("[Bw printer] Error writing to file: {e}")


        else:
            print(f"[BW printer] Ignored message '{self.filtro}' ")



if __name__ == "__main__": 
    try: 
        filtro = sys.arv[1]
        assert filtro in ["doc","txt"]
    except: 
        print("Usage: python bw_printer.py [bw|gs]")
        sys.exit(1)


    conn = stomp.Connection([('127.0.0.1',61613)])
    conn.set_listener('',BWListener(filtro))


    conn.connect(wait = True)
    conn.subscribe(destination='/queue/bw',id = 1, ack = "auto")
    while True:
        time.sleep(60)
