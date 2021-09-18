package com.example.webfluxtest.Controller;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


@Service
public class GpuUtils implements CommandLineRunner {
    public String nowgpuinfo;

    public void getinfo() {
        Socket socket = null;
        Thread thread = null;

        nowgpuinfo = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();
//            String host = addr.getHostAddress();
            String host = "xxxx";
            System.out.println(host);
            // 初始化套接字，设置访问服务的主机和进程端口号，HOST是访问python进程的主机名称，可以是IP地址或者域名，PORT是python进程绑定的端口号
            socket = new Socket(host, 8980);
            System.out.println(socket.isConnected());

            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
            Runnable task = new Runnable() {
                @Override
                public void run() { // 覆盖重写抽象方法
                    while(true) {
                        try {
                            if ((nowgpuinfo = br.readLine()) == null) break;
                            if(nowgpuinfo.length() < 10)
                                continue;
//                            System.out.println(tmp);
                            Thread.sleep(30);
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread = new Thread(task); // 启动线程
            thread.start();
        } catch (IOException  e) {
            e.printStackTrace();
        }  finally {
            System.out.println("远程接口正在调用.");
        }
    }

    @Override
    public void run(String... args) throws Exception {
        getinfo();
    }
}