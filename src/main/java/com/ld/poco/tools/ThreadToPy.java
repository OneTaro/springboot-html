package com.ld.poco.tools;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;


public class ThreadToPy {
    private final static DumperOptions OPTIONS = new DumperOptions();

    public static void main(String[] args) throws FileNotFoundException {
//        通过py调用cmd命令，启动该文件
//        线程数量通过读取yml文件来决定启动几个线程，通过cmd启动py脚本
//        yml文件里面有一个项确定跑哪些case项
//        py也读取这些case项，读一个删一个，每次只读第一个（字典还是列表？可以吧key生成一个列表，启动之后删除，随机启动一个）
//        刚好case删完，线程启动完，再留一个线程跑失败的用例，等到之前线程跑完，查询用例集是否为空，不为空就再启动一个线程
//        启动线程就清空失败用例集
//        1.判断用例集有几个项，然后启动几个线程
//        2.跑完之后检查失败项是否为空
//        3.启动makehtml启动javaweb服务（如何编译打包）
//        4.尝试使用数据库，就不用编译打包了，直接html读取库文件
        //获取当前文件路径
        File dumpFile = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\pocotools\\caes.yml");
        OPTIONS.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(OPTIONS);

        LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) yaml.load(new FileReader(dumpFile));
        MyRunThread myRunThread = new MyRunThread();
        ArrayList list = (ArrayList) dataMap.get("case_keys");//需要跑的项
        myRunThread.GoRun(list);
//遍历上面的线程列表，等里面所有线程都执行完，才执行
        System.out.println("11111111111111");
        ArrayList list2 = (ArrayList) dataMap.get("case_keys2");//失败项
        if (list2 != null) {
            myRunThread.GoRun(list2);
        }
            //等待上面线程都执行完，再执行makehtml方法
            //调用py的makeHTML方法
        }
    }


class Processor extends Thread {
    public void run() {
//        这里调用python，去运行方法
        System.out.println("helloworld");
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end");
    }
}

class MyRunThread {
    public void GoRun(ArrayList list) {
        int length = list.size();
        //创建一个数组，把所以线程对象放进去
        Thread tho[] = new Thread[length];
        for (int i = 0; i < length; i++) {

            tho[i] = new Processor();
            tho[i].setName("t" + i);//给线程起名
            tho[i].start();//系统线程启动后自动调用run方
        }
        for (int i = 0; i < length; i++) {
            try {
                tho[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}