package com.example.springboard.service.category.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboard.mapper.CategoryMapper;
import com.example.springboard.service.category.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryMapper categoryMapper;
}
