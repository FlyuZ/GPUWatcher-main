import json
import time
import os
import pynvml
import socket
import threading

pynvml.nvmlInit()
gpu_nums = pynvml.nvmlDeviceGetCount()
handle_list = [pynvml.nvmlDeviceGetHandleByIndex(i) for i in range(gpu_nums)]
body = {
    'gpu_nums': gpu_nums,
    'gpu_info': {}, 
    '_date': None}

def get_gpu_info():
    gpu_info = {}
    for i in range(len(handle_list)):
        meminfo = pynvml.nvmlDeviceGetMemoryInfo(handle_list[i])
        gpu_info[i] = {
            'status': '{:.1f}M/{:.1f}M'.format(meminfo.used / 2**20, meminfo.total / 2**20),
            'percentage': round(meminfo.used / meminfo.total * 100)
        }
    return gpu_info

class ServerThreading(threading.Thread):
    # words = text2vec.load_lexicon()
    def __init__(self, clientsocket, recvsize=1024 * 1024, encoding="utf-8"):
        threading.Thread.__init__(self)
        self._socket = clientsocket
        self._recvsize = recvsize
        self._encoding = encoding
        pass

    def run(self):
        print("开启线程.....")
        try:
            while True:
                body['gpu_info'] = get_gpu_info()
                body['_date'] = time.strftime('%Y-%m-%d %H:%M:%S')
                self._socket.send(("%s" % json.dumps(body)).encode(self._encoding))
                self._socket.send(("\n").encode(self._encoding))
                time.sleep(5)
            pass
        except Exception as identifier:
            self._socket.send("500".encode(self._encoding))
            print(identifier)
            pass
        finally:
            self._socket.close()
        print("任务结束.....")
        pass

    def __del__(self):
        pass


def socketapi(port):
    # 创建服务器套接字
    serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # 获取本地主机名称
    host = socket.gethostname()
    # 将套接字与本地主机和端口绑定
    serversocket.bind((host, port))
    # 设置监听最大连接数
    serversocket.listen(5)
    # 获取本地服务器的连接信息
    myaddr = serversocket.getsockname()
    print("服务器地址:%s" % str(myaddr))
    # 循环等待接受客户端信息
    while True:
        # 获取一个客户端连接
        clientsocket, addr = serversocket.accept()
        print("连接地址:%s" % str(addr))
        try:
            t = ServerThreading(clientsocket)  # 为每一个请求开启一个处理线程
            t.setDaemon(True)
            t.start()
            pass
        except Exception  or KeyboardInterrupt:
            exit()
        if not t.is_alive():
            break
        pass
        
    serversocket.close()
    pass

if __name__ == "__main__":
     socketapi(8980) # 端口自定