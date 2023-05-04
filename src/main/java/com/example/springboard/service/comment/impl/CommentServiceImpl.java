package com.example.springboard.service.comment.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboard.mapper.CommentMapper;
import com.example.springboard.service.comment.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
	@Autowired
	private CommentMapper commentMapper;
}
