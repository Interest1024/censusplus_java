package com.example.dao.impl;

/**
 * Created by E470 on 4/12/2018.
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.example.dao.Nsw_addressesDao;
import com.example.model.Nsw_addresses;

@Repository
public class Nsw_addressesDaoImpl extends JdbcDaoSupport implements Nsw_addressesDao {

    @Autowired
    @Qualifier("dataSource")
    javax.sql.DataSource dataSource;


    @PostConstruct
    private void initialize(){
        setDataSource(dataSource);
    }

    @Override
    public List<Nsw_addresses> findNsw_addressByKeyWords(String keyWords) {
        String sql = "SELECT * from public.nsw_addresses " +
                "where tsv_address @@ plainto_tsquery('" + keyWords + "') " +
                "limit 5 ";
        System.out.println(sql);
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);

        List<Nsw_addresses> result = new ArrayList<Nsw_addresses>();
        for(Map<String, Object> row:rows){
            Nsw_addresses add = new Nsw_addresses();
            add.setAddress_detail_pid((String)row.get("address_detail_pid"));
            add.setAddress((String)row.get("address"));
            add.setLocality_name((String) row.get("locality_name"));
            add.setMb_2016_code((String) row.get("mb_2016_code"));
            result.add(add);
        }

        return result;
    }


}
