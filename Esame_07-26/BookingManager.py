import time
import stomp 
import requests





topic_req = "/topic/request"
topic_res="/topic/response"
Flask_host = "http://127.0.0.1:5000"


def parse_message(body): 
    try: 
        parts = body.strip().split(";")
        command = parts[0].strip()
        data={}

        for field in parts[1:]: 
            key,value = field.strip().split("=")
            data[key] = value.strip()
        return command,data
    except Exception as e: 
        raise ValueError(f"Parsing error: {e}")
    
def handle_create(data): 
    data["nights"] = int(data["nights"])
    data["people"] = int(data["people"])
    data["cost"] = int(data["cost"])

    response = requests.post(f"{Flask_host}/data/prenotazione", json=data)
    return response
def handle_update(data): 
    data["nights"] = int(data["nights"])
    data["discount"] = int(data["discount"])

    response = requests.put(f"{Flask_host}/data/prenotazione",json= data)
class BookingManager(stomp.ConnectionListener): 
    def on_message(self,frame): 
        try: 
            command,data = parse_message(frame.body)

            if command == "CREATE": 
                response = handle_create(data)
            elif command == "UPDATE": 
                response = handle_update(data)
            else:
                raise ValueError(f"unsopported command type: {command}")
            
            result = response.json()
            conn.send(destination=topic_res,body = str(result))
        except Exception as e: 
            print(str(e))
            conn.send(destination = topic_req,body = f"Error: {str(e)}")


if __name__ =="__main__": 
    conn = stomp.Connection()
    conn.set_listener(' ', BookingManager())
    conn.connect(wait=True)
    conn.subscribe(destination=topic_req,id = 1, ack="auto")

    try: 
        while True: 
            time.sleep(1)
    except KeyboardInterrupt: 
        conn.disconnect()