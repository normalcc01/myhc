package com.example.myhc;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.lang.Console;
import cn.hutool.db.DbUtil;
import cn.hutool.db.ds.simple.SimpleDataSource;
import cn.hutool.db.handler.NumberHandler;
import cn.hutool.db.sql.SqlExecutor;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.io.Reader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

@SpringBootApplication
public class MyhcApplication {

    private static final int LOCAL_PORT = 8889;

    public static void main(String[] args) {
        TimeInterval timerAll = DateUtil.timer();
        TimeInterval timerStep = DateUtil.timer();
        Console.log("-------系统启动中，请稍等-------");

        try {
            SpringApplication.run(MyhcApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Console.print("-------系统启动完成,");
        Console.log("启动耗时{}ms", timerStep.intervalRestart());

        Console.log("-------开始获取本机地址-------");
        try {
            InetAddress host = InetAddress.getLocalHost();
            String hostAddress = host.getHostAddress();
            String hostName = host.getHostName();
            Console.log("-------当前主机ip地址为：{}", hostAddress);
            Console.log("-------当前主机地址名为{}：", hostName);
            Console.log("-------获取本机地址完成,耗时{}ms-------", timerStep.intervalRestart());
            Console.log();
            Console.log();

            Console.log("-------数据库校验开始-------");
            if(checkDatabaseInit()){
                Console.log("-------数据库校验成功-------");
            }else {
                Scanner in = new Scanner(System.in);
                System.out.println("系统检测到数据库未初始化，是否初始化Y/N？");
                String exec = in.next();
                if("Y".equalsIgnoreCase(exec)){
                    Console.log("开始执行数据库初始化");
                    timerStep.intervalRestart();
                    runnerDatabaseInit();
                    Console.log("数据库初始化完成,耗时:{}ms", timerStep.intervalMs());
                }else if("N".equalsIgnoreCase(exec)){
                    Console.log("取消数据库初始化操作");
                }else {
                    System.exit(0);
                }
            }

            Console.log("-------开始打印：重要信息-------");
            Console.log("-------当前 本机访问地址   http://127.0.0.1:{}", LOCAL_PORT);
            Console.log("-------当前 局域网访问地址 http://{}:{}", hostAddress, LOCAL_PORT);
            Console.log();

        } catch (UnknownHostException e) {
            Console.error("本机地址解析失败，{}", e);
        }
        Console.log("系统准备就绪，耗时{}ms", timerAll.intervalMs());

        Console.log();
        Console.log(">>>>>>>欢迎使用<<<<<<<");

    }

    /**
     * 检查数据库初始化
     *
     * @return boolean
     */
    private static boolean checkDatabaseInit(){
        DataSource ds = new SimpleDataSource("init");

        Connection connection = null;
        try {
            connection = ds.getConnection();
            String sql = "select count(*) from information_schema.tables where table_schema='hc'";
            Number number = SqlExecutor.query(connection, sql, new NumberHandler());
            int tables = number.intValue();

            Console.log("数据库表数量：{}", tables);
            return tables > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DbUtil.close(connection);
        }

        return false;
    }


    /**
     * 数据库初始化
     */
    private static void runnerDatabaseInit() {
        DataSource ds = new SimpleDataSource("init");

        Connection conn = null;
        try {
            conn = ds.getConnection();
            ScriptRunner runner = new ScriptRunner(conn);
            FileReader fileReader = new FileReader("database/hc-mysql-init.sql");
            Reader read = fileReader.getReader();
            runner.runScript(read);
            runner.closeConnection();
            Console.log("数据库初始化成功");
        } catch (SQLException e) {
            Console.error("数据库初始化失败,{}", e.getMessage());
        } finally {
            DbUtil.close(conn);
        }
    }

}
