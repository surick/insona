package com.jieweifu.controllers.insona;

import com.jieweifu.models.Result;
import com.jieweifu.models.insona.Table;
import com.jieweifu.services.insona.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@SuppressWarnings("unused")
@RestController("InsonaTable")
@RequestMapping("insona/table")
public class TableController {

    private TableService tableService;

    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping("listTables")
    public Result listTables() {
        return new Result().setData(tableService.listTables());
    }

    @GetMapping("listMap")
    public Result listMap() {
        List<Table> tables = tableService.listTables();
        int max = 0;
        List<Data> list = new ArrayList<>();
        for (Table table : tables) {
            Data data = new Data();
            data.setName(table.getCity());
            data.setValue(table.getNormalProduct());
            list.add(data);
            max += table.getNormalProduct();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("max", max);
        return new Result().setData(map);
    }

    class Data {
        private String name;
        private Integer value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "{" +
                    "name:'" + name + '\'' +
                    ", value:" + value +
                    '}';
        }
    }
}
