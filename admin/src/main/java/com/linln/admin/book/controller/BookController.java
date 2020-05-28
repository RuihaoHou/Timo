package com.linln.admin.book.controller;

import com.linln.admin.book.validator.BookValid;
import com.linln.common.enums.StatusEnum;
import com.linln.common.utils.EntityBeanUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.utils.StatusUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.excel.ExcelUtil;
import com.linln.component.excel.annotation.Excel;
import com.linln.modules.book.domain.Book;
import com.linln.modules.book.service.BookService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author 侯瑞皓
 * @date 2020/05/26
 * @description 书籍管理操作
 */
@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("book:index")
    public String index(Model model, Book book) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.contains())
                .withMatcher("author", match -> match.contains());

        // 获取数据列表
        Example<Book> example = Example.of(book, matcher);
        Page<Book> list = bookService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/book/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("book:add")
    public String toAdd() {
        return "/book/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("book:edit")
    public String toEdit(@PathVariable("id") Book book, Model model) {
        model.addAttribute("book", book);
        return "/book/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"book:add", "book:edit"})
    @ResponseBody
    public ResultVo save(@Validated BookValid valid, Book book) {

        // 复制保留无需修改的数据
        if (book.getId() != null) {
            Book beBook = bookService.getById(book.getId());
            EntityBeanUtil.copyProperties(beBook, book);
        }

        // 保存数据
        bookService.save(book);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("book:detail")
    public String toDetail(@PathVariable("id") Book book, Model model) {
        model.addAttribute("book",book);
        return "/book/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("book:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (bookService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}