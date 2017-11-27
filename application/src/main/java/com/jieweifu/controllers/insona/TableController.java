package com.jieweifu.controllers.insona;

import com.jieweifu.models.Result;
import com.jieweifu.services.insona.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
