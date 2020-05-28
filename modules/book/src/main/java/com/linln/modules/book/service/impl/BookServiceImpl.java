package com.linln.modules.book.service.impl;

import com.linln.common.data.PageSort;
import com.linln.common.enums.StatusEnum;
import com.linln.modules.book.domain.Book;
import com.linln.modules.book.repository.BookRepository;
import com.linln.modules.book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 侯瑞皓
 * @date 2020/05/26
 */
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Book getById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Book> getPageList(Example<Book> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return bookRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param book 实体对象
     */
    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return bookRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}