package com.linln.admin.book.validator;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author 侯瑞皓
 * @date 2020/05/26
 */
@Data
public class BookValid implements Serializable {
    @NotEmpty(message = "书籍名称不能为空")
    private String name;
    @NotEmpty(message = "作者不能为空")
    private String author;
    @NotNull(message = "价格不能为空")
    private Double price;
}