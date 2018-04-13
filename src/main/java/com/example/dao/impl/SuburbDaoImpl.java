package com.example.dao.impl;

import com.example.dao.SuburbDao;
import com.example.model.Suburb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Created by E470 on 4/14/2018.
 */
@Repository
public class SuburbDaoImpl extends JdbcDaoSupport implements SuburbDao {

    @Autowired
    @Qualifier("dataSource")
    javax.sql.DataSource dataSource;


    @PostConstruct
    private void initialize(){
        setDataSource(dataSource);
    }

    @Override
    public Suburb findSuburbByMb_2016_code(String inputMb_2016_code){

        String sql = "SELECT ms.ssc_code, bdy.name, ST_X(st_centroid(bdy.geom)) as lng, "
                + "ST_Y(st_centroid(bdy.geom)) as lat "
                + "from public.mb_ssc_2016 as ms "
                + "INNER JOIN census_2016_web.ssc AS bdy ON ms.ssc_code = bdy.id "
                + "where mb_code_2016 = '" + inputMb_2016_code +"' "
                + "limit 1";

        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);

        if( rows.size() <= 0){
            return null;
        }

        Map<String,Object> row = rows.get(0);
        Suburb subu = new Suburb();
        subu.setSsc_code((String)row.get("ssc_code"));
        String nameFromDb = (String)row.get("name");
        String name = nameFromDb.substring(0, nameFromDb.indexOf("(")).trim();
        subu.setName(name);
        subu.setCentreLat((Double)row.get("lat"));
        subu.setCentreLng((Double)row.get("lng"));

        return subu;
    }
}
