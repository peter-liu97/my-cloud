package com.mycloud.common;

import lombok.*;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommonPage<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Integer totalPage;
    private Long total;
    private List<T> list;

    /**
     * 将SpringData分页后的list转为分页信息
     */
    public static <T> CommonPage<T> restPage(Page<T> pageInfo) {
        CommonPage<T> result = new CommonPage<T>();
        result.setTotalPage(pageInfo.getTotalPages());
        result.setPageNum(pageInfo.getNumber());
        result.setPageSize(pageInfo.getSize());
        result.setTotal(pageInfo.getTotalElements());
        result.setList(pageInfo.getContent());
        return result;
    }

    public static <T> CommonPage<T> restPage(List<T> pageInfo, PageInfo info, int tatil) {
        List<T> p = pageInfo;
        int page = info.getPage(); //第几页
        int size = info.getSize(); //页面大小
        if (page == 0 || size == 0) {
            throw new IllegalArgumentException("page or size not of zero");
        }
        long t = tatil;
        t = Math.max(p.size(), t); //一共用多少行

        int s = (int) ((t / size)) + (t % size == 0 ? 0 : 1);  //一共几页
        page = Math.min(page, s);// 第几页
        List<T> list = new ArrayList<T>();
        for (int i = (page - 1) * size; i < Math.min(t, size * page) && i > -1; i++) {
            list.add(p.get(i));
        }
        CommonPage<T> commonPage = new CommonPage<T>();
        commonPage.setList(list);
        commonPage.setPageSize(Math.min(size, list.size()));
        commonPage.setTotalPage(s);
        commonPage.setTotal(t);
        commonPage.setPageNum(page);
        return commonPage;
    }
}
