package com.example.springboard.service.board;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.springboard.dto.BoardDTO;
import com.example.springboard.dto.BoardFileDTO;

public interface BoardService {
	void insertBoard(BoardDTO boardDTO, List<BoardFileDTO> uploadFileList);
	
	List<BoardDTO> getBoardList(BoardDTO boardDTO);
	
	void updateBoardCnt(int boardNo);
	
	BoardDTO getBoard(int boardNo);
	
	void deleteBoard(int boardNo);
	
	void updateBoard(BoardDTO boardDTO);
	
	List<BoardFileDTO> getBoardFileList(int boardNo);
	
//	Page<BoardDTO> getPageBoardList(BoardDTO boardDTO, Pageable pageable);
}
