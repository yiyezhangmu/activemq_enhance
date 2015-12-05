package org.apache.activemq.web.query;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class PageUtil {
	private static final String PAGESIZE = "100";
	private static final String PAGE = "1";

	public static boolean isEmpty(String str) {
		if (null == str || "".equals(str.trim())) {
			return true;
		}
		return false;
	}

	private static int parseInt(String str, int defalut) {
		Integer value = null;
		try {
			value = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			value = defalut;
		}

		return value;
	}

	public static PageInfo getPageInfoForList(List list, HttpServletRequest request) {
		PageInfo pageInfo = new PageInfo();
		if (null != list) {
			String page = request.getParameter("page");
			String pageSize = request.getParameter("pageSize");
			if (isEmpty(page)) {
				page = PAGE;
			}
			if (null == pageSize) {
				pageSize = PAGESIZE;
			}
			int count = list.size();
			int size = parseInt(pageSize, Integer.parseInt(PAGESIZE));
			int pageCount = count / size;
			int currPage = parseInt(page, Integer.parseInt(PAGE));
			if (currPage > 1) {
				request.setAttribute("prePage", currPage - 1);
				pageInfo.setPrePage(currPage - 1);
			}
			request.setAttribute("pageSize", size);
			if (count % size != 0) {
				pageCount = pageCount + 1;
			}
			int start = (currPage - 1) * size;
			if (currPage >= pageCount) {
				start = (pageCount - 1) * size;
				size = count - start;
				currPage=pageCount;

			} else {
				request.setAttribute("nextPage", currPage + 1);
				pageInfo.setNextPage(currPage + 1);
			}
			pageInfo.setPageSize(size);
			pageInfo.setCurrPage(currPage);
			pageInfo.setStart(start);
			pageInfo.setEnd(start + size);
			pageInfo.setTotalCount(count);
			pageInfo.setTotalPage(pageCount);
		}
		return pageInfo;
	}
}
