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
    public TableController(TableService tableService){
        this.tableService = tableService;
    }

    @GetMapping("listTables")
    public Result listTables(){
        return new Result().setData(tableService.listTables());
    }

    @GetMapping("maxTotal")
    public Result maxTotal(){
        List<Table> tables = tableService.listTables();
        List<Integer> list = new ArrayList<>();
        tables.forEach(
                table -> {
                    list.add(table.getNormalProduct());
                }
        );
        Integer max = Collections.max(list);
        System.out.println(max);
        return new Result().setData(max);
    }

    @GetMapping("listMap")
    public Result listMap(){
        List<Data> list = new ArrayList<>();
        List<Table> tables = tableService.listTables();
        tables.forEach(
                table ->{
                    Data data = new Data();
                    data.setName(table.getCity());
                    data.setValue(table.getNormalProduct());
                    list.add(data);
                }

        );
        return new Result().setData(list);
    }

    class Data{
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
