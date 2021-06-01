package lucky.yc.community.dto;

import lombok.Data;
import lucky.yc.community.model.User;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {
    private List<T> data;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    //    当前所在页
    private Integer page;
    //    页码数列表
    private List<Integer> pages = new ArrayList<>();
    //        totalPage表示分页页面数目
    private Integer totalPage;




    /**
     * 计算分页总数，逻辑处理
     *
     * @param totalCount 数据表条目数
     * @param page       当前所在页
     * @param size       每页默认显示条目数
     */
    public void setPaginations(Integer totalPage, Integer page) {

        this.totalPage = totalPage;
//        传输过来的page赋值给当前类page
        this.page = page;

//        分页页码显示逻辑
        pages.add(page);
        for (int i = 1; i <= 3; i++) {
//            当前页码 前面页码显示
            if (page - i > 0) {
                pages.add(0, page - i);
            }
            //            当前页码 后面页码显示
            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }

//        是否显示上一页
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }
        //        是否显示下一页
        if (page.equals(totalPage)) {
            showNext = false;
        } else {
            showNext = true;
        }
//        此列表包含指定的元素，则返回true.显示第一页
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }
        //显示最后一页
        if (pages.contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }
    }

    /**
     * 计算分页总数，逻辑处理
     *
     * @param totalCount 数据表条目数
     * @param page       当前所在页
     * @param size       每页默认显示条目数
     */
    public void setPagination(Integer totalCount, Integer page, Integer size) {

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        //        小于1 page等于1
        if (page < 1) {
            page = 1;
        }
//        大于总页数 page等于最大页码
        if (page > totalPage) {
            page = totalPage;
        }

//        传输过来的page赋值给当前类page
        this.page = page;

//        分页页码显示逻辑
        pages.add(page);
        for (int i = 1; i <= 3; i++) {
//            当前页码 前面页码显示
            if (page - i > 0) {
                pages.add(0, page - i);
            }
            //            当前页码 后面页码显示
            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }

//        是否显示上一页
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }
        //        是否显示下一页
        if (page.equals(totalPage)) {
            showNext = false;
        } else {
            showNext = true;
        }
//        此列表包含指定的元素，则返回true.显示第一页
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }
        //显示最后一页
        if (pages.contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }
    }
}
