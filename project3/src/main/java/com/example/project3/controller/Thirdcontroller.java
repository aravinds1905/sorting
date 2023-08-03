package com.example.project3.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project3.model.Third;
import com.example.project3.repository.Thirdrepository;

@RestController
@RequestMapping("/api")
public class Thirdcontroller {
    Thirdrepository thirdrepository;

    private Sort.Direction getSortDirection(String sortString) {
        if (sortString.equals("asc"))
            return Sort.Direction.ASC;

        else if (sortString.equals("desc"))
            return Sort.Direction.DESC;

        return Sort.Direction.ASC;
    }

    @GetMapping("paginate_and_sort")
    public ResponseEntity<Map<String, Object>> getDetails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        List<Order> order = new ArrayList<Order>();
        if (sort[0].contains(",")) {
            for (String sortOrd : sort) {
                String[] sortString = sortOrd.split(",");
                order.add(new Order(getSortDirection(sortString[1]), sortString[0]));

            }
        }

        else {
            order.add(new Order(getSortDirection(sort[1]), sort[0]));
        }

        List<Third> third = new ArrayList<Third>();
        Pageable pages = PageRequest.of(page, size, Sort.by(order));
        Page<Third> fPage;
        fPage = thirdrepository.findAll(pages);
        third = fPage.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("currentPage", fPage.getNumber());
        response.put("pageasize", fPage.getTotalElements());
        response.put("totalpage", fPage.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}