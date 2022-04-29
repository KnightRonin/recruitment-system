package com.yisen.hbaseboot;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

/**
 * HBase操作
 * 包括连接hbase和建表
 *
 * @author Administrator
 */
public class test {

    /**
     * hadoop配置
     */
    private static Configuration configuration;

    /**
     * hbase客户端连接
     */
    private static Connection connection;

    private static Admin admin;
    //    hbase在hdfs的存储路径
    private static final String HBASE_ROOT_DIR = "hdfs://175.178.196.83:9000/hbase";

    /**
     * HBase初始化，创建连接
     */
    public static void init() {
        configuration = HBaseConfiguration.create();
//        configuration = new Configuration();
//        HBaseConfiguration.addHbaseResources(configuration);
        configuration.set("hbase.rootdir", HBASE_ROOT_DIR);
        configuration.set("hbase.zookeeper.quorum", "175.178.196.83");
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        try {
            // 创建连接
            connection = ConnectionFactory.createConnection(configuration);
            admin = connection.getAdmin();
            System.out.println("创建连接成功");
        } catch (IOException e) {
            System.out.println("创建连接失败");
            e.printStackTrace();
        }
    }

    /**
     * 关闭hbase连接
     */
    public static void close() {
        if (null != admin) {
            try {
                admin.close();
                System.out.println("close admin");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (null != connection) {
            try {
                connection.close();
                System.out.println(" close connection");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 创建表
     * @param tableName:表名
     * @param columnFamily:列簇
     */
    public static void createTable(String tableName, String[] columnFamily) {
        init();
        TableName table = TableName.valueOf(tableName);
        try {

            if (admin.tableExists(table)) {
                System.out.println("表:" + tableName + "存在无法创建");
            } else {
                HTableDescriptor hTableDescriptor = new HTableDescriptor(table);
                for (String col : columnFamily) {
                    HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(col);
                    hTableDescriptor.addFamily(hColumnDescriptor);
                }
                admin.createTable(hTableDescriptor);
                System.out.println("create:" + tableName + "success");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //close();
        }
    }

    public static void main(String[] args) {
//         init();
        String[] colFamily = {"sname", "course"};
        createTable("Score1", colFamily);
//        close();
    }

}