package com.uuc.job.service.usercenter.model.common;

import lombok.Data;


@Data
public class UcPagination {

    private Integer total;

    private Integer count;

    private Integer per_page;

    private Integer current_page;

    private Integer total_pages;
}
