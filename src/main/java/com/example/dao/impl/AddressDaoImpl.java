package com.example.dao.impl;

/**
 * Created by E470 on 4/12/2018.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.example.dao.AddressDao;
import com.example.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class AddressDaoImpl extends JdbcDaoSupport implements AddressDao {

    @Autowired
    @Qualifier("dataSource")
    javax.sql.DataSource dataSource;


    @PostConstruct
    private void initialize(){
        setDataSource(dataSource);
    }

    @Override
    public List<Address> findNsw_addressByKeyWords(String keyWords) {
        String sql = "SELECT * from public.nsw_addresses "
                + "where tsv_address @@ plainto_tsquery('" + keyWords + "') "
                + "limit 10 ";
        //System.out.println("AddressDao::findNsw_addressByKeyWords:Info sql: "+sql);
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);

        List<Address> result = new ArrayList<Address>();
        for(Map<String, Object> row:rows){
            Address add = new Address();
            add.setAddress_detail_pid((String)row.get("address_detail_pid"));
            add.setAddress((String)row.get("address"));
            add.setLocality_name((String) row.get("locality_name"));
            add.setMb_2016_code((String) row.get("mb_2016_code"));
            result.add(add);
        }

        return result;
    }

    @Override
    public Address findAddressStringByAddress(String address) {
        String sql = "SELECT * "
                + "from public.nsw_addresses "
                + "where address = '" + address + "' "
                + "limit 1";

        //System.out.println("AddressDao::findNsw_addressByKeyWords:Info sql: "+sql);
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);

        if (rows.size() == 0){
            return null;
        }

        Map<String,Object> row = rows.get(0);

        Address addr = new Address();
        addr.setAddress_detail_pid((String)row.get("address_detail_pid"));
        addr.setAddress((String)row.get("address"));
        addr.setLocality_name((String) row.get("locality_name"));
        addr.setMb_2016_code((String) row.get("mb_2016_code"));

        return addr;
    }



}
