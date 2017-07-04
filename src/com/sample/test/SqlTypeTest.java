package com.sample.test;

import com.sample.database.DataBase;
import com.sample.util.DBCPUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by yizijun on 2017/6/28 0028.
 */
public class SqlTypeTest {

    public static void main(String[] args) {

        Connection conn = DBCPUtil.getConnection();
        String sql = "select * from type_test";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
           ResultSet res = pre.executeQuery();
            res.next();
            for(int i = 1; i <= 38; ++i) {
                if(null != DataBase.getValue(res, String.valueOf(i)))
                System.out.println(i + "   " + DataBase.getValue(res, String.valueOf(i)).getClass());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

/********************************mysql类型和java类型对应表****************************
 `1` tinyint(4) DEFAULT NULL,
 `2` smallint(6) DEFAULT NULL,
 `3` mediumint(9) DEFAULT NULL,
 `4` int(11) DEFAULT NULL,
 `5` int(11) DEFAULT NULL,
 `6` bigint(20) DEFAULT NULL,
 `7` bit(1) DEFAULT NULL,
 `8` double DEFAULT NULL,
 `9` double DEFAULT NULL,
 `10` float DEFAULT NULL,
 `11` decimal(10,0) DEFAULT NULL,
 `12` decimal(10,0) DEFAULT NULL,
 `13` char(255) DEFAULT NULL,
 `14` varchar(255) DEFAULT NULL,
 `15` date DEFAULT NULL,
 `16` time DEFAULT NULL,
 `17` year(4) DEFAULT NULL,
 `18` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
 `19` datetime DEFAULT NULL,
 `20` tinyblob,
 `21` blob,
 `22` mediumblob,
 `23` longblob,
 `24` tinytext,
 `25` mediumtext,
 `26` longtext,
 `27` enum('') DEFAULT NULL,
 `28` set('') DEFAULT NULL,
 `29` binary(255) DEFAULT NULL,
 `30` varbinary(255) DEFAULT NULL,
 `31` point DEFAULT NULL,
 `32` linestring DEFAULT NULL,
 `33` polygon DEFAULT NULL,
 `34` geometry DEFAULT NULL,
 `35` multipoint DEFAULT NULL,
 `36` multilinestring DEFAULT NULL,
 `37` multipolygon DEFAULT NULL,
 `38` geometrycollection DEFAULT NULL



 1   class java.lang.Integer
 2   class java.lang.Integer
 3   class java.lang.Integer
 4   class java.lang.Integer
 5   class java.lang.Integer
 6   class java.lang.Long
 7   class java.lang.Boolean
 8   class java.lang.Double
 9   class java.lang.Double
 10   class java.lang.Float
 11   class java.math.BigDecimal
 12   class java.math.BigDecimal
 13   class java.lang.String
 14   class java.lang.String
 15   class java.sql.Date
 16   class java.sql.Time
 17   class java.sql.Date
 18   class java.sql.Timestamp
 19   class java.sql.Timestamp
 24   class java.lang.String
 25   class java.lang.String
 26   class java.lang.String
 27   class java.lang.String
 28   class java.lang.String




 ******************************************************************************/