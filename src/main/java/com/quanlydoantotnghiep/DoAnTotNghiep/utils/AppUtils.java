//package com.quanlydoantotnghiep.DoAnTotNghiep.utils;
//
//import com.quanlydoantotnghiep.DoAnTotNghiep.dto.ObjectResponse;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//
//import java.util.List;
//
//public class AppUtils {
//
//    public static Pageable createPageable(int pageNumber, int pageSize, String sortBy, String sortDir) {
//
//        // sorting
//        Sort sort = Sort.by(sortBy);
//        if(sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()))
//            sort.ascending();
//        else
//            sort.descending();
//
//        // paginating
//        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
//
//        return pageable;
//    }
//
//    public static <P,C>ObjectResponse createObjectResponse(Page<P> pageContent, List<C> dtoContent) {
//
//        ObjectResponse<C> response = new ObjectResponse();
//
//        response.setContent(dtoContent);
//        response.setTotalElements(pageContent.getTotalElements());
//        response.setPageSize(pageContent.getSize());
//        response.setPageNumber(pageContent.getNumber()+1);
//        response.setTotalPages(pageContent.getTotalPages());
//        response.setLast(pageContent.isLast());
//
//        return response;
//    }
//}
