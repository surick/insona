package com.jieweifu.controllers.insona;

import com.jieweifu.common.business.BaseContextHandler;
import com.jieweifu.common.utils.ErrorUtil;
import com.jieweifu.models.Result;
import com.jieweifu.models.insona.Message;
import com.jieweifu.services.insona.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@RestController("InsonaMessage")
@RequestMapping("insona/message")
public class MessageController {

    private MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * 新增
     */
    @PostMapping("saveMessage")
    public Result saveMessage(@Valid @RequestBody Message message, Errors errors) {
        if (errors.hasErrors())
            return new Result().setError(ErrorUtil.getErrors(errors));
        String userName = BaseContextHandler.getUserName();
        String time = String.valueOf(System.currentTimeMillis());
        message.setPublishTime(time);
        message.setPublishUser(userName);
        messageService.saveMessage(message);
        return new Result().setMessage("新增成功");
    }

    /**
     * 删除
     */
    @DeleteMapping("removeMessage")
    public Result removeMessage(@RequestBody List<String> ids) {
        for (String id : ids) {
            if (messageService.getMessage(Integer.parseInt(id)) == null) {
                return new Result().setError("无效id");
            }
            messageService.removeMessage(Integer.parseInt(id));
        }
        return new Result().setMessage("删除成功");
    }

    /**
     * 修改
     */
    @PutMapping("updateMessage")
    public Result updateMessage(@RequestBody Message message) {
        System.out.println(message.getIsPublished());
        if (messageService.getMessage(message.getId()) == null)
            return new Result().setError("不存在");
        messageService.updateMessage(message);
        return new Result().setMessage("修改成功");
    }

    /**
     * 分页查询
     *
     * @param pageIndex 页码
     * @param pageSize  页条目数
     * @return map
     */
    @GetMapping("listMessage/{pageIndex}/{pageSize}")
    public Result listMessage(@PathVariable("pageIndex") Integer pageIndex,
                              @PathVariable("pageSize") Integer pageSize) {
        if (pageIndex < 0 || pageSize < 0)
            return new Result().setError("页码或条目数不合法");
        List<Message> messageList = messageService.listMessage(pageIndex, pageSize);
        int total = messageService.getMessageTotal();
        Map<String, Object> map = new HashMap<>();
        map.put("list", messageList);
        map.put("total", total);
        return new Result().setData(map);
    }

}
