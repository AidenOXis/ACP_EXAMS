from PrintProxy import Proxy
import random,time,sys




IP = 'localhost'


try: 
    service = sys.argv[1]
    port = int(sys.argv[2])

except IndexError: 
    print("[CLient] Error")
    sys.exit(-1)

proxy = Proxy(IP,port)

tipi = ["bw","gs","color"]
estensioni = ["doc","txt"]



for _ in range(10): 
    num = random.randint(0,100)
    ext = random.choice(estensioni)
    tipo = random.choice(tipi)


    path = f"/user/file_{num}.{ext}"


    print(f"[User] Sending print request: {path,{tipo}}")
    proxy.print(path,tipo)
    time.sleep(10)
