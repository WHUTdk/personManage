package com.dingkai.personManage.business.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author dingkai
 * @Date 2020/7/12 19:47
 */
@Data
public class PagedResponseVO<T> {

    private Long total;

    private Long totalPages;

    private Integer pageNo;

    private Integer pageSize;

    private List<T> list;

}
