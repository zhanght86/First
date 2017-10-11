package com.sinosoft.common;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.Assert;

/**
 * 分页
 *
 * @author zh
 *
 */
public class Page<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5859907455479273251L;

    public static final int DEFAULT_PAGE_SIZE = 10;

    private int pageSize = DEFAULT_PAGE_SIZE; //默认一页为10

    private long start; // 	页码起始值
    
    private List<T> rows = new ArrayList<T>(); // 返回list数据

    private long total; // 返回总的条数

    /**
     * 构造返回界面的类型
     * @param 总的条数
     * @param 返回的数据
     */
    public Page(long totalSize, List<T> data) {
        Assert.isTrue(totalSize >= 0, "Total size must not be negative!");
        this.total = totalSize;
        this.rows = data;
        if (this.rows == null) {
            this.rows = new ArrayList<T>();
        }
    }


	/**
	 * 返回带几页的数据
	 * @param 起始页
	 * @param 总的数据
	 * @param 一页的值
	 * @param 数据
	 */
    public Page(long start, long totalSize, int pageSize, List<T> data) {
        this(totalSize, data);
        Assert.isTrue(pageSize > 0, "Page size must be greater than 0!");
        this.pageSize = pageSize;
    }

	/**
	 * 返回总的条数
	 * @return
	 */
    public long getTotal() {
        return this.total;
    }

    
    /**
     * 返回页数
     * @return
     */
    public long getPageCount() {
        if (total % pageSize == 0) {
            return total / pageSize;
        } else {
            return total / pageSize + 1;
        }
    }

    /**
     * 获取页数
     * @return
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 返回数据
     * @return
     */
    public List<T> getRows() {
        return rows;
    }

	/**
	 * 获取当前页
	 * @return
	 */
    public long getPageIndex() {
        return start / pageSize;
    }

    /**
     * 获取下一页
     * @return
     */
    public boolean hasNextPage() {
        return this.getPageIndex() < this.getPageCount() - 1;
    }

    /**
     * 获取前一页
     * @return
     */
    public boolean hasPreviousPage() {
        return this.getPageIndex() > 0;
    }

    /**
     * 获取当前页的开始元素是几号
     * @param 当前页
     * @param 一页的大小
     * @return
     */
    public static int getStartOfPage(int pageIndex, int pageSize) {
        return pageIndex * pageSize;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.SHORT_PREFIX_STYLE);
    }
}